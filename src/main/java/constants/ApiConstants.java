package constants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ApiConstants {

    static final Logger logger = LogManager.getLogger(ApiConstants.class);

    public static final int EPL_LEAGUE_API_ID = 445;

    public static final String LEAGUE_TABLE              = "leagueTable";
    public static final String BASE_ENDPOINT             = "http://api.football-data.org/v1/";
    public static final String COMPETITIONS_ENDPOINT     = BASE_ENDPOINT + "competitions/";
    public static final String FIXTURES_BY_TEAM_ENDPOINT = BASE_ENDPOINT + "/teams/%d/fixtures?timeFrameStart=%s&timeFrameEnd=%s";
    public static final String TEAM_IDS_ENDPOINT         = COMPETITIONS_ENDPOINT + "%d/teams/";

    public static String getApiKey() throws IOException {
        File configFile = new File("src/main/resources/config.cfg");
        if (!configFile.exists()) {
            throw new RuntimeException("Cannot locate config file in src/main/resources directory!");
        }

        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("API_KEY")) {
                int index = line.indexOf("=");
                if (index > -1) {
                    return line.substring(index + 1);
                }
                throw new RuntimeException("Could not parse API_KEY field in config file!");
            }
        }
        throw new RuntimeException("Did not find API_KEY field in config file!");
    }

    public static String getTeamIdsEndpoint(int leagueId) {
        return String.format(TEAM_IDS_ENDPOINT, leagueId);
    }

    /*
     * Returns URL to retrieve all upcoming matches for a team between today and last day getEnumByStringName league play
     */
    public static String getFixturesByTeamEndpoint(int teamId) {
        return String.format(FIXTURES_BY_TEAM_ENDPOINT, teamId,
                OtherConstants.todaysDate(), OtherConstants.PREMIER_LEAGUE_LAST_MATCHDAY_DATE);
    }

    /*
     * TODO: Returns URL to retrieve all upcoming matches for a team between startDate and endDate
     * */
    public static String getFixturesByTeamEndpointStartAndEndDateSpecified(int teamId, String startDate, String endDate) {
        return null;
    }



}
