package main.java.javafx.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.javafx.dao.DatabaseConnection;
import main.java.javafx.dao.EmployeeDAO;
import main.java.javafx.faceRecog.Training;
import main.java.javafx.model.Department;
import main.java.javafx.model.Employee;

import java.util.ResourceBundle;
import java.sql.Statement;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;


public class addEmployeeController implements Initializable {

    @FXML
    private Button cancel_btn;

    @FXML
    private Button confirm_btn;

    @FXML
    private ComboBox<Department> department;

    @FXML
    private TextField email;

    @FXML
    private TextField firstName;

    @FXML
    private TextField jobTitle;

    @FXML
    private TextField lastName;

    @FXML
    private TextField phone;

    private DashboardController dashboardController;
    private double x = 0;
    private double y = 0;

    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller; // Store the reference
    }
    

    public void cancelButton(ActionEvent event){
        firstName.setText("");
        lastName.setText("");
        jobTitle.setText("");
        phone.setText("");
        email.setText("");
    
        // cancel_btn.getScene().getWindow().hide();
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

        department.setItems(departments);
    }


    public void addEmployee(ActionEvent event) {
        Employee employee = new Employee();
        employee.setFirstName(firstName.getText());
        employee.setLastName(lastName.getText());
        employee.setEmail(email.getText());
        employee.setPhoneNum(phone.getText());
        employee.setJobTitle(jobTitle.getText());
        
        Department selectedDepartment = department.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            employee.setDepartmentID(selectedDepartment.getId());
            employee.setDepartmentName(selectedDepartment.getName());
        } else {
            System.out.println("Please select a department.");
            return;
        }
        
        employee.setHireDate(LocalDate.now());
        
        EmployeeDAO emp = new EmployeeDAO();
        
        if (emp.isAddEmployeeSuccessful(employee)) {
            System.out.println("Employee added successfully.");
            // Refresh the TableView if dashboardController is available
            if (dashboardController != null) {
                dashboardController.loadEmployeeData();
            }
            Stage stage = (Stage) confirm_btn.getScene().getWindow();
            stage.close();
            try {
                // Load the custom alert
                Stage stage2 = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/Alert.fxml"));
                Parent root = loader.load();
                
                AlertController alertController = loader.getController();
                alertController.setConfirmName("Continue");
                alertController.setHeader("Face Registration");
                alertController.setMessage("Prepare your face and look directly at the camera");
                alertController.confirmButtonStyle(53, 185, 64);
                alertController.cancelButtonStyle_delete("#db593c", "#db593c");
                alertController.captureFace(employee.getFirstName(), String.valueOf(employee.getEmployeeID()));
                
                Scene scene = new Scene(root);
                stage2.setScene(scene);
                stage2.initModality(Modality.WINDOW_MODAL);
                stage2.initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
                
                // Configure stage position and style
                stage2.setX(650);
                stage2.setY(300);
                stage2.initStyle(StageStyle.TRANSPARENT);
                
                // Make the stage draggable
                root.setOnMousePressed(mouseEvent -> {
                    x = mouseEvent.getSceneX();
                    y = mouseEvent.getSceneY();
                });
                root.setOnMouseDragged(mouseEvent -> {
                    stage2.setX(mouseEvent.getScreenX() - x);
                    stage2.setY(mouseEvent.getScreenY() - y);
                    stage2.setOpacity(0.9);
                });
                root.setOnMouseReleased(mouseEvent -> stage2.setOpacity(1));
                
                // Display the alert
                stage2.show();
                
                Platform.runLater(root::requestFocus);
            } catch (Exception e) {
                System.err.println("Error displaying alert:");
                e.printStackTrace();
            }
            
            // Clear input fields
            firstName.setText("");
            lastName.setText("");
            jobTitle.setText("");
            phone.setText("");
            email.setText("");
        } else {
            // Show error alert for duplicate email
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Message");
            alert.setHeaderText(null);
            alert.setContentText("Email Already Exists!");
            alert.showAndWait();
        }
    }
    


    @Override
    public void initialize(URL url, ResourceBundle resource){
        loadDepartments();
    }
}
