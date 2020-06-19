package ee.stalnuhhin.intgame.outputfile;

import ee.stalnuhhin.intgame.models.Team;
import ee.stalnuhhin.intgame.models.TeamResult;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public final class RatingResultsFileUtils {
    private RatingResultsFileUtils() {
        // empty
    }

    public static void writeFile(List<TeamResult> teamResults, String filepath) throws IOException {
        FileWriter fw = new FileWriter(filepath + "/rating-results.txt");
        int questionsCount = teamResults.get(0).getQuestionsCount();
        fw.write(String.join(",", Collections.nCopies(3 + questionsCount, "\"\"")));
        fw.write("\n");
        fw.write("\"Team ID\",\"Название\",\"Город\",");
        for (int i = 1; i < questionsCount; i++) {
            fw.write("\"" + i + "\",");
        }
        fw.write("\"" + questionsCount + "\"");
        fw.write("\n");

        for (TeamResult teamResult : teamResults) {
            if (teamResult.isPlayed()) {
                writeTeamData(fw, teamResult.getTeam(), teamResult.getTakenQuestions(), questionsCount);
            }
        }
        fw.close();
    }

    private static void writeTeamData(FileWriter fw, Team team, List<Long> takenQuestions, int questionsCount) throws IOException {
        String teamId = team.getRatingId() == null ? "" : team.getRatingId();
        fw.write("\"" + teamId + "\",\"" + team.getName() + "\",\"" + team.getCity() + "\",");
        for (int i = 1; i <= questionsCount; i++) {
            fw.write("\"" + (takenQuestions.contains(Long.valueOf(i)) ? 1 : 0) + "\"");
            if (i != questionsCount) {
                fw.write(",");
            }
        }
        fw.write("\n");
    }
}
