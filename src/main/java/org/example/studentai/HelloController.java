package org.example.studentai;

import Lankomumas.Attendance;
import Lankomumas.AttendanceFileManager;
import StudentuInformacija.Student;
import StudentuInformacija.StudentFileManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML private TextField vardasField;
    @FXML private TextField pavardeField;
    @FXML private TextField grupeField;
    @FXML private TableView<Student> tableView;
    @FXML private TableColumn<Student, String> vardasColumn;
    @FXML private TableColumn<Student, String> pavardeColumn;
    @FXML private TableColumn<Student, String> grupeColumn;
    @FXML private TextField groupField;
    @FXML private DatePicker datePicker;
    @FXML private TableView<Attendance> attendanceTable;
    @FXML private TableColumn<Attendance, String> dateColumn;
    @FXML private TableColumn<Attendance, String> statusColumn;
    @FXML private TableColumn<Attendance, String> nameColumn;
    @FXML private TableColumn<Attendance, String> surnameColumn;
    @FXML private TableColumn<Attendance, String> groupColumn;



    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private ObservableList<Attendance> attendanceList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        vardasColumn.setCellValueFactory(new PropertyValueFactory<>("vardas"));
        pavardeColumn.setCellValueFactory(new PropertyValueFactory<>("pavarde"));
        grupeColumn.setCellValueFactory(new PropertyValueFactory<>("grupe"));

        // Lankomumo lentelės duomenų susiejimas
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.setItems(studentList);
    }

    private boolean studentExists(String vardas, String pavarde, String grupe) {
        for (Student student : studentList) {
            if (student.getVardas().equals(vardas) && student.getPavarde().equals(pavarde) && student.getGrupe().equals(grupe)) {
                return true; // Studentas jau egzistuoja
            }
        }
        return false; // Studentas dar neįrašytas
    }


    @FXML
    public void addStudent() {
        String vardas = vardasField.getText();
        String pavarde = pavardeField.getText();
        String grupe = grupeField.getText();

        if (!vardas.isEmpty() && !pavarde.isEmpty() && !grupe.isEmpty()) {
            Student student = new Student(vardas, pavarde, grupe);
            studentList.add(student);
            tableView.refresh();

            StudentFileManager.saveStudent(student); // Taisyta!

            vardasField.clear();
            pavardeField.clear();
            grupeField.clear();
        }
    }

    @FXML
    public void markAttendance() {
        Student selectedStudent = tableView.getSelectionModel().getSelectedItem();
        LocalDate selectedDate = datePicker.getValue();

        if (selectedStudent != null && selectedDate != null) {
            AttendanceFileManager.saveAttendance(selectedDate, selectedStudent.getVardas(), selectedStudent.getPavarde(), selectedStudent.getGrupe(), "Atvyko");
            System.out.println("Lankomumas pažymėtas!");
        } else {
            System.out.println("Pasirink studentą ir datą.");
        }
    }
    @FXML
    public void loadAttendanceByDate() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            List<Attendance> attendanceList = AttendanceFileManager.loadAttendanceByDate(selectedDate);

            System.out.println("Rasta įrašų: " + attendanceList.size());

            attendanceTable.setItems(FXCollections.observableArrayList(attendanceList));
            attendanceTable.refresh();
        } else {
            System.out.println("Pasirink datą.");
        }
    }

    @FXML
    public void loadStudentList() {
        studentList.setAll(StudentFileManager.loadStudents());
        tableView.refresh();
    }

    @FXML
    public void selectStudent() {
        Student selectedStudent = tableView.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            vardasField.setText(selectedStudent.getVardas());
            pavardeField.setText(selectedStudent.getPavarde());
            grupeField.setText(selectedStudent.getGrupe());
        }
    }
    @FXML
    public void editStudent() {
        Student selectedStudent = tableView.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            selectedStudent.setVardas(vardasField.getText());
            selectedStudent.setPavarde(pavardeField.getText());
            selectedStudent.setGrupe(grupeField.getText());

            tableView.refresh(); // Atnaujina lentelę

            StudentFileManager.updateStudentFile(studentList); // Atnaujina duomenis faile

            vardasField.clear();
            pavardeField.clear();
            grupeField.clear();
        } else {
            System.out.println("Pasirink studentą prieš redaguojant.");
        }
    }
    @FXML
    public void filterByGroup() {
        String selectedGroup = groupField.getText(); // Grupės pavadinimas iš vartotojo įvesties
        List<Student> filteredStudents = new ArrayList<>();

        for (Student student : StudentFileManager.loadStudents()) {
            if (student.getGrupe().equals(selectedGroup)) {
                filteredStudents.add(student);
            }
        }

        studentList.setAll(filteredStudents);
        tableView.refresh();
    }

}
