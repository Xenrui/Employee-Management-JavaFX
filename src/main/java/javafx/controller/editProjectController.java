package main.java.javafx.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
import main.java.javafx.dao.DatabaseConnection;
import main.java.javafx.dao.DepartmentDAO;
import main.java.javafx.dao.ProjectDAO;
import main.java.javafx.model.Department;
import main.java.javafx.model.Project;

public class editProjectController implements Initializable{

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
    Project currentProject;

    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller; // Store the reference
    }

    public void cancelButton(ActionEvent event){
        projectTitle_textArea.setText("");
        projectDescription_textArea.setText("");
        // cancel_btn.getScene().getWindow().hide();
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
    
    public void setProject(Project project){
        currentProject = project;
        projectTitle_textArea.setText(project.getProjectName());
        projectDescription_textArea.setText(project.getDescription());
        deadline_date.setValue(project.getEndDate());

        Department department = DepartmentDAO.getDepartmentById(project.getDepartmentId());
        department_comboBox.setValue(department);
    }

    public void confirmButton(ActionEvent actionEvent){
        Project project = new Project();
        project.setProjectId(currentProject.getProjectId());
        project.setProjectName(projectTitle_textArea.getText());
        project.setDescription(projectDescription_textArea.getText());
        project.setEndDate(deadline_date.getValue());

        Department selectedDepartment = department_comboBox.getSelectionModel().getSelectedItem();

        if (selectedDepartment != null) {
            project.setDepartment(selectedDepartment.getId());
        } else {
            System.out.println("Please select a department.");
            return;
        }

        ProjectDAO pro = new ProjectDAO();
        if(pro.isUpdateProjectSuccessful(project)){
            
            if (dashboardController != null) {
                dashboardController.loadProjects(); // Refresh the TableView
            }    

            Stage stage = (Stage) confirm_btn.getScene().getWindow();
            stage.close();
        }
        else{

            Alert alert = new Alert(AlertType.ERROR);
            
            alert.setTitle("Message!");
            alert.setHeaderText(null);
            alert.setContentText("Edit Project Failed");
            alert.showAndWait();
        } 

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        loadDepartments();
    }

}
