import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentDAO {
    public String getDepartmentNameById(int departmentId) {
        String departmentName = null;
        String query = "SELECT department_name FROM departments WHERE department_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, departmentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                departmentName = resultSet.getString("department_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departmentName;
    }
}
