package main.java.javafx.model;

import java.time.LocalDateTime;

public class Attendance {
    private int attendanceID;
    private int employeeID;
    private LocalDateTime timestamp;
    private String status; // ontime/late

    // Constructor
    public Attendance(int employeeID, LocalDateTime timestamp, String status) {
        this.employeeID = employeeID;
        this.timestamp = timestamp;
        this.status = status;
    }

    // Getter methods
    public int getAttendanceID() {
        return attendanceID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }
}
