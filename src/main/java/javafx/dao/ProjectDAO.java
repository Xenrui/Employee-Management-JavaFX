package main.java.javafx.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.javafx.model.Project;

public class ProjectDAO {
    
    public boolean isAddProjectSuccessful(Project project){
        String sql = "INSERT INTO projects (project_name, description, start_date, end_date, department_id) VALUES (?,?,?,?,?)";
        
        try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)){
                
                stmt.setString(1, project.getProjectName());
                stmt.setString(2, project.getDescription());
                stmt.setDate(3, Date.valueOf(project.getStartDate())); 
                stmt.setDate(4, (Date.valueOf(project.getEndDate())));
                stmt.setInt(5, project.getDepartmentId());

                stmt.executeUpdate();
				

        } catch (SQLException e){
			e.printStackTrace();
            return false;
        }
		return true;
    }

    public boolean isUpdateProjectSuccessful(Project project){
        String query = "UPDATE projects SET project_name = ?, description = ?, end_date = ?, department_id = ? WHERE project_id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
                
                stmt.setString(1, project.getProjectName());
                stmt.setString(2, project.getDescription());
                stmt.setDate(3, Date.valueOf(project.getEndDate()));
                stmt.setInt(4, project.getDepartmentId());
                stmt.setInt(5, project.getProjectId());

                int affectedRows = stmt.executeUpdate();
                System.out.println("Affected rows: " + affectedRows);
                return affectedRows > 0;
        } catch (SQLException e){
            System.err.println("Error updating project with ID " + project.getProjectId());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean isDeleteProjectSuccessfule(Project project){
        String query = "DELETE FROM projects where project_id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
                stmt.setInt(1, project.getProjectId());

                int affectedRows = stmt.executeUpdate();
                
                return affectedRows > 0;
            } catch (SQLException e){
                e.printStackTrace();
                return false; 
            }
    }

    public static Map<LocalDate, List<String>> getAllDeadlines() {
        // Change the Map to hold a list of project names for each LocalDate
        Map<LocalDate, List<String>> deadlines = new HashMap<>();
        String query = "SELECT project_name, end_date FROM projects";

        try {
            // Establish connection and create statement
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String projectName = rs.getString("project_name");
                LocalDate endDate = rs.getDate("end_date").toLocalDate();

                // If the date already has entries, add the project to the list
                deadlines.computeIfAbsent(endDate, k -> new ArrayList<>()).add(projectName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return deadlines;
    }
    
    public static ObservableList<Project> getAllProjects() {
        ObservableList<Project> projects = FXCollections.observableArrayList();
        String query = "SELECT project_id, project_name, description, start_date, end_date, department_id, is_Finished FROM projects"; // Adjust query as needed
 
        try {
            // Establish the connection
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            
            // Execute the SQL query
            ResultSet resultSet = statement.executeQuery(query);

            LocalDate today = LocalDate.now();
            
        // Process the ResultSet
            while (resultSet.next()) {
                Project project = new Project();
                project.setProjectId(resultSet.getInt("project_id"));
                project.setProjectName(resultSet.getString("project_name"));
                project.setDescription(resultSet.getString("description"));
                project.setStartDate(resultSet.getDate("start_date").toLocalDate());
                project.setEndDate(resultSet.getDate("end_date").toLocalDate());
                project.setDepartment(resultSet.getInt("department_id"));
                
                LocalDate endDate = project.getEndDate();
                project.setStatus(resultSet.getBoolean("is_Finished") || endDate.isBefore(today));

                projects.add(project);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projects;
    }

    public static ObservableList<Project> getProjectsByDepartment(int departmentId){
        ObservableList<Project> projects = FXCollections.observableArrayList();
        String query = "SELECT project_id, project_name, description, start_date, end_date, is_Finished FROM projects WHERE department_id = ? "; // Adjust query as needed
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, departmentId);
            LocalDate today = LocalDate.now();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Project project = new Project();
                    project.setProjectId(rs.getInt("project_id"));
                    project.setProjectName(rs.getString("project_name"));
                    project.setDescription(rs.getString("description"));
                    project.setStartDate(rs.getDate("start_date").toLocalDate());
                    project.setEndDate(rs.getDate("end_date").toLocalDate());

                    LocalDate endDate = project.getEndDate();
                    project.setStatus(rs.getBoolean("is_Finished") || endDate.isBefore(today));

                    projects.add(project);
                    
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }
}
