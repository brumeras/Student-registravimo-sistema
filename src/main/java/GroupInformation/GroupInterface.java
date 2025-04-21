package GroupInformation;

import StudentuInformacija.Student;

import java.util.List;

public interface GroupInterface {
    String getGroupName();
    List<Student> getStudents();
    void addStudent(Student student);
}
