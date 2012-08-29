package persistence;

import dk.statsbiblioteket.digitaltv.access.model.RitzauProgram;
import junit.framework.TestCase;
import testing.persistence.CfuTvDAO;
import testing.persistence.CfuTvHibernateUtil;
import testing.persistence.NotInitialisedException;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: asj
 * Date: 17-08-12
 * Time: 09:54
 * To change this template use File | Settings | File Templates.
 */
public class CfuTvDAOTest extends TestCase {
    private CfuTvDAO dao;

    public void setUp() throws NotInitialisedException{
        File cfgFile = new File("src/test/resources/hibernate.cfg.xml");
        CfuTvHibernateUtil util = CfuTvHibernateUtil.initialiseFactory(cfgFile);
        dao = new CfuTvDAO(CfuTvHibernateUtil.getInitialisedFactory());
    }

    public void testGetAllRequests(){ /*
        Date d1 = new Date(100, 1, 1);
        Date d2 = new Date(100, 3, 1);
        Date d3 = new Date(100, 7, 1);
        Date d4 = new Date(100, 11, 1);
        Date d5 = new Date(101, 2, 1);
        Date d6 = new Date(101, 4, 1);
        Date d7 = new Date(102, 1, 1);
        Date d8 = new Date(102, 10, 1);
        RitzauProgram r1 = new RitzauProgram();
        r1.setTitel("rT1");
        r1.setKortomtale("Boring");
        r1.setStarttid(d1);
        r1.setSluttid(d8);
        r1.setChannel_name("r1");
        RitzauProgram r2 = new RitzauProgram();
        r2.setTitel("rT2");
        r2.setKortomtale("Not so boring");
        r2.setStarttid(d2);
        r2.setSluttid(d3);
        r2.setChannel_name("r2");
        RitzauProgram r3 = new RitzauProgram();
        r3.setTitel("rT3");
        r3.setKortomtale("Somewhat interesting");
        r3.setStarttid(d4);
        r3.setSluttid(d5);
        r3.setChannel_name("r3");
        RitzauProgram r4 = new RitzauProgram();
        r4.setTitel("rT4");
        r4.setKortomtale("Awesome");
        r4.setStarttid(d6);
        r4.setSluttid(d7);
        r4.setChannel_name("r4");
        CfuTvDAO carDAO = dao;
        carDAO.create(r1); carDAO.create(r2); carDAO.create(r3); carDAO.create(r4);
        final List<RitzauProgram> requests = carDAO.getTop(100);
        if(requests != null){
            System.out.println(requests.size());
            assertTrue(requests.size() > 0);
        } else {
            System.out.println("requests list == null");
        }
        for(RitzauProgram c:requests){
            System.out.println(c.getId() + " | " + c.getChannel_name() + " | " + c.getStarttid() + " | " +
                    c.getSluttid() + " | " + c.getTitel() +
                    " | " + c.getKortomtale());
        }  */
    }
}
