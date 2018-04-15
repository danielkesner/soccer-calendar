package cache;

import client.FootballDataRestClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigService;
import constants.CacheConstants;
import model.CompetitionEnum;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheService {

    private FootballDataRestClient _client = new FootballDataRestClient();
    private static final Logger logger = LogManager.getLogger(CacheService.class);

    private static final File competitionIdsCacheFile = new File(CacheConstants.COMPETITION_IDS_CACHE_FILE);

    private Map<CompetitionEnum, String> _competitionIds = new HashMap<CompetitionEnum, String>();

    private List<Integer> _allCompetitionIds = new ArrayList<Integer>();

    public List<Integer> getAllCompetitionsIds() {
        try {
            createCompetitionIdsMap();
        } catch (IOException ioe) {

        }
        List<Integer> ids = new ArrayList<Integer>(_competitionIds.size());
        for (String id : _competitionIds.values()) {
            ids.add(Integer.parseInt(id));
        }
        return ids;
    }

    public int getCompetitionIdByCompetitionEnum(CompetitionEnum ce) {
        return _competitionIds.isEmpty() ? -1
                : Integer.parseInt(_competitionIds.get(ce));
    }

    public void onStart() {
        CacheService cacheService = new CacheService();
        cacheService.loadCache();
        _allCompetitionIds = getAllCompetitionsIds();
    }

    private void loadCache() {
        CacheService cacheLoader = new CacheService();
        try {
            cacheLoader.writeCompetitionIds();
        } catch (IOException ioe) {
            logger.error("CacheLoader failed to populate competitionIds.json!");
        }
    }

    /* Hits /competitions endpoint, parses response and loads the
     * competitionIds.json file with key-value pairs of the form "competition": "id"
     * (only for teams in the top $threshold) */
    private void writeCompetitionIds() throws IOException {

        createCompetitionIdsMap();

        if (_competitionIds == null) {
            throw new RuntimeException("Failed to parse competition/id pairs from /competitions endpoint!");
        }

        // If file is not empty, clear it (shouldn't happen, but just to be safe)
        if (competitionIdsCacheFile.length() != 0) {
            FileUtils.writeStringToFile(competitionIdsCacheFile, "", (Charset) null, false);
        }

        // Write each key-value pair to file
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(competitionIdsCacheFile, _competitionIds);
    }

    private void createCompetitionIdsMap() throws IOException {
        List<String> competitionsToKeep = ConfigService.getCompetitionsAsStrings();

        for (JsonNode competition : _client.getCompetitionsNode()) {
            String competitionName = competition.get("caption").asText();
            if (competitionsToKeep.contains(competitionName)) {
                _competitionIds.put(CompetitionEnum.getEnumByStringName(competitionName), competition.get("id").asText());
            }
        }
    }

}
