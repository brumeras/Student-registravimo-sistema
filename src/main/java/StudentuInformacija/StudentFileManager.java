package StudentuInformacija;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class StudentFileManager {

    private static final String FILE_NAME = "students.txt";

    public static void clearStudentFileOnStartup() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, false))) {
            // Perrašome tuščią failą, kad nebūtų jokių senų duomenų
            System.out.println("🗑️ Studentų sąrašas TXT faile buvo išvalytas!");
        } catch (IOException e) {
            System.out.println("❌ Klaida išvalant studentų failą: " + e.getMessage());
        }
    }

    public static void saveStudents(List<Student> students) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, false))) { // Perrašome failą
            for (Student student : students) {
                writer.println(student.getVardas() + "," + student.getPavarde() + "," + student.getGrupe());
            }
            System.out.println("✅ Studentų failas atnaujintas!");
        } catch (IOException e) {
            System.out.println("❌ Klaida įrašant studentus į failą: " + e.getMessage());
        }
    }


    // Metodas, skirtas nuskaityti visus studentus iš failo
    public static List<Student> loadStudents() {
        List<Student> studentList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    studentList.add(new Student(data[0], data[1], data[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Klaida skaitant studentų failą: " + e.getMessage());
        }
        return studentList;
    }
    public static void updateStudentFile(List<Student> studentList) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, false))) { // Perrašome failą
            for (Student student : studentList) {
                writer.println(student.getVardas() + "," + student.getPavarde() + "," + student.getGrupe());
            }
        } catch (IOException e) {
            System.out.println("Klaida atnaujinant studentų failą: " + e.getMessage());
        }
    }

}
