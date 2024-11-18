
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
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

    @FXML
    private VBox departmentContainer;

    @FXML
    private GridPane projectGrid;

    @FXML
    private ScrollPane projectScrollPane;

    @FXML
    private Button addProject_btn;

    private Button activeButton = null;
    private FontAwesomeIcon activeIcon = null;
    private AnchorPane activeForm = null;
    private double x = 0;
    private double y = 0;
    private ObservableList<Employee> employeeList;
    private ObservableList<Department> departmentList;
    private departmentDetailsController departmentDetailsControllerInstance;

    public void exit(){ //close the program
        System.exit(0);
    }

    private void user(){ //set the username of in the window pane
        user.setText(UserDAO.username);
    }
    
    public void setNumbers(){ //set the total number of employee and active dept in dashboard
        EmployeeDAO emp = new EmployeeDAO();
        ObservableList<Employee> employees = emp.getAllEmployees();

        int numEmp = employees.size();
        System.out.println(numEmp);
        ObservableList<Department> departments = DepartmentDAO.getActiveDepartments();

        int numDep = departments.size();
        System.out.println(numDep);
        
        numEmployees.setText(String.valueOf(numEmp));

        numDepartments.setText(String.valueOf(numDep));

    }

    public void initialize() { //refresh the departmentContainer
        departmentContainer.getChildren().clear();
        // Other initialization logic for the dashboard...
        populateDepartments();
    }
    
    private void populateDepartments() { //call createDepartment for each departments
        ObservableList<Department> departments = DepartmentDAO.getAllDepartment();

        for (Department department: departments) {
            HBox departmentBox = createDepartmentBox(department);
            departmentContainer.getChildren().add(departmentBox); // Ensure departmentContainer is properly linked to your FXML
        }
    }

    private HBox createDepartmentBox(Department department) { //creates Department Hbox
        // Create an HBox representing a department
        HBox departmentBox = new HBox();
        departmentBox.setSpacing(10);
        departmentBox.setPrefHeight(200);
        departmentBox.setStyle("-fx-border-color: lightgray; -fx-padding: 10; -fx-background-color: #f9f9f9;");
    
        // Label for department name
        Label departmentLabel = new Label(department.getName());
        
        // FontAwesomeIcon for active/inactive status
        FontAwesomeIcon statusIcon = new FontAwesomeIcon();
        if (department.getActive()) {
            statusIcon.setGlyphName("CHECK_CIRCLE");  // Set the active icon
            statusIcon.setFill(Color.GREEN); // White icon for active
        } else {
            statusIcon.setGlyphName("TIMES_CIRCLE");  // Set the inactive icon
            statusIcon.setFill(Color.RED);; // White icon for inactive
        }
        
        statusIcon.setSize("24"); 

        // Create a Region to push the status icon to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);  // Make the spacer grow to take up available space
    
        // Add department label, spacer, and status icon to the HBox
        departmentBox.getChildren().addAll(departmentLabel,spacer, statusIcon);
    
        // Add a click event to open the department details pop-up
        departmentBox.setOnMouseClicked(event -> openDepartmentDetails(event, department));
    
        // Change background color on hover
        departmentBox.setOnMouseEntered(event -> departmentBox.setStyle("-fx-border-color: lightgray; -fx-padding: 10; -fx-background-color: #e0e0e0;"));
        departmentBox.setOnMouseExited(event -> departmentBox.setStyle("-fx-border-color: lightgray; -fx-padding: 10; -fx-background-color: #f9f9f9;"));
    
        return departmentBox;
    }

    private void openDepartmentDetails(MouseEvent event, Department department) { //calls the DepartmentDetails.FXML 

        try {
            // Load Department Details FXML
            Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("DepartmentDetails.fxml"));
                Parent root = loader.load();


                // Pass the department to the controller
                departmentDetailsController controller = loader.getController();
                controller.setDashboardController(this);
                System.out.println(department.getActive());
                controller.setDepartment(department);

                departmentDetailsControllerInstance = controller;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void refreshDepartmentDetailsInDetailsController() { //refresh the DepartmentDetails.FXML after the edit in editDepartment.FXML
        if (departmentDetailsControllerInstance != null) {
            departmentDetailsControllerInstance.refreshDepartmentDetails(); // Call the refresh method in DepartmentDetailsController
        }
    }

    public void createDepartmentPieChart(){ //creates the PieChart in the Dashboard window
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

    private void initEmployeeTable(){ //initializes the employee table(calls the editEmp and deleteEmployee)
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

            private HBox actionsBox = new HBox(8);

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

    public void loadDepartmentData(){ //called to refresh the department table(to be deleted)
        department_table.setItems(DepartmentDAO.getAllDepartment());
    }

    public void loadEmployeeData(){ //called to refresh the employee table
        
        employeeList = new EmployeeDAO().getAllEmployees();
        employeeTable.setItems(employeeList);
    }
    
    private void alterAddEmployeeIcon(Button button, FontAwesomeIcon icon){ //alter the icon in addEmployee button
        button.setOnMouseEntered(event -> {
            icon.setFill(Color.rgb(57, 125, 189));
        });

        button.setOnMouseExited(event -> {
            icon.setFill(Color.rgb(255, 255, 255));
        });
    }

    private void selectButton(Button button, FontAwesomeIcon icon, AnchorPane form) { //switches tha form when a new button is clicked from the navbutton()

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
    
    public void navButton() { //adjust hover and click design of the navigation button  &&  initiates method when a button is clicked

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
            // initDepartmentTable();
            initialize();
        });
        report_btn.setOnMouseClicked(event -> {
            selectButton(report_btn, reportIcon, reportDash_form);
        });
    }

    public void defaultHomeDesign(){ //selects dashboard as an initial window right after running the program
            selectButton(dash_btn, dashicon, dashboard_form);
    }
    
    public void deleteEmployee(ActionEvent event, Employee employee){ //calls alert.FXML and modify it to delete employee(insde the table view)
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

    public void editEmployee(ActionEvent event, Employee employee){ //calls editEmp.FXML to edit employee (inside the table view)
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

    public void addEmployee(ActionEvent actionEvent) throws Exception { //calls addEmp.FXML to add new employee
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

    public void addDepartment(ActionEvent event) throws Exception { //calls addDepartment.FXML to add new department
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addDepartment.fxml"));
        Parent root = loader.load();

        addDepartmentController controller = loader.getController();
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
    }
    
    public void loadProjects(GridPane projectGrid, List<Project> projects) {
        projectGrid.getChildren().clear(); // Clear any existing cards
        
        int column = 0;
        int row = 0;

        for (Project project : projects) {
            VBox card = createProjectCard(project);

            projectGrid.add(card, column, row);
            column++;

            // Move to next row after 3 cards
            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }

    private VBox createProjectCard(Project project) {
        // Create a VBox for the card
        VBox card = new VBox();
        card.setSpacing(10);
        card.setStyle("-fx-padding: 15; -fx-background-color: #f9f9f9; -fx-border-color: lightgray; -fx-border-radius: 10; -fx-background-radius: 10;");
        
        // Add project name
        Label projectNameLabel = new Label(project.getProjectName());
        projectNameLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Add project description
        Label projectDescriptionLabel = new Label(project.getDescription());
        projectDescriptionLabel.setWrapText(true);

        // Add start and end dates
        Label startDateLabel = new Label("Start Date: " + project.getStartDate());
        Label endDateLabel = new Label("End Date: " + project.getEndDate());

        // Add a button for actions
        Button viewDetailsButton = new Button("View Details");
        viewDetailsButton.setStyle("-fx-background-color: #0078d7; -fx-text-fill: white;");
        viewDetailsButton.setOnAction(event -> viewProjectDetails(project));

        // Arrange components in the card
        card.getChildren().addAll(projectNameLabel, projectDescriptionLabel, startDateLabel, endDateLabel, viewDetailsButton);

        return card;
    }

    // Placeholder for viewProjectDetails method
    private void viewProjectDetails(Project project) {
        System.out.println("Viewing details for project: " + project.getProjectName());
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
        // initDepartmentTable();
        setNumbers();
        initialize();
    }
}
