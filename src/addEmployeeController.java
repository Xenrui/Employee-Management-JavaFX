import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


import java.sql.Statement;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String query = "SELECT department_id, department_name FROM departments"; // Adjust query as needed

        try {
            // Establish the connection
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            
            // Execute the SQL query
            ResultSet resultSet = statement.executeQuery(query);
            
        // Process the ResultSet
            while (resultSet.next()) {
                int id = resultSet.getInt("department_id");
                String departmentName = resultSet.getString("department_name");
                departments.add(new Department(id, departmentName));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        department.setItems(departments);
    }


    public void addEmployee(){

        Employee employee = new Employee();
        employee.setFirstName(firstName.getText());
        employee.setLastName(lastName.getText());
        employee.setEmail(email.getText());
        employee.setPhoneNum(phone.getText());
        employee.setJobTitle(jobTitle.getText());
        Department selectedDepartment = department.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            employee.setDepartmentID(selectedDepartment.getId()); 
        } else {
            System.out.println("Please select a department.");
            return; 
        }
        employee.setHireDate(LocalDate.now());

        EmployeeDAO emp = new EmployeeDAO();
        emp.addEmployee(employee);
        System.out.println("Employee added");

    }

    @Override
    public void initialize(URL url, ResourceBundle resource){
        loadDepartments();
        
    }
}
