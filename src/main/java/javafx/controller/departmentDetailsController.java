package main.java.javafx.controller;


import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.javafx.dao.DepartmentDAO;
import main.java.javafx.dao.EmployeeDAO;
import main.java.javafx.dao.ProjectDAO;
import main.java.javafx.model.Department;
import main.java.javafx.model.Employee;
import main.java.javafx.model.EmployeeTableRow;
import main.java.javafx.model.Project;

public class departmentDetailsController implements Initializable {

    @FXML
    private Button deleteDepartment_btn;

    @FXML
    private FontAwesomeIcon deleteDepartment_icon;

    @FXML
    private Label departmentDetailsLabel;

    @FXML
    private Label departmentNameLabel;

    @FXML
    private Label deparmentEmployeeNum;
    
    @FXML
    private Button editDepartment_btn;

    @FXML
    private FontAwesomeIcon editDepartment_icon;

    @FXML
    private FontAwesomeIcon statusIcon;

    @FXML
    private Button exit_btn;
    
    @FXML
    private TableView<EmployeeTableRow> employeeTable;
    
    @FXML
    private TableColumn<EmployeeTableRow, String> employeeName_col;

    @FXML
    private TableColumn<EmployeeTableRow, String> jobTitle_col;

    @FXML
    private VBox projectContainer;

    private Department currentDepartment;
    private double x = 0;
    private double y = 0;
    private int employeeNum;

