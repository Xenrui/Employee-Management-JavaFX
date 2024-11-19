import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class editEmployeeController implements Initializable {

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

    private int employeeID;
    private DashboardController dashboardController;

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

    public void editEmployee(Employee employee){
        employeeID = employee.getEmployeeID();
        firstName.setText(employee.getFirstName());
        lastName.setText(employee.getLastName());
        email.setText(employee.getEmail());
        phone.setText(employee.getPhoneNum());
        jobTitle.setText(employee.getJobTitle());

        
        Department selectedDepartment = new Department();
        selectedDepartment.setDepartmentID(employee.getDepartmentID());
        selectedDepartment.setDepartmentName(employee.getDepartmentName()); 

        department.setValue(selectedDepartment);
    }
    
    public void updateEmployee(ActionEvent event){
        
        Employee employee = new Employee();
        employee.setFirstName(firstName.getText());
        employee.setLastName(lastName.getText());
        employee.setEmail(email.getText());
        employee.setPhoneNum(phone.getText());
        employee.setJobTitle(jobTitle.getText());
        employee.setEmployeeID(employeeID);
        Department selectedDepartment = department.getSelectionModel().getSelectedItem();

        if (selectedDepartment != null) {
            employee.setDepartmentID(selectedDepartment.getId());
            employee.setDepartmentName(selectedDepartment.getName()); 
        } else {
            System.out.println("Please select a department.");
            return;
        }

        EmployeeDAO emp = new EmployeeDAO();
        if(emp.isUpdateEmployeeSuccessful(employee)){
            System.out.println("Employee added");

            if (dashboardController != null) {
                dashboardController.loadEmployeeData(); // Refresh the TableView
            }    

            firstName.setText("");
            lastName.setText("");
            jobTitle.setText("");
            phone.setText("");
            email.setText("");

            Stage stage = (Stage) confirm_btn.getScene().getWindow();
            stage.close();
        }
        else{

            Alert alert = new Alert(AlertType.ERROR);
            
            alert.setTitle("Message!");
            alert.setHeaderText(null);
            alert.setContentText("Edit Employee Failed");
            alert.showAndWait();
        } 
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       loadDepartments();
    }
}
