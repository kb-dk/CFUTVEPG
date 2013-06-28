package testing.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: dth
 * Date: 27-06-13
 * Time: 13:28
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="programs")
@XmlRootElement(name = "Program")
public class Program {
    private int id;
    private String title;
    private String reducedtitle;
    private String description;
    private String reduceddescription;
    private String episodetitle;
    private String language;
    private String forfra;
    private Integer arkiv;
    private Integer dvb;
    private String category;
    private String actors;
    private String extra;
    private Timestamp scheduledstart;
    private String scheduledduration;
    private Timestamp scheduledend;
    private String channelName;
    private String ysEpg;
    private String ysDownload;
    private Date yearanddate;
    private Boolean checked;

    @javax.persistence.Column(name = "id")
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @javax.persistence.Column(name = "title")
    @Basic
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @javax.persistence.Column(name = "reducedtitle")
    @Basic
    public String getReducedtitle() {
        return reducedtitle;
    }

    public void setReducedtitle(String reducedtitle) {
        this.reducedtitle = reducedtitle;
    }

    @javax.persistence.Column(name = "description")
    @Basic
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @javax.persistence.Column(name = "reduceddescription")
    @Basic
    public String getReduceddescription() {
        return reduceddescription;
    }

    public void setReduceddescription(String reduceddescription) {
        this.reduceddescription = reduceddescription;
    }

    @javax.persistence.Column(name = "episodetitle")
    @Basic
    public String getEpisodetitle() {
        return episodetitle;
    }

    public void setEpisodetitle(String episodetitle) {
        this.episodetitle = episodetitle;
    }

    @javax.persistence.Column(name = "language")
    @Basic
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @javax.persistence.Column(name = "forfra")
    @Basic
    public String getForfra() {
        return forfra;
    }

    public void setForfra(String forfra) {
        this.forfra = forfra;
    }

    @javax.persistence.Column(name = "arkiv")
    @Basic
    public Integer getArkiv() {
        return arkiv;
    }

    public void setArkiv(Integer arkiv) {
        this.arkiv = arkiv;
    }

    @javax.persistence.Column(name = "dvb")
    @Basic
    public Integer getDvb() {
        return dvb;
    }

    public void setDvb(Integer dvb) {
        this.dvb = dvb;
    }

    @javax.persistence.Column(name = "category")
    @Basic
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @javax.persistence.Column(name = "actors")
    @Basic
    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    @javax.persistence.Column(name = "extra")
    @Basic
    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @javax.persistence.Column(name = "scheduledstart")
    @Basic
    public Timestamp getScheduledstart() {
        return scheduledstart;
    }

    public void setScheduledstart(Timestamp scheduledstart) {
        this.scheduledstart = scheduledstart;
    }

    @javax.persistence.Column(name = "scheduledduration")
    @Basic
    public String getScheduledduration() {
        return scheduledduration;
    }

    public void setScheduledduration(String scheduledduration) {
        this.scheduledduration = scheduledduration;
    }

    @javax.persistence.Column(name = "scheduledend")
    @Basic
    public Timestamp getScheduledend() {
        return scheduledend;
    }

    public void setScheduledend(Timestamp scheduledend) {
        this.scheduledend = scheduledend;
    }

    @javax.persistence.Column(name = "channel_name")
    @Basic
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @javax.persistence.Column(name = "ys_epg")
    @Basic
    public String getYsEpg() {
        return ysEpg;
    }

    public void setYsEpg(String ysEpg) {
        this.ysEpg = ysEpg;
    }

    @javax.persistence.Column(name = "ys_download")
    @Basic
    public String getYsDownload() {
        return ysDownload;
    }

    public void setYsDownload(String ysDownload) {
        this.ysDownload = ysDownload;
    }

    @javax.persistence.Column(name = "yearanddate")
    @Basic
    public Date getYearanddate() {
        return yearanddate;
    }

    public void setYearanddate(Date yearanddate) {
        this.yearanddate = yearanddate;
    }

    @javax.persistence.Column(name = "checked")
    @Basic
    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Program program = (Program) o;

        if (id != program.id) return false;
        if (actors != null ? !actors.equals(program.actors) : program.actors != null) return false;
        if (arkiv != null ? !arkiv.equals(program.arkiv) : program.arkiv != null) return false;
        if (category != null ? !category.equals(program.category) : program.category != null) return false;
        if (channelName != null ? !channelName.equals(program.channelName) : program.channelName != null)
            return false;
        if (checked != null ? !checked.equals(program.checked) : program.checked != null) return false;
        if (description != null ? !description.equals(program.description) : program.description != null)
            return false;
        if (dvb != null ? !dvb.equals(program.dvb) : program.dvb != null) return false;
        if (episodetitle != null ? !episodetitle.equals(program.episodetitle) : program.episodetitle != null)
            return false;
        if (extra != null ? !extra.equals(program.extra) : program.extra != null) return false;
        if (forfra != null ? !forfra.equals(program.forfra) : program.forfra != null) return false;
        if (language != null ? !language.equals(program.language) : program.language != null) return false;
        if (reduceddescription != null ? !reduceddescription.equals(program.reduceddescription) : program.reduceddescription != null)
            return false;
        if (reducedtitle != null ? !reducedtitle.equals(program.reducedtitle) : program.reducedtitle != null)
            return false;
        if (scheduledduration != null ? !scheduledduration.equals(program.scheduledduration) : program.scheduledduration != null)
            return false;
        if (scheduledend != null ? !scheduledend.equals(program.scheduledend) : program.scheduledend != null)
            return false;
        if (scheduledstart != null ? !scheduledstart.equals(program.scheduledstart) : program.scheduledstart != null)
            return false;
        if (title != null ? !title.equals(program.title) : program.title != null) return false;
        if (yearanddate != null ? !yearanddate.equals(program.yearanddate) : program.yearanddate != null)
            return false;
        if (ysDownload != null ? !ysDownload.equals(program.ysDownload) : program.ysDownload != null) return false;
        if (ysEpg != null ? !ysEpg.equals(program.ysEpg) : program.ysEpg != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (reducedtitle != null ? reducedtitle.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (reduceddescription != null ? reduceddescription.hashCode() : 0);
        result = 31 * result + (episodetitle != null ? episodetitle.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (forfra != null ? forfra.hashCode() : 0);
        result = 31 * result + (arkiv != null ? arkiv.hashCode() : 0);
        result = 31 * result + (dvb != null ? dvb.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (actors != null ? actors.hashCode() : 0);
        result = 31 * result + (extra != null ? extra.hashCode() : 0);
        result = 31 * result + (scheduledstart != null ? scheduledstart.hashCode() : 0);
        result = 31 * result + (scheduledduration != null ? scheduledduration.hashCode() : 0);
        result = 31 * result + (scheduledend != null ? scheduledend.hashCode() : 0);
        result = 31 * result + (channelName != null ? channelName.hashCode() : 0);
        result = 31 * result + (ysEpg != null ? ysEpg.hashCode() : 0);
        result = 31 * result + (ysDownload != null ? ysDownload.hashCode() : 0);
        result = 31 * result + (yearanddate != null ? yearanddate.hashCode() : 0);
        result = 31 * result + (checked != null ? checked.hashCode() : 0);
        return result;
    }
}
