package utilities;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class TimeUtility {

    static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public static LocalDateTime convertToLocalDateTime(Instant instant){
        ZoneId myZoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, myZoneId);
        return localDateTime;
    }
    public static Instant convertToInstantUTC(LocalDateTime localDateTime){
        ZoneId myZoneId = ZoneId.systemDefault();
        ZoneOffset myZoneOffset = myZoneId.getRules().getOffset(localDateTime);
        Instant instant = localDateTime.toInstant(myZoneOffset);
        return instant;
    }
    public static LocalDateTime parseStringToLocalDateTime(String string){
        //Example format "2022-10-01T12:00:00"
        LocalDateTime localDateTime = LocalDateTime.parse(string);
        return localDateTime;
    }
    public static Instant parseStringToInstant(String string){
        //Example format "2022-10-01T12:00:00Z"
        // (Z stands for Zero Timezone/UTC)
        Instant instant = Instant.parse(string);
        return instant;
    }
    public static String convertTimestampToLocalDateTimeString(String utcTimestampString){
        //Because the UTC Timestamp String from the database is in the incorrect format
        //I needed to convert the string to UTC format, save the String as a UTC Instant,
        //Convert that Instant to the System LocalDateTime, and then convert that to a String
        //So that I can easily add it into the TableView

        String timestampString = utcTimestampString.replaceAll("\\s", "T");

        Instant instant = TimeUtility.parseStringToInstant(timestampString + "Z");

        LocalDateTime localDateTime = TimeUtility.convertToLocalDateTime(instant);

        String localDateTimeString = localDateTime.toString().replace("T", "     ");

        return localDateTimeString;

    }


}
