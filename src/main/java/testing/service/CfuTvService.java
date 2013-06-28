package testing.service;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testing.GlobalData;
import testing.model.Program;
import testing.persistence.CfuTvDAO;

import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: asj
 * Date: 16-08-12
 * Time: 11:03
 * To change this template use File | Settings | File Templates.
 */
public class CfuTvService {
    private Logger log;
    private CfuTvDAO cfuTvDAO = new CfuTvDAO();
    private String baseUrl = GlobalData.getYouSeeAccessUrl();
    private String extension = ".ts"; //extension of downloaded file, .ts chosen as it is also used in preexisting yousee stuff

    public CfuTvService(){
        log = LoggerFactory.getLogger(CfuTvService.class);
    }

    /**
     * Search for RitzauPrograms in the database and return a list of a reduced version of RitzauProgram.
     * @param channel_name Which channel to search for.
     * @param from Start of search interval, includes year, month, day, hour and minutes.
     * @param to End of search interval.
     * @param title Which title to search for.
     * @param description Phrase or word in description of the program. Not case sensitive.
     * @return A reduced version of a RitzauProgram containing channel name, id, start and end time, title and short description.
     */
    public List<Program> search(String channel_name, Date from, Date to, String title,
                                String description) {
        List<Program> programs = cfuTvDAO.search(channel_name, from, to, title, description);
        return programs;
    }

    /**
     * Find a RitzauProgram in the database and uses the PBCoreGenerator to return PBCore xml of found program.
     * @param programId programId of the wanted RitzauProgram.
     * @return full PBCore xml (as defined by the template) of a RitzauProgram.
     */
    public String getFullPost(Integer programId){
        Program program = cfuTvDAO.getByFullId(programId);
        PBCoreGenerator generator = new PBCoreGenerator();
        return generator.generateXmlFromTemplate(program);
    }

    /**
     * Finds and uploads a program, based on id and offsets, to the FtpServer as a file with requested filename, along
     * with PBCore xml about the program.
     * Returns a status code depending on whether it was successful.
     * @param programId Id of wanted program.
     * @param fileName Wanted filename.
     * @param offsetStart Offset from start of the program.
     * @param offsetEnd Offset from end of the program.
     * @return Status code depending on whether it was successful or not.
     */
    public int getProgramSnippet(Integer programId, String fileName, Date offsetStart, Date offsetEnd){
        log.info("----------------getProgramSnippet method called---------------");
        int statusCode = 1;
        Program program;

        program = cfuTvDAO.getByFullId(programId);

        if(program == null){
            return -0; //program is null...which error code would that be?
        }
        String sBChannelId = program.getChannelName();
        String youSeeChannelId = program.getYsDownload();
        youSeeChannelId = youSeeChannelId.replaceAll(" ","%20"); //Replace space with http equivalent
        String channelUrlPart = youSeeChannelId + "_"; //ChannelId part of the url.
        Date startTid;
        Date slutTid;

        startTid = program.getScheduledstart();
        slutTid = program.getScheduledend();
        //offsetting start
        startTid.setSeconds(startTid.getSeconds() - offsetStart.getSeconds());
        startTid.setMinutes(startTid.getMinutes() - offsetStart.getMinutes());
        startTid.setHours(startTid.getHours() - offsetStart.getHours());
        //offsetting end
        slutTid.setSeconds(slutTid.getSeconds() + offsetEnd.getSeconds());
        slutTid.setMinutes(slutTid.getMinutes() + offsetEnd.getMinutes());
        slutTid.setHours(slutTid.getHours() + offsetEnd.getHours());
        //offsetting complete
        String fromUrlPart = dateToUrlPart(startTid) + "_"; //From part of the url.
        String toUrlPart = dateToUrlPart(slutTid) + extension; //To part of the url.
        String downloadUrl = baseUrl + channelUrlPart + fromUrlPart + toUrlPart; //Putting the parts together.
        String xml = getFullPost(programId);
        statusCode = downloadFileByStream(fileName, downloadUrl, xml); //Actual file handling.
        return statusCode;
    }

    /**
     * Cuts and uploads a interval on a channel, based on channel and interval, to the FtpServer as a file with
     * requested filename.
     * Returns a status code depending on whether it was successful.
     * @param sBChannelId Which channel.
     * @param fileName Wanted filename.
     * @param from Start of the cut.
     * @param to End of the cut.
     * @return Status code depending on whether it was successful or not.
     */
    public int getRawCut(String sBChannelId,String fileName,Date from,Date to){
        log.info("----------------getProgramSnippet method called---------------");
        int statusCode = 1;
        String youSeeChannelId;

        youSeeChannelId = cfuTvDAO.search(sBChannelId, null, null, null, null).get(0).getYsDownload();
        youSeeChannelId = youSeeChannelId.replaceAll(" ","%20"); //Replace space with http equivalent
        String channelUrlPart = youSeeChannelId + "_"; //ChannelId part of the url.
        String fromUrlPart = dateToUrlPart(from) + "_"; //From part of the url.
        String toUrlPart = dateToUrlPart(to) + extension; //To part of the url.
        String downloadUrl = baseUrl + channelUrlPart + fromUrlPart + toUrlPart; //Putting the parts together.
        statusCode = downloadFileByStream(fileName, downloadUrl,null); //Actual file handling.
        return statusCode;
    }

