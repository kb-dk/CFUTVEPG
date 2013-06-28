package testing.persistence;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import testing.GlobalData;
import testing.model.Program;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: asj
 * Date: 16-08-12
 * Time: 11:16
 * To change this template use File | Settings | File Templates.
 */
public class CfuTvDAO{
    /**
     * Search for RitzauPrograms in the database and return a list of programs matching input data.
     * @param channel_name
     * @param from
     * @param to
     * @param title
     * @param description Kortomtale, langomtale1 eller langomtale2.
     * @return List of RitzauPrograms matching input data
     */
    public List<Program> search(String channel_name, Date from, Date to, String title,
                                      String description){
        Session session = CfuTvHibernateUtil.getInitialisedFactory().getSession();
        Criteria criteria = session.createCriteria(Program.class);

        List<String> list = GlobalData.getAllowedChannels();
        Criterion sbChannelId = Restrictions.in("channelName",list);
        criteria.add(sbChannelId);
        Criterion daysBack = Restrictions.ge("scheduledstart", GlobalData.getDaysBack());
        criteria.add(daysBack);
        if(channel_name != null && channel_name.trim().length() != 0){
            Criterion channel_criterion = Restrictions.eq("channelName",channel_name);
            criteria.add(channel_criterion);
        }
        if(from != null){
            Criterion from_criterion = Restrictions.ge("scheduledstart",from);
            criteria.add(from_criterion);
        }
        if(to != null){
            Criterion to_criterion = Restrictions.le("scheduledend", to);
            criteria.add(to_criterion);
        }
        if(title != null && title.trim().length() != 0){
            Criterion title_criterion = Restrictions.or(Restrictions.like("title", "%"+title+"%"),Restrictions.like("reducedtitle", "%"+title+"%"));
            criteria.add(title_criterion);
        }
        if(description != null && description.trim().length() != 0){
            Criterion description_criterion1 = Restrictions.or(Restrictions.like("description","%"+description+"%"),Restrictions.like("reduceddescription","%"+description+"%"));
            criteria.add(description_criterion1);
        }

        return (List<Program>) criteria.list();
    }

    /**
     * Finds and returns a single RitzauProgram based on id.
     * @param id which program.
     * @return Found RitzauProgram.
     */
    public Program getByFullId(Integer id){
        Criteria criteria = CfuTvHibernateUtil.getInitialisedFactory().getSession().createCriteria(Program.class);
        Criterion daysBack = Restrictions.ge("scheduledstart", GlobalData.getDaysBack());
        criteria.add(daysBack);
        if(id != null){
            Criterion id_criterion = Restrictions.eq("id", id);
            criteria.add(id_criterion);
        }
        return (Program) criteria.uniqueResult();
    }
}
