package testing.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dth
 * Date: 27-06-13
 * Time: 10:58
 * To change this template use File | Settings | File Templates.
 */
public class DateAdapter extends XmlAdapter<Date, java.sql.Date> {

    @Override
    public java.util.Date marshal(java.sql.Date sqlDate) throws Exception {
        if(null == sqlDate) {
            return null;
        }
        return new java.util.Date(sqlDate.getTime());
    }

    @Override
    public java.sql.Date unmarshal(java.util.Date utilDate) throws Exception {
        if(null == utilDate) {
            return null;
        }
        return new java.sql.Date(utilDate.getTime());
    }

}
