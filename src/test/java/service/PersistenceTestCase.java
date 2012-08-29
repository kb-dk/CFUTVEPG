package service;

import junit.framework.TestCase;
import testing.service.ServiceException;


/**
 * Created with IntelliJ IDEA.
 * User: asj
 * Date: 17-08-12
 * Time: 13:51
 * To change this template use File | Settings | File Templates.
 */
public class PersistenceTestCase extends TestCase {
    public void setUp() throws ServiceException {
        /*
        File cfgFile = new File("src/test/resources/hibernate.cfg.xml");
        CfuTvHibernateUtil.initialiseFactory(cfgFile);
        CfuTvService service2 = new CfuTvService();
        for (RitzauProgram request: service2.getTop(1000)) {
            service2.delete(request);
        }*/
    }

    public void tearDown() {

    }

    public void testDummy() {}
}
