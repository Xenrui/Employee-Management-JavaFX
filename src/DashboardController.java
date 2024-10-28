
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import de.jensd.fx.glyphs.fontawesome.*;
import java.lang.classfile.components.ClassPrinter.Node;
import java.net.URL;
import java.util.ResourceBundle;
import de.jensd.*;
import javax.swing.Action;

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
    private TableView<Employee> employeeTable;
    
    @FXML
    private TableColumn<Employee, Integer> employeeID_col;
    
    @FXML
    private TableColumn<?, ?> employeeName_col;

    @FXML
    private TableColumn<Employee, String> firstName_col;

    @FXML
    private TableColumn<Employee, String> lastName_col;

    @FXML
    private TableColumn<Employee, String> employeeEmail_col;
   
    @FXML
    private TableColumn<Employee, String> employeePhone_col;

    @FXML
    private TableColumn<Employee, String> employeeDept_col;

    @FXML
    private TableColumn<Employee, String> employeeJob_col;

    @FXML
    private TableColumn<Employee, String> actions_col;

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
    private ObservableList<Employee> employeeList;
    

    public void exit(){
        System.exit(0);
    }

    private void user(){
        user.setText(UserDAO.username);
    }
    
    public void employeePieChart(PieChart employeePie){
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("Category 1", 40),
            new PieChart.Data("Category 2", 30),
            new PieChart.Data("Category 3", 20),
            new PieChart.Data("Category 4", 10)
        
        );
        employeePie.setData(pieChartData);
        
    }

    private void initTable(){
        employeeID_col.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        firstName_col.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName_col.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        employeeEmail_col.setCellValueFactory(new PropertyValueFactory<>("email"));
        employeePhone_col.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        employeeDept_col.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        employeeJob_col.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));

        actions_col.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button();
            private FontAwesomeIcon editIcon = new FontAwesomeIcon();
       
            {
                editIcon.setGlyphName("EDIT");
    
                editButton.setGraphic(editIcon);
                editButton.setOnAction(event -> {
                    Employee employee = getTableView().getItems().get(getIndex());
                    editEmployee(event, employee);
                    System.out.println("Editing employee: " + employee.getFirstName());
                    
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });
        loadEmployeeData();
    }

    public void loadEmployeeData(){
        
        employeeList = new EmployeeDAO().getAllEmployees();
        employeeTable.setItems(employeeList);
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
    public void editEmployee(ActionEvent event, Employee employee){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editEmp.fxml"));
            Parent root = loader.load();


            editEmployeeController controller = loader.getController();
            controller.editEmployee(employee);
            controller.setDashboardController(this);
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

    public void addEmployee(ActionEvent actionEvent) throws Exception {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addEmp.fxml"));
        Parent root = loader.load();

        addEmployeeController controller = loader.getController();
        controller.setDashboardController(this);

        // Apply the CSS to the root scene
        Scene scene = new Scene(root);
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
        employeePieChart(employeePie);
        initTable();
    }

}
