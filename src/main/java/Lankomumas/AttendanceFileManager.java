package Lankomumas;

import StudentuInformacija.Student;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
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

    public static List<Student> loadStudents() {
        List<Student> studentList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) { // Patikriname, ar yra būtent 3 duomenys
                    studentList.add(new Student(data[0], data[1], data[2])); // Vardas, Pavardė, Grupė
                }
            }
        } catch (IOException e) {
            System.out.println("Klaida skaitant studentų failą: " + e.getMessage());
        }
        return studentList;
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
    public static void saveStudent(Student student) {
        try (FileWriter fileWriter = new FileWriter("students.txt", true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(student.getVardas() + "," + student.getPavarde() + "," + student.getGrupe()); // Taisyta tvarka
        } catch (IOException e) {
            System.out.println("Klaida įrašant studentą: " + e.getMessage());
        }
    }
    public static List<Attendance> loadAttendanceByDate(LocalDate selectedDate) {
        List<Attendance> attendanceList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader reader = new BufferedReader(new FileReader("attendance.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("📂 Iš failo nuskaityta: " + line);
                String[] data = line.split(",");
                if (data.length == 5) {
                    LocalDate date = LocalDate.parse(data[3], formatter);
                    if (date.equals(selectedDate)) {
                        attendanceList.add(new Attendance(data[0], data[1], data[2], date, data[4]));
                        System.out.println("✅ Pridėta į sąrašą: " + data[0] + " (" + data[2] + ")");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Klaida skaitant lankomumo failą: " + e.getMessage());
        }

        System.out.println("🔎 Iš viso rastų lankomumo įrašų: " + attendanceList.size());
        return attendanceList;
    }



    public static List<LocalDate> loadFilledDays() {
        List<LocalDate> filledDays = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("attendance.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    LocalDate date = LocalDate.parse(data[3]);
                    if (!filledDays.contains(date)) {
                        filledDays.add(date);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Klaida skaitant lankomumo failą: " + e.getMessage());
        }

        System.out.println("📅 Užpildytos dienos: " + filledDays);
        return filledDays;
    }

    public static List<Attendance> loadAllAttendance() {
        List<Attendance> attendanceList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader reader = new BufferedReader(new FileReader("attendance.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    LocalDate date = LocalDate.parse(data[3], formatter);
                    attendanceList.add(new Attendance(data[0], data[1], data[2], date, data[4]));
                }
            }
        } catch (Exception e) {
            System.out.println("Klaida skaitant failą: " + e.getMessage());
        }
        return attendanceList;
    }
    public static List<Attendance> loadAttendanceByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Attendance> filteredList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader reader = new BufferedReader(new FileReader("attendance.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    LocalDate date = LocalDate.parse(data[3], formatter);
                    if ((date.isEqual(startDate) || date.isAfter(startDate)) &&
                            (date.isEqual(endDate) || date.isBefore(endDate))) {
                        filteredList.add(new Attendance(data[0], data[1], data[2], date, data[4]));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Klaida skaitant lankomumo failą: " + e.getMessage());
        }

        return filteredList;
    }
    public static void saveAllAttendance(List<Attendance> attendanceList) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("attendance.txt", false))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (Attendance attendance : attendanceList) {
                String line = attendance.getName() + "," +
                        attendance.getSurname() + "," +
                        attendance.getGroup() + "," +
                        attendance.getDate().format(formatter) + "," +
                        attendance.getStatus();
                writer.println(line);
            }
            System.out.println("✅ Visi lankomumo įrašai atnaujinti.");
        } catch (IOException e) {
            System.out.println("❌ Klaida įrašant visus lankomumo duomenis: " + e.getMessage());
        }
    }



}
