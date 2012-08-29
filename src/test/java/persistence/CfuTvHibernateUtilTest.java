package persistence;

import junit.framework.TestCase;
import testing.persistence.CfuTvHibernateUtil;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: asj
 * Date: 17-08-12
 * Time: 09:39
 * To change this template use File | Settings | File Templates.
 */
public class CfuTvHibernateUtilTest extends TestCase {
    public void testInitialiseFactory() throws Exception{
        File cfgFile = new File("src/test/resources/hibernate.cfg.xml");
        CfuTvHibernateUtil util = CfuTvHibernateUtil.initialiseFactory(cfgFile);
    }

    public void testGetSessionFactory() throws Exception{
        File cfgFile = new File("src/test/resources/hibernate.cfg.xml");
        CfuTvHibernateUtil util = CfuTvHibernateUtil.initialiseFactory(cfgFile);
        util.getSessionFactory();
    }

    public void testGetSession() throws Exception{
        File cfgFile = new File("src/test/resources/hibernate.cfg.xml");
        CfuTvHibernateUtil util = CfuTvHibernateUtil.initialiseFactory(cfgFile);
        util.getSession();
    }
}
