package testing.service;

import dk.statsbiblioteket.digitaltv.access.model.RitzauProgram;
import org.slf4j.LoggerFactory;
import testing.model.ReducedRitzauProgram;
import testing.persistence.CfuTvDAO;
import testing.persistence.CfuTvHibernateUtil;
import testing.persistence.NotInitialisedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: asj
 * Date: 16-08-12
 * Time: 11:03
 * To change this template use File | Settings | File Templates.
 */
public class CfuTvService {
    private final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

    public void insert(RitzauProgram request) throws ServiceException{
        if (request.getSluttid().after(request.getStarttid())) {
            throw new ServiceException("fromTime (" + request.getStarttid() + ") must not be after toTime (" + request.getSluttid() + ")");
        }
        try {
            getDao().create(request);
        } catch (NotInitialisedException ex) {
            throw new ServiceException(ex);
        }
    }

    public void update(RitzauProgram request) throws ServiceException{
        try {
            getDao().update(request);
        } catch (NotInitialisedException ex) {
            throw new ServiceException(ex);
        }
    }

    public void delete(RitzauProgram request) throws ServiceException{
        try {
            getDao().delete(request);
        } catch (NotInitialisedException ex) {
            throw new ServiceException(ex);
        }
    }

    public List<ReducedRitzauProgram> search(String channel_name, Date from, Date to, String title,
                                             String description) throws ServiceException{
        try{
            List<RitzauProgram> fullPrograms = getDao().search(channel_name, from, to, title, description);
            List<ReducedRitzauProgram> result = new ArrayList<ReducedRitzauProgram>();
            for(RitzauProgram rp:fullPrograms){
                result.add(new ReducedRitzauProgram(rp.getChannel_name(),rp.getId(),rp.getStarttid(),
                        rp.getSluttid(),rp.getTitel(),rp.getKortomtale()));
            }
            return result;
        } catch(NotInitialisedException ex){
            throw new ServiceException(ex);
        }
    }

    private CfuTvDAO getDao() throws NotInitialisedException{
        return new CfuTvDAO(CfuTvHibernateUtil.getInitialisedFactory());
    }
}
