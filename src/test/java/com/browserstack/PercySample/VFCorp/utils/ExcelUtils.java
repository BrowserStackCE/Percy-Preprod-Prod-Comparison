package com.browserstack.PercySample.VFCorp.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

    public List<Map<String, String>> readExcelData(String filePath, String sheetName, String env) {
        List<Map<String, String>> dataList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row header = sheet.getRow(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Map<String, String> rowMap = new HashMap<>();
                for (int j = 0; j < header.getLastCellNum(); j++) {
                    String key = header.getCell(j).getStringCellValue();
                    if(key.equalsIgnoreCase(env) || key.equalsIgnoreCase("PAGES")) {
                        if(row!=null) {
                            if (row.getCell(j) != null) {
                                String value = row.getCell(j).getStringCellValue();
                                rowMap.put(key, value);
                            }
                        }
                    }
                }
                dataList.add(rowMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

}
