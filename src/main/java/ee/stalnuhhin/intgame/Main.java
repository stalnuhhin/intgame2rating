package ee.stalnuhhin.intgame;

import ee.stalnuhhin.intgame.integration.ChgkRatingIntegrationUtils;
import ee.stalnuhhin.intgame.models.FileType;
import ee.stalnuhhin.intgame.models.TeamResult;
import ee.stalnuhhin.intgame.outputfile.PlayersResultsFileUtils;
import ee.stalnuhhin.intgame.outputfile.RatingResultsFileUtils;
import ee.stalnuhhin.intgame.utils.ExcelUtils;
import ee.stalnuhhin.intgame.utils.TeamResultFromBookConverterUtils;
import ee.stalnuhhin.intgame.utils.TeamResultSortUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final boolean RUN_INTEGRATION_WITH_CHGK_RATING = true;
    private static final String[] GAMES = {
            "deliverycup2"
    };

    public static void main(String[] args) throws IOException {
        for (String game : GAMES) {
            convert("files/" + game);
        }
    }

    private static void convert(String filepath) throws IOException {
        Map<String, TeamResult> results = new HashMap<>();

        Workbook team = ExcelUtils.getWorkbook(filepath + "/teams.xlsx");
        TeamResultFromBookConverterUtils.convert(results, team, FileType.TEAMS);
        team.close();
        LOGGER.info("Teams file read!");

        Workbook verbose = ExcelUtils.getWorkbook(filepath + "/results.xlsx");
        TeamResultFromBookConverterUtils.convert(results, verbose, FileType.RESULTS);
        verbose.close();
        LOGGER.info("Results file read!");

        if (RUN_INTEGRATION_WITH_CHGK_RATING) {
            ChgkRatingIntegrationUtils.integrate(results);
        }

        List<TeamResult> teamResults = new ArrayList<>(results.values());
        TeamResultSortUtils.sort(teamResults);

        PlayersResultsFileUtils.writeFile(teamResults, filepath);
        RatingResultsFileUtils.writeFile(teamResults, filepath);
    }
}