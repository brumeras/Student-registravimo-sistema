package GroupInformation;

import StudentuInformacija.Student;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGroup implements GroupInterface {
    protected String groupName;
    protected List<Student> students = new ArrayList<>();

    public AbstractGroup(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String getGroupName() {
        return groupName;
    }

    @Override
    public List<Student> getStudents() {
        return students;
    }

    @Override
    public void addStudent(Student student) {
        students.add(student);
    }
}
