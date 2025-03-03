package main.java.javafx.controller;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.java.javafx.dao.DepartmentDAO;
import main.java.javafx.dao.EmployeeDAO;
import main.java.javafx.dao.ProjectDAO;
import main.java.javafx.faceRecog.Capture;
import main.java.javafx.faceRecog.Training;
import main.java.javafx.model.Department;
import main.java.javafx.model.Employee;
import main.java.javafx.model.Project;

public class AlertController {
    @FXML
    private Button alertCancelButton;

    @FXML
    private Button alertConfirmButton;

    @FXML
    private Label alertHeader;

    @FXML
    private Label alertMessage;

    @FXML
    private Label confirmMessage;

    @FXML
    private Label cancelMessage;

    private DashboardController dashboardController;
    private departmentDetailsController deptdetailController;

    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller; // Store the reference
    }

    public void setDeleteDepartmentCont(departmentDetailsController controller){
        this.deptdetailController = controller;
    }
    
    public void setHeader(String header){
        alertHeader.setText(header);
    }

    public void setMessage(String message){
        alertMessage.setText(message);
    }

    public void setConfirmName(String confirm){
        confirmMessage.setText(confirm);
    }

    public void confirmButtonStyle(int r, int g, int b){
        alertConfirmButton.setStyle("-fx-background-color: rgb(" + r + "," + g + "," + b + "); -fx-cursor: hand;");
        confirmMessage.setTextFill(Color.WHITE);

        alertConfirmButton.setOnMouseEntered(event -> {
            alertConfirmButton.setStyle("-fx-background-color: #fff; -fx-cursor: hand; -fx-border-color: rgb(" + r + "," + g + "," + b + ");");
            confirmMessage.setTextFill(Color.rgb(r,g,b));
        });
        alertConfirmButton.setOnMouseExited(event -> {
            alertConfirmButton.setStyle("-fx-background-color: rgb(" + r + "," + g + "," + b + "); -fx-cursor: hand;");
            confirmMessage.setTextFill(Color.WHITE);
        });
    }

    public void cancelButtonStyle_delete(String firstColor, String secondColor){
        alertCancelButton.setStyle("-fx-background-color: linear-gradient(to right," + firstColor + "," + secondColor + "); -fx-cursor: hand;");
        cancelMessage.setStyle("-fx-text-fill: #fff;");
        
        alertCancelButton.setOnMouseEntered(event -> {
            alertCancelButton.setStyle("-fx-background-color: #fff; -fx-cursor: hand; -fx-border-color: linear-gradient(to right," + firstColor + "," + secondColor + ");");
            cancelMessage.setStyle("-fx-text-fill: linear-gradient(to right," + firstColor + "," + secondColor + ");");
        });
        alertCancelButton.setOnMouseExited(event -> {
            alertCancelButton.setStyle("-fx-background-color: linear-gradient(to right," + firstColor + "," + secondColor + "); -fx-cursor: hand;");
            cancelMessage.setStyle("-fx-text-fill: #fff;");
        });
    }

    public void deleteDepartment(Department department){
        alertConfirmButton.setOnAction(event -> {
            DepartmentDAO dep = new DepartmentDAO();
            if(dep.isDeleteDepartmentSuccessful(department)){
                System.out.println("Department Deleted Successfully");
                
                if(deptdetailController != null){
                    System.out.println("Department updated Successfully");
                    deptdetailController.refreshDepartmentDetails();
                    deptdetailController.exit();
                }
                
                Stage stage = (Stage) alertConfirmButton.getScene().getWindow();
                stage.close();

            } else {
                Alert alert = new Alert(AlertType.ERROR);
                
                alert.setTitle("Delete Department Failed!");
                alert.setHeaderText(null);
                alert.setContentText(department.getName() + " has an existing employee/project");
                alert.showAndWait();
            }
        });
    }
    
    public void deleteEmployee(Employee employee){
        alertConfirmButton.setOnAction(event -> {
            EmployeeDAO emp = new EmployeeDAO();
            if(emp.isDeleteEmployeeSuccessful(employee)){
                String basePath = System.getProperty("user.dir");
                String photoDirectory = basePath + "\\src\\main\\resources\\photos\\";

                // Create a file object for the directory
                File directory = new File(photoDirectory);

                // Get the list of all files in the directory
                File[] files = directory.listFiles();

                if (files != null) {
                    for (File file : files) {
                        // Check if the file name starts with the employee's name and person ID
                        if (file.getName().startsWith(employee.getFirstName() + "." + employee.getEmployeeID() + ".")) {
                            if (file.delete()) {
                                System.out.println("Deleted photo: " + file.getAbsolutePath());
                            } else {
                                System.out.println("Failed to delete photo: " + file.getAbsolutePath());
                            }
                        }
                    }
                }

                System.out.println("Employee Deleted Successfully: " + employee.getFirstName() + " " + employee.getLastName());
                if(dashboardController != null){
                    dashboardController.loadEmployeeData();
                }
    
                Stage stage = (Stage) alertConfirmButton.getScene().getWindow();
                stage.close();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                
                alert.setTitle("Message!");
                alert.setHeaderText(null);
                alert.setContentText("Delete Employee Failed");
                alert.showAndWait();
            }
        });
    }

    public void deleteProject(Project project){
        alertConfirmButton.setOnAction(event -> {
            ProjectDAO pro = new ProjectDAO();
            if(pro.isDeleteProjectSuccessfule(project)){
                System.out.println("Project Deleted Successfully");
                
                if(dashboardController != null){
                    System.out.println("Projects updated Successfully");
                    dashboardController.loadProjects();
                }
                
                Stage stage = (Stage) alertConfirmButton.getScene().getWindow();
                stage.close();

            } else {
                Alert alert = new Alert(AlertType.ERROR);
                
                alert.setTitle("Error!");
                alert.setHeaderText(null);
                alert.setContentText("Project Delete Failed");
                alert.showAndWait();
            }
        });
    }

    public void captureFace(String label, String id){
        
        alertConfirmButton.setOnAction(event -> {
            Capture cp = new Capture();
            Training train = new Training();

            try {
                cp.capture(label, id);
                train.training();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Stage stage = (Stage) alertConfirmButton.getScene().getWindow();
            stage.close();
            
        });
    }

    @FXML
    public void initialize() {
        alertCancelButton.setOnAction(event -> {
            // Close the alert when cancel is clicked
            alertCancelButton.getScene().getWindow().hide();
        });

        
    }





}