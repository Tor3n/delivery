package Core.Drivers;

import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.utilities.ImmediateErrorHandler;
import java.util.LinkedList;
import java.util.List;

public class ImmediateErrorHandlerImpl implements ImmediateErrorHandler{
    private final List<ErrorData> constructionErrors = new LinkedList<>();
    private final List<ErrorData> destructionErrors = new LinkedList<>();

    @Override
    public void postConstructFailed(ActiveDescriptor<?> immediateService, Throwable exception) {
        synchronized (constructionErrors) {
            constructionErrors.add(new ErrorData(immediateService, exception));
            constructionErrors.notifyAll();
        }
    }

    @Override
    public void preDestroyFailed(ActiveDescriptor<?> immediateService, Throwable exception) {
        synchronized (constructionErrors) {
            destructionErrors.add(new ErrorData(immediateService, exception));
            constructionErrors.notifyAll();
        }
    }

    List<ErrorData> waitForAtLeastOneConstructionError(long waitTime) throws InterruptedException {
        synchronized (constructionErrors) {
            while (constructionErrors.isEmpty() && waitTime > 0) {
                long currentTime = System.currentTimeMillis();
                constructionErrors.wait(waitTime);
                long elapsedTime = System.currentTimeMillis() - currentTime;
                waitTime -= elapsedTime;
            }
            return new LinkedList<>(constructionErrors);
        }
    }
}
