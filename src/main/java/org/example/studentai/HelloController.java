package org.example.studentai;

import Lankomumas.Attendance;
import Lankomumas.AttendanceFileManager;
import Lankomumas.ExcelFileHandler;
import StudentuInformacija.Student;
import StudentuInformacija.StudentFileManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.io.IOException;
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
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField studentSearchField;
    @FXML private TextField groupSearchField;
    @FXML private TableView<LocalDate> daysTable;
    @FXML private TableColumn<LocalDate, String> daysColumn;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private ObservableList<Attendance> attendanceList = FXCollections.observableArrayList();
    private ObservableList<LocalDate> filledDaysList = FXCollections.observableArrayList();
    private ObservableList<Student> originalStudentList = FXCollections.observableArrayList();


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
        daysColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
        daysTable.setItems(filledDaysList);
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
            // Iškviečiame metodą, kad atnaujintume lankomumo įrašus pagal pasirinktą datą ir studento duomenis
            List<Attendance> loadedList = AttendanceFileManager.loadAttendanceByDate(selectedDate);

            // Filtruojame tik tuos įrašus, kurie atitinka redaguotus studentus
            List<Attendance> filteredAttendance = new ArrayList<>();

            for (Attendance att : loadedList) {
                for (Student student : studentList) {
                    if (student.getVardas().equals(att.getName()) &&
                            student.getPavarde().equals(att.getSurname()) &&
                            student.getGrupe().equals(att.getGroup())) {
                        filteredAttendance.add(att);
                    }
                }
            }

            // Atnaujiname lentelę su filtruotais įrašais
            this.attendanceList.setAll(filteredAttendance);
            attendanceTable.setItems(attendanceList);
            attendanceTable.refresh();

            System.out.println("🔎 Rasta įrašų: " + filteredAttendance.size());
        } else {
            System.out.println("⚠️ Pasirink datą.");
        }
    }

    @FXML
    public void loadStudentList() {
        List<Student> students = StudentFileManager.loadStudents();
        originalStudentList.setAll(students);
        studentList.setAll(students);  // rodomas sąrašas
        tableView.setItems(studentList);
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
            // Išsaugoti senus duomenis (kad galėtume rasti įrašus)
            String oldVardas = selectedStudent.getVardas();
            String oldPavarde = selectedStudent.getPavarde();
            String oldGrupe = selectedStudent.getGrupe();

            // Pakeisti studento objektą su naujais duomenimis iš laukų
            selectedStudent.setVardas(vardasField.getText());
            selectedStudent.setPavarde(pavardeField.getText());
            selectedStudent.setGrupe(grupeField.getText());

            // Atnaujinti studentų failą (išsaugoti visus pakeistus studentus)
            StudentFileManager.updateStudentFile(studentList);

            // Atnaujinti lankomumo įrašus
            List<Attendance> attendanceList = AttendanceFileManager.loadAllAttendance();
            List<Attendance> updatedAttendance = new ArrayList<>();

            for (Attendance att : attendanceList) {
                if (att.getName().equals(oldVardas) &&
                        att.getSurname().equals(oldPavarde) &&
                        att.getGroup().equals(oldGrupe)) {

                    // Pakeisti lankomumo įrašo duomenis pagal naujus studento duomenis
                    att.setName(selectedStudent.getVardas());
                    att.setSurname(selectedStudent.getPavarde());
                    att.setGroup(selectedStudent.getGrupe());
                }
                updatedAttendance.add(att);
            }

            // Išsaugoti atnaujintus lankomumo įrašus į failą
            AttendanceFileManager.saveAllAttendance(updatedAttendance);

            // Atnaujinti lentelę, kad matytumėte pakeistus duomenis
            tableView.refresh();

            // Išvalyti laukus po redagavimo
            vardasField.clear();
            pavardeField.clear();
            grupeField.clear();

            System.out.println("✅ Studentas ir lankomumo įrašai atnaujinti.");
        } else {
            System.out.println("⚠️ Pasirink studentą prieš redaguojant.");
        }
    }


    private void updateAttendanceList(String vardas, String pavarde, String grupe) {
        // Naudojame visus lankomumo įrašus ir filtruojame pagal studento duomenis
        List<Attendance> allAttendance = AttendanceFileManager.loadAllAttendance();
        List<Attendance> filteredAttendance = allAttendance.stream()
                .filter(att -> att.getName().equalsIgnoreCase(vardas) &&
                        att.getSurname().equalsIgnoreCase(pavarde) &&
                        att.getGroup().equalsIgnoreCase(grupe))
                .toList();

        attendanceList.setAll(filteredAttendance); // Atverčiame tik tuos įrašus, kurie atitinka redaguotus duomenis
        attendanceTable.setItems(attendanceList); // Atnaujiname lentelę
        attendanceTable.refresh(); // Užtikriname, kad lentelė atnaujinta
    }

    @FXML
    public void filterByGroup() {
        String selectedGroup = groupField.getText().trim();
        if (!selectedGroup.isEmpty()) {
            List<Student> allStudents = StudentFileManager.loadStudents(); // arba naudoti originalStudentList
            List<Student> filteredStudents = new ArrayList<>();

            for (Student student : allStudents) {
                if (student.getGrupe().trim().equals(selectedGroup)) {
                    filteredStudents.add(student);
                }
            }

            studentList.setAll(filteredStudents);
            tableView.setItems(studentList);
            tableView.refresh();

            System.out.println("✅ Rasti " + filteredStudents.size() + " įrašai grupėje " + selectedGroup);
        } else {
            System.out.println("⚠️ Įveskite grupės pavadinimą.");
        }
    }



    @FXML
    public void filterByStudent() {
        String input = studentSearchField.getText().trim();
        if (!input.isEmpty()) {
            String[] parts = input.split("\\s+"); // Padalina pagal tarpą

            if (parts.length >= 2) {
                String vardas = parts[0];
                String pavarde = parts[1];

                List<Attendance> allAttendance = AttendanceFileManager.loadAllAttendance();

                List<Attendance> filteredList = allAttendance.stream()
                        .filter(att -> att.getName().equalsIgnoreCase(vardas)
                                && att.getSurname().equalsIgnoreCase(pavarde))
                        .toList();

                System.out.println("✅ Rasta " + filteredList.size() + " įrašų studentui " + vardas + " " + pavarde);
                attendanceTable.setItems(FXCollections.observableArrayList(filteredList));
                attendanceTable.refresh();
            } else {
                System.out.println("⚠️ Įveskite vardą ir pavardę, pvz.: „Eglė Eglaitė“");
            }
        } else {
            System.out.println("⚠️ Įveskite studento vardą ir pavardę.");
        }
    }

    public void filterAttendanceByGroup() {
        String groupName = groupSearchField.getText();
        if (!groupName.isEmpty()) {
            System.out.println("🔍 Filtruojama grupė: " + groupName);

            // Naudojame naują metodą, kad visada turėtume visus įrašus
            List<Attendance> allAttendance = AttendanceFileManager.loadAllAttendance();

            List<Attendance> filteredList = allAttendance.stream()
                    .filter(attendance -> attendance.getGroup().trim().equalsIgnoreCase(groupName.trim()))
                    .toList();

            System.out.println("✅ Rasti " + filteredList.size() + " įrašai grupėje " + groupName);

            attendanceTable.setItems(FXCollections.observableArrayList(filteredList));
            attendanceTable.refresh();
        } else {
            System.out.println("⚠️ Įveskite grupės pavadinimą.");
        }
    }

    @FXML
    public void loadFilledDays() {
        List<LocalDate> filledDays = AttendanceFileManager.loadFilledDays();
        filledDaysList.setAll(filledDays);
        daysTable.refresh();
    }

    @FXML
    public void filterByDateRange() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null && !endDate.isBefore(startDate)) {
            List<Attendance> filteredList = AttendanceFileManager.loadAttendanceByDateRange(startDate, endDate);

            System.out.println("📆 Intervalas: " + startDate + " - " + endDate);
            System.out.println("🔍 Rasta " + filteredList.size() + " įrašų šiame intervale.");

            attendanceList.setAll(filteredList);
            attendanceTable.setItems(attendanceList);
            attendanceTable.refresh();
        } else {
            System.out.println("⚠️ Pasirink tinkamą datų intervalą.");
        }
    }

    public void updateFilteredStudentList() {
        // Po redagavimo atlikti filtravimą (jei reikia) pagal grupę ar studentą
        if (!groupSearchField.getText().isEmpty()) {
            filterByGroup();
        } else if (!studentSearchField.getText().isEmpty()) {
            filterByStudent();
        } else {
            studentList.setAll(StudentFileManager.loadStudents());
            tableView.setItems(studentList);
            tableView.refresh();
        }
    }
    @FXML
    public void generateAttendancePDF() {
        if (!attendanceList.isEmpty()) {
            AttendanceFileManager.AttendancePDFGenerator.generateAttendancePDF(attendanceList);
        } else {
            System.out.println("⚠️ Nėra duomenų PDF failui generuoti.");
        }
    }
    @FXML
    private void handleCreateExcelFromTxt() {
        // Nurodome TXT failo kelią ir norimą Excel failo kelią
        String txtFilePath = "C:\\Users\\semil\\desktop\\studentai\\attendance.txt"; // Nurodykite savo TXT failo kelią
        String excelFilePath = "C:\\Users\\semil\\desktop\\studentai\\lankomumas.xlsx"; // Nurodykite savo Excel failo kelią

        try {
            ExcelFileHandler.createExcelFromTextFile(txtFilePath, excelFilePath); // Sukuriame Excel failą iš TXT
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
