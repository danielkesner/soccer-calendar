package client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigService;
import constants.ApiConstants;
import model.CompetitionEnum;
import model.Fixture;
import model.Team;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FootballDataRestClient {

    private static final Logger logger = LogManager.getLogger(FootballDataRestClient.class);
    private static ObjectMapper mapper = new ObjectMapper();
    private int restCallCounter = 0;

    public int getRestCallCounter() { return restCallCounter; }

    private JsonNode makeRequest(String _url) throws IOException {
        URL url = new URL(_url);
        HttpURLConnection apiConnection = (HttpURLConnection) url.openConnection();
        apiConnection.setRequestMethod("GET");
        apiConnection.setRequestProperty("X-Auth-Token", ApiConstants.getApiKey());
        if (ConfigService.debug()) {
            logger.info("Made API call to the following endpoint: " + _url);
            logger.info("Total calls for this client instance: " + ++restCallCounter);
        }
        JsonNode response = null;
        InputStream stream = null;
        try {
            stream = apiConnection.getInputStream();
            response =  mapper.readTree(stream);
        } catch (Exception ex) {
            logger.info(stream != null ? IOUtils.toString(stream, (Charset)null)
                : "InputStream object is null!");
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return response;
    }

    /* Returns the raw payload from http://api.football-data.org/v1/competitions/ */
    public JsonNode getCompetitionsNode() throws IOException {
        return makeRequest(ApiConstants.COMPETITIONS_ENDPOINT);
    }

    /* Returns the raw payload from /v1/competitions/{id}/leagueTable */
    public JsonNode getLeagueTableByCompetitionId(int competitionId) throws IOException {
        return makeRequest(ApiConstants.getLeagueTableEndpoint(competitionId));
    }

    /* Returns the raw payload from /v1/teams/{teamId}/fixtures?timeFrame=n30
     * (default to next 30 days) */
    public JsonNode getFixtureNodeByTeam(Team team) throws IOException {
        return makeRequest(ApiConstants.getFixturesByTeamNextNMatchesEndpoint(team.getTeamApiId(), 30));
    }

    public List<Fixture> getUpcomingMatchesByTeam(Team team, CompetitionEnum competitionEnum) {
        List<Fixture> upcomingFixturesList = new ArrayList<Fixture>();

        JsonNode upcomingFixturesNode = null;
        try {
            upcomingFixturesNode = getFixtureNodeByTeam(team);
        } catch (IOException ioe) {
        }

        if (upcomingFixturesNode == null) {
            throw new RuntimeException("Failed to create upcomingFixtures JsonNode in getUpcomingMatchesByTeam()!");
        }

        for (JsonNode fixture : upcomingFixturesNode.get("fixtures")) {
            Fixture currentFixture = new Fixture();
            String date = fixture.get("date").asText().replace("\"", "");
            String homeTeamName = fixture.get("homeTeamName").asText().replaceAll("\"", "");
            String awayTeamName = fixture.get("awayTeamName").asText().replaceAll("\"", "");

            int homeTeamApiId;
            int awayTeamApiId;
            if (team.getTeamApiId() == Team.getTeamApiId(fixture.get("_links").get("homeTeam").get("href").asText())) {
                awayTeamApiId = Team.getTeamApiId(fixture.get("_links").get("awayTeam").get("href").asText());
                homeTeamApiId = Team.getTeamApiId(fixture.get("_links").get("homeTeam").get("href").asText());
            } else {
                homeTeamApiId = Team.getTeamApiId(fixture.get("_links").get("homeTeam").get("href").asText());
                awayTeamApiId = Team.getTeamApiId(fixture.get("_links").get("awayTeam").get("href").asText());
            }

            currentFixture.setDate(date);

            Team homeTeam = new Team(homeTeamName, competitionEnum, -1, homeTeamApiId);
            Team awayTeam = new Team(awayTeamName, competitionEnum, -1, awayTeamApiId);
            currentFixture.setHomeTeam(homeTeam);
            currentFixture.setAwayTeam(awayTeam);
            upcomingFixturesList.add(currentFixture);
        }

        return upcomingFixturesList;
    }

    public int getCompetitionIdByLeague(CompetitionEnum competition) throws IOException {
        String competitionName = competition.getCompetitionName(competition);
        JsonNode competitionNode = makeRequest(ApiConstants.COMPETITIONS_ENDPOINT);

        for (JsonNode eachCompetition : competitionNode) {
            if (eachCompetition.get("caption").toString().contains(competitionName)) {
                return Integer.parseInt(eachCompetition.get("id").asText());
            }
        }
        return -1;
    }

}
