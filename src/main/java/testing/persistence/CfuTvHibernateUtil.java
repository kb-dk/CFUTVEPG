package testing.persistence;

import dk.statsbiblioteket.mediaplatform.ingest.model.persistence.HibernateUtilIF;
import dk.statsbiblioteket.mediaplatform.ingest.model.persistence.NotInitialiasedException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import testing.model.Program;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: asj
 * Date: 16-08-12
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */
public class CfuTvHibernateUtil implements HibernateUtilIF {
    private static SessionFactory sessionFactory;

    private CfuTvHibernateUtil() {}

    public static CfuTvHibernateUtil getInitialisedFactory(){
        if(sessionFactory != null && !sessionFactory.isClosed()){
            return new CfuTvHibernateUtil();
        } else {
            throw new NotInitialiasedException("Attempt to access uninitialised Hibernate utility.");
        }
    }

    /**
     * Initialise hibernate from a configuration file on first call. Subsequent calls will not reinitialise the
     * hibernate connection unless the sessionFactory is first closed.
     * @param cfgFile  The configuration file.
     * @return An instance of this class.
     */
    public static CfuTvHibernateUtil initialiseFactory(File cfgFile) {
        if(sessionFactory == null || sessionFactory.isClosed()){
            try{
                if(sessionFactory == null){
                    AnnotationConfiguration configuration = (new AnnotationConfiguration()).configure(cfgFile);
                    configuration.addAnnotatedClass(Program.class);
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

    /**
     * Gets a session factory
     * @return
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Gets a hibernate session. This class must be initialised before this method is called.
     * @return a Session
     */
    public Session getSession() {
        if(sessionFactory == null || sessionFactory.isClosed()){
            throw new RuntimeException("Attempt to use CfuTvHibernateUtil before it was initialised" +
                    "or after sessionFactory was closed");
        }
        return sessionFactory.openSession();
    }
}
