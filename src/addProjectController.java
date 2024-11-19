import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class addProjectController implements Initializable {
    @FXML
    private Button cancel_btn;

    @FXML
    private Button confirm_btn;

    @FXML
    private DatePicker deadline_date;

    @FXML
    private ComboBox<Department> department_comboBox;

    @FXML
    private TextArea projectDescription_textArea;

    @FXML
    private TextField projectTitle_textArea;

    DashboardController dashboardController;

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;  // Ensure the reference is set
    }
    
    public void cancelButton(){
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }

    private void loadDepartments() {
        ObservableList<Department> departments = FXCollections.observableArrayList();
        String query = "SELECT department_id, department_name FROM departments WHERE active = true"; // Adjust query as needed
 
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
                departments.add(department);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        department_comboBox.setItems(departments);
    }

    public void addProject(ActionEvent event){ 
        Project project = new Project();
        project.setProjectName(projectTitle_textArea.getText());
        project.setDescription(projectDescription_textArea.getText());
        project.setStartDate(LocalDate.now());
        Department selectedDepartment = department_comboBox.getSelectionModel().getSelectedItem();

        if (selectedDepartment != null) {
            project.setDepartment(selectedDepartment.getId());
        } else {
            System.out.println("Please select a department.");
            return;
        }
        
        LocalDate deadline = deadline_date.getValue();
        if (deadline != null) {
            project.setEndDate(deadline); // Deadline from DatePicker is already a LocalDate
        } else {
            // Handle the case where the deadline is not set
            System.out.println("Deadline date is not set.");
            return;
        }

        ProjectDAO projectDAO = new ProjectDAO();
        if(projectDAO.isAddProjectSuccessful(project)){
            System.out.println("Project added");

            if (dashboardController != null) {
                System.out.println("Reload Projects");
                dashboardController.loadProjects(); // Refresh the projects
            }

            project.setProjectName("");
            project.setDescription("");

            Stage stage = (Stage) confirm_btn.getScene().getWindow();
            stage.close();
        }
        else{

            Alert alert = new Alert(AlertType.ERROR);
            
            alert.setTitle("Message!");
            alert.setHeaderText(null);
            alert.setContentText("Add Project Unsuccessful!");
            alert.showAndWait();
        } 
        
    }

    private static class DatePickerCell extends javafx.scene.control.DateCell {
        @Override
        public void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);
    
            // Disable past dates
            if (date.isBefore(LocalDate.now())) {
                setDisable(true);
                setStyle("-fx-background-color: #d3d3d3;"); // Optional: Grey out past dates
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        loadDepartments();
        deadline_date.setDayCellFactory(picker -> new DatePickerCell());
    }

}
