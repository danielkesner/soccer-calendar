package config;

import model.CompetitionEnum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigService {

    private static final String DEFAULT_CONFIG_FILE_LOCATION = "src/main/resources/config.cfg";
    private static String configFilePath = DEFAULT_CONFIG_FILE_LOCATION;

    /* By default, path is set to default location; can modify new path in constructor */
    public ConfigService(String pathToConfigFile) {
        configFilePath = pathToConfigFile;
    }

    /*
     * Return the threshold value for a league (cutoff point of games we care about seeing)
     * */
    public static int getStandingsThresholdValue() throws IOException {
        return Integer.parseInt(getValue("STANDINGS_THRESHOLD"));
    }

    /*
     * Return the number of competitions to get data on
     * */
    public static int getNumberOfCompetitions() throws IOException {
        return (getCompetitions() != null && !getCompetitions().isEmpty()) ? getCompetitions().size() : -1;
    }

    /*
     * Returns a list of CompetitionEnums that we want to get data on
     */
    public static List<CompetitionEnum> getCompetitions() throws IOException {
        List<String> list = Arrays.asList(getValue("COMPETITIONS").split(","));
        List<CompetitionEnum> ret = new ArrayList<CompetitionEnum>(list.size());
        for (String sEnum : list) {
            CompetitionEnum competitionEnum = CompetitionEnum.getEnumByStringName(sEnum.replace("\"", ""));
            ret.add(competitionEnum);
        }
        return ret.size() == list.size() ? ret : null;
    }

    public static List<String> getCompetitionsAsStrings() throws IOException {
        return Arrays.asList(getValue("COMPETITIONS").split(","));
    }

    /*
     * Given a key that may or may not exist in config.cfg, return corresponding value
     * or throw an appropriate exception
     * */
    public static String getValue(String key) throws IOException {
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            throw new RuntimeException("Cannot locate config file at path: " + configFile.getAbsolutePath());
        }

        String line;
        BufferedReader reader = new BufferedReader(new FileReader(configFile));

        while ((line = reader.readLine()) != null) {
            if (line.startsWith(key)) {
                int index = line.indexOf("=");
                if (index > -1) {
                    return line.substring(index + 1);
                }
                throw new RuntimeException("Could not parse " + key + " field in config file!");
            }
        }
        throw new RuntimeException("Did not find " + key + " field in config file!");
    }

    public static boolean debug() {
        return System.getProperty("logging") != null && System.getProperty("logging").equals("debug");
    }
}
