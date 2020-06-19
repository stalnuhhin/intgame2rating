package ee.stalnuhhin.intgame;

import ee.stalnuhhin.intgame.integration.ChgkRatingIntegrationUtils;
import ee.stalnuhhin.intgame.models.FileType;
import ee.stalnuhhin.intgame.models.Team;
import ee.stalnuhhin.intgame.models.TeamResult;
import ee.stalnuhhin.intgame.utils.ExcelUtils;
import ee.stalnuhhin.intgame.utils.TeamResultFromBookConverterUtils;
import ee.stalnuhhin.intgame.utils.TeamResultSortUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final boolean RUN_INTEGRATION_WITH_CHGK_RATING = true;
    private static final String TEAMS_FILE = "teams.xlsx";
    private static final String TEAMS_FILE_OUT = "teams.txt";
    private static final String VERBOSE_FILE = "verbose.xlsx";
    private static final String VERBOSE_FILE_OUT = "verbose.txt";

    public static void main(String[] args) throws IOException {
        Map<String, TeamResult> results = new HashMap<>();

        Workbook team = ExcelUtils.getWorkbook(TEAMS_FILE);
        TeamResultFromBookConverterUtils.convert(results, team, FileType.TEAM);
        team.close();
        LOGGER.info("Teams file read!");

        Workbook verbose = ExcelUtils.getWorkbook(VERBOSE_FILE);
        TeamResultFromBookConverterUtils.convert(results, verbose, FileType.VERBOSE);
        verbose.close();
        LOGGER.info("Verbose file read!");

        if (RUN_INTEGRATION_WITH_CHGK_RATING) {
            ChgkRatingIntegrationUtils.integrate(results);
        }

        writeVerbose(results);
    }

    private static void writeVerbose(Map<String, TeamResult> results) throws IOException {
        FileWriter fw = new FileWriter("verbose.txt");
        int questionsCount = results.values().iterator().next().getQuestionsCount();
        fw.write(String.join(",", Collections.nCopies(3 + questionsCount, "\"\"")));
        fw.write("\n");
        fw.write("\"Team ID\",\"Название\",\"Город\",");
        for (int i = 1; i < questionsCount; i++) {
            fw.write("\"" + i + "\",");
        }
        fw.write("\"" + questionsCount + "\"");
        fw.write("\n");

        List<TeamResult> teamResults = new ArrayList<>(results.values());
        TeamResultSortUtils.sort(teamResults);
        for (TeamResult teamResult : teamResults) {
            if (teamResult.isPlayed()) {
                Team team = teamResult.getTeam();
                String teamId = team.getRatingId() == null ? "" : team.getRatingId();
                fw.write("\"" + teamId + "\",\"" + team.getName() + "\",\"" + team.getCity() + "\",");
                List<Long> takenQuestions = teamResult.getTakenQuestions();
                for (int i = 1; i <= questionsCount; i++) {
                    fw.write("\"" + (takenQuestions.contains(Long.valueOf(i)) ? 1 : 0) + "\"");
                    if (i != questionsCount) {
                        fw.write(",");
                    }
                }
                fw.write("\n");
            }
        }
        fw.close();
    }
}