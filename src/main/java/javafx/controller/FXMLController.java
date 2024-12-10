package main.java.javafx.controller;

import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import main.java.javafx.dao.UserDAO;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class FXMLController {
   
    @FXML
    private Button exit_btn;

    @FXML
    private Button exit_btn1;

    @FXML
    private Button login_btn;

    @FXML
    private AnchorPane login_form;

    @FXML
    private PasswordField password_login;

    @FXML
    private Hyperlink login_hyp;

    @FXML
    private PasswordField password_signup;

    @FXML
    private Hyperlink register;

    @FXML
    private Button signup_btn;

    @FXML
    private TextField email_signup;

    @FXML
    private AnchorPane signup_form;

    @FXML
    private TextField username_login;

    @FXML
    private TextField username_signup;

    @FXML
    private ImageView logo;


    private double x = 0;
    private double y = 0;

    public void exit(){
        System.exit(0);
    }

    public void changeForm(ActionEvent event){
        TranslateTransition loginTransition = new TranslateTransition();
        loginTransition.setDuration(Duration.seconds(0.7));
        loginTransition.setNode(login_form);

        TranslateTransition logoTransition = new TranslateTransition();
        logoTransition.setDuration(Duration.seconds(1));
        logoTransition.setNode(logo);

        if(event.getSource() == register){
            loginTransition.setToX(800);
            loginTransition.play();
            logoTransition.setToX(-400);
            logoTransition.play();

            username_signup.setText("");
            password_signup.setText("");
            email_signup.setText("");

        }
        else if(event.getSource() == signup_btn || event.getSource() == login_hyp){
            loginTransition.setToX(0);
            loginTransition.play();
            logoTransition.setToX(0);
            logoTransition.play();
            
            username_login.setText("");
            password_login.setText("");
        }
    }

    public void signup(ActionEvent event) throws Exception{
        String username = username_signup.getText();
        String email = email_signup.getText();
        String password = password_signup.getText();

        UserDAO user = new UserDAO();

        if(user.registerUser(email, username, password)){
            changeForm(event);
        }
        else{
            username_signup.setText("");
            password_signup.setText("");
            email_signup.setText("");
            return;
        }
    }

    public void login() throws Exception{
        String username = username_login.getText();
        String password = password_login.getText();

        UserDAO user = new UserDAO();

        if(user.loginUser(username, password)){

            UserDAO.username = username;

            Parent root = FXMLLoader.load(getClass().getResource("/main/resources/fxml/Dashboard.FXML"));
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
            stage.setScene(scene);
            stage.show();
            login_btn.getScene().getWindow().hide();
            }

        else{
           
            Alert alert = new Alert(AlertType.INFORMATION);
            
            alert.setTitle("Message!");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect Username/Password!");
            
            ImageView graphic = new ImageView(new Image("/main/resources/images/alertIcon.png"));
            graphic.setFitWidth(50);
            graphic.setFitHeight(50);
            alert.setGraphic(graphic);

            alert.showAndWait();
            
        }  
    }

    




}
