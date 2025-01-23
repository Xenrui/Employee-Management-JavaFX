package main.java.javafx.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.javafx.model.Attendance;
import main.java.javafx.model.Employee;

public class AttendanceDAO {
    private static final LocalTime ONTIME_CUTOFF = LocalTime.of(9, 0); // 9:00 AM
    
    public boolean insertAttendance(Attendance attendance) {
        String query = "INSERT INTO Attendance (employee_id, timestamp, status) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Get the status based on the timestamp
            String status = attendance.getTimestamp().toLocalTime().isBefore(ONTIME_CUTOFF) ? "ontime" : "late";
            
            stmt.setInt(1, attendance.getEmployeeID());
            stmt.setTimestamp(2, Timestamp.valueOf(attendance.getTimestamp()));
            stmt.setString(3, status);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Attendance getEmployeeAttendanceForDate(int employeeId, LocalDate date) {
        Attendance attendance = null;
        String query = "SELECT * FROM Attendance WHERE employee_id = ? AND DATE(timestamp) = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, employeeId); // Set employee ID
            stmt.setDate(2, Date.valueOf(date)); // Set the specific date
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp("timestamp");
                    String status = rs.getString("status"); // "ontime" or "late"
                    
                    // Create an Attendance object from the result set
                    attendance = new Attendance(employeeId, timestamp.toLocalDateTime(), status);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return attendance;
    }

    // Method to get attendance logs for today
    public Map<String, List<Employee>> getAttendanceStatus(LocalDate date) {
        // Create lists to hold employees based on their attendance status
        List<Employee> onTimeEmployees = new ArrayList<>();
        List<Employee> lateEmployees = new ArrayList<>();
        List<Employee> notPresentEmployees = new ArrayList<>();
        
        // First, fetch all attendance logs for the given date
        List<Attendance> attendanceList = new ArrayList<>();
        String query = "SELECT * FROM Attendance WHERE DATE(timestamp) = ? ORDER BY timestamp DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setDate(1, Date.valueOf(date));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int employeeId = rs.getInt("employee_id");
                    Timestamp timestamp = rs.getTimestamp("timestamp");
                    String status = rs.getString("status"); // "ontime" or "late"
                    
                    // Fetch employee details (e.g., first name, last name, etc.)
                    EmployeeDAO emp = new EmployeeDAO();
                    Employee employee = emp.getEmployeebyId(employeeId);
                    
                    if (employee != null) {
                        Attendance attendance = new Attendance(employeeId, timestamp.toLocalDateTime(), status);
                        attendanceList.add(attendance);
                        
                        // Add the employee to the respective list based on their attendance status
                        if ("ontime".equalsIgnoreCase(status)) {
                            onTimeEmployees.add(employee);
                        } else if ("late".equalsIgnoreCase(status)) {
                            lateEmployees.add(employee);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Get the list of all employees for the given date (could be from a database or other data source)
        EmployeeDAO emp = new EmployeeDAO();
        List<Employee> allEmployees = emp.getAllEmployees();
        
        // Loop through all employees and check if they're in the attendance list. If not, they're "not present"
        for (Employee employee : allEmployees) {
            boolean isPresent = false;
            
            // Check if the employee is present in the attendance list
            for (Attendance attendance : attendanceList) {
                if (attendance.getEmployeeID() == employee.getEmployeeID()) {
                    isPresent = true;
                    break;
                }
            }
            
            if (!isPresent) {
                // Add to the "not present" list if the employee isn't in the attendance records
                notPresentEmployees.add(employee);
            }
        }
        
        // Create a map to hold all categorized employees
        Map<String, List<Employee>> categorizedAttendance = new HashMap<>();
        categorizedAttendance.put("On-Time", onTimeEmployees);
        categorizedAttendance.put("Late", lateEmployees);
        categorizedAttendance.put("Not Present", notPresentEmployees);
        
        return categorizedAttendance;
    }

    public List<Attendance> getAttendanceForDate(LocalDate date) {
        List<Attendance> attendanceList = new ArrayList<>();
        String query = "SELECT * FROM Attendance WHERE DATE(timestamp) = ? ORDER BY timestamp DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Set the date parameter in the query
            stmt.setDate(1, Date.valueOf(date));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int attendanceId = rs.getInt("attendance_id");
                    int employeeId = rs.getInt("employee_id");
                    Timestamp timestamp = rs.getTimestamp("timestamp");
                    String status;
                    if(LocalDateTime.now().toLocalTime().isBefore(LocalTime.of(9, 0))){
                        status = "Ontime";
                    } else {
                        status = "Late";
                    }
                    // Create an Attendance object and add it to the list
                    Attendance attendance = new Attendance(employeeId, timestamp.toLocalDateTime(), status);
                    attendanceList.add(attendance);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendanceList;
    }
}