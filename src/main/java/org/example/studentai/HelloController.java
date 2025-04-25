package org.example.studentai;

import Lankomumas.Attendance;
import Lankomumas.AttendanceFileManager;
import Lankomumas.CsvFileHandler;
import Lankomumas.ExcelFileHandler;
import StudentuInformacija.Student;
import StudentuInformacija.StudentFileManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static StudentuInformacija.StudentFileManager.updateStudentFile;

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
    @FXML private Button exportButton;

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
        AttendanceFileManager.clearAttendanceFileOnStartup();
    }

    @FXML
    public void addStudent() {
        String vardas = vardasField.getText().trim();
        String pavarde = pavardeField.getText().trim();
        String grupe = grupeField.getText().trim();

        if (!vardas.isEmpty() && !pavarde.isEmpty() && !grupe.isEmpty()) {
            Student student = new Student(vardas, pavarde, grupe);

            studentList.add(student);
            updateStudentFile(studentList); // Perrašome failą

            tableView.refresh();
            System.out.println("✅ Studentas pridėtas!");
        } else {
            System.out.println("⚠️ Užpildykite visus laukus!");
        }
    }



    @FXML
    public void markAttendance() {
        Student selectedStudent = tableView.getSelectionModel().getSelectedItem();
        LocalDate selectedDate = datePicker.getValue();

        if (selectedStudent != null && selectedDate != null) {
            // Sukuriame naują lankomumo įrašą
            Attendance newEntry = new Attendance(
                    selectedStudent.getVardas(),
                    selectedStudent.getPavarde(),
                    selectedStudent.getGrupe(),
                    selectedDate,
                    "Atvyko"
            );

            // Pridedame į sąrašą
            attendanceList.add(newEntry);

            // IŠSAUGOME ATNAUJINTĄ LANKOMUMĄ Į TXT FAILĄ
            AttendanceFileManager.saveAllAttendance(attendanceList);

            tableView.refresh();
            System.out.println("✅ Lankomumas pažymėtas!");
        } else {
            System.out.println("⚠️ Pasirink studentą ir datą.");
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
        // Įkeliame studentus TIK iš failo
        List<Student> loadedStudents = StudentFileManager.loadStudents();

        studentList.setAll(loadedStudents); // Perrašome sąrašą su naujais duomenimis
        originalStudentList.setAll(loadedStudents);

        tableView.setItems(studentList);
        tableView.refresh();

        System.out.println("✅ Studentų sąrašas atnaujintas! Įkelti " + loadedStudents.size() + " studentai.");
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
            updateStudentFile(studentList);

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

            // Atnaujinti studentų filtrus (jei reikia)
            updateFilteredStudentList();

            System.out.println("✅ Studentas ir lankomumo įrašai atnaujinti.");
        } else {
            System.out.println("⚠️ Pasirink studentą prieš redaguojant.");
        }
    }


    @FXML
    public void filterByGroup() {
        String selectedGroup = groupField.getText().trim();
        if (!selectedGroup.isEmpty()) {
            List<Student> allStudents = studentList; // Naudojame tik naujausią sąrašą
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
        if (!groupSearchField.getText().isEmpty()) {
            filterByGroup();
            tableView.refresh();  // Užtikriname, kad filtravimas vizualiai atsinaujintų
        } else if (!studentSearchField.getText().isEmpty()) {
            filterByStudent();
            tableView.refresh();  // Užtikriname, kad rodomi filtruoti studentai
        } else {
            studentList.setAll(StudentFileManager.loadStudents());
            originalStudentList.setAll(studentList); // Atnaujiname originalų sąrašą

            tableView.setItems(studentList);
            tableView.refresh();
        }
    }

    @FXML
    public void generateAttendancePDF()
    {
        if(!attendanceList.isEmpty())
        {
            AttendanceFileManager.AttendancePDFGenerator.generateAttendancePDF(attendanceList);
        }
        else
        {
            System.out.println("Nera duomenu PDF failui.");
        }
    }

    @FXML
    public void importData() {
        List<String> choices = List.of("CSV", "Excel");
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Importavimas");
        dialog.setHeaderText("Pasirinkite failo tipą");
        dialog.setContentText("Importuoti iš:");

        dialog.showAndWait().ifPresent(choice -> {
            switch (choice) {
                case "CSV" -> importFromCsv();
                case "Excel" -> importFromExcel();
            }
        });
    }


    private void importFromCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setTitle("Pasirinkite CSV failą");

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                List<Student> students = CsvFileHandler.loadStudentsFromCsv(selectedFile.getAbsolutePath());

                if (students.isEmpty()) {
                    System.out.println("⚠️ Importuotas CSV failas neturi studentų!");
                    return;
                }

                // PAŠALINAME VISUS SENUS STUDENTUS IR NAUDOJAME TIK IMPORTUOTUS
                studentList.setAll(students);

                // PERRAŠOME TXT FAILĄ, KAD JAME BŪTŲ TIK NAUJI STUDENTAI
                StudentFileManager.saveStudents(students);

                tableView.setItems(studentList);
                tableView.refresh();

                System.out.println("✅ Sėkmingai importuota iš CSV! Įkelti " + students.size() + " studentai.");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("❌ Klaida importuojant iš CSV: " + e.getMessage());
            }
        }
    }



    private void importFromExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setTitle("Pasirinkite Excel failą");

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                List<Student> students = ExcelFileHandler.loadStudentsFromExcel(selectedFile.getAbsolutePath());

                if (students.isEmpty()) {
                    System.out.println("⚠️ Importuotas Excel failas neturi studentų!");
                    return;
                }

                // PAŠALINAME SENUS STUDENTUS IR NAUDOJAME TIK NAUJAI IMPORTUOTUS
                studentList.setAll(students);

                // PERRAŠOME TXT FAILĄ, KAD JAME BŪTŲ TIK NAUJI STUDENTAI
                StudentFileManager.saveStudents(students);

                tableView.setItems(studentList);
                tableView.refresh();

                System.out.println("✅ Sėkmingai importuota iš Excel! Įkelti " + students.size() + " studentai.");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("❌ Klaida importuojant iš Excel: " + e.getMessage());
            }
        }
    }



    @FXML
    public void exportData() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null && !endDate.isBefore(startDate)) {
            List<Attendance> filteredList = AttendanceFileManager.loadAttendanceByDateRange(startDate, endDate);

            if (filteredList.isEmpty()) {
                System.out.println("⚠️ Pasirinktame intervale nėra duomenų!");
                return;
            }

            // Renkamės CSV arba Excel formatą
            List<String> choices = List.of("CSV", "Excel");
            ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
            dialog.setTitle("Eksportavimas");
            dialog.setHeaderText("Pasirinkite formatą");
            dialog.setContentText("Eksportuoti į:");

            dialog.showAndWait().ifPresent(choice -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Pasirinkite vietą failui išsaugoti");

                String extension = switch (choice) {
                    case "CSV" -> ".csv";
                    case "Excel" -> ".xlsx";
                    default -> throw new IllegalStateException("Nežinomas formatas: " + choice);
                };

                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(choice + " Files", "*" + extension));

                String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
                fileChooser.setInitialFileName("attendance_" + date + extension);

                File selectedFile = fileChooser.showSaveDialog(null);
                if (selectedFile != null) {
                    String filePath = selectedFile.getPath().endsWith(extension) ? selectedFile.getPath() : selectedFile.getPath() + extension;

                    // Eksportuojame **tik** pasirinkto intervalo duomenis
                    switch (choice) {
                        case "CSV" -> CsvFileHandler.saveAttendanceToCsv(filteredList, filePath);
                        case "Excel" -> ExcelFileHandler.saveAttendanceToExcel(filteredList, filePath);
                    }

                    System.out.println("✅ Lankomumo duomenys eksportuoti į " + filePath);
                }
            });

        } else {
            System.out.println("⚠️ Pasirink tinkamą datų intervalą prieš eksportuojant.");
        }
    }


}
