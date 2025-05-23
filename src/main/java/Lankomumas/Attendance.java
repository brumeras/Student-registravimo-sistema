package Lankomumas;

import java.time.LocalDate;

public class Attendance extends AbstractAttendance {
    private String name;
    private String surname;
    private String group;

    public Attendance(String name, String surname, String group, LocalDate date, String status) {
        super(date, status);
        this.name = name;
        this.surname = surname;
        this.group = group;
    }

    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getGroup() { return group; }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setGroup(String group) {
        this.group = group;
    }

}

