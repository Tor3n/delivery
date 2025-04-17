$(function () {
  window.swaggerUi = new SwaggerUi({
    url: "/api/openapi.json",
    dom_id: "swagger-ui",
    validatorUrl: null,  // ← This completely disables validation
    supportedSubmitMethods: ['get', 'post', 'put', 'delete'],
    onComplete: function() {
      console.log("Swagger UI loaded");
    },
    docExpansion: "none",
    showRequestHeaders: false
  });
  
  window.swaggerUi.load();
});
