package discomfortdeliverer.translation_app.exceptions;

public class InternalServiceException extends Exception{
    public InternalServiceException(Throwable cause) {
        super(cause);
    }

    public InternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
