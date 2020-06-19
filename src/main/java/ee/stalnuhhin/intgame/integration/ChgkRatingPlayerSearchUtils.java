package ee.stalnuhhin.intgame.integration;

import ee.stalnuhhin.intgame.models.Player;
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

public final class ChgkRatingPlayerSearchUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChgkRatingPlayerSearchUtils.class);
    private static final WebTarget WEB_TARGET
            = ClientBuilder.newClient().target("https://rating.chgk.info/api/players.json/search");

    private ChgkRatingPlayerSearchUtils() {
        // empty
    }

    public static void findPlayers(TeamResult teamResult) {
        findPlayer(teamResult.getTeam().getCaptain());
        teamResult.getTeam().getPlayers().forEach(p -> findPlayer(p));
    }

    private static void findPlayer(Player player) {
        JSONArray players = findPlayer(player.getSurname(), player.getName(), player.getPatronym());
        if (players.size() == 1) {
            fillPlayer(player, (JSONObject) players.get(0));
            return;
        }

        if (players.size() == 0) {
            players = findPlayer(player.getSurname(), player.getName(), null);
            if (players.size() == 1) {
                fillPlayer(player, (JSONObject) players.get(0));
                return;
            }

            if (players.size() == 0) {
                players = findPlayer(player.getSurname(), null, null);
                if (players.size() == 1) {
                    fillPlayer(player, (JSONObject) players.get(0));
                    return;
                }
            }
        }

        if (players.size() > 0) {
            player.setPossiblePlayers(players);
        }
    }

    private static void fillPlayer(Player player, JSONObject playerJson) {
        String idplayer = (String) playerJson.get("idplayer");
        LOGGER.info("Setting player id=" + idplayer);
        player.setRatingId(idplayer);

        String patronymic = (String) playerJson.get("patronymic");
        if (player.getPatronym() == null && patronymic != null) {
            LOGGER.info("Setting player patronymic=" + patronymic);
            player.setPatronym(patronymic);
        }

        String name = (String) playerJson.get("name");
        if (player.getName() == null && name != null) {
            LOGGER.info("Setting player name=" + name);
            player.setName(name);
        }
    }

    private static JSONArray findPlayer(String surname, String name, String patronymic) {
        if (surname == null || "".equals(surname.trim())) {
            LOGGER.info("Not searching at chgk rating if player surname is blank.");
            return new JSONArray();
        }

        WebTarget searchWebTarget = WEB_TARGET.queryParam("surname", surname);
        if (name != null) {
            searchWebTarget = searchWebTarget.queryParam("name", name);
        }
        if (patronymic != null) {
            searchWebTarget = searchWebTarget.queryParam("patronymic", patronymic);
        }
        Invocation.Builder invocationBuilder = searchWebTarget.request(MediaType.APPLICATION_JSON);
        LOGGER.info("Running integration for player search: surname=" + surname
                + (name == null ? "" : ", name=" + name)
                + (patronymic == null ? "" : ", patronymic=" + patronymic));
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
