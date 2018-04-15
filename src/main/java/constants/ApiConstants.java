package constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ApiConstants {

    public static final String BASE_ENDPOINT = "http://api.football-data.org/v1/";
    public static final String COMPETITIONS_ENDPOINT = BASE_ENDPOINT + "competitions/";
    public static final String LEAGUE_TABLE_ENDPOINT = COMPETITIONS_ENDPOINT + "%d/leagueTable";
    public static final String FIXTURES_BY_TEAM_NEXT_N_MATCHES_ENDPOINT = BASE_ENDPOINT + "teams/%d/fixtures?timeFrame=n%d";

    public static final String getFixturesByTeamNextNMatchesEndpoint(int teamId, int numberOfDaysInTimeframe) {
        return String.format(FIXTURES_BY_TEAM_NEXT_N_MATCHES_ENDPOINT, teamId, numberOfDaysInTimeframe);
    }

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

    public static String getLeagueTableEndpoint(int leagueId) { return String.format(LEAGUE_TABLE_ENDPOINT, leagueId); }

}
