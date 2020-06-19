package ee.stalnuhhin.intgame.integration;

import ee.stalnuhhin.intgame.models.TeamResult;

import java.util.Map;

public final class ChgkRatingIntegrationUtils {
    private ChgkRatingIntegrationUtils() {
        // empty
    }

    public static void integrate(Map<String, TeamResult> results) {
        for (TeamResult teamResult : results.values()) {
            ChgkRatingTeamSearchUtils.findTeam(teamResult);
            ChgkRatingPlayerSearchUtils.findPlayers(teamResult);
        }
    }
}