    private DashboardController dashboardController;

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;  // Ensure the reference is set
    }

    public void exit(){
        Stage stage = (Stage) exit_btn.getScene().getWindow();
        stage.close();
    }

    public void setDepartment(Department department) {
        this.currentDepartment = department;
        loadEmployeeData();
        loadDepartmentDetails(); // Fetch and display department details based on the ID
        populateProjects();
    }

    public void alterEditEmployeeIcon(Button button, FontAwesomeIcon icon){

        editDepartment_btn.setOnMouseEntered(event -> {
            editDepartment_icon.setFill(Color.rgb(57, 125, 189));
            editDepartment_btn.setStyle("-fx-background-color: #fff; -fx-background-radius: 7px; -fx-border-color: #397dbd; -fx-border-radius: 7px; -fx-cursor: hand;");
        });
        editDepartment_btn.setOnMouseExited(event -> {
            editDepartment_icon.setFill(Color.WHITE);
            editDepartment_btn.setStyle("-fx-background-color: #397dbd; -fx-background-radius: 7px; -fx-border-color: #397dbd; -fx-border-radius: 7px; -fx-cursor: hand;"); 
        });

        deleteDepartment_btn.setOnMouseEntered(event -> {
            deleteDepartment_icon.setFill(Color.rgb(219, 89, 60));
            deleteDepartment_btn.setStyle("-fx-background-color: #fff; -fx-background-radius: 7px; -fx-border-color: #db593c; -fx-border-radius: 7px; -fx-cursor: hand;");
        });
        deleteDepartment_btn.setOnMouseExited(event -> {
            deleteDepartment_icon.setFill(Color.WHITE);
            deleteDepartment_btn.setStyle("-fx-background-color: #db593c; -fx-background-radius: 7px; -fx-border-color: #db593c; -fx-border-radius: 7px; -fx-cursor: hand;"); 
        });

    }

    public void refreshDepartmentDetails() { // Method to load department details based on the department ID
        dashboardController.initialize();
        Department department = DepartmentDAO.getDepartmentById(currentDepartment.getId());
        currentDepartment = department;
        loadDepartmentDetails();  // This will reload the department details
    }
    
    private Department loadDepartmentDetails() {
        
        if (currentDepartment != null) {
            departmentNameLabel.setText(currentDepartment.getName()); // Set department name
            departmentDetailsLabel.setText(currentDepartment.getDescription()); // Set other details
            deparmentEmployeeNum.setText(Integer.toString(employeeNum));
            if (currentDepartment.getActive()) {
                statusIcon.setGlyphName("CHECK_CIRCLE");  // Set the active icon
                statusIcon.setFill(Color.GREEN); // Green icon for active
            } else {
                statusIcon.setGlyphName("TIMES_CIRCLE");  // Set the inactive icon
                statusIcon.setFill(Color.RED);; // Red icon for inactive
            }
        
            statusIcon.setSize("40"); 
            
        } else {
            departmentNameLabel.setText("Department not found");
            departmentDetailsLabel.setText("");
        }
        return currentDepartment;
    }

    public void editDepartment(ActionEvent event) {
        try {
            // Fetch the department details before editing
            Department department = loadDepartmentDetails();
            if (department != null) {
                // Create a new stage for editing
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/EditDepartment.fxml"));
                Parent root = loader.load();

                // Pass the department to the controller
                editDepartmentController controller = loader.getController();
                controller.setDashboardController(dashboardController);
                controller.editDepartment(department);

                // Apply the scene to the stage
                Scene scene = new Scene(root);
                stage.setScene(scene);

                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());

                Stage parentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                Parent parentRoot = parentStage.getScene().getRoot();
            
                // Create an overlay pane with a semi-transparent grey background
                Pane overlay = new Pane();
                overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);"); // 30% opacity grey
                overlay.setPrefSize(parentRoot.getBoundsInParent().getWidth(), parentRoot.getBoundsInParent().getHeight());
            
                // Add overlay to the parent root
                if (parentRoot instanceof Pane) {
                    ((Pane) parentRoot).getChildren().add(overlay);
                } else {
                    StackPane stackPane = new StackPane(parentRoot);
                    stackPane.getChildren().add(overlay);
                    parentStage.getScene().setRoot(stackPane);
                }
            
                // Apply blur effect to the parent window
                GaussianBlur blurEffect = new GaussianBlur(10); // Apply blur
                parentRoot.setEffect(blurEffect);
            
                // Restore the parent window when the modal is closed
                stage.setOnHidden(e -> {
                    parentRoot.setEffect(null); // Remove blur
                    parentRoot.setOpacity(1); // Restore opacity
                    if (parentRoot instanceof Pane) {
                        ((Pane) parentRoot).getChildren().remove(overlay);
                    } else {
                        parentStage.getScene().setRoot(parentRoot); // Remove overlay
                    }
                });
            
                // Handle window dragging
                root.setOnMousePressed((MouseEvent mouseEvent) -> {
                    x = mouseEvent.getSceneX();
                    y = mouseEvent.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent mouseEvent) -> {
                    stage.setX(mouseEvent.getScreenX() - x);
                    stage.setY(mouseEvent.getScreenY() - y);
                    stage.setOpacity(0.9);
                });

                root.setOnMouseReleased((MouseEvent mouseEvent) -> {
                    stage.setOpacity(1);
                });

                // Set stage style to transparent
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.show();

                Platform.runLater(root::requestFocus);
            } else {
                // Handle case if department not found
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Department not found. Please try again.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteDepartment(ActionEvent event){
        try{
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/Alert.fxml"));
            Parent root = loader.load();

            AlertController controller = loader.getController();
            controller.setDeleteDepartmentCont(this);
            controller.setHeader("Delete Department");
            controller.setMessage("Do you want to remove " + currentDepartment.getName());
            controller.cancelButtonStyle_delete("#03305a", "#00d4ff");
            controller.confirmButtonStyle(219, 89, 60);
            controller.setConfirmName("DELETE");
            controller.deleteDepartment(currentDepartment);
            // Apply the CSS to the root scene
            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
            Stage parentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Parent parentRoot = parentStage.getScene().getRoot();
        
            // Create an overlay pane with a semi-transparent grey background
            Pane overlay = new Pane();
            overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);"); // 30% opacity grey
            overlay.setPrefSize(parentRoot.getBoundsInParent().getWidth(), parentRoot.getBoundsInParent().getHeight());
        
            // Add overlay to the parent root
            if (parentRoot instanceof Pane) {
                ((Pane) parentRoot).getChildren().add(overlay);
            } else {
                StackPane stackPane = new StackPane(parentRoot);
                stackPane.getChildren().add(overlay);
                parentStage.getScene().setRoot(stackPane);
            }
        
            // Apply blur effect to the parent window
            GaussianBlur blurEffect = new GaussianBlur(10); // Apply blur
            parentRoot.setEffect(blurEffect);
        
            // Restore the parent window when the modal is closed
            stage.setOnHidden(e -> {
                parentRoot.setEffect(null); // Remove blur
                parentRoot.setOpacity(1); // Restore opacity
                if (parentRoot instanceof Pane) {
                    ((Pane) parentRoot).getChildren().remove(overlay);
                } else {
                    parentStage.getScene().setRoot(parentRoot); // Remove overlay
                }
            });
            
            // Handle window dragging
            root.setOnMousePressed((MouseEvent mouseEvent) -> {
                x = mouseEvent.getSceneX();
                y = mouseEvent.getSceneY();
            });

            root.setOnMouseDragged((MouseEvent mouseEvent) -> {
                stage.setX(mouseEvent.getScreenX() - x);
                stage.setY(mouseEvent.getScreenY() - y);
                stage.setOpacity(0.9);
            });

            root.setOnMouseReleased((MouseEvent mouseEvent) -> {
                stage.setOpacity(1);
            });

            // Set stage style to transparent to allow for rounded corners
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();

            Platform.runLater(root::requestFocus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadEmployeeData(){
        if (currentDepartment == null) {
            return;
        }

        EmployeeDAO employeeDAO = new EmployeeDAO();
        Map<String, String> employees = employeeDAO.getEmployeesInSameDepartment(currentDepartment.getId());
        employeeNum = employees.size();
        ObservableList<EmployeeTableRow> employeeRows = FXCollections.observableArrayList();
        for (Map.Entry<String, String> entry : employees.entrySet()) {
            EmployeeTableRow row = new EmployeeTableRow(entry.getKey(), entry.getValue());
            employeeRows.add(row);
        }
        employeeTable.setFocusTraversable(false);
        employeeTable.requestFocus();
        employeeTable.setItems(employeeRows);


    }
    
    private void populateProjects() { //call createProjectBox for each projects
        projectContainer.getChildren().clear();
        projectContainer.setFocusTraversable(false);
        ObservableList<Project> projects = ProjectDAO.getProjectsByDepartment(currentDepartment.getId());

        for(Project project: projects){
            VBox deadlineBox = createProjectBox(project);
            projectContainer.getChildren().add(deadlineBox);
        }
    }

    private VBox createProjectBox(Project project) {

        VBox card = new VBox(10);

        // Create an HBox representing the project
        HBox titleBox = new HBox();
        card.setStyle("-fx-border-color: lightgray; -fx-padding: 10; -fx-background-color: #f9f9f9;");
        card.setAlignment(Pos.CENTER_LEFT);
    
        // Label for project name
        Label projectNameLabel = new Label(project.getProjectName());
        projectNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14"); // Optional styling for label
        
        // Label for project deadline
        Label deadlineLabel = new Label("Deadline: " + project.getEndDate());
        deadlineLabel.setStyle("-fx-font-style: italic;"); 
    
        // Align the labels to the left-center
        HBox.setHgrow(projectNameLabel, Priority.NEVER);  
        projectNameLabel.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(deadlineLabel, Priority.NEVER); 
        deadlineLabel.setAlignment(Pos.CENTER_LEFT);
    
        // Create a Region to push the deadline label to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);  
        
        Label status = new Label();
        if(project.isFinished()){
            // toggle.foc
            status.setText("Finished");
            status.setStyle("-fx-text-fill: rgb(0,200,0); -fx-font-style: italic");
        } else {
            status.setText("Not Finished");
            status.setStyle("-fx-text-fill: rgb(255,0,0); -fx-font-style: italic");
        }
        // Add project name, deadline label, and spacer to the HBox
        titleBox.getChildren().addAll(projectNameLabel, spacer, status);
        
        VBox header = new VBox(3);
        header.getChildren().addAll(titleBox ,deadlineLabel);

        Label description = new Label();
        description.setText(project.getDescription());        
        

        card.getChildren().addAll(header, description);
        
        // Change background color on hover
        card.setOnMouseEntered(event -> card.setStyle("-fx-border-color: lightgray; -fx-padding: 10; -fx-background-color: #e0e0e0;"));
        card.setOnMouseExited(event -> card.setStyle("-fx-border-color: lightgray; -fx-padding: 10; -fx-background-color: #f9f9f9;"));
        card.setOnMouseClicked(event -> {
            dashboardController.viewProjectDetails(event, project);
        });
        return card;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        
        employeeName_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        jobTitle_col.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        alterEditEmployeeIcon(editDepartment_btn, editDepartment_icon);
    }
    
}

