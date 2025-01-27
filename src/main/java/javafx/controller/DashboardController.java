package main.java.javafx.controller;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.javafx.dao.AttendanceDAO;
import main.java.javafx.dao.DepartmentDAO;
import main.java.javafx.dao.EmployeeDAO;
import main.java.javafx.dao.ProjectDAO;
import main.java.javafx.dao.UserDAO;
import main.java.javafx.faceRecog.Recognition;
import main.java.javafx.faceRecog.Training;
import main.java.javafx.model.Attendance;
import main.java.javafx.model.Department;
import main.java.javafx.model.Employee;
import main.java.javafx.model.Project;
import java.io.IOException;
import java.net.URL;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.bytedeco.librealsense2.global.realsense2;

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
    private Button attendance_btn;

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
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

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
    private FontAwesomeIcon addProjectIcon;

    @FXML
    private Label numDepartments;

    @FXML
    private Label numEmployees;

    @FXML
    private VBox departmentContainer;

    @FXML
    private VBox deadlineContainer;

    @FXML
    private GridPane projectGrid;

    @FXML
    private ScrollPane projectScrollPane;

    @FXML
    private Button addProject_btn;

    @FXML
    private AnchorPane calendarAnchorPane;

    @FXML
    private Label monthLabel;

    @FXML
    private Button prevMonthBtn;

    @FXML
    private Button nextMonthBtn;

    @FXML
    private AnchorPane salesChart;

    @FXML
    private Button logoutButton;

    @FXML
    private TitledPane userName;

    @FXML
    private FontAwesomeIcon logoutIcon;

    @FXML
    private FontAwesomeIcon attendanceIcon;

    @FXML
    private FontAwesomeIcon checkAttendance_icon;

    @FXML
    private Button checkAttendance_btn;

    @FXML
    private AnchorPane attendanceDash_form;

    @FXML
    private AnchorPane checkAttendancePane;

    @FXML
    private AnchorPane attendancePane;

    @FXML
    private Pane cameraPane;

    @FXML
    private Label faceDetected;

    @FXML
    private Label idDetected;

    @FXML
    private Label departmentDetected;

    @FXML
    private Label timeDetected;

    @FXML
    private Label onTimeNumber;

    @FXML
    private Label lateNumber;

    @FXML
    private Label absentNumber;

    @FXML
    private Button timeIn_btn;

    @FXML
    private VBox attendanceVbox;
    @FXML
    private VBox attendanceVbox1;

    @FXML
    private ScrollPane attendanceScrollpane;

    @FXML
    private VBox employeeAttendanceVbox;

    @FXML
    private ScrollPane employeeAttendanceSroll;

    private Button activeButton = null;
    private FontAwesomeIcon activeIcon = null;
    private AnchorPane activeForm = null;
    private double x = 0;
    private double y = 0;
    private ObservableList<Employee> employeeList;
    private departmentDetailsController departmentDetailsControllerInstance;
    private YearMonth currentYearMonth = YearMonth.now(); // Store current year and month
    private Map<LocalDate, List<String>> events; // Store event data
    Recognition recog = new Recognition();


    //GLOBAL 
    public void exit(){ //close the program
        System.exit(0);
    }

    public void logout(ActionEvent actionEvent){
        try{ 
            Parent root = FXMLLoader.load(getClass().getResource("/main/resources/fxml/FXML.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
       
            root.setOnMousePressed((MouseEvent event) ->{
                
                x = event.getSceneX();
                y = event.getSceneY();
                
            });
            
            root.setOnMouseDragged((MouseEvent event) ->{
                
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
                
                stage.setOpacity(0.9);
                
            });
            
            root.setOnMouseReleased((MouseEvent event) ->{
                
                stage.setOpacity(1);
                
            });
            
            stage.initStyle(StageStyle.TRANSPARENT);
            logoutButton.getScene().getWindow().hide();
            Image logoImage = new Image("file:/C:/FILES/Code/Java/EmployeeManagementApp/src/main/resources/images/logoIcon.png");
            stage.getIcons().add(logoImage);
            stage.setScene(scene);
            stage.show();
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void user(){ //set the username of in the window pane
        userName.setText(UserDAO.username);
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

        attendance_btn.setOnMouseEntered(event -> {
            if (activeButton != attendance_btn) {
                attendance_btn.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #000; -fx-border-radius: 7px;");
                attendanceIcon.setFill(Color.BLACK);
            }
        });

        attendance_btn.setOnMouseExited(event -> {
            if (activeButton != attendance_btn) {
                attendance_btn.setStyle("-fx-background-color: transparent; -fx-border-radius: 7px; -fx-cursor: hand;");
                attendanceIcon.setFill(Color.WHITE);
            }
        });
    
        // Click events to select a button and update the activeButton variable
        dash_btn.setOnMouseClicked(event -> {
            selectButton(dash_btn, dashicon, dashboard_form);
            setNumbers();
            setupCalendar();
            updateAttendanceLogDash();
            populateDeadlines();
        });
        employee_btn.setOnMouseClicked(event -> {
            selectButton(employee_btn, employeeIcon, employeeDash_form);
            initEmployeeTable();
        });
        department_btn.setOnMouseClicked(event -> {
            selectButton(department_btn, departmentIcon, departmentDash_form);
            createDepartmentBarChart();
            initialize();
        });
        report_btn.setOnMouseClicked(event -> {
            selectButton(report_btn, reportIcon, reportDash_form);
            loadProjects();
        });
        attendance_btn.setOnMouseClicked(event -> {
            selectButton(attendance_btn, attendanceIcon, attendanceDash_form);
            attendancePane.setVisible(true);
            checkAttendancePane.setVisible(false);
            populateAttendance();
            trackEmployeeAttendanceForDay(LocalDate.now());
            updateAttendanceLog();
        });
    }

    public void defaultHomeDesign(){ //selects dashboard as an initial window right after running the program
            selectButton(dash_btn, dashicon, dashboard_form);
    }

    private void alterIcons(Button button, FontAwesomeIcon icon){ //alter the icon in addEmployee button
        button.setOnMouseEntered(event -> {
            icon.setFill(Color.rgb(57, 125, 189));
        });
        button.setOnMouseExited(event -> {
            icon.setFill(Color.rgb(255, 255, 255));
        });
    }

    private void alterUserIcon(Button button, FontAwesomeIcon icon){ //alter the icon in addEmployee button
        button.setOnMouseEntered(event -> {
            icon.setFill(Color.rgb(255, 255, 255));
        });
        button.setOnMouseExited(event -> {
            icon.setFill(Color.rgb(0, 0, 0));
        });
    }
    
    
    //DASHBOARD WINDOW
    public void setNumbers(){ //set the total number of employee and active dept in dashboard
        EmployeeDAO emp = new EmployeeDAO();
        ObservableList<Employee> employees = emp.getAllEmployees();

        int numEmp = employees.size();
        ObservableList<Department> departments = DepartmentDAO.getActiveDepartments();

        int numDep = departments.size();
        
        numEmployees.setText(String.valueOf(numEmp));

        numDepartments.setText(String.valueOf(numDep));

    }
    
    private void populateDeadlines() { //call createDeadlineBox for each projects
        deadlineContainer.getChildren().clear();
        ObservableList<Project> projects = ProjectDAO.getAllProjects();
        LocalDate today = LocalDate.now();
        for(Project project: projects){
            if(project.getEndDate().isAfter(today)){
                HBox deadlineBox = createDeadlineBox(project);
                deadlineContainer.getChildren().add(deadlineBox);
            }
        }
    }

    private HBox createDeadlineBox(Project project) {
        // Create an HBox representing the project
        HBox deadlineBox = new HBox();
        deadlineBox.setSpacing(10);
        deadlineBox.setPrefHeight(40);
        deadlineBox.setMaxHeight(40);
        deadlineBox.setMinHeight(40);
        deadlineBox.setStyle("-fx-border-color: lightgray; -fx-padding: 10; -fx-background-color: #f9f9f9;");
        deadlineBox.setAlignment(Pos.CENTER_LEFT);
    
        // Label for project name
        Label projectNameLabel = new Label(project.getProjectName());
        projectNameLabel.setStyle("-fx-font-weight: bold;"); // Optional styling for label

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
    
        // Add project name, deadline label, and spacer to the HBox
        deadlineBox.getChildren().addAll(projectNameLabel, spacer, deadlineLabel);
    
    
        // Change background color on hover
        deadlineBox.setOnMouseEntered(event -> deadlineBox.setStyle("-fx-border-color: lightgray; -fx-padding: 10; -fx-background-color: #e0e0e0;"));
        deadlineBox.setOnMouseExited(event -> deadlineBox.setStyle("-fx-border-color: lightgray; -fx-padding: 10; -fx-background-color: #f9f9f9;"));
        deadlineBox.setOnMouseClicked(event -> {
            viewProjectDetails(event, project);
        });
        return deadlineBox;
    }

    private void setupCalendar() {
        currentYearMonth = YearMonth.now(); // Set the current month
        events = ProjectDAO.getAllDeadlines(); // Load event data
        updateCalendar(); // Populate the calendar
        // updateMonthLabel();
    }

    public void changeMonth(ActionEvent event) {  // Update the calendar display after changing the month
        if (event.getSource() == prevMonthBtn) {
            currentYearMonth = currentYearMonth.minusMonths(1);  // Move to the previous month
        } else if (event.getSource() == nextMonthBtn) {
            currentYearMonth = currentYearMonth.plusMonths(1);  // Move to the next month
        }
        updateCalendar(); 
    }
    
    private void updateCalendar() {  // Call method to update the calendar display and month
       
        populateCalendar(calendarAnchorPane, currentYearMonth, events);

        String monthText = currentYearMonth.getMonth().toString() + " " + currentYearMonth.getYear();
        monthLabel.setText(monthText); 
    }
    
    private void populateCalendar(AnchorPane anchorPane, YearMonth yearMonth, Map<LocalDate, List<String>> events) { //create a calendar
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate currentDate = LocalDate.now();

        // Create a new GridPane for the calendar
        GridPane calendarGrid = new GridPane();
        calendarGrid.setHgap(5);
        calendarGrid.setVgap(5);
        calendarGrid.setStyle("-fx-padding: 10;");
    
        // Add column constraints for equal spacing
        for (int i = 0; i < 7; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100.0 / 7);
            calendarGrid.getColumnConstraints().add(columnConstraints);
        }
    
        // Add row constraints for equal spacing
        for (int i = 0; i <= 6; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / 7);
            calendarGrid.getRowConstraints().add(rowConstraints);
        }
    
        // Add day labels (Sunday to Saturday)
        String[] dayLabels = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < 7; i++) {
            Label dayLabel = new Label(dayLabels[i]);
            dayLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center;");
            dayLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            calendarGrid.add(dayLabel, i, 0);
        }
    
        // Start populating the calendar dates
        int column = firstDay.getDayOfWeek().getValue() % 7;
        int row = 1;
    
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = yearMonth.atDay(day);
            Label dateLabel = new Label(String.valueOf(day));
            dateLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            dateLabel.setStyle("-fx-border-color: lightgray; -fx-padding: 5; -fx-alignment: center;");

            if (date.equals(currentDate)) {
                dateLabel.setStyle("-fx-background-color: darkgrey; -fx-border-color: darkgrey; -fx-alignment: center; -fx-text-fill: white;");
            }

            // Add click event to display event details
            dateLabel.setOnMouseClicked(event -> {
                // Retrieve the list of event details for the given date
                List<String> eventDetails = events.getOrDefault(date, Collections.singletonList("No events on this day."));
                
                // Convert the list of event details to a string with each event on a new line
                String eventText = String.join("\n", eventDetails);
                
                // Display the alert with the event details
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Event Details");
                alert.setHeaderText(null);
                alert.setContentText(eventText);  // Set the content text to the concatenated string
                alert.showAndWait();
            });


            if (events.containsKey(date)) {
                Circle eventMarker = new Circle(5, Color.RED); 
                eventMarker.setLayoutX(dateLabel.getWidth() - 10); 
                eventMarker.setLayoutY(10); 

                // Add the marker to the label (or another container like a StackPane)
                StackPane dateStack = new StackPane(dateLabel, eventMarker);
                dateStack.setAlignment(Pos.CENTER);
                StackPane.setAlignment(eventMarker, Pos.TOP_RIGHT); 
                calendarGrid.add(dateStack, column, row);
            } else {
                
                calendarGrid.add(dateLabel, column, row);
            }
            column++;
            if (column == 7) {
                column = 0;
                row++;
            }
        }
    
        // Clear existing children from the AnchorPane
        anchorPane.getChildren().clear();
    
        // Add the GridPane to the AnchorPane
        anchorPane.getChildren().add(calendarGrid);
    
        // Bind the GridPane size to the AnchorPane size
        calendarGrid.prefWidthProperty().bind(anchorPane.widthProperty());
        calendarGrid.prefHeightProperty().bind(anchorPane.heightProperty());
    }

    public void updateAttendanceLogDash() {
        // Get today's date formatted as 'yyyy-MM-dd' for fetching today's attendance
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        
        // Query the database for attendance logs for today
        AttendanceDAO dao = new AttendanceDAO();
        List<Attendance> attendanceList = dao.getAttendanceForDate(today);
    
        // Clear previous entries in the ScrollPane
        attendanceVbox1.getChildren().clear();
        
        // Iterate through the attendance list and add them to the ScrollPane
        for (Attendance attendance : attendanceList) {
            // Fetch employee information
            EmployeeDAO empDao = new EmployeeDAO();
            Employee employee = empDao.getEmployeebyId(attendance.getEmployeeID());
            
            if (employee != null) {
                // Format the attendance log entry (Employee name and check-in time)
                String logText = employee.getFirstName() + " " + employee.getLastName() + " - " + 
                                 attendance.getTimestamp().format(formatter);
                Label logLabel = new Label(logText);
                
                // Optionally, style the label
                logLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
                
                // Add the log entry to the VBox
                attendanceVbox1.getChildren().add(logLabel);
            }
        }
    
        // Scroll to the bottom to show the latest attendance
        attendanceVbox.requestFocus();
    } 
  
    //EMPLOYEE WINDOW
    public void loadEmployeeData(){ //called to refresh the employee table
        
        employeeList = new EmployeeDAO().getAllEmployees();
        employeeTable.setItems(employeeList);
    }
    
    public void deleteEmployee(ActionEvent event, Employee employee){ //calls alert.FXML and modify it to delete employee(insde the table view)
        try{
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/Alert.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/editEmp.fxml"));
            Parent root = loader.load();
            
            
            editEmployeeController controller = loader.getController();
            controller.editEmployee(employee);
            controller.setDashboardController(this);
            

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

    public void addEmployee(ActionEvent actionEvent) throws Exception { //calls addEmployee.fxml
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/addEmp.fxml"));
        Parent root = loader.load();
        
        addEmployeeController controller = loader.getController();
        controller.setDashboardController(this);
        
        // Apply the CSS to the root scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        // Get the current window (parent window)
        Stage parentStage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
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
    
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parentStage);
    
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


    //DEPARTMENT WINDOW
    public void addDepartment(ActionEvent event) throws Exception { // Calls addDepartment.FXML to add new department
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/addDepartment.fxml"));
        Parent root = loader.load();

        addDepartmentController controller = loader.getController();
        controller.setDashboardController(this);

        // Apply the CSS to the root scene
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());

        // Apply blur effect and grey background to the parent stage
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
    }

    public void initialize() { //refresh the departmentContainer
        departmentContainer.getChildren().clear();
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
        departmentBox.setPrefHeight(80);
        departmentBox.setMaxHeight(80);
        departmentBox.setMinHeight(80);
        departmentBox.setStyle("-fx-border-color: lightgray; -fx-padding: 10; -fx-background-color: #f9f9f9;");
        departmentBox.setAlignment(Pos.CENTER_LEFT);
        // Label for department name
        Label departmentLabel = new Label(department.getName());
        departmentLabel.setStyle("-fx-font-weight: bold;"); // Optional styling for label

        // Align the label to the left-center
        HBox.setHgrow(departmentLabel, Priority.NEVER);  // Ensure the label doesn't grow horizontally
        departmentLabel.setAlignment(Pos.CENTER_LEFT);

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

    private void openDepartmentDetails(MouseEvent event, Department department) {
        try {
            // Load Department Details FXML
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/DepartmentDetails.fxml"));
            Parent root = loader.load();

            // Pass the department to the controller
            departmentDetailsController controller = loader.getController();
            controller.setDashboardController(this);
            controller.setDepartment(department);

            departmentDetailsControllerInstance = controller;

            // Apply the scene to the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Get the current window (parent window)
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

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);

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

    public void createDepartmentBarChart() {
        // Clear existing data
        EmployeeDAO employeeDAO = new EmployeeDAO();
        List<Employee> employees = employeeDAO.getAllEmployees();
        barChart.getData().clear();
    
        // Prepare a map for department counts
        Map<String, Integer> departmentCount = new HashMap<>();
        for (Employee employee : employees) {
            String department = employee.getDepartmentName();
            departmentCount.put(department, departmentCount.getOrDefault(department, 0) + 1);
        }
    
        // Create a new series for employees
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Employees");
    
        // Add data to the series
        for (Map.Entry<String, Integer> entry : departmentCount.entrySet()) {
            String department = entry.getKey();
            int count = entry.getValue();
            series.getData().add(new XYChart.Data<>(department, count));
        }
    
        // Add the series to the BarChart
        barChart.getData().add(series);
    
        // Set the X-axis and Y-axis labels dynamically (optional)
        xAxis.setLabel("Departments");
        yAxis.setLabel("Employee Count");
    
        // Apply rotation to the X-axis labels (optional)
        xAxis.setTickLabelRotation(45);
    }
    
    public void loadDepartmentData(){ //called to refresh the department table(to be deleted)

        department_table.setItems(DepartmentDAO.getAllDepartment());
    }


    //PROJECT WINDOW
    public void addProject(ActionEvent event) { // Calls addProject.fxml to add a new project
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/addProject.fxml"));
            Parent root = loader.load();

            addProjectController controller = loader.getController();
            controller.setDashboardController(this);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());

            // Apply blur effect and grey background to the parent stage
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

    public void loadProjects() { //loads all existing projects into a gridpane
        projectGrid.getChildren().clear(); // Clear any existing cards
        ObservableList<Project> projects = ProjectDAO.getAllProjects();
    
        int column = 0;
        int row = 0;
    
        // Ensure that the GridPane resizes dynamically
        for (Project project : projects) {
            VBox card = createProjectCard(project);
    
            // Add the card to the GridPane
            projectGrid.add(card, column, row);
            
            // Make the card grow to fill available space in its cell
            GridPane.setVgrow(card, Priority.ALWAYS);
            GridPane.setHgrow(card, Priority.NEVER);
            column++;
    
            // Move to the next row after 3 cards
            if (column == 2) {
                column = 0;
                row++;
            }
        }
    }

    private VBox createProjectCard(Project project) { //create a project card
        // Create a VBox for the card
        VBox card = new VBox();
        card.setSpacing(10);
        card.setStyle("-fx-padding: 15; -fx-background-color: #f9f9f9; -fx-border-color: lightgray; -fx-border-radius: 10; -fx-background-radius: 10;");
        
        // Create a StackPane for the header to hold project name and edit button
        StackPane header = new StackPane();
        header.setMinHeight(Region.USE_PREF_SIZE);  // Ensure header does not occupy extra space


        // Project name inside the header
        Label projectNameLabel = new Label(project.getProjectName());
        projectNameLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        StackPane.setAlignment(projectNameLabel, Pos.CENTER_LEFT); // Align project name to the left
        
        Label departmentProject = new Label();
        Department department = DepartmentDAO.getDepartmentById(project.getDepartmentId());
        departmentProject.setText(department.getName());
        departmentProject.setStyle("-fx-font-size: 10;");

        //Status Label
        Label statusLabel = new Label();
        if(project.isFinished()){
            statusLabel.setText("Finished");
            statusLabel.setStyle("-fx-text-fill: rgb(0,200,0); -fx-font-style: italic;");
        } else {
            statusLabel.setText("Not Finished");
            statusLabel.setStyle("-fx-text-fill: rgb(255,0,0); -fx-font-style: italic;");
        }
        StackPane.setAlignment(statusLabel, Pos.CENTER_RIGHT);

        HBox labels = new HBox(6);
        labels.setAlignment(Pos.CENTER_LEFT);
        labels.getChildren().addAll(projectNameLabel, departmentProject);

        HBox actionsBox = new HBox(5);
        FontAwesomeIcon editIcon = new FontAwesomeIcon();
        FontAwesomeIcon deleteIcon = new FontAwesomeIcon();
        Button deleteButton = new Button();
        Button editButton = new Button();
        editIcon.setGlyphName("EDIT");
        editIcon.setFill(Color.WHITE);
        editButton.setStyle("-fx-background-color: #397dbd; -fx-background-radius: 7px; -fx-border-color: #397dbd; -fx-border-radius: 7px; -fx-cursor: hand;");          
        editButton.setGraphic(editIcon);
        editButton.setOnMouseClicked(event -> editProject(event, project));

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
        deleteButton.setOnMouseClicked(event -> deleteProject(event, project));

        deleteButton.setOnMouseEntered(event -> {
            deleteIcon.setFill(Color.rgb(219, 89, 60));
            deleteButton.setStyle("-fx-background-color: #fff; -fx-background-radius: 7px; -fx-border-color: #db593c; -fx-border-radius: 7px; -fx-cursor: hand;");
        });
        deleteButton.setOnMouseExited(event -> {
            deleteIcon.setFill(Color.WHITE);
            deleteButton.setStyle("-fx-background-color: #db593c; -fx-background-radius: 7px; -fx-border-color: #db593c; -fx-border-radius: 7px; -fx-cursor: hand;"); 
        });

        actionsBox.setAlignment(Pos.CENTER_RIGHT);
        actionsBox.getChildren().addAll(editButton, deleteButton);


        // Add both project name and edit button to the header
        header.getChildren().addAll(labels, actionsBox);
        header.autosize();
    
        // Add project description
        Label projectDescriptionLabel = new Label(project.getDescription());
        projectDescriptionLabel.setWrapText(true);

        // Add start and end dates
        Label startDateLabel = new Label("Start Date: " + project.getStartDate());
        Label endDateLabel = new Label("End Date: " + project.getEndDate());
    
        // Arrange components in the card
        card.getChildren().addAll(header,statusLabel, projectDescriptionLabel, startDateLabel, endDateLabel);
        card.setStyle("-fx-padding: 15; -fx-background-color: #f9f9f9; -fx-border-color: lightgray; -fx-border-radius: 10; -fx-background-radius: 10;");
        card.setOnMouseEntered(event -> card.setStyle("-fx-padding: 15; -fx-background-color: lightgray; -fx-border-color: lightgray; -fx-border-radius: 10; -fx-background-radius: 10;"));
        card.setOnMouseExited(event -> card.setStyle("-fx-padding: 15; -fx-background-color: #f9f9f9; -fx-border-color: lightgray; -fx-border-radius: 10; -fx-background-radius: 10;"));
        card.setOnMouseClicked(event -> {viewProjectDetails(event, project);});
        return card;
    }

    private void editProject(MouseEvent event, Project project){

        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/editProject.fxml"));
            Parent root = loader.load();
            
            
            editProjectController controller = loader.getController();
            controller.setDashboardController(this);
            controller.setProject(project);

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

    private void deleteProject(MouseEvent event, Project project){
        try{
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/Alert.fxml"));
            Parent root = loader.load();
            AlertController alertController = loader.getController();
            
            alertController.setConfirmName("Delete");
            alertController.setHeader("Delete Project");
            alertController.setMessage("Are you sure you want to delete " + project.getProjectName());
            alertController.confirmButtonStyle(219, 89, 60);
            alertController.cancelButtonStyle_delete(" #03305a", "#00d4ff");
            alertController.deleteProject(project);
            alertController.setDashboardController(this);
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
    
    public void viewProjectDetails(MouseEvent event, Project project) {
        try {
            // Load Department Details FXML
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/ProjectDetails.fxml"));
            Parent root = loader.load();

            // Pass the department to the controller
            projectDetailsController controller = loader.getController();
            controller.setDashboardController(this);
            controller.setProject(project);

            // departmentDetailsControllerInstance = controller;

            // Apply the scene to the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Get the current window (parent window)
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

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);

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


    //ATTENDANCE WINDOW
    public void checkAttendance() {
        attendancePane.setVisible(false);
        checkAttendancePane.setVisible(true);
        
        Training train = new Training();
        train.training();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        
        try {
            recog.recog(cameraPane, detectedName -> {
                if (detectedName != null && !detectedName.isEmpty()) {
                    
                    // Get the ID dynamically whenever a face is detected
                    int idNumber = recog.getIdNumber();
        
                    if (idNumber != 0) {
                        EmployeeDAO emp = new EmployeeDAO();
                        Employee employee = emp.getEmployeebyId(idNumber);
        
                        if (employee != null) {
                            Platform.runLater(() -> {
                                faceDetected.setText(employee.getFirstName() + " " + employee.getLastName());
                                idDetected.setText(Integer.toString(employee.getEmployeeID()));
                                departmentDetected.setText(employee.getDepartmentName());
                                timeDetected.setText(LocalTime.now().format(formatter).toString());
                                
                                // Save attendance when the button is clicked
                                timeIn_btn.setOnAction(event -> {
                                    int employeeID = Integer.parseInt(idDetected.getText());
                                    LocalDateTime currentTime = LocalDateTime.now();
                                    String status;
                                    if(currentTime.toLocalTime().isBefore(LocalTime.of(9, 0))){
                                        status = "Ontime";
                                    } else {
                                        status = "Late";
                                    }
                                    Attendance attendance = new Attendance(employeeID, currentTime, status);
                                    AttendanceDAO dao = new AttendanceDAO();

                                    if (dao.insertAttendance(attendance)) {
                                        System.out.println("Attendance saved successfully.");
                                        updateAttendanceLog();
                                        populateAttendance();
                                        trackEmployeeAttendanceForDay(LocalDate.now());
                                        checkAttendancePane.setVisible(false);
                                        attendancePane.setVisible(true);
                                        releaseCamera();
                                    } else {
                                        System.out.println("Failed to save attendance.");
                                    }
                                });
                            });
                        } else {
                            System.out.println("No employee found for ID: " + idNumber);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void releaseCamera() {
        attendancePane.setVisible(true);
        checkAttendancePane.setVisible(false);
        if (recog != null) {
            recog.releaseCamera();
        }
    }   

    public void updateAttendanceLog() {
        // Get today's date formatted as 'yyyy-MM-dd' for fetching today's attendance
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        
        // Query the database for attendance logs for today
        AttendanceDAO dao = new AttendanceDAO();
        List<Attendance> attendanceList = dao.getAttendanceForDate(today);
    
        // Clear previous entries in the ScrollPane
        attendanceVbox.getChildren().clear();
        
        // Iterate through the attendance list and add them to the ScrollPane
        for (Attendance attendance : attendanceList) {
            // Fetch employee information
            EmployeeDAO empDao = new EmployeeDAO();
            Employee employee = empDao.getEmployeebyId(attendance.getEmployeeID());
            
            if (employee != null) {
                // Format the attendance log entry (Employee name and check-in time)
                String logText = employee.getFirstName() + " " + employee.getLastName() + " - " + 
                                 attendance.getTimestamp().format(formatter);
                Label logLabel = new Label(logText);
                
                // Optionally, style the label
                logLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
                
                // Add the log entry to the VBox
                attendanceVbox.getChildren().add(logLabel);
            }
        }
    
        // Scroll to the bottom to show the latest attendance
        attendanceVbox.requestFocus();
    }  

    private void populateAttendance() { // Fetch all employees and display them with attendance status

        employeeAttendanceVbox.getChildren().clear();

        EmployeeDAO emp = new EmployeeDAO();
        ObservableList<Employee> employees = emp.getAllEmployees(); // Fetch all employees from the database
    
        for (Employee employee : employees) {
            HBox employeeBox = createEmployeeBox(employee); // Create an HBox for each employee
            employeeAttendanceVbox.getChildren().add(employeeBox); // Add each employee box to the container (VBox or ScrollPane)
        }
    }
    
    private HBox createEmployeeBox(Employee employee) { // Create HBox for each employee
        // Create an HBox representing an employee
        HBox employeeBox = new HBox();
        employeeBox.setSpacing(10);
        employeeBox.setPrefHeight(80);
        employeeBox.setMaxHeight(80);
        employeeBox.setMinHeight(80);
        employeeBox.setStyle("-fx-border-color: lightgray; -fx-padding: 10; -fx-background-color: #f9f9f9;");
        employeeBox.setAlignment(Pos.CENTER_LEFT);
    
        // Label for employee name
        Label employeeLabel = new Label(employee.getFirstName() + " " + employee.getLastName());
        employeeLabel.setStyle("-fx-font-weight: bold;");
    
        // Align the label to the left-center
        HBox.setHgrow(employeeLabel, Priority.NEVER);  // Ensure the label doesn't grow horizontally
        employeeLabel.setAlignment(Pos.CENTER_LEFT);
    
        // FontAwesomeIcon for on-time/late status
        FontAwesomeIcon statusIcon = new FontAwesomeIcon();
        
        // Query the attendance status for the employee to determine if they are on time or late
        AttendanceDAO attendanceDAO = new AttendanceDAO();
        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceDAO.getEmployeeAttendanceForDate(employee.getEmployeeID(), today);
        
        if (attendance != null) {
            // Check if the employee is on time or late
            String status = attendance.getStatus(); // Assuming 'ontime' or 'late' is returned from the attendance log
            if (status.equals("ontime")) {
                statusIcon.setGlyphName("CHECK_CIRCLE");  // Green check for on-time
                statusIcon.setFill(Color.GREEN);
            } else {
                statusIcon.setGlyphName("EXCLAMATION_CIRCLE");  // Exclamation for late
                statusIcon.setFill(Color.RED);
            }
        } else {
            // If no attendance record for today, consider the employee as "Not Present"
            statusIcon.setGlyphName("TIMES_CIRCLE");  // Red cross for not present
            statusIcon.setFill(Color.GRAY);
        }
        
        statusIcon.setSize("24");
    
        // Create a Region to push the status icon to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);  // Make the spacer grow to take up available space
    
        // Add employee label, spacer, and status icon to the HBox
        employeeBox.getChildren().addAll(employeeLabel, spacer, statusIcon);
    
        // Add a click event to open employee details or any other functionality
        // employeeBox.setOnMouseClicked(event -> openEmployeeDetails(event, employee));
    
        // Change background color on hover
        employeeBox.setOnMouseEntered(event -> employeeBox.setStyle("-fx-border-color: lightgray; -fx-padding: 10; -fx-background-color: #e0e0e0;"));
        employeeBox.setOnMouseExited(event -> employeeBox.setStyle("-fx-border-color: lightgray; -fx-padding: 10; -fx-background-color: #f9f9f9;"));
    
        return employeeBox;
    }
    
    public void trackEmployeeAttendanceForDay(LocalDate date) {
        // Initialize counts
        int onTimeCount = 0;
        int lateCount = 0;
        int absentCount = 0;
    
        // Get all employees
        EmployeeDAO empDao = new EmployeeDAO();
        List<Employee> allEmployees = empDao.getAllEmployees();  // Assuming getAllEmployees() returns all employees
    
        // Loop through each employee
        for (Employee employee : allEmployees) {
            // Fetch the attendance for the employee on the given date
            AttendanceDAO attendanceDAO = new AttendanceDAO();
            Attendance attendance = attendanceDAO.getEmployeeAttendanceForDate(employee.getEmployeeID(), date);
    
            if (attendance != null) {
                // If the employee has an attendance record, check the status
                String status = attendance.getStatus();
    
                if ("ontime".equals(status)) {
                    onTimeCount++;
                } else if ("late".equals(status)) {
                    lateCount++;
                }
            } else {
                // If no attendance record, mark as absent
                absentCount++;
            }
        }
    
        // Update labels (assuming you have Label objects for ontime, late, and absent counts)
        onTimeNumber.setText(String.valueOf(onTimeCount));
        lateNumber.setText(String.valueOf(lateCount));
        absentNumber.setText(String.valueOf(absentCount));
    }
    

    @Override
    public void initialize(URL url, ResourceBundle resource){
        
        alterIcons(addEmployee_btn, addEmployee_icon);
        alterIcons(addDepartment_btn, addDepartment_icon);
        alterIcons(addProject_btn, addProjectIcon);
        alterUserIcon(logoutButton, logoutIcon);
        alterIcons(checkAttendance_btn, checkAttendance_icon);
        navButton();
        defaultHomeDesign();
        user();
        setupCalendar();
        initEmployeeTable();
        setNumbers();
        initialize();
        loadProjects();
        populateDeadlines();
        updateAttendanceLog();
        updateAttendanceLogDash();
        
    }
}