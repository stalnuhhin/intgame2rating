package ee.stalnuhhin.intgame.integration;

import ee.stalnuhhin.intgame.models.TeamResult;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public final class ChgkRatingTeamSearchUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChgkRatingTeamSearchUtils.class);
    private static final WebTarget WEB_TARGET
            = ClientBuilder.newClient().target("https://rating.chgk.info/api/teams.json/search");

    private ChgkRatingTeamSearchUtils() {
        // empty
    }

    public static void findTeam(TeamResult teamResult) {
        JSONArray teams = findTeam(teamResult.getTeam().getName(), teamResult.getTeam().getCity());
        if (teams.size() == 1) {
            JSONObject team = (JSONObject) teams.get(0);
            String idteam = (String) team.get("idteam");
            LOGGER.info("Found 1 team by name and town, setting team id=" + idteam);
            teamResult.getTeam().setRatingId(idteam);
        } else {
            if (teams.size() == 0) {
                teams = findTeam(teamResult.getTeam().getName(), null);
            }
            if (teams.size() > 0) {
                teamResult.setPossibleTeams(teams);
            }
        }
    }

    private static JSONArray findTeam(String name, String city) {
        if (name == null || "".equals(name.trim())) {
            LOGGER.info("Not searching at chgk rating if team name is blank.");
            return new JSONArray();
        }

        WebTarget searchWebTarget = WEB_TARGET.queryParam("name", name);
        if (city != null) {
            searchWebTarget = searchWebTarget.queryParam("town", city);
        }
        Invocation.Builder invocationBuilder = searchWebTarget.request(MediaType.APPLICATION_JSON);
        LOGGER.info("Running integration for teams search: name=" + name + (city == null ? "" : ", city=" + city));
        String response = invocationBuilder.get(String.class);

        JSONParser parser = new JSONParser();
        JSONObject json;
        try {
            json = (JSONObject) parser.parse(response);
        } catch (ParseException e) {
            LOGGER.error("JSON parse error!");
            throw new RuntimeException(e);
        }
        JSONArray items = (JSONArray) json.get("items");
        LOGGER.info("Search result size: " + items.size());
        return items;
    }
}
