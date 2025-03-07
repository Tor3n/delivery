package Core.Drivers;

import org.glassfish.hk2.api.ActiveDescriptor;

/**
 * ErrorData to store Descriptor and exception
 */
public class ErrorData {
    private final ActiveDescriptor<?> descriptor;
    private final Throwable th;

    public ErrorData(ActiveDescriptor<?> descriptor, Throwable th) {
        this.descriptor = descriptor;
        this.th = th;
    }

    public ActiveDescriptor<?> getDescriptor() {
        return descriptor;
    }

    public Throwable getThrowable() {
        return th;
    }

    @Override
    public String toString() {
        return "ErrorData{" +
                "descriptor=" + descriptor +
                ", th=" + th +
                '}';
    }
}
