package testing.persistence;

/**
 * Created with IntelliJ IDEA.
 * User: asj
 * Date: 16-08-12
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
public class NotInitialisedException extends RuntimeException {
    public NotInitialisedException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public NotInitialisedException(String message) {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
