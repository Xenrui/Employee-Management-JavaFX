package main.java.javafx.dao;




import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class UserDAO {

    public static String username;
    private static int adminId; 

    // Method to get the current logged-in admin's ID
    public static int getAdminId() {
        return adminId;
    }

    // Method to set the logged-in admin's ID
    public static void setAdminId(int adminId) {
        UserDAO.adminId = adminId;
    }

    public boolean loginUser(String username, String password) {
        String sql = "SELECT admin_id FROM admins WHERE admin_username = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Set the admin_id after successful login
                adminId = rs.getInt("admin_id");  
                System.out.println(adminId);
                return true;
            } else {
                return false;  // Invalid credentials
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    public boolean isAccountExisting(String username, String email) {
        boolean accountExists = false;

        String query = "SELECT * FROM admins WHERE admin_email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set query parameters
            stmt.setString(1, email);

            // Execute the query
            ResultSet rs = stmt.executeQuery();

            // If result set is not empty, account exists
            if (rs.next()) {
                accountExists = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accountExists;
    }

    public boolean isEmailValid(String username, String email){

        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

        if (email == null) {
            return false;  // If email is null, return false
        }
        
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
        
    }

    public boolean registerUser(String email, String username, String password){
        String sql = "INSERT admins(admin_username, admin_email, password) VALUES (?, ?, ?)";

        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){

            if(username.isEmpty() | password.isEmpty() | email.isEmpty()){
                Alert alert = new Alert(AlertType.ERROR);
                
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Enter all field blanks!");
                alert.showAndWait();
                return false;
            }

            if(isAccountExisting(username, email)){
                Alert alert = new Alert(AlertType.ERROR);
                
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Email Already Exists!");
                alert.showAndWait();
                return false;
            }
            
            if(isEmailValid(username, email)){
                pstmt.setString(1, username);
                pstmt.setString(2, email);
                pstmt.setString(3, password);

                // Execute the insert operation
                int rowsAffected = pstmt.executeUpdate();

                // Check if the insert was successful
                if (rowsAffected > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Registration Successful!");
                    alert.showAndWait();
                    return true;
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Registration failed. Please try again.");
                    alert.showAndWait();
                    return false;
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid Email");
                    alert.showAndWait();
                    return false;
            }      
        } catch(SQLException e){
            System.out.println("Error" + e.getMessage());
            return false;
        }
    }
}
