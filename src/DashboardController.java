
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.lang.classfile.components.ClassPrinter.Node;
import java.net.URL;
import java.util.ResourceBundle;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;

public class DashboardController implements Initializable {


    @FXML
    private Button dash_btn;

    @FXML
    private AnchorPane dashboardBg;

    @FXML
    private FontAwesomeIcon dashicon;

    @FXML
    private Button employee_btn;

    @FXML
    private FontAwesomeIcon employeeIcon;

    @FXML
    private FontAwesomeIcon departmentIcon;

    @FXML
    private Button department_btn;

    @FXML
    private FontAwesomeIcon reportIcon;

    @FXML
    private Button report_btn;

    @FXML
    private Label user;
    
    @FXML
    private TableView<?> employeeTable;
    
    @FXML
    private TableColumn<?, ?> employeeID_col;
    
    @FXML
    private TableColumn<?, ?> employeeName_col;
    
    @FXML
    private TableColumn<?, ?> employeeEmail_col;
   
    @FXML
    private TableColumn<?, ?> employeePhone_col;

    @FXML
    private TableColumn<?, ?> employeeDept_col;

    @FXML
    private TableColumn<?, ?> employeeJob_col;

    @FXML
    private TableColumn<?, ?> actions_col;

    @FXML
    private Button addEmployee_btn;

    @FXML
    private FontAwesomeIcon addEmployee_icon;

    @FXML
    private PieChart employeePie;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private AnchorPane employeeDash_form;

    @FXML
    private AnchorPane departmentDash_form;

    @FXML
    private AnchorPane reportDash_form;

    private Button activeButton = null;
    private FontAwesomeIcon activeIcon = null;
    private AnchorPane activeForm = null;
    private double x = 0;
    private double y = 0;

    public void exit(){
        System.exit(0);
    }

    private void user(){
        user.setText(UserDAO.username);
    }
    
    private void alterAddEmployeeIcon(Button button, FontAwesomeIcon icon){
        button.setOnMouseEntered(event -> {
            icon.setFill(Color.rgb(57, 125, 189));
        });

        button.setOnMouseExited(event -> {
            icon.setFill(Color.rgb(255, 255, 255));
        });
    }

        private void selectButton(Button button, FontAwesomeIcon icon, AnchorPane form) {

            if (activeForm != null) {
                activeForm.setVisible(false);
            }    
        
            // Reset the previous active button and icon styles
            if (activeButton != null && activeIcon != null) {
                activeButton.setStyle("-fx-background-color: transparent; -fx-border-radius: 7px; -fx-cursor: hand;");
                activeIcon.setFill(Color.WHITE);
                dashboardBg.setVisible(true);
            }
            
            // Set the new active button and icon
            activeButton = button;
            activeButton.setStyle("-fx-background-color: #fff; -fx-text-fill: #000; -fx-border-radius: 7px;");
            
            activeIcon = icon;
            icon.setFill(Color.BLACK);
        
            activeForm = form;
            form.setVisible(true);
            
        }
    
    public void navButton() {

        // Hover effect for dash_btn
        dash_btn.setOnMouseEntered(event -> {
            if (activeButton != dash_btn) { // Only apply hover if not selected
                dash_btn.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #000; -fx-border-radius: 7px;");
                dashicon.setFill(Color.BLACK);
            }
        });
        dash_btn.setOnMouseExited(event -> {
            if (activeButton != dash_btn) { // Reset to default if not selected
                dash_btn.setStyle("-fx-background-color: transparent; -fx-border-radius: 7px; -fx-cursor: hand;"); 
                dashicon.setFill(Color.WHITE);
            }
        });
    
        // Repeat hover setup for other buttons
        employee_btn.setOnMouseEntered(event -> {
            if (activeButton != employee_btn) {
                employee_btn.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #000; -fx-border-radius: 7px;");
                employeeIcon.setFill(Color.BLACK);
            }
        });

        employee_btn.setOnMouseExited(event -> {
            if (activeButton != employee_btn) {
                employee_btn.setStyle("-fx-background-color: transparent; -fx-border-radius: 7px; -fx-cursor: hand;");
                employeeIcon.setFill(Color.WHITE);
            }
        });
    
        department_btn.setOnMouseEntered(event -> {
            if (activeButton != department_btn) {
                department_btn.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #000; -fx-border-radius: 7px;");
                departmentIcon.setFill(Color.BLACK);
            }
        });
        department_btn.setOnMouseExited(event -> {
            if (activeButton != department_btn) {
                department_btn.setStyle("-fx-background-color: transparent; -fx-border-radius: 7px; -fx-cursor: hand;");
                departmentIcon.setFill(Color.WHITE);
            }
        });
    
        report_btn.setOnMouseEntered(event -> {
            if (activeButton != report_btn) {
                report_btn.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #000; -fx-border-radius: 7px;");
                reportIcon.setFill(Color.BLACK);
            }
        });
        report_btn.setOnMouseExited(event -> {
            if (activeButton != report_btn) {
                report_btn.setStyle("-fx-background-color: transparent; -fx-border-radius: 7px; -fx-cursor: hand;");
                reportIcon.setFill(Color.WHITE);
            }
        });
    
        // Click events to select a button and update the activeButton variable
        dash_btn.setOnMouseClicked(event -> {
            selectButton(dash_btn, dashicon, dashboard_form);
        });
        employee_btn.setOnMouseClicked(event -> {
            selectButton(employee_btn, employeeIcon, employeeDash_form);
        });
        department_btn.setOnMouseClicked(event -> {
            selectButton(department_btn, departmentIcon, departmentDash_form);
        });
        report_btn.setOnMouseClicked(event -> {
            selectButton(report_btn, reportIcon, reportDash_form);
        });
    }

    public void defaultHomeDesign(){
            selectButton(dash_btn, dashicon, dashboard_form);
    }

    public void addEmployee(ActionEvent actionEvent) throws Exception {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("addEmp.fxml"));

        // Apply the CSS to the root scene
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("design.css").toExternalForm());
        stage.setScene(scene);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow());

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
    }

    @Override
    public void initialize(URL url, ResourceBundle resource){
        
        alterAddEmployeeIcon(addEmployee_btn, addEmployee_icon);
        navButton();
        defaultHomeDesign();
        user();
    }

}
