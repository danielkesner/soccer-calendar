package team;

import datamodel.Competition;
import datamodel.Team;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TeamUnitTests {

    /*
    * Two teams with same name, same league, and same rank (all 3 fields equal)
    * */
    @Test
    public void basicEqualTestTrue() {
        Team t1 = new Team("Arsenal", Competition.CompetitionEnum.PREMIER_LEAGUE, 5);
        Team t2 = new Team("Arsenal", Competition.CompetitionEnum.PREMIER_LEAGUE, 5);
        Assert.assertTrue(t1.equals(t2), "Two teams both named 'Arsenal' that are both in PREMIER_LEAGUE are not returning equal!");
    }

    /*
    * Two teams with same name, same league, and different rank
    * */
    @Test
    public void complexEqualTestTrue() {
        Team t1 = new Team("Arsenal", Competition.CompetitionEnum.PREMIER_LEAGUE, 15);
        Team t2 = new Team("Arsenal", Competition.CompetitionEnum.PREMIER_LEAGUE, 1);
        Assert.assertTrue(t1.equals(t2), "Two teams both named 'Arsenal' that are both in PREMIER_LEAGUE but with different ranks are not returning equal!");
    }

    /*
     * Two teams with same name and same rank, but different leagues
     * */
    @Test
    public void simpleEqualTestFalse() {
        Team t1 = new Team("Arsenal", Competition.CompetitionEnum.BUNDESLIGA, 1);
        Team t2 = new Team("Arsenal", Competition.CompetitionEnum.PREMIER_LEAGUE, 1);
        Assert.assertFalse(t1.equals(t2), "Two teams both named 'Arsenal' that are in different leagues (PL/Bundesliga) are not returning unequal!");
    }
}
