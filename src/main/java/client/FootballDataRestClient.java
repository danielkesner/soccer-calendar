package client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigService;
import constants.ApiConstants;
import datamodel.Fixture;
import datamodel.Competition;
import datamodel.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FootballDataRestClient {

    private static final Logger logger = LogManager.getLogger(FootballDataRestClient.class);

    private static ObjectMapper mapper = new ObjectMapper();

    protected JsonNode getResponseFromUrl(String _url) throws IOException {
        URL url = new URL(_url);
        HttpURLConnection apiConnection = (HttpURLConnection) url.openConnection();
        apiConnection.setRequestMethod("GET");
        apiConnection.setRequestProperty("X-Auth-Token", ApiConstants.getApiKey());
        return mapper.readTree(apiConnection.getInputStream());
    }

    /*
    * Returns the raw json payload (stored in JsonNode)
    * a given League's standings (EPL table, Bundesliga table, etc.)
    */
    public JsonNode getLeagueTableByCompetitionId(int competitionId) throws IOException {
        return getResponseFromUrl(ApiConstants.COMPETITIONS_ENDPOINT + competitionId + "/" + ApiConstants.LEAGUE_TABLE).get("standing");
    }

    public JsonNode getLeagueTableByCompetitionEnum(Competition.CompetitionEnum competitionEnum) throws IOException {
        return getLeagueTableByCompetitionId(getCompetitionIdByLeague(competitionEnum));
    }

    /*
     * Returns a JsonNode containing sub-nodes for each team in the league
     * */
    public JsonNode getAllTeamsPerCompetition(Competition.CompetitionEnum leagueEnum) throws IOException {
        return getResponseFromUrl(ApiConstants.getTeamIdsEndpoint(leagueEnum.getApi_id(leagueEnum))).get("teams");
    }

    /*
     * Returns a List containing id's for the top $threshold teams in a given competition
     * */
    public List<Integer> getTeamIdsForTopThresholdTeams(Competition.CompetitionEnum competitionEnum) throws IOException {
        int standingsThresholdValue = ConfigService.getStandingsThresholdValue();
        logger.info("In getTeamIdsForTopThresholdTeams(), standingsThresholdValue is " + standingsThresholdValue);
        List<Integer> returnIds = new ArrayList<Integer>(ConfigService.getNumberOfCompetitions());
        JsonNode allTeams = getLeagueTableByCompetitionId(getCompetitionIdByLeague(competitionEnum));

        // Get id's for top $standingsThresholdValue teams
        for (int i = 0; i < standingsThresholdValue; i++) {
            returnIds.add(getTeamIdFromTableSubNode(allTeams.get(i)));
        }

        return returnIds.isEmpty() ? null : returnIds;
    }

    /*
     * Given the results of getLeagueTableByCompetitionId().get(0 < i < JsonNode.size()), return team i's ID
     * */
    public int getTeamIdFromTableSubNode(JsonNode leagueTable) {
        String linksNode = leagueTable.get("_links").toString();
        int index = linksNode.lastIndexOf("/") + 1;
        String beginningOfId = linksNode.substring(index);
        // Strip away everything that isn't a number ('}' and quotes)
        String regex = "[\"{}]"; // ex  65"}}
        try {
            Integer.parseInt(beginningOfId.replaceAll(regex, ""));
            return Integer.parseInt(beginningOfId.replaceAll(regex, ""));
        } catch (NumberFormatException nfe) {
            logger.error("Could not determine team ID, links node returned by client was: " + linksNode);
            return -1;
        }
    }

    /*
     * Returns an integer list containing the team id for each team in a given League
     * */
    public List<Integer> getAllTeamIdsPerLeague(Competition.CompetitionEnum leagueEnum) throws IOException {
        List<Integer> ids = new ArrayList<Integer>();
        JsonNode response = getAllTeamsPerCompetition(leagueEnum);

        for (int i = 0; i < response.size(); i++) {
            String linkUrl = response.get(i).get("_links").get("self").get("href").asText();
            int index = linkUrl.lastIndexOf("/");   // place pointer before team id, then grab substring
            ids.add(Integer.parseInt(linkUrl.substring(index + 1)));
        }

        return ids;
    }

    /*
     * Given a team name (i.e. 'Arsenal') passed as a string, return the
     * integer id that identifies that team
     *
     * */
    public int getTeamIdFromTableSubNode(Competition.CompetitionEnum leagueEnum, String teamName) throws IOException {
        JsonNode teams = getAllTeamsPerCompetition(leagueEnum);

        for (JsonNode teamNode : teams) {
            // Strip extraneous quotes from name/shortName fields; if match, return
            if (teamName.equals(teamNode.get("shortName").asText().replace("\"", ""))
                    || teamName.equals(teamNode.get("name").asText().replace("\"", ""))) {
                String linkUrl = teamNode.get("_links").get("self").get("href").asText();
                int index = linkUrl.lastIndexOf("/");
                return Integer.parseInt(linkUrl.substring(index + 1));
            }
        }
        return -1;
    }

    public List<Fixture> getUpcomingMatchesPayloadByTeam(Competition.CompetitionEnum league, String teamName) throws IOException {
        int teamIndex = getTeamIdFromTableSubNode(league, teamName);
        List<Fixture> upcomingFixturesList = new ArrayList<Fixture>();

        if (teamIndex < 0) {
            throw new RuntimeException("Failed to fetch API id for " + teamName + " in league " + league);
        }

        JsonNode upcomingFixturesNode = getResponseFromUrl(ApiConstants.getFixturesByTeamEndpoint(teamIndex)).get("fixtures");

        for (JsonNode fixtureNode : upcomingFixturesNode) {
            Fixture currentFixture = new Fixture();
            String date = fixtureNode.get("date").asText().replace("\"", "");
            String homeTeam = fixtureNode.get("homeTeamName").asText();
            String awayTeam = fixtureNode.get("awayTeamName").asText();
            currentFixture.setAwayTeam(new Team(awayTeam));
            currentFixture.setHomeTeam(new Team(homeTeam));
            currentFixture.setDate(date);
            currentFixture.setCompetitionEnum(league);
            upcomingFixturesList.add(currentFixture);
        }

        return upcomingFixturesList;
    }

    public int getCompetitionIdByLeague(Competition.CompetitionEnum competition) throws IOException {
        String competitionName = competition.getCompetitionName(competition);
        JsonNode competitionNode = getResponseFromUrl(ApiConstants.COMPETITIONS_ENDPOINT);

        for (JsonNode eachCompetition : competitionNode) {
            if (eachCompetition.get("caption").toString().contains(competitionName)) {
                return Integer.parseInt(eachCompetition.get("id").asText());
            }
        }
        return -1;
    }


    public int getRankingOfTeam(Competition.CompetitionEnum competitionEnum, Team team) throws IOException {
        JsonNode table = getLeagueTableByCompetitionId(getCompetitionIdByLeague(competitionEnum));
        for (JsonNode eachTeam : table) {
            if (eachTeam.get("teamName").asText().replaceAll("\'", "").equals(team.getTeamName())) {
                return eachTeam.get("position").asInt();
            }
        }
        return -1;
    }

    public int getRankingOfTeam(Competition.CompetitionEnum competitionEnum, String team) throws IOException {
        JsonNode table = getLeagueTableByCompetitionId(getCompetitionIdByLeague(competitionEnum));
        for (JsonNode eachTeam : table) {
            if (eachTeam.get("teamName").asText().replaceAll("\'", "").equals(team)) {
                return eachTeam.get("position").asInt();
            }
        }
        return -1;
    }

    public boolean isTeamWithinThresholdRanking(Competition.CompetitionEnum competitionEnum, Team team) throws IOException {
        return getRankingOfTeam(competitionEnum, team) <= ConfigService.getStandingsThresholdValue();
    }


}
