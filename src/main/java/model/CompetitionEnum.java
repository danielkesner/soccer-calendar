package model;

import constants.LeagueNameConstants;

import java.util.HashMap;
import java.util.Map;

public enum CompetitionEnum {

    PREMIER_LEAGUE(LeagueNameConstants.PREMIER_LEAGUE),
    LA_LIGA(LeagueNameConstants.LA_LIGA),
    BUNDESLIGA(LeagueNameConstants.BUNDESLIGA),
    LIGUE_1(LeagueNameConstants.LIGUE_1);

    private String competitionName;
    private static Map<String, CompetitionEnum> leagueNameMap = new HashMap<String, CompetitionEnum>(values().length);

    CompetitionEnum(String competitionName) {
        this.competitionName = competitionName;
    }

    public String getCompetitionName(CompetitionEnum leagueEnum) {
        return leagueEnum.competitionName;
    }

    static {
        for (CompetitionEnum leagueEnum : values()) {
            leagueNameMap.put(leagueEnum.competitionName, leagueEnum);
        }
    }

    public static CompetitionEnum getEnumByStringName(String league) {
        CompetitionEnum result = leagueNameMap.get(league);
        if (result == null) {
            throw new IllegalArgumentException("Invalid competitionName name: " + league);
        }
        return result;
    }

    public String toString() {
        return this.competitionName;
    }

}

