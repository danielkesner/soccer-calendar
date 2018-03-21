package constants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OtherConstants {

    public static final String PREMIER_LEAGUE_LAST_MATCHDAY_DATE = "2018-05-13";

    public static String todaysDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
}
