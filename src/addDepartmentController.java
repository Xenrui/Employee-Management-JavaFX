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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class addDepartmentController implements Initializable {
    @FXML
    private Button cancel_btn;

    @FXML
    private Button confirm_btn;

    @FXML
    private TextField departmentName_textfield;

    @FXML
    private ComboBox<String> status_comboBox;

    @FXML
    private Label addDepartment_label;

    private DashboardController dashboardController;

    private boolean isEditMode = false;

    private Department editableDepartment;

    public void setHeader(String header){
        this.addDepartment_label.setText(header);
    }
    public void editMode(Boolean isEditMode){
        this.isEditMode = isEditMode;
    }
    
    public void confirmButton(ActionEvent event){
        Department department = new Department();
        department.setDepartmentName(departmentName_textfield.getText());
        department.setDepartmentID(editableDepartment.getId());
        if(status_comboBox.getSelectionModel().getSelectedItem() == "Active"){
            department.setActive(true);
        } else {
            department.setActive(false);
        }

        if(!isEditMode){
            System.out.println("ADD MODE");
            DepartmentDAO departmentDAO = new DepartmentDAO();
            if(departmentDAO.isAddDepartmentSuccessful(department)){
                departmentName_textfield.setText("");

                if (dashboardController != null) {
                    dashboardController.loadDepartmentData(); // Refresh the TableView
                }
                Stage stage = (Stage) confirm_btn.getScene().getWindow();
                stage.close();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                
                alert.setTitle("Message!");
                alert.setHeaderText(null);
                alert.setContentText("Add Department Unsuccessful");
                alert.showAndWait();
            }
        } else {
            System.out.println("EDIT MODE");
            DepartmentDAO departmentDAO = new DepartmentDAO();
            if(departmentDAO.isUpdateDepartmentSuccessful(department)){
                departmentName_textfield.setText("");

                if (dashboardController != null) {
                    dashboardController.loadDepartmentData(); // Refresh the TableView
                }
                Stage stage = (Stage) confirm_btn.getScene().getWindow();
                stage.close();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                
                alert.setTitle("Message!");
                alert.setHeaderText(null);
                alert.setContentText("Add Department Unsuccessful");
                alert.showAndWait();
            }
        }
        isEditMode = false;
    }


    public void editDepartment(Department department){
        this.editableDepartment = department;
        isEditMode = true;
        departmentName_textfield.setText(department.getName());
        
        if (department.getActive()) {
            status_comboBox.getSelectionModel().select("Active");
        } else {
            status_comboBox.getSelectionModel().select("Inactive");
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
