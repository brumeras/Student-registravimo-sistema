package Lankomumas;

import java.time.LocalDate;

public abstract class AbstractAttendance implements AttendanceInterface {
    protected LocalDate date;
    protected String status;

    public AbstractAttendance(LocalDate date, String status) {
        this.date = date;
        this.status = status;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String getStatus() {
        return status;
    }
}
