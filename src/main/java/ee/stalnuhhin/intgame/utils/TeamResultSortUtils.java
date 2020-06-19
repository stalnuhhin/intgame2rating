package ee.stalnuhhin.intgame.utils;

import ee.stalnuhhin.intgame.models.TeamResult;

import java.util.List;

public final class TeamResultSortUtils {
    private TeamResultSortUtils() {
        // empty
    }

    public static void sort(List<TeamResult> teamResults) {
        teamResults.sort((o1, o2) -> {
            if (o1.getTeam().getRatingId() == null && o2.getTeam().getRatingId() == null) {
                return 0;
            }
            if (o1.getTeam().getRatingId() == null) {
                return -1;
            }
            if (o2.getTeam().getRatingId() == null) {
                return 1;
            }
            return o1.getTeam().getRatingId().compareTo(o2.getTeam().getRatingId());
        });
    }
}
