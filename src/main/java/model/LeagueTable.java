package model;

import cache.CacheService;
import com.fasterxml.jackson.databind.JsonNode;
import config.ConfigService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeagueTable {

    private List<Team> _standings;
    private JsonNode _leagueTableResponse;
    private CompetitionEnum _competition;
    private List<Team> topTeams;
    private int competitionId;
    private CacheService cacheService = new CacheService();

    private Logger logger = LogManager.getLogger(LeagueTable.class);

    public LeagueTable(JsonNode leagueTableResponse) {
        this._leagueTableResponse = leagueTableResponse;
        populateTableObject();
        populateTopTeams();
        competitionId = cacheService.getCompetitionIdByCompetitionEnum(_competition);
    }

    public int getCompetitionId() {
        return this.competitionId;
    }

    public List<Team> getTopTeamsInLeagueTable() {
        return this.topTeams;
    }

    public CompetitionEnum getCompetitionOfLeagueTable() {
        return this._competition;
    }

    // Add all teams with rank >= ConfigService.getThreshold()
    private void populateTopTeams()  {
        int threshold;
        try {
            threshold = ConfigService.getStandingsThresholdValue();
        } catch (IOException ioe) {
            throw new RuntimeException("Failed to get standings threshold value!");
        }
        topTeams = new ArrayList<Team>(threshold);
        for (int i = 0; i < threshold; i++) {
            topTeams.add(_standings.get(i));
        }
    }

    private void populateTableObject() {
        _standings = new ArrayList<Team>();
        _competition = getCompetitionEnum();

        // Each node in /root/standing array is an individual team
        for (JsonNode eachTeam : _leagueTableResponse.get("standing")) {

            String teamLink = eachTeam.get("_links").get("team").get("href").asText();
            int api_id = Integer.parseInt(teamLink.substring(teamLink.lastIndexOf('/') + 1, teamLink.length()));

            // Populate Team object
            Team currentTeam = new Team(eachTeam.get("teamName").asText(),
                    _competition, eachTeam.get("position").asInt(), api_id);
            _standings.add(currentTeam);
        }
    }

    private CompetitionEnum getCompetitionEnum() {
        return CompetitionEnum.getEnumByStringName(
                _leagueTableResponse.get("leagueCaption").asText()
        );
    }

}
