package Lankomumas;


import StudentuInformacija.Student;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvFileHandler {

    public static List<Student> loadStudentsFromCsv(String path) throws IOException {
        List<Student> students = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String vardas = parts[0].trim();
                    String pavarde = parts[1].trim();
                    String grupe = parts[2].trim();
                    students.add(new Student(vardas, pavarde, grupe));
                }
            }
        }

        return students;
    }
    public static void saveAttendanceToCsv(List<Attendance> attendanceList, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("Vardas,Pavardė,Grupė,Data,Statusas");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (Attendance attendance : attendanceList) {
                writer.println(attendance.getName() + "," +
                        attendance.getSurname() + "," +
                        attendance.getGroup() + "," +
                        attendance.getDate().format(formatter) + "," +
                        attendance.getStatus());
            }

            System.out.println("✅ Lankomumo duomenys eksportuoti į CSV: " + filePath);
        } catch (IOException e) {
            System.out.println("❌ Klaida eksportuojant CSV: " + e.getMessage());
        }
    }


}
