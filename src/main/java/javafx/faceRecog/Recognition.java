package main.java.javafx.faceRecog;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import main.java.javafx.controller.DashboardController;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.global.opencv_imgproc; // Ensure this import is present

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javafx.embed.swing.SwingFXUtils;
import java.util.function.Consumer;

public class Recognition {

    // Camera frame converters
    private OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
    private Java2DFrameConverter java2DConverter = new Java2DFrameConverter();

    // For face detection and recognition
    private CascadeClassifier faceDetector;
    private LBPHFaceRecognizer recognizer;
    private HashMap<String, String> data;
    DashboardController dashboardController;
    OpenCVFrameGrabber camera = new OpenCVFrameGrabber(0);
    private volatile boolean running = false;
    private int idNumber;

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;  // Ensure the reference is set
    }

    public int getIdNumber(){
        return idNumber;
    }
    public void recog(Pane cameraPane, Consumer<String> nameCallback) throws Exception {

        String basePath = System.getProperty("user.dir");
        faceDetector = new CascadeClassifier(basePath + "\\src\\main\\resources\\haarcascade_frontalface_alt.xml");
        recognizer = LBPHFaceRecognizer.create();
        recognizer.read(basePath + "\\src\\main\\resources\\classifierLBPH.yml");

        // Load data for recognized people
        data = new HashMap<>();
        Path path = Paths.get(basePath + "\\src\\main\\resources\\namedata.csv");
        try {
            List<String> list = Files.readAllLines(path);
            for (String line : list) {
                String[] parts = line.split(",");
                data.put(parts[0], parts[1]);
            }
            System.out.println(list);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create ImageView to display camera feed
        ImageView cameraView = new ImageView();
        cameraView.setPreserveRatio(true);
        cameraView.setFitWidth(cameraPane.getPrefWidth());
        cameraView.setFitHeight(cameraPane.getPrefHeight());
        Platform.runLater(() -> cameraPane.getChildren().add(cameraView));
    
        // Initialize camera grabber
        camera.setImageWidth(640);
        camera.setImageHeight(480   );
        camera.start();
        
        running = true;
        // Start the recognition thread
        new Thread(() -> {
            try {
                while(running) {
                    Frame frame = camera.grab();
                    Mat mat = converter.convert(frame);
    
                    // Convert to grayscale for face detection
                    Mat grayImage = new Mat();
                    opencv_imgproc.cvtColor(mat, grayImage, opencv_imgproc.COLOR_BGRA2GRAY);
    
                    // Detect faces
                    RectVector detectedFaces = new RectVector();
                    faceDetector.detectMultiScale(grayImage, detectedFaces, 1.1, 1, 0, new Size(150, 150), new Size(500, 500));
    
                    // Process detected faces
                    for (int i = 0; i < detectedFaces.size(); i++) {
                        Rect faceData = detectedFaces.get(i);
                        opencv_imgproc.rectangle(mat, faceData, new Scalar(0, 0, 255, 0)); // Draw rectangle around face
    
                        // Extract face region and recognize
                        Mat capturedFace = new Mat(grayImage, faceData);
                        opencv_imgproc.resize(capturedFace, capturedFace, new Size(160, 160));
    
                        IntPointer label = new IntPointer(1);
                        DoublePointer confidence = new DoublePointer(1);
                        recognizer.predict(capturedFace, label, confidence);
    
                        // Get the recognized label and display it
                        int selection = label.get(0);
                        if (selection != -1 && data.containsKey(Integer.toString(selection))) {
                            idNumber = selection;  // Update ID only when a face is detected
                            String name = data.get(Integer.toString(selection)) + " - " + confidence.get(0);
                    
                            if (nameCallback != null) {
                                String finalName = name.split(" - ")[0]; // Extract name, exclude confidence
                                Platform.runLater(() -> nameCallback.accept(finalName));
                            }
                        } else {
                            System.out.println("No recognized face detected.");
                        }
                        
                        capturedFace.release();
                    }
    
                    // Convert Mat to JavaFX Image
                    Image image = SwingFXUtils.toFXImage(java2DConverter.getBufferedImage(frame), null);
                    Platform.runLater(() -> cameraView.setImage(image));
    
                    // Release resources
                    grayImage.release();
                    mat.release();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    camera.stop();
                    camera.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    

    public void releaseCamera() {
        running = false;
        if (camera != null) {
            try {
                camera.stop(); // Stop the camera
                camera.close(); // Release the camera resources
                System.out.println("Camera successfully released.");
            } catch (Exception e) {
                System.err.println("Error releasing camera: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
