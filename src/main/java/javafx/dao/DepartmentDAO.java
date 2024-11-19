package main.java.javafx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.javafx.model.Department;

public class DepartmentDAO {

    public static String getDepartmentNameById(int departmentId) {
        String departmentName = null;
        String query = "SELECT department_name FROM departments WHERE department_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, departmentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                departmentName = resultSet.getString("department_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departmentName;
    }

    public static Department getDepartmentById(int id) {
        // Replace with your database query logic
        // Example:
        Department department = null;
        String query = "SELECT * FROM departments WHERE department_id = " + id;

        try (Connection connection = DatabaseConnection.getConnection(); 
             Statement statement = connection.createStatement(); 
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                department = new Department();
                department.setDepartmentID(resultSet.getInt("department_id"));
                department.setDepartmentName(resultSet.getString("department_name"));
                department.setDescription(resultSet.getString("department_description"));
                department.setActive(resultSet.getBoolean("active"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return department;
    }

    public static ObservableList<Department> getAllDepartment() {
        ObservableList<Department> departments = FXCollections.observableArrayList();
        String query = "SELECT department_id, department_name, department_description, active FROM departments"; // Adjust query as needed
 
        try {
            // Establish the connection
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            
            // Execute the SQL query
            ResultSet resultSet = statement.executeQuery(query);
            
        // Process the ResultSet
            while (resultSet.next()) {
                Department department = new Department();
                department.setDepartmentID(resultSet.getInt("department_id"));
                department.setDepartmentName(resultSet.getString("department_name"));
                department.setDescription(resultSet.getString("department_description"));
                department.setActive(resultSet.getBoolean("active"));
                departments.add(department);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return departments;
    }
    
    public static ObservableList<Department> getActiveDepartments() {
        ObservableList<Department> departments = FXCollections.observableArrayList();
        String query = "SELECT department_id, department_name, department_description FROM departments WHERE active = true"; // Adjust query as needed
 
        try {
            // Establish the connection
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            
            // Execute the SQL query
            ResultSet resultSet = statement.executeQuery(query);
            
        // Process the ResultSet
            while (resultSet.next()) {
                Department department = new Department();
                department.setDepartmentID(resultSet.getInt("department_id"));
                department.setDepartmentName(resultSet.getString("department_name"));
                department.setDescription(resultSet.getString("department_description"));
                departments.add(department);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return departments;
    }

    public boolean isDeleteDepartmentSuccessful(Department department){
        String sql = "DELETE FROM departments WHERE department_id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, department.getId());

                int affectedRows = stmt.executeUpdate();

                return affectedRows > 0;
            } catch(SQLException e){
                System.out.println("Delete Department Unsuccessful ");
                e.printStackTrace();
                return false;
            }
    }

    public boolean isUpdateDepartmentSuccessful(Department department){
        String query = "UPDATE departments SET department_name = ?, department_description = ?, active = ? WHERE department_id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
                stmt.setString(1, department.getName());
                stmt.setString(2, department.getDescription());
                stmt.setBoolean(3, department.getActive());
                stmt.setInt(4, department.getId());

                int affectedRows = stmt.executeUpdate();
                System.out.println("Affected rows: " + affectedRows);
                return affectedRows > 0;
        } catch (SQLException e){
            System.err.println("Error updating department with ID " + department.getId());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean isAddDepartmentSuccessful(Department department) {
        String sql = "INSERT INTO departments (department_name, active) SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM departments WHERE department_name = ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            // Set parameters for the insert statement
            stmt.setString(1, department.getName());
            stmt.setBoolean(2, department.getActive());
            stmt.setString(3, department.getName()); // Use the same name to check for existence
    
            // Execute the update
            int rowsAffected = stmt.executeUpdate();
    
            // If rowsAffected is greater than 0, the insert was successful
            return rowsAffected > 0;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
}
