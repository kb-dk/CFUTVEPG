package testing.persistence;

import dk.statsbiblioteket.digitaltv.access.model.RitzauProgram;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: asj
 * Date: 16-08-12
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */
public class CfuTvHibernateUtil {
    private static SessionFactory sessionFactory;

    private CfuTvHibernateUtil() {}

    public static CfuTvHibernateUtil getInitialisedFactory(){
        if(sessionFactory != null && !sessionFactory.isClosed()){
            return new CfuTvHibernateUtil();
        } else {
            throw new NotInitialisedException("Attempt to access uninitialised Hibernate utility.");
        }
    }

    public static CfuTvHibernateUtil initialiseFactory(File cfgFile) {
        if(sessionFactory == null || sessionFactory.isClosed()){
            try{
                if(sessionFactory == null){
                    AnnotationConfiguration configuration = (new AnnotationConfiguration()).configure(cfgFile);
                    configuration.addAnnotatedClass(RitzauProgram.class);
                    sessionFactory = configuration.buildSessionFactory();
                }
            } catch (Throwable ex){
                System.err.println("Initial SessionFactory creation failed. " + ex);
                ex.printStackTrace();
                throw new ExceptionInInitializerError(ex);
            }
        }
        return new CfuTvHibernateUtil();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public Session getSession() {
        if(sessionFactory == null || sessionFactory.isClosed()){
            throw new RuntimeException("Attempt to use CfuTvHibernateUtil before it was initialised" +
                    "or after sessionFactory was closed");
        }
        return sessionFactory.openSession();
    }
}
