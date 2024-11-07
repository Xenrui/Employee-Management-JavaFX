
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

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

    @FXML
    private TableColumn<Department, String> departmentStatus_col;

    @FXML
    private TableColumn<Department, Integer> departmentID_col;

    @FXML
    private TableColumn<Department, String> departmentName_col;

    @FXML
    private TableView<Department> department_table;

    @FXML
    private TableColumn<Department, String> department_actionsCol;

    @FXML
    private Button addDepartment_btn;

    @FXML
    private FontAwesomeIcon addDepartment_icon;

    @FXML
    private Label numDepartments;

    @FXML
    private Label numEmployees;


    private Button activeButton = null;
    private FontAwesomeIcon activeIcon = null;
    private AnchorPane activeForm = null;
    private double x = 0;
    private double y = 0;
    private ObservableList<Employee> employeeList;
    private ObservableList<Department> departmentList;

    public void exit(){
        System.exit(0);
    }

    private void user(){
        user.setText(UserDAO.username);
    }
    
    public void setNumbers(){
        EmployeeDAO emp = new EmployeeDAO();
        ObservableList<Employee> employees = emp.getAllEmployees();

        int numEmp = employees.size();
        System.out.println(numEmp);
        DepartmentDAO dep = new DepartmentDAO();
        ObservableList<Department> departments = dep.getActiveDepartments();

        int numDep = departments.size();
        System.out.println(numDep);
        
        numEmployees.setText(String.valueOf(numEmp));

        numDepartments.setText(String.valueOf(numDep));

    }
    public void createDepartmentPieChart(){
        EmployeeDAO emp = new EmployeeDAO();
        ObservableList<Employee> employees = emp.getAllEmployees();

        Map<String, Integer> departmentCount = new HashMap<>();

        for(Employee employee : employees){
            String departmentName = employee.getDepartmentName();
            departmentCount.put(departmentName, departmentCount.getOrDefault(departmentName, 0) + 1);
        }

        int totalEmployees = employees.size();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for(Map.Entry<String, Integer> entry : departmentCount.entrySet()){
            String departmentName = entry.getKey();
            int count = entry.getValue();
            double percentage = (double) count / totalEmployees * 100; // Calculate percentage

            // Create PieChart.Data with the percentage for the slice's display
            PieChart.Data pieData = new PieChart.Data(departmentName + " (" + String.format("%.1f%%", percentage) + ")", count);
            
            pieChartData.add(pieData);
            employeePie.setData(pieChartData);
        }
    }

    private void initDepartmentTable(){
        departmentID_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        departmentName_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        // departmentStatus_col.setCellValueFactory(new PropertyValueFactory<>("active"));

        departmentStatus_col.setCellValueFactory(param -> new SimpleStringProperty(
        param.getValue().getActive() ? "Active" : "Inactive"
        ));
        
        department_table.getStylesheets().add(getClass().getResource("dashboard.css").toExternalForm());
        
        department_actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button();
            private FontAwesomeIcon editIcon = new FontAwesomeIcon();
            
            private final Button deleteButton = new Button();
            private FontAwesomeIcon deleteIcon = new FontAwesomeIcon();

            private HBox actionsBox = new HBox(5);

            {
                editIcon.setGlyphName("EDIT");
                editIcon.setFill(Color.WHITE);
                editButton.setStyle("-fx-background-color: #397dbd; -fx-background-radius: 7px; -fx-border-color: #397dbd; -fx-border-radius: 7px; -fx-cursor: hand;");          
                editButton.setGraphic(editIcon);
                editButton.setOnAction(event -> {
                    Department department = getTableView().getItems().get(getIndex());
                    System.out.println(department.getId());
                    editDepartment(event, department);

                    System.out.println("Editing department: " + department.getName());
                    
                });

                editButton.setOnMouseEntered(event -> {
                    editIcon.setFill(Color.rgb(57, 125, 189));
                    editButton.setStyle("-fx-background-color: #fff; -fx-background-radius: 7px; -fx-border-color: #397dbd; -fx-border-radius: 7px; -fx-cursor: hand;");
                });
                editButton.setOnMouseExited(event -> {
                    editIcon.setFill(Color.WHITE);
                    editButton.setStyle("-fx-background-color: #397dbd; -fx-background-radius: 7px; -fx-border-color: #397dbd; -fx-border-radius: 7px; -fx-cursor: hand;"); 
                });

                deleteIcon.setGlyphName("TRASH");
                deleteIcon.setFill(Color.WHITE);
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setStyle("-fx-background-color: #db593c; -fx-background-radius: 7px; -fx-border-color: #db593c; -fx-border-radius: 7px; -fx-cursor: hand;"); 
                deleteButton.setOnAction(event -> {

                    //delete function
                    
                    Department department = getTableView().getItems().get(getIndex());
                    deleteDepartment(event, department);
                    System.out.println("Deleting department: " + department.getName());
                });

                deleteButton.setOnMouseEntered(event -> {
                    deleteIcon.setFill(Color.rgb(219, 89, 60));
                    deleteButton.setStyle("-fx-background-color: #fff; -fx-background-radius: 7px; -fx-border-color: #db593c; -fx-border-radius: 7px; -fx-cursor: hand;");
                });
                deleteButton.setOnMouseExited(event -> {
                    deleteIcon.setFill(Color.WHITE);
                    deleteButton.setStyle("-fx-background-color: #db593c; -fx-background-radius: 7px; -fx-border-color: #db593c; -fx-border-radius: 7px; -fx-cursor: hand;"); 
                });

                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.getChildren().addAll(editButton, deleteButton);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionsBox);
                }
            }
        });
        
        loadDepartmentData();

    }
    
    private void initEmployeeTable(){
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
            
            private final Button deleteButton = new Button();
            private FontAwesomeIcon deleteIcon = new FontAwesomeIcon();

            private HBox actionsBox = new HBox(5);

            {
                editIcon.setGlyphName("EDIT");
                editIcon.setFill(Color.WHITE);
                editButton.setStyle("-fx-background-color: #397dbd; -fx-background-radius: 7px; -fx-border-color: #397dbd; -fx-border-radius: 7px; -fx-cursor: hand;");          
                editButton.setGraphic(editIcon);
                editButton.setOnAction(event -> {
                    Employee employee = getTableView().getItems().get(getIndex());
                    editEmployee(event, employee);
                    System.out.println("Editing employee: " + employee.getFirstName());
                    
                });

                editButton.setOnMouseEntered(event -> {
                    editIcon.setFill(Color.rgb(57, 125, 189));
                    editButton.setStyle("-fx-background-color: #fff; -fx-background-radius: 7px; -fx-border-color: #397dbd; -fx-border-radius: 7px; -fx-cursor: hand;");
                });
                editButton.setOnMouseExited(event -> {
                    editIcon.setFill(Color.WHITE);
                    editButton.setStyle("-fx-background-color: #397dbd; -fx-background-radius: 7px; -fx-border-color: #397dbd; -fx-border-radius: 7px; -fx-cursor: hand;"); 
                });

                deleteIcon.setGlyphName("TRASH");
                deleteIcon.setFill(Color.WHITE);
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setStyle("-fx-background-color: #db593c; -fx-background-radius: 7px; -fx-border-color: #db593c; -fx-border-radius: 7px; -fx-cursor: hand;"); 
                deleteButton.setOnAction(event -> {
                    
                    Employee employee = getTableView().getItems().get(getIndex());
                    deleteEmployee(event, employee);
                });

                deleteButton.setOnMouseEntered(event -> {
                    deleteIcon.setFill(Color.rgb(219, 89, 60));
                    deleteButton.setStyle("-fx-background-color: #fff; -fx-background-radius: 7px; -fx-border-color: #db593c; -fx-border-radius: 7px; -fx-cursor: hand;");
                });
                deleteButton.setOnMouseExited(event -> {
                    deleteIcon.setFill(Color.WHITE);
                    deleteButton.setStyle("-fx-background-color: #db593c; -fx-background-radius: 7px; -fx-border-color: #db593c; -fx-border-radius: 7px; -fx-cursor: hand;"); 
                });

                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.getChildren().addAll(editButton, deleteButton);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionsBox);
                }
            }
        });

        loadEmployeeData();
    }

    public void loadDepartmentData(){
        departmentList = new DepartmentDAO().getAllDepartment();
        department_table.setItems(departmentList);
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
            createDepartmentPieChart();
            setNumbers();
            
        });
        employee_btn.setOnMouseClicked(event -> {
            selectButton(employee_btn, employeeIcon, employeeDash_form);
            initEmployeeTable();
        });
        department_btn.setOnMouseClicked(event -> {
            selectButton(department_btn, departmentIcon, departmentDash_form);
            initDepartmentTable();
        });
        report_btn.setOnMouseClicked(event -> {
            selectButton(report_btn, reportIcon, reportDash_form);
        });
    }

    public void defaultHomeDesign(){
            selectButton(dash_btn, dashicon, dashboard_form);
    }
    
    public void deleteEmployee(ActionEvent event, Employee employee){
        try{
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Alert.fxml"));
            Parent root = loader.load();
            AlertController alertController = loader.getController();
            
            alertController.setConfirmName("Delete");
            alertController.setHeader("Delete Employee");
            alertController.setMessage("Are you sure you want to remove " + employee.getFirstName() + " " + employee.getLastName());
            alertController.confirmButtonStyle(219, 89, 60);
            alertController.cancelButtonStyle_delete(" #03305a", "#00d4ff");
            alertController.deleteEmployee(employee);
            alertController.setDashboardController(this);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
            
            stage.setX(650);
            stage.setY(300);
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
 
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    public void editEmployee(ActionEvent event, Employee employee){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editEmp.fxml"));
            Parent root = loader.load();


            editEmployeeController controller = loader.getController();
            controller.editEmployee(employee);
            controller.setDashboardController(this);

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

    public void deleteDepartment(ActionEvent event, Department department){
        try{
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Alert.fxml"));
            Parent root = loader.load();

            AlertController controller = loader.getController();
            controller.setDashboardController(this);
            controller.setHeader("Delete Department");
            controller.setMessage("Do you want to remove" + department.getName());
            controller.cancelButtonStyle_delete("#03305a", "#00d4ff");
            controller.confirmButtonStyle(219, 89, 60);
            controller.setConfirmName("DELETE");
            controller.deleteDepartment(department);
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
    
    public void editDepartment(ActionEvent event, Department department) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addDepartment.fxml"));
            Parent root = loader.load();

            addDepartmentController controller = loader.getController();
            controller.setDashboardController(this);
            controller.setHeader("Edit Department");
            controller.editDepartment(department);
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


    public void addDepartment(ActionEvent event) throws Exception {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addDepartment.fxml"));
        Parent root = loader.load();

        addDepartmentController controller = loader.getController();
        controller.setDashboardController(this);
        controller.setHeader("Add Department");

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
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resource){
        
        alterAddEmployeeIcon(addEmployee_btn, addEmployee_icon);
        alterAddEmployeeIcon(addDepartment_btn, addDepartment_icon);
        navButton();
        defaultHomeDesign();
        user();
        createDepartmentPieChart();
        initEmployeeTable();
        initDepartmentTable();
        setNumbers();
    }
}
