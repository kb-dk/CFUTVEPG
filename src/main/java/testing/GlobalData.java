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
