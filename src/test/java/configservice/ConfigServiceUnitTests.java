package configservice;

import config.ConfigService;
import datamodel.Competition;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ConfigServiceUnitTests {

    @Test
    public void numberOfCompetitionsTest() throws IOException {
        int ret = ConfigService.getNumberOfCompetitions();
        Assert.assertEquals(ret, 4, "Expected 4 competitions in config file, found: " + ret);
    }

    /*
     * Expected 4 competitions: PREMIER_LEAGUE, LA_LIGA, BUNDESLIGA, LIGUE_1
     * */
    @Test
    public void getCompetitionsMockFile1Test() throws IOException {
        ConfigService service = new ConfigService("src/test/resources/mock_config_file_1.cfg");
        List<Competition.CompetitionEnum> list = service.getCompetitions();
        Assert.assertNotNull(list, "ConfigService.getCompetitions() returned null!");
        Assert.assertEquals(list.size(), 4, "Expected 4 CompetitionEnums, found: " + list.size());
        List<String> expectedStringValues = Arrays.asList("Premier League", "Primera Division", "Bundesliga", "Ligue 1");
        for (Competition.CompetitionEnum eachEnum : list) {
            Assert.assertTrue(expectedStringValues.contains(eachEnum.toString()));
        }
    }
}
