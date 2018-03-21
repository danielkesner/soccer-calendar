package datamodel;

import constants.LeagueNameConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Competition {

    CompetitionEnum league;
    List<Team> standings;

    public Competition(CompetitionEnum competition, List<Team> standings) {
        if (competition == null || standings == null) {
            throw new IllegalArgumentException("Cannot create League object with null parameters!");
        }
        this.league = competition;
        this.standings = standings;
    }

    //TODO: Change the logic here since IDs are going to be generated in AppDataCache
    public enum CompetitionEnum {
        PREMIER_LEAGUE(LeagueNameConstants.PREMIER_LEAGUE, 445),
        LA_LIGA(LeagueNameConstants.LA_LIGA, -1),
        BUNDESLIGA(LeagueNameConstants.BUNDESLIGA, -1),
        LIGUE_1(LeagueNameConstants.LIGUE_1, -1);

        private String competitionName;
        private int api_id;
        private static final Map<String, CompetitionEnum> leagueNameMap = new HashMap<String, CompetitionEnum>(values().length);
        private static final Map<Integer, CompetitionEnum> leagueIdMap = new HashMap<Integer, CompetitionEnum>(values().length);

        CompetitionEnum(String competitionName, int api_id) {
            this.competitionName = competitionName;
            this.api_id = api_id;
        }

        public void setApi_id(int id) { this.api_id = id; }

        public int getApi_id(CompetitionEnum leagueEnum) { return leagueEnum.api_id; }

        public String getCompetitionName(CompetitionEnum leagueEnum) {
            return leagueEnum.competitionName;
        }

        static {
            for (CompetitionEnum leagueEnum : values()) {
                leagueNameMap.put(leagueEnum.competitionName, leagueEnum);
                leagueIdMap.put(leagueEnum.api_id, leagueEnum);
            }
        }

        public static CompetitionEnum getEnumByStringName(String league) {
            CompetitionEnum result = leagueNameMap.get(league);
            if (result == null) {
                throw new IllegalArgumentException("Invalid competitionName name: " + league);
            }
            return result;
        }

        public static CompetitionEnum getEnumById(int id) {
            CompetitionEnum result = leagueIdMap.get(id);
            if (result == null) {
                throw new IllegalArgumentException("Invalid ID when searching for corresponding LeagueEnum: " + id);
            }
            return result;
        }

        public String toString() {
            return this.competitionName;
        }

    }
}
