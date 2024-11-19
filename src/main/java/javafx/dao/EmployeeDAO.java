package main.java.javafx.dao;

import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.javafx.model.Employee;

public class EmployeeDAO {
    
    public boolean isAddEmployeeSuccessful(Employee employee){
        String sql = "INSERT INTO employees (first_name, last_name, email, phone_number, hire_date, job_title, department_id) VALUES (?,?,?,?,?,?,?)";
        
        try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)){
                
                stmt.setString(1, employee.getFirstName());
                stmt.setString(2, employee.getLastName());
                stmt.setString(3, employee.getEmail()); 
                stmt.setString(4, employee.getPhoneNum());
                stmt.setDate(5, Date.valueOf(employee.getHireDate()));
                stmt.setString(6, employee.getJobTitle());
                stmt.setInt(7, employee.getDepartmentID());

                stmt.executeUpdate();
				

        } catch (SQLException e){
            if (e.getSQLState().equals("23000")) {
				System.err.println("Error: Duplicate entry for email '" + employee.getEmail() + "'. Please use a different email.");

			} else {
				e.printStackTrace();
			}
            return false;
        }
		return true;
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

    public boolean isDeleteEmployeeSuccessful(Employee employee){
        String query = "DELETE FROM employees where employee_id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
                stmt.setInt(1, employee.getEmployeeID());

                int affectedRows = stmt.executeUpdate();
                
                return affectedRows > 0;
            } catch (SQLException e){
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

					DepartmentDAO dept = new DepartmentDAO();
	
					employee.setDepartmentName(dept.getDepartmentNameById(employee.getDepartmentID()));

					employees.add(employee);

				}

				
			} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return employees;
	}
}
