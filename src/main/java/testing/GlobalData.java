package testing;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: asj
 * Date: 28-08-12
 * Time: 13:42
 * To change this template use File | Settings | File Templates.
 */
public class GlobalData{
    private static List<String> allowedChannels;
    private static Date daysBack;
    private static String pathToTemplate;
    private static String pathToFtpServer;

    public static String getPathToFtpServer() {
        return pathToFtpServer;
    }

    public static void setPathToFtpServer(String pathToFtpServer) {
        GlobalData.pathToFtpServer = pathToFtpServer;
    }

    public static String getPathToTemplate() {
        return pathToTemplate;
    }

    public static void setPathToTemplate(String pathToTemplate) {
        GlobalData.pathToTemplate = pathToTemplate;
    }

    public static List<String> getAllowedChannels() {
        return allowedChannels;
    }

    public static void setAllowedChannels(List<String> allowedChannels) {
        GlobalData.allowedChannels = allowedChannels;
    }

    public static Date getDaysBack() {
        return daysBack;
    }

    public static void setDaysBack(Date daysBack) {
        GlobalData.daysBack = daysBack;
    }
}
