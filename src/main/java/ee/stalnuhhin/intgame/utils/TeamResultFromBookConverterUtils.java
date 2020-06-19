package ee.stalnuhhin.intgame.utils;

import ee.stalnuhhin.intgame.models.FileType;
import ee.stalnuhhin.intgame.models.TeamResult;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TeamResultFromBookConverterUtils {
    private TeamResultFromBookConverterUtils() {
        // empty
    }

    public static void convert(Map<String, TeamResult> results, Workbook book, FileType fileType) {
        final Sheet sheet = book.getSheetAt(0);
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();

        final Row titleRow = sheet.getRow(firstRowNum);
        short firstCellNum = titleRow.getFirstCellNum();
        short lastCellNum = titleRow.getLastCellNum();
        final List<String> titles = ExcelUtils.getTitles(titleRow, firstCellNum, lastCellNum);

        for (int i = firstRowNum + 1; i < lastRowNum; i++) {
            final Map<String, String> data = new HashMap<>();
            final Row row = sheet.getRow(i);
            for (int j = firstCellNum; j < lastCellNum; j++) {
                data.put(titles.get(j), ExcelUtils.getCellValue(row.getCell(j)));
            }
            String teamName = TeamResultFromDataConverterUtils.getTeamName(fileType, data);
            if (!results.containsKey(teamName)) {
                results.put(teamName, new TeamResult());
            }
            TeamResult teamResult = results.get(teamName);
            if (FileType.RESULTS == fileType) {
                TeamResultFromDataConverterUtils.fillVerbose(teamResult, data, titles);
            } else {
                TeamResultFromDataConverterUtils.fillTeam(teamResult, data);
            }
        }
    }
}
