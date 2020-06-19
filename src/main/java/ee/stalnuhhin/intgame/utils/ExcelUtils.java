package ee.stalnuhhin.intgame.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ExcelUtils {
    private ExcelUtils() {
        // empty
    }

    public static List<String> getTitles(Row titleRow, short firstCellNum, short lastCellNum) {
        final List<String> titles = new ArrayList<>();
        for (int i = firstCellNum; i < lastCellNum; i++) {
            titles.add(ExcelUtils.getCellValue(titleRow.getCell(i)));
        }
        return titles;
    }

    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return String.valueOf(cell.getColumnIndex());
        }
    }

    public static Workbook getWorkbook(String filename) {
        try {
            return WorkbookFactory.create(new FileInputStream(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
