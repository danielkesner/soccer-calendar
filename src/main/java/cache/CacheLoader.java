package cache;

import client.FootballDataRestClient;
import config.ConfigService;
import datamodel.Competition;

import java.io.IOException;
import java.util.List;

public class CacheLoader {

    public static final AppDataCache cache = new AppDataCache();
    private static FootballDataRestClient client = new FootballDataRestClient();

    /*
    *
    * This method should be the first thing run when the app starts up
    * TODO: [x] Get competitions IDs for each competition (league), store them in AppDataCache
    * TODO: [] For the top $threshold teams in each competition, get their team IDs and store them
    *
    * */
    public static void loadCache() {

    }

    private void loadCompetitionsIdList() throws IOException {
        List<Competition.CompetitionEnum> allCompetitions = ConfigService.getCompetitions();
        if (allCompetitions == null || allCompetitions.isEmpty()) {
            throw new RuntimeException("Failed to receive valid List from ConfigService.getCompetitions()!");
        }

        for (Competition.CompetitionEnum competitionEnum : allCompetitions) {
            int id = client.getCompetitionIdByLeague(competitionEnum);
            if (id == -1) {
                throw new RuntimeException("Failed to get competition id for league: " + competitionEnum.toString());
            }
            cache.getCompetitionsIdsList().put(competitionEnum, id);
        }
    }


}
