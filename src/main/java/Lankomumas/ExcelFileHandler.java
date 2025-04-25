package Lankomumas;

import StudentuInformacija.Student;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelFileHandler {

    public static List<Student> loadStudentsFromExcel(String path) throws IOException {
        List<Student> students = new ArrayList<>();

        FileInputStream fis = new FileInputStream(new File(path));
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            if (row.getRowNum() == 0) continue; // Skip header if present

            Cell vardasCell = row.getCell(0);
            Cell pavardeCell = row.getCell(1);
            Cell grupeCell = row.getCell(2);

            if (vardasCell != null && pavardeCell != null && grupeCell != null) {
                String vardas = vardasCell.getStringCellValue().trim();
                String pavarde = pavardeCell.getStringCellValue().trim();
                String grupe = grupeCell.getStringCellValue().trim();

                students.add(new Student(vardas, pavarde, grupe));
            }
        }

        workbook.close();
        fis.close();

        return students;
    }
    public static void saveAttendanceToExcel(List<Attendance> attendanceList, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Lankomumas");
            Row headerRow = sheet.createRow(0);

            String[] columns = {"Vardas", "Pavardė", "Grupė", "Data", "Statusas"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            int rowNum = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (Attendance attendance : attendanceList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(attendance.getName());
                row.createCell(1).setCellValue(attendance.getSurname());
                row.createCell(2).setCellValue(attendance.getGroup());
                row.createCell(3).setCellValue(attendance.getDate().format(formatter));
                row.createCell(4).setCellValue(attendance.getStatus());
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            System.out.println("✅ Lankomumo duomenys eksportuoti į Excel: " + filePath);
        } catch (IOException e) {
            System.out.println("❌ Klaida eksportuojant Excel: " + e.getMessage());
        }
    }

}