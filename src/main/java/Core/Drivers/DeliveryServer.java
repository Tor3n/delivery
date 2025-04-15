package Core.Drivers;


import Api.Adapters.BackgroudJobs.BackgroundTasks;
import Api.Adapters.Http.restAPI.RestApiApplication;
import Api.Adapters.Kafka.SimpleKafkaAdapterImpl;
import Core.Configuration;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.jetty.InstrumentedQueuedThreadPool;
import io.micrometer.core.instrument.binder.jetty.JettyConnectionMetrics;
import io.micrometer.core.instrument.binder.jetty.TimedHandler;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.openapitools.client.api.DefaultApi;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.inject.Singleton;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class DeliveryServer implements AutoCloseable{

    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryServer.class);
    private final AtomicBoolean duringShutdown = new AtomicBoolean(false);
    public static final String DEFAULT_SERVICE_LOCATOR_NAME = "shared-locator";
    private final Optional<PrometheusMeterRegistry> promMetricRegistry;
    private final Server jettyWebServer;
    private final Configuration conf;
    private final ServiceLocator sharedServiceLocator;

    public DeliveryServer(Configuration conf) throws IOException {
        this(conf, DEFAULT_SERVICE_LOCATOR_NAME);
    }

    public DeliveryServer(Configuration conf, String serviceLocatorName) throws IOException {
        LOGGER.debug("Instantiated Delivery server");
        this.conf = conf;
        promMetricRegistry = Optional.of(new PrometheusMeterRegistry(PrometheusConfig.DEFAULT));
        jettyWebServer = setupJettyServer();
        sharedServiceLocator = ServiceLocatorFactory.getInstance().create(serviceLocatorName);
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = Configuration.load();
        conf.printShortInfo();
        try (DeliveryServer server = new DeliveryServer(conf)) {
            server.startDeliveryServer();
        }
    }

    public void startDeliveryServer() {
        //TODO
        //initMetrics();

        TimedHandler timedHandler = new TimedHandler(Metrics.globalRegistry, Tags.empty());
        jettyWebServer.setHandler(timedHandler);

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        timedHandler.setHandler(contexts);
        ServiceLocatorUtilities.enableImmediateScope(sharedServiceLocator);
        ServiceLocatorUtilities.addClasses(sharedServiceLocator,
                ImmediateErrorHandlerImpl.class);
        ImmediateErrorHandlerImpl handler = sharedServiceLocator.getService(ImmediateErrorHandlerImpl.class);


        ServiceLocatorUtilities.bind(
                sharedServiceLocator,
                new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(conf).to(Configuration.class);
                        bindAsContract(SimpleKafkaAdapterImpl.class).in(Singleton.class);
                        bindAsContract(BackgroundTasks.class).in(Singleton.class);
                    }
                });

        ServiceLocatorUtilities.getService(sharedServiceLocator, BackgroundTasks.class.getName());

        //final WebAppContext defaultWebApp = setupWebAppContext(contexts, conf,
        //       conf.getString(Configuration.ConfVars.DELIVERY_SERVER_UI_DEFAULT_DIR), "/");

        //initWebApp(defaultWebApp);
        initSwaggerRest();

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        LOGGER.info("Starting delivery server");
        try {
            jettyWebServer.start(); // Instantiates ZeppelinServer
            if (conf.getJettyName() != null) {
                org.eclipse.jetty.http.HttpGenerator.setJettyVersion(conf.getJettyName());
            }
        } catch (Exception e) {
            LOGGER.error("Error while running jettyServer", e);
            System.exit(-1);
        }

        LOGGER.info("Done, delivery server started");
        try {
            List<ErrorData> errorDatas = handler.waitForAtLeastOneConstructionError(5000);
            for (ErrorData errorData : errorDatas) {
                LOGGER.error("Error in Construction", errorData.getThrowable());
            }
            if (!errorDatas.isEmpty()) {
                LOGGER.error("{} error(s) while starting - Termination", errorDatas.size());
                System.exit(-1);
            }
        } catch (InterruptedException e) {
            // Many fast unit tests interrupt the Zeppelin server at this point
            LOGGER.error("Interrupt while waiting for construction errors - init shutdown", e);
            shutdown();
            Thread.currentThread().interrupt();
        }

        if (jettyWebServer.isStopped() || jettyWebServer.isStopping()) {
            LOGGER.debug("jetty server is stopped {} - is stopping {}", jettyWebServer.isStopped(), jettyWebServer.isStopping());
        } else {
            try {
                jettyWebServer.join();
            } catch (InterruptedException e) {
                LOGGER.error("Interrupt while waiting for jetty threads - init shutdown", e);
                shutdown();
                Thread.currentThread().interrupt();
            }
        }
    }

    private Server setupJettyServer() {
        InstrumentedQueuedThreadPool threadPool =
                new InstrumentedQueuedThreadPool(Metrics.globalRegistry, Tags.empty(),
                        conf.getInt(Configuration.ConfVars.DELIVERY_SERVER_JETTY_THREAD_POOL_MAX),
                        conf.getInt(Configuration.ConfVars.DELIVERY_SERVER_JETTY_THREAD_POOL_MIN),
                        conf.getInt(Configuration.ConfVars.DELIVERY_SERVER_JETTY_THREAD_POOL_TIMEOUT));
        final Server server = new Server(threadPool);

        ServerConnector connector = null;
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.addCustomizer(new ForwardedRequestCustomizer());

        if (conf.getBoolean(Configuration.ConfVars.DELIVERY_SERVER_JETTY_USE_SSL)) {
            //TODO no ssl yet
        } else {
            HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpConfig);
            connector =
                    new ServerConnector(
                            server,
                            httpConnectionFactory);
            connector.setPort(conf.getServerPort());
        }

        int timeout = 1000 * 30;
        connector.setIdleTimeout(timeout);
        connector.setHost(conf.getServerAddress());
        connector.addBean(new JettyConnectionMetrics(Metrics.globalRegistry, Tags.empty()));
        server.addConnector(connector);
        return server;
    }

    private static WebAppContext setupWebAppContext(
            ContextHandlerCollection contexts, Configuration conf, String path, String contextPath) {
        WebAppContext webApp = new WebAppContext();
        webApp.setContextPath(contextPath);
        path = "/home/toren/Code/DDD_training/microservices/"+path;
        LOGGER.info("Path is: {}", path);
        File file = new File(path);
        System.out.println(">>exists: "+file.exists());

        webApp.setResourceBase(file.getPath());
        //webApp.setResourceBase("../UI");
        webApp.setWelcomeFiles(new String[]{"index.html"});

        ServletHolder defaultServlet = new ServletHolder("default", DefaultServlet.class);
        defaultServlet.setInitParameter("dirAllowed", "false"); // Disable directory listing
        webApp.addServlet(defaultServlet, "/");

        contexts.addHandler(webApp);

        return webApp;
    }

    private void initWebApp(WebAppContext webApp) {
        webApp.addEventListener(
                new ServletContextListener() {
                    @Override
                    public void contextInitialized(ServletContextEvent servletContextEvent) {
                        servletContextEvent
                                .getServletContext()
                                .setAttribute(ServletProperties.SERVICE_LOCATOR, sharedServiceLocator);
                    }

                    @Override
                    public void contextDestroyed(ServletContextEvent servletContextEvent) {}
                });

        // Create `DeliveryServer` using reflection and setup REST Api
        final ServletHolder servletHolder =
                new ServletHolder(new org.glassfish.jersey.servlet.ServletContainer());

        servletHolder.setInitParameter("javax.ws.rs.Application", RestApiApplication.class.getName());
        servletHolder.setName("rest");
        webApp.addServlet(servletHolder, "/api/*");

        //TODO prometheus endpoint probably...
        //setupPrometheusContextHandler(webApp);

        //TODO health endpoints probably...
        //setupHealthCheckContextHandler(webApp);
    }

    private void initSwaggerRest() {
        // Handler for API endpoints (JAX-RS)
        ServletContextHandler apiContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        apiContext.setContextPath("/api");

        // Register Jersey Servlet with our Application
        ServletHolder apiServlet = new ServletHolder(new ServletContainer());
        apiServlet.setInitParameter("javax.ws.rs.Application", RestApiApplication.class.getName());
        apiServlet.setName("rest");
        apiContext.addServlet(apiServlet, "/*");


        // Handler for Swagger UI
        ServletContextHandler swaggerUIContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        swaggerUIContext.setContextPath("/swagger-ui");

        swaggerUIContext.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                    throws ServletException, IOException {

                if (req.getRequestURI().endsWith("swagger-ui-init.js")) {
                    resp.setContentType("application/javascript");
                    // Read from classpath
                    try (InputStream is = getClass().getResourceAsStream("/swagger-ui-init.js")) {
                        IOUtils.copy(is, resp.getOutputStream());
                    }
                } else {
                    // Serve original Swagger UI
                    super.doGet(req, resp);
                }
            }
        }), "/swagger-ui-init.js");

        swaggerUIContext.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                    throws IOException {
                if ("/swagger-ui".equals(req.getRequestURI())) {
                    resp.sendRedirect("/swagger-ui/swagger-ui.html");
                }
            }
        }), "/swagger-ui");


        // Configure DefaultServlet to serve Swagger UI files
        ServletHolder swaggerUIHolder = new ServletHolder("default", DefaultServlet.class);

        // Point to Swagger UI files in the webjars
        String swaggerUIPath = Server.class
                .getClassLoader()
                .getResource("META-INF/resources/webjars/swagger-ui/5.10.3")
                .toExternalForm();

        swaggerUIHolder.setInitParameter("resourceBase", swaggerUIPath);
        swaggerUIHolder.setInitParameter("dirAllowed", "false");
        swaggerUIHolder.setInitParameter("pathInfoOnly", "true");

        // Serve index.html by default
        swaggerUIHolder.setInitParameter("welcomeServlets", "true");
        swaggerUIHolder.setInitParameter("redirectWelcome", "true");



        swaggerUIContext.addServlet(swaggerUIHolder, "/*");

        // Combine both contexts
        jettyWebServer.setHandler(new org.eclipse.jetty.server.handler.ContextHandlerCollection(apiContext, swaggerUIContext));
    }

    @Override
    public void close() throws Exception {
        shutdown();
    }

    public void shutdown() {
        shutdown(0);
    }

    public void shutdown(int exitCode) {
        if (!duringShutdown.getAndSet(true)) {
            LOGGER.info("Shutting down Delivery Server ... - ExitCode {}", exitCode);
            try {
                if (jettyWebServer != null) {
                    jettyWebServer.stop();
                }
            } catch (Exception e) {
                LOGGER.error("Error while stopping servlet container", e);
            }
            LOGGER.info("Bye");
            if (exitCode != 0) {
                System.exit(exitCode);
            }
        }
    }




}
