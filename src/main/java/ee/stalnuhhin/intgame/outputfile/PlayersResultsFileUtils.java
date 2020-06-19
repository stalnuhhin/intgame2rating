package ee.stalnuhhin.intgame.outputfile;

import ee.stalnuhhin.intgame.models.Player;
import ee.stalnuhhin.intgame.models.Team;
import ee.stalnuhhin.intgame.models.TeamResult;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public final class PlayersResultsFileUtils {
    private PlayersResultsFileUtils() {
        // empty
    }

    public static void writeFile(List<TeamResult> teamResults, String filepath) throws IOException {
        FileWriter fw = new FileWriter(filepath + "/rating-players.txt");
        for (TeamResult teamResult : teamResults) {
            if (teamResult.isPlayed()) {
                writeTeamData(fw, teamResult.getTeam());
            }
        }
        fw.close();
    }

    private static void writeTeamData(FileWriter fw, Team team) {
        String ratingId = team.getRatingId() == null ? "" : team.getRatingId();
        String teamName = team.getName() == null ? "" : team.getName();
        String teamCity = team.getCity() == null ? "" : team.getCity();

        writePlayer(fw, ratingId, teamName, teamCity, "К", team.getCaptain());
        team.getPlayers().forEach(p -> writePlayer(fw, ratingId, teamName, teamCity, "Л", p));
    }

    private static void writePlayer(FileWriter fw,
                                    String ratingId,
                                    String teamName,
                                    String teamCity,
                                    String playerType,
                                    Player player) {
        String playerRatingId = player.getRatingId() == null ? "" : player.getRatingId().trim();
        String surname = player.getSurname() == null ? "" : player.getSurname().trim();
        String patronym = player.getPatronym() == null ? "" : player.getPatronym().trim();
        String name = player.getName() == null ? "" : player.getName().trim();
        try {
            fw.write(ratingId + ";" + teamName + ";" + teamCity + ";" + playerType
                    + ";" + playerRatingId + ";" + surname + ";" + patronym + ";" + name);
            fw.write("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
