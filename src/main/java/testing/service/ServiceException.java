package testing.service;

/**
 * Created with IntelliJ IDEA.
 * User: asj
 * Date: 16-08-12
 * Time: 11:09
 * To change this template use File | Settings | File Templates.
 */
public class ServiceException extends Exception {
    public ServiceException(String message, Throwable cause) {
        super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public ServiceException(Throwable cause) {
        super(cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public ServiceException(String message) {
        super(message);
    }
}
