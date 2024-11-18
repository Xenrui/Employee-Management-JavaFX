
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
public class departmentDetailsController {

    @FXML
    private Button deleteDepartment_btn;

    @FXML
    private FontAwesomeIcon deleteDepartment_icon;

    @FXML
    private Label departmentDetailsLabel;

    @FXML
    private Label departmentNameLabel;

    @FXML
    private Button editDepartment_btn;

    @FXML
    private FontAwesomeIcon editDepartment_icon;

    @FXML
    private Button exit_btn;

    private Department currentDepartment;

    private double x = 0;
    private double y = 0;

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
        loadDepartmentDetails(); // Fetch and display department details based on the ID
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EditDepartment.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Alert.fxml"));
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
    
}

