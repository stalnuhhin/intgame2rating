package ee.stalnuhhin.intgame.utils;

import ee.stalnuhhin.intgame.models.FileType;
import ee.stalnuhhin.intgame.models.Team;
import ee.stalnuhhin.intgame.models.TeamResult;

import java.util.List;
import java.util.Map;

public final class TeamResultFromDataConverterUtils {
    private TeamResultFromDataConverterUtils() {
        // empty
    }

    public static String getTeamName(FileType fileType, Map<String, String> data) {
        return data.get(fileType == FileType.RESULTS ? "name" : "team_name");
    }

    public static void fillTeam(TeamResult result, Map<String, String> data) {
        Team team = result.getTeam();
        team.setId(data.get("id"));
        team.setName(data.get("team_name"));
        team.setCity(data.get("city"));
        team.getCaptain().setName(data.get("name"));
        team.getCaptain().setPatronym(data.get("patronym"));
        team.getCaptain().setSurname(data.get("surname"));
    }

    public static void fillVerbose(TeamResult result, Map<String, String> data, List<String> titles) {
        int q = 1;
        while (titles.contains("" + q + ".0")) {
            String value = data.get("" + q + ".0");
            if (value != null && value.equals("+")) {
                result.getTakenQuestions().add(Long.valueOf(q));
            }
            q++;
        }
        result.setQuestionsCount(q - 1);
        result.setPlayed(true);
    }
}
