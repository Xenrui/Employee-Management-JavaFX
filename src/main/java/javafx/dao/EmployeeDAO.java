package main.java.javafx.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.javafx.model.Employee;

public class EmployeeDAO {
    
    public boolean isAddEmployeeSuccessful(Employee employee) {
        String sql = "INSERT INTO employees (first_name, last_name, email, phone_number, hire_date, job_title, department_id) VALUES (?,?,?,?,?,?,?)";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
    
            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getEmail());
            stmt.setString(4, employee.getPhoneNum());
            stmt.setDate(5, Date.valueOf(employee.getHireDate()));
            stmt.setString(6, employee.getJobTitle());
            stmt.setInt(7, employee.getDepartmentID());
    
            int affectedRows = stmt.executeUpdate();  // Executes the INSERT query
    
            // If the insertion was successful, retrieve the generated employeeID
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedEmployeeID = generatedKeys.getInt(1);  // Get the auto-generated ID (first column)
                        employee.setEmployeeID(generatedEmployeeID);  // Set the employee ID in the Employee object
                        return true;  // Employee added successfully
                    }
                }
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) {
                System.err.println("Error: Duplicate entry for email '" + employee.getEmail() + "'. Please use a different email.");
            } else {
                e.printStackTrace();
            }
            return false;  // Insertion failed
        }
        return false;  // No rows affected (shouldn't happen if the insert was successful)
    }

	public boolean isUpdateEmployeeSuccessful(Employee employee) {
        String updateQuery = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, phone_number = ?, job_title = ?, department_id = ? WHERE employee_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            // Set parameters for the prepared statement
            pstmt.setString(1, employee.getFirstName());
            pstmt.setString(2, employee.getLastName());
            pstmt.setString(3, employee.getEmail());
            pstmt.setString(4, employee.getPhoneNum());
            pstmt.setString(5, employee.getJobTitle());
            pstmt.setInt(6, employee.getDepartmentID()); // Ensure this field exists in Employee
            pstmt.setInt(7, employee.getEmployeeID()); // Use the employee's unique ID for the WHERE clause

            // Execute the update
            int affectedRows = pstmt.executeUpdate();

            // Return true if one or more rows were affected, false otherwise
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle exception (you might want to log it or throw a custom exception)
        }
	}

    public boolean isDeleteEmployeeSuccessful(Employee employee) {
        // First delete the employee's attendance records
        String queryAttendance = "DELETE FROM attendance WHERE employee_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(queryAttendance)) {
            stmt.setInt(1, employee.getEmployeeID());
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows <= 0) {
                return false;  // If no rows are affected, return false
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    
        // Then delete the employee record from the employees table
        String queryEmployee = "DELETE FROM employees WHERE employee_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(queryEmployee)) {
            stmt.setInt(1, employee.getEmployeeID());
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;  // Return true if employee deletion was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public ObservableList<Employee> getAllEmployees() {
		ObservableList<Employee> employees = FXCollections.observableArrayList();
		String sql = "SELECT * FROM employees";

		try (Connection conn = DatabaseConnection.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql)) {

				while (rs.next()) {
					Employee employee = new Employee();
					employee.setEmployeeID(rs.getInt("employee_id"));
					employee.setFirstName(rs.getString("first_name"));
					employee.setLastName(rs.getString("last_name"));
					employee.setEmail(rs.getString("email"));
					employee.setPhoneNum(rs.getString("phone_number"));
					employee.setHireDate(rs.getDate("hire_date").toLocalDate());
                    employee.setJobTitle(rs.getString("job_title"));
					employee.setSalary(rs.getFloat("salary"));
					employee.setDepartmentID(rs.getInt("department_id"));
					employee.setDepartmentName(DepartmentDAO.getDepartmentNameById(employee.getDepartmentID()));

					employees.add(employee);
				}

				
			} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return employees;
	}

    public Employee getEmployeebyId(int id) {
		Employee employee = new Employee();
		String sql = "SELECT * FROM employees where employee_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, id);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                employee.setEmployeeID(rs.getInt("employee_id"));
                employee.setFirstName(rs.getString("first_name"));
                employee.setLastName(rs.getString("last_name"));
                employee.setEmail(rs.getString("email"));
                employee.setPhoneNum(rs.getString("phone_number"));
                employee.setHireDate(rs.getDate("hire_date").toLocalDate());
                employee.setJobTitle(rs.getString("job_title"));
                employee.setSalary(rs.getFloat("salary"));
                employee.setDepartmentID(rs.getInt("department_id"));
                employee.setDepartmentName(DepartmentDAO.getDepartmentNameById(employee.getDepartmentID()));

            }
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employee;
    }

    public Map<String, String> getEmployeesInSameDepartment(int departmentId) {
        String sql = "SELECT first_name, last_name, job_title FROM employees WHERE department_id = ?";
        Map<String, String> employeeMap = new HashMap<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, departmentId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("first_name") + " " + rs.getString("last_name");
                    String jobTitle = rs.getString("job_title");

                    employeeMap.put(name, jobTitle);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employeeMap;
    }

}
