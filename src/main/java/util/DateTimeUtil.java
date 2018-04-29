package util;

public class DateTimeUtil {

    public static String incrementTimeByTwoHours(String originalDateTime) {
        StringBuilder result = new StringBuilder(originalDateTime.length());
        int beginTime = originalDateTime.lastIndexOf('T') + 1;
        String originalTime = originalDateTime.substring(beginTime, originalDateTime.length() - 1);
        int originalHours = Integer.parseInt(originalTime.substring(0,2));
        result.append(originalDateTime.substring(0, beginTime));
        result.append(originalHours + 2);
        String restOfDateTimeAfterHours = originalDateTime.substring(originalDateTime.indexOf(':'));
        result.append(restOfDateTimeAfterHours);
        return result.toString();
    }

}
