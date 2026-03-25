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

		Row headerRow = sheet.getRow(0);
		int rows = sheet.getPhysicalNumberOfRows();
		int cols = headerRow.getPhysicalNumberOfCells();

		Object[][] data = new Object[rows - 1][1];

		for (int i = 1; i < rows; i++) {

			Map<String, String> rowData = new HashMap<>();

			for (int j = 0; j < cols; j++) {

				String key = headerRow.getCell(j).toString();
				String value = sheet.getRow(i).getCell(j).toString();

				rowData.put(key, value);
			}

			data[i - 1][0] = rowData;
		}

		wb.close();
		return data;
	}
}