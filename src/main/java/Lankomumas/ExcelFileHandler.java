package Lankomumas;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

public class ExcelFileHandler {

    // Sukuria Excel failą
    public static void createExcelFromTextFile(String txtFilePath, String excelFilePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(txtFilePath));
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Lankomumas");

        // Sukuriame antraštės eilutę
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Vardas");
        headerRow.createCell(1).setCellValue("Pavardė");
        headerRow.createCell(2).setCellValue("Grupė");
        headerRow.createCell(3).setCellValue("Data");
        headerRow.createCell(4).setCellValue("Statusas");

        String line;
        int rowNum = 1;  // Nuo 1, nes 0 jau yra antraštė

        // Nuskaityti eilutes iš TXT failo ir įrašyti į Excel
        while ((line = reader.readLine()) != null) {
            String[] columns = line.split(",");
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(columns[0].trim());  // Vardas
            row.createCell(1).setCellValue(columns[1].trim());  // Pavardė
            row.createCell(2).setCellValue(columns[2].trim());  // Grupė
            row.createCell(3).setCellValue(columns[3].trim());  // Data
            row.createCell(4).setCellValue(columns[4].trim());  // Statusas
        }

        reader.close();

        // Išsaugoti Excel failą
        try (FileOutputStream fileOut = new FileOutputStream(new File(excelFilePath))) {
            workbook.write(fileOut);
        }

        workbook.close();
        System.out.println("✅ Lankomumo duomenys perkelti į Excel failą.");
    }
}
