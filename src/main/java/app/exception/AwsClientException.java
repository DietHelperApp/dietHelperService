package app.exception;

public class AwsClientException extends Exception {

    public AwsClientException(Exception exception) {
        super("failed call to aws client", exception);
    }
}
