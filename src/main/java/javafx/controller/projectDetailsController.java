package main.java.javafx.controller;

import java.time.LocalDate;
import java.util.PrimitiveIterator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import main.java.javafx.dao.DepartmentDAO;
import main.java.javafx.model.Project;

public class projectDetailsController {
    

    @FXML
    private ComboBox<?> department_comboBox;

    @FXML
    private Button exit_btn;

    @FXML
    private TextArea projectDescription;

    @FXML
    private Label projectName;

    @FXML
    private Label departmentName;
    @FXML
    private Label deadlineDate;

    DashboardController dashboardController;

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;  // Ensure the reference is set
    }

    public void exit(){
        Stage stage = (Stage) exit_btn.getScene().getWindow();
        stage.close();
    }

    public void setProject(Project project){
        projectName.setText(project.getProjectName());
        projectDescription.setText(project.getDescription());
        LocalDate deadline = project.getEndDate();
        deadlineDate.setText(deadline.toString());
        String department = DepartmentDAO.getDepartmentNameById(project.getDepartmentId());
        departmentName.setText(department);
    }
}
