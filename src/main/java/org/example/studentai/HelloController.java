package org.example.studentai;

import StudentuInformacija.Student;
import StudentuInformacija.StudentFileManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class HelloController {

    @FXML private TextField vardasField;
    @FXML private TextField pavardeField;
    @FXML private TextField grupeField;
    @FXML private TableView<Student> tableView;
    @FXML private TableColumn<Student, String> vardasColumn;
    @FXML private TableColumn<Student, String> pavardeColumn;
    @FXML private TableColumn<Student, String> grupeColumn;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        vardasColumn.setCellValueFactory(new PropertyValueFactory<>("vardas"));
        pavardeColumn.setCellValueFactory(new PropertyValueFactory<>("pavarde"));
        grupeColumn.setCellValueFactory(new PropertyValueFactory<>("grupe"));

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
            if (!studentExists(vardas, pavarde, grupe)) {
                Student student = new Student(vardas, pavarde, grupe);
                studentList.add(student);
                tableView.refresh();

                // Įrašome studentą į failą
                StudentFileManager.saveStudent(student);

                // Išvalo laukus po įrašymo
                vardasField.clear();
                pavardeField.clear();
                grupeField.clear();
            } else {
                System.out.println("Šis studentas jau yra įrašytas.");
            }
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

}
