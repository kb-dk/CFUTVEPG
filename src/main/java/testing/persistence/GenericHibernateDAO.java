package testing.persistence;

import dk.statsbiblioteket.digitaltv.utils.db.GenericDAO;
import org.hibernate.Session;

/**
 * Created with IntelliJ IDEA.
 * User: asj
 * Date: 16-08-12
 * Time: 09:47
 * To change this template use File | Settings | File Templates.
 */
public class GenericHibernateDAO<T, PK extends java.io.Serializable> implements GenericDAO<T, PK> {
    private CfuTvHibernateUtil util;
    private Class<T> type;

    public GenericHibernateDAO(Class<T> type, CfuTvHibernateUtil util){
        this.type = type;
        this.util = util;
    }


    public PK create(T o) {
        Session sess = getSession();
        PK key;
        try{
            sess.beginTransaction();
            key = (PK) sess.save(o);
            sess.getTransaction().commit();
        } finally {
            sess.close();
        }
        return key;
    }


    public T read(PK pk) {
        Session sess = getSession();
        T result = (T) sess.get(type, pk);
        sess.close();
        return result;
    }


    public void update(T o) {
        Session sess = getSession();
        sess.beginTransaction();
        sess.update(o);
        sess.getTransaction().commit();
        sess.close();
    }


    public void delete(T o) {
        Session sess = getSession();
        sess.beginTransaction();
        sess.delete(o);
        sess.getTransaction().commit();
        sess.close();
    }

    protected Session getSession(){
        return util.getSession();
    }
}
