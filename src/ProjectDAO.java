import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    public static ObservableList<Project> getAllProjects() {
        ObservableList<Project> projects = FXCollections.observableArrayList();
        String query = "SELECT project_id, project_name, description, start_date, end_date, department_id FROM projects"; // Adjust query as needed
 
        try {
            // Establish the connection
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            
            // Execute the SQL query
            ResultSet resultSet = statement.executeQuery(query);
            
        // Process the ResultSet
            while (resultSet.next()) {
                Project project = new Project();
                project.setProjectId(resultSet.getInt("project_id"));
                project.setProjectName(resultSet.getString("project_name"));
                project.setDescription(resultSet.getString("description"));
                project.setStartDate(resultSet.getDate("start_date").toLocalDate());
                project.setEndDate(resultSet.getDate("end_date").toLocalDate());
                project.setDepartment(resultSet.getInt("department_id"));

                projects.add(project);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projects;
    }
}
