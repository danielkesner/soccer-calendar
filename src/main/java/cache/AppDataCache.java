package cache;

import config.ConfigService;
import datamodel.Competition;
import datamodel.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppDataCache {

    private static final Logger log = LogManager.getLogger(AppDataCache.class);

    Map<Competition.CompetitionEnum, Integer> competitionsIdsList;
    List<HashMap<Team, Integer>> teamIdsByCompetitionList;


    public AppDataCache() {
        /* Initialize storage */
        try {
            competitionsIdsList = new HashMap<Competition.CompetitionEnum, Integer>(ConfigService.getNumberOfCompetitions(), 1);
            teamIdsByCompetitionList = new ArrayList<HashMap<Team, Integer>>(ConfigService.getNumberOfCompetitions() * ConfigService.getStandingsThresholdValue());
        } catch (IOException ioe) {
            //TODO: Add better logging/messaging here
            log.error("Could not initialize storage when constructing AppDataCache!");
            throw new RuntimeException(ioe);
        }
    }

    public Map<Competition.CompetitionEnum, Integer> getCompetitionsIdsList() {
        return competitionsIdsList;
    }

    public void setCompetitionsIdsList(Map<Competition.CompetitionEnum, Integer> competitionsIdsList) {
        this.competitionsIdsList = competitionsIdsList;
    }

    public List<HashMap<Team, Integer>> getTeamIdsByCompetitionList() {
        return teamIdsByCompetitionList;
    }

    public void setTeamIdsByCompetitionList(List<HashMap<Team, Integer>> teamIdsByCompetitionList) {
        this.teamIdsByCompetitionList = teamIdsByCompetitionList;
    }


}
