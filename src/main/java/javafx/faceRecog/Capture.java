package main.java.javafx.faceRecog;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

public class Capture {

    public void capture(String name, String personId) throws Exception, InterruptedException {
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        OpenCVFrameGrabber camera = new OpenCVFrameGrabber(0); // Camera index 0
		
        try {
            camera.start();
            System.out.println("Camera started successfully.");
        } catch (Exception e) {
            System.out.println("Error starting the camera: " + e.getMessage());
			converter.close();
			camera.close();
            return;
        }
        
        String basePath = System.getProperty("user.dir");
        String classifierPath = basePath + "\\src\\main\\resources\\haarcascade_frontalface_alt.xml";
        CascadeClassifier faceDetector = new CascadeClassifier(classifierPath);
		Path csvPath = Paths.get(basePath + "\\src\\main\\resources\\namedata.csv");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvPath.toString(), true))) {
            bw.write(personId + "," + name);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to CSV: " + e.getMessage());
			faceDetector.close();
			converter.close();
			camera.close();
            return;
        }
        if (faceDetector.empty()) {
            System.out.println("Error: Failed to load cascade classifier!");
			faceDetector.close();
			converter.close();
			camera.close();
            return;
        }

        CanvasFrame cFrame = new CanvasFrame("Capture", CanvasFrame.getDefaultGamma() / camera.getGamma());
        cFrame.setVisible(true);

        int sampleCount = 30;
        int sample = 1;

        Frame capturedFrame;
        Mat colorImage = new Mat();
        Mat grayImage = new Mat();

        while ((capturedFrame = camera.grab()) != null && cFrame.isVisible()) {
            colorImage = converter.convert(capturedFrame);
            opencv_imgproc.cvtColor(colorImage, grayImage, opencv_imgproc.COLOR_BGRA2GRAY);

            RectVector detectedFaces = new RectVector();
            faceDetector.detectMultiScale(grayImage, detectedFaces, 1.1, 1, 0, new Size(150, 150), new Size(500, 500));

            if (detectedFaces.size() > 0) {
                Rect face = detectedFaces.get(0);
                Mat capturedFace = new Mat(grayImage, face);
                opencv_imgproc.resize(capturedFace, capturedFace, new Size(160, 160));

                String photoPath = basePath + "\\src\\main\\resources\\photos\\" + name + "." + personId + "." + sample + ".jpg";
                opencv_imgcodecs.imwrite(photoPath, capturedFace);

                System.out.println("Photo " + sample + " captured: " + photoPath);
                sample++;

                if (sample > sampleCount) {
                    break;
                }
            }

            cFrame.showImage(capturedFrame);
        }

        cFrame.dispose();
        camera.stop();
		camera.close();
		converter.close();
		faceDetector.close();

        System.out.println("Capture process completed.");
    }
}
