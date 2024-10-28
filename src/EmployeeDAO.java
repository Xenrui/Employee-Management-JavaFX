import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    
    public boolean isAddEmployeeSuccessful(Employee employee){
        String sql = "INSERT INTO employees (first_name, last_name, email, phone_number, hire_date, job_title, department_id) VALUES (?,?,?,?,?,?,?)";
        
        try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)){
                
                stmt.setString(1, employee.getFirstName());
                stmt.setString(2, employee.getLastName());
                stmt.setString(3, employee.getEmail()); 
                stmt.setString(4, employee.getPhoneNum());
                stmt.setDate(5, Date.valueOf(employee.getHireDate()));
                stmt.setString(6, employee.getJobTitle());
                stmt.setInt(7, employee.getDepartmentID());

                stmt.executeUpdate();
				

        } catch (SQLException e){
            if (e.getSQLState().equals("23000")) {
				System.err.println("Error: Duplicate entry for email '" + employee.getEmail() + "'. Please use a different email.");
				return false;
			} else {
				e.printStackTrace();
				return false;
			}
        }
		return true;
    }

    public List<Employee> getAllEmployees() {
		List<Employee> employees = new ArrayList<>();
		String sql = "SELECT * FROM employees";

		try (Connection conn = DatabaseConnection.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql)) {

				while (rs.next()) {
					Employee employee = new Employee();
					employee.setEmployeeID(rs.getInt("employee_id"));
					employee.setFirstName(rs.getString("first_name"));
					employee.setLastName(rs.getString("last_name"));
					employee.setEmail(rs.getString("email"));
					employee.setPhoneNum(rs.getString("phone_number"));
					employee.setHireDate(rs.getDate("hire_date").toLocalDate());
                    employee.setJobTitle(rs.getString("job_title"));
					employee.setSalary(rs.getFloat("salary"));
					employee.setDepartmentID(rs.getInt("department_id"));
					employees.add(employee);
				}

			} catch (SQLException e) {
			e.printStackTrace();
		}
		return employees;
	}
}
