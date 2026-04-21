package utils;

import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.util.*;

public class ExcelReader {

    public static Object[][] getData(String sheetName) throws Exception {

        String path = System.getProperty("user.dir") + "/src/test/java/resources/CustomerAPIData.xlsx";

        FileInputStream fis = new FileInputStream(path);
        Workbook wb = WorkbookFactory.create(fis);
        Sheet sheet = wb.getSheet(sheetName);

        if (sheet == null) {
            throw new RuntimeException("Sheet not found: " + sheetName);
        }

        DataFormatter formatter = new DataFormatter();

        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new RuntimeException("Header row is missing in sheet: " + sheetName);
        }

        int rows = sheet.getPhysicalNumberOfRows();
        int cols = headerRow.getLastCellNum();

        Object[][] data = new Object[rows - 1][1];

        for (int i = 1; i < rows; i++) {

            Row currentRow = sheet.getRow(i);
            Map<String, String> rowData = new HashMap<>();

            for (int j = 0; j < cols; j++) {

                // ✅ Safe header read
                Cell headerCell = headerRow.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String key = formatter.formatCellValue(headerCell).trim();

                // ✅ Safe row read
                String value = "";
                if (currentRow != null) {
                    Cell cell = currentRow.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    value = formatter.formatCellValue(cell);
                }

                rowData.put(key, value);
            }

            data[i - 1][0] = rowData;
        }

        wb.close();
        fis.close();

        return data;
    }
}