package Lankomumas;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;

public class AttendanceFileManager {
    private static final String FILE_NAME = "attendance.txt";

    public static void saveAttendance(LocalDate date, String studentName, String status) {
        try (FileWriter fileWriter = new FileWriter(FILE_NAME, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(date + "," + studentName + "," + status);
        } catch (IOException e) {
            System.out.println("Klaida įrašant lankomumą į failą: " + e.getMessage());
        }
    }

    public static List<Attendance> loadAttendanceByDate(LocalDate selectedDate) {
        List<Attendance> attendanceList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    LocalDate date = LocalDate.parse(data[0]);
                    if (date.equals(selectedDate)) {
                        attendanceList.add(new Attendance(date, data[2]));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Klaida skaitant lankomumo failą: " + e.getMessage());
        }
        return attendanceList;
    }
    public static void saveAttendance(LocalDate date, String studentName, String studentSurname, String group, String status) {
        try (FileWriter fileWriter = new FileWriter("attendance.txt", true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(studentName + "," + studentSurname + "," + group + "," + date + "," + status);
            System.out.println("Lankomumas įrašytas: " + studentName + " " + studentSurname);
        } catch (IOException e) {
            System.out.println("Klaida įrašant lankomumą: " + e.getMessage());
        }
    }

}
