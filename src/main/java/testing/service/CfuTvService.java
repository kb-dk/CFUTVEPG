package testing.service;

import dk.statsbiblioteket.digitaltv.access.model.RitzauProgram;
import org.slf4j.LoggerFactory;
import testing.GlobalData;
import testing.model.ReducedRitzauProgram;
import testing.persistence.CfuTvDAO;
import testing.persistence.CfuTvHibernateUtil;
import testing.persistence.NotInitialisedException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

    public String getFullPost(Long programId) throws ServiceException{
        try{
            RitzauProgram program = getDao().getByFullId(programId);
            boolean tvMeterAvailable = getDao().hasTvmeter(programId);
            PBCoreGenerator generator = new PBCoreGenerator();
            return generator.generateXmlFromTemplate(program,tvMeterAvailable);
        } catch(NotInitialisedException ex){
            throw new ServiceException(ex);
        }
    }

    public boolean getProgramSnippet(Long programId, String fileName) throws ServiceException{
        String xml = getFullPost(programId);
        try{
            FileWriter fstream = new FileWriter(GlobalData.getPathToFtpServer()+fileName+".xml");
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(xml);
            out.close();
        } catch(IOException ex){
            throw new ServiceException(ex);
        }
        return true;
    }

    private CfuTvDAO getDao() throws NotInitialisedException{
        return new CfuTvDAO(CfuTvHibernateUtil.getInitialisedFactory());
    }
}
