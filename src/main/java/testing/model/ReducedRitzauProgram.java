package testing.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: asj
 * Date: 27-08-12
 * Time: 11:17
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "reducedRitzauProgram")
public class ReducedRitzauProgram {
    private String channel_name;
    private Long Id;
    private Date starttid;
    private Date sluttid;
    private String titel;
    private String kortomtale;

    public ReducedRitzauProgram(){}

    public ReducedRitzauProgram(String channel_name, Long id, Date starttid, Date sluttid,
                                String titel, String kortomtale) {
        this.channel_name = channel_name;
        Id = id;
        this.starttid = starttid;
        this.sluttid = sluttid;
        this.titel = titel;
        this.kortomtale = kortomtale;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Date getStarttid() {
        return starttid;
    }

    public void setStarttid(Date starttid) {
        this.starttid = starttid;
    }

    public Date getSluttid() {
        return sluttid;
    }

    public void setSluttid(Date sluttid) {
        this.sluttid = sluttid;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getKortomtale() {
        return kortomtale;
    }

    public void setKortomtale(String kortomtale) {
        this.kortomtale = kortomtale;
    }
}
