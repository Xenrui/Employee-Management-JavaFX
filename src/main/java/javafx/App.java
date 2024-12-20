package main.java.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.input.MouseEvent;

public class App extends Application {

    private double x = 0;
    private double y = 0;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main/resources/fxml/FXML.fxml"));
        
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
        Image logoImage = new Image("file:/C:/FILES/Code/Java/EmployeeManagementApp/src/main/resources/images/logoIcon.png");
        stage.getIcons().add(logoImage);
        stage.setScene(scene);
        stage.show();
    }

    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        launch(args);
    }
}