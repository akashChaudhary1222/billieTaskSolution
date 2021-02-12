package main.java.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static java.io.File.separator;

public class ExcelUtil {

    public static File getExcelFromPath(String excelName) {
        if (excelName != null) {
            return new File(System.getProperty("user.dir") + separator + "src" + separator + "test" + separator
                    + "resources" + separator + excelName + ".xlsx");
        } else {
            return null;
        }
    }

    public static HashMap loadExcelLines(File fileName) {
        HashMap<String, LinkedHashMap<Integer, List>> outerMap = new LinkedHashMap();
        LinkedHashMap<Integer, List> hashMap = new LinkedHashMap();
        String sheetName = null;
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(fileName);
            XSSFWorkbook workBook = new XSSFWorkbook(fis);

            for (int i = 0; i < workBook.getNumberOfSheets(); ++i) {
                XSSFSheet sheet = workBook.getSheetAt(i);
                sheetName = workBook.getSheetName(i);
                Iterator rows = sheet.rowIterator();

                while (rows.hasNext()) {
                    XSSFRow row = (XSSFRow) rows.next();
                    Iterator cells = row.cellIterator();
                    LinkedList data = new LinkedList();

                    while (cells.hasNext()) {
                        XSSFCell cell = (XSSFCell) cells.next();
                        cell.setCellType(1);
                        if (StringUtils.isNotBlank(cell.getStringCellValue()))
                            data.add(cell);
                    }
                    if (!data.isEmpty())
                        hashMap.put(row.getRowNum(), data);
                }

                outerMap.put(sheetName, hashMap);
                hashMap = new LinkedHashMap();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException var20) {
                    var20.printStackTrace();
                }
            }

        }

        return outerMap;
    }

    public static Object[][] fromHashMapToTwoDimensionalArray(Map<Integer, List> excelSheetMap) {
        int rowsCount = excelSheetMap.size() - 1;
        int columnsCount = excelSheetMap.get(0).size();
        Object[][] testDataArray = new Object[rowsCount][columnsCount];
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                testDataArray[i][j] = excelSheetMap.get(i + 1).get(j).toString();
            }
        }
        return testDataArray;
    }
}