    /**
     * Returns an ArrayList of all files found at download destination.
     * @return ArrayList of all files found at download destination.
     */
    public ArrayList<File> getStatusAll(){
        String location = GlobalData.getDownloadDestination();
        File dir = new File(location);
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isFile();
            }
        };
        File[] files = dir.listFiles(filter);
        if(files == null){ //Either dir does not exist or it is not a directory.
            return null;
        }
        return new ArrayList<File>(Arrays.asList(files));
    }

    /**
     * Returns the file found at download destination or null if no file with that filename is found.
     * @param filename wanted file, remember to include extension.
     * @return File found at download destination or null.
     */
    public File getStatus(String filename){
        String location = GlobalData.getDownloadDestination() + filename;
        File result = new File(location);
        return result;
    }

    /**
     * Downloads and places a file and associated xml from inputUrl in download destination with filename.
     * Returns a status code depending on how it went.
     * Status codes:
     * 200: OK.
     * 400: Bad information in url.
     * 404: Content not available in main archive. Try again later.
     * 409: A file with chosen filename already exists on FtpServer.
     * 410: Content not available.
     * 500: Internal server error or unexpected status code.
     * @param filename Wanted file name.
     * @param inputUrl Url to download from.
     * @param xml Associated xml.
     * @return Status code.
     */
    private int downloadFileByStream(String filename, String inputUrl, String xml){
        log.info("------------downloadFileByStream called with " + inputUrl +"--------------");
        String targetLocation = GlobalData.getDownloadDestination(); //So you only have to change it one place instead of two.
        String target = targetLocation+filename+extension;
        log.info("------------target = " + target + "--------------");
        File tmp = null;
        tmp = new File(target);
        if(tmp.exists()){
            log.info("------------Error 409: File already exists. " + target + "--------------");
            return 409; //Error 409 Conflict: File already exists
        }
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(inputUrl);
        InputStream reader = null;
        try{
            client.executeMethod(method);
            reader = method.getResponseBodyAsStream();
        } catch(HttpException ex){
            method.releaseConnection(); //Not sure if this is needed here, but just to be sure.
        } catch(IOException ex){
            method.releaseConnection(); //Not sure if this is needed here, but just to be sure.
        }
        int statusCode = method.getStatusCode();
        if(statusCode == 200){ //StatusCode okay, downloading the file.
            new DownloadService(target, method, reader, xml, targetLocation, filename).start();
        }
        return statusCode;
    }

    private Date convertToUTC(Date date){
        Locale locale = Locale.getDefault();
        TimeZone currentTimeZone = TimeZone.getDefault();
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT,locale);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String utctime = formatter.format(date);
        Date utcDate = null;
        try {
            utcDate = new SimpleDateFormat("d-M-y H:m:s").parse(utctime);
        } catch (ParseException e) {
        }
        return utcDate;
    }

    /**
     * Translates a date to a String that looks like part of the url needed to access the download web page.
     * @param date to be translated.
     * @return String that looks like part of the url needed to access the download web page.
     */
    private String dateToUrlPart(Date date){
        String result = "";
        //Year
        int year = date.getYear() + 1900; //Adjust for date.getYears() getting years since 1900.
        result += year;
        //Month
        int month = date.getMonth() + 1; //Adjusting for date.getMonth() starting with 0 instead of 1.
        if(month < 10){
            result += "0"; //Range = 1-9, so would give f.ex. 8 instead of 08, so fixing that.
        }
        result += month;
        //Day
        int day = date.getDate();
        if(day < 10){
            result += "0"; //Range = 1-9, so would give f.ex. 8 instead of 08, so fixing that.
        }
        result += day + "_";
        //Hour
        int hour = date.getHours();
        if(hour < 10){
            result += "0"; //Range = 1-9 would give f.ex. 8 instead of 08, so fixing that.
        }
        result += hour;
        //Minutes
        int minutes = date.getMinutes();
        if(minutes < 10){
            result += "0"; //Range = 1-9 would give f.ex. 8 instead of 08, so fixing that.
        }
        result += minutes;
        //Seconds
        int seconds = date.getSeconds();
        if(seconds < 10){
            result += "0"; //Range = 1-9 would give f.ex. 8 instead of 08, so fixing that.
        }
        result += seconds;
        return result;
    }

}
