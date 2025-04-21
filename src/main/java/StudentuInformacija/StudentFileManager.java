package StudentuInformacija;

import StudentuInformacija.Student;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

public class StudentFileManager {

    private static final String FILE_NAME = "students.txt";

    public static void saveStudent(Student student, String group) {
        try (FileWriter fileWriter = new FileWriter(FILE_NAME, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(group + "," + student.getVardas() + "," + student.getPavarde());
        } catch (IOException e) {
            System.out.println("Klaida įrašant studentą į failą: " + e.getMessage());
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
