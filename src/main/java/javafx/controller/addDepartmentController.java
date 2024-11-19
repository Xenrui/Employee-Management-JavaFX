package main.java.javafx.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.javafx.dao.DepartmentDAO;
import main.java.javafx.model.Department;

public class addDepartmentController implements Initializable {
    @FXML
    private Button cancel_btn;

    @FXML
    private Button confirm_btn;

    @FXML
    private TextArea departmentDescription_textfield;

    @FXML
    private TextField departmentName_textfield;

    @FXML
    private ComboBox<String> status_comboBox;

    private DashboardController dashboardController;
    
    public void confirmButton(ActionEvent event){
        Department department = new Department();
        department.setDepartmentName(departmentName_textfield.getText());
        department.setDescription(departmentDescription_textfield.getText());
        if(status_comboBox.getSelectionModel().getSelectedItem() == "Active"){
            department.setActive(true);
        } else {
            department.setActive(false);
        }
        
        System.out.println("ADD MODE");
            DepartmentDAO departmentDAO = new DepartmentDAO();
            if(departmentDAO.isAddDepartmentSuccessful(department)){
                departmentName_textfield.setText("");

                if (dashboardController != null) {
                    dashboardController.initialize(); // Refresh the TableView
                }
                Stage stage = (Stage) confirm_btn.getScene().getWindow();
                stage.close();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Message!");
                alert.setHeaderText(null);
                alert.setContentText("Department Already Exists");
                alert.showAndWait();
            }
    }

    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller; // Store the reference
    }

    public void cancelButton(ActionEvent event){
        departmentName_textfield.setText("");

        // cancel_btn.getScene().getWindow().hide();
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        status_comboBox.getItems().setAll("Active", "Inactive");
    }
}
