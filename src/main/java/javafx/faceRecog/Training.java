package main.java.javafx.faceRecog;



import javax.swing.JOptionPane;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_face.EigenFaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;

public class Training {

    public void training() {
        File directory = new File("C:\\FILES\\Code\\Java\\EmployeeManagementApp\\src\\main\\resources\\photos");
        FilenameFilter imageFilter = new FilenameFilter() {   // filter image types
            public boolean accept(File dir, String name) {
                return name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".png");
            }
        };
        
        if(directory.length() == 0){
            System.out.println("No Employee");
            return;
        }

        File[] files = directory.listFiles(imageFilter);    // vector to store the images according to the filter
        MatVector photos = new MatVector(files.length);  // store images
        Mat labels = new Mat(files.length, 1, opencv_core.CV_32SC1);  // store labels for photos
        IntBuffer bufferLabels = labels.createBuffer();  // buffer to store labels
        int counter = 0;  // count images
        
        for (File image : files) {   // process each image for training
            Mat photo = opencv_imgcodecs.imread(image.getAbsolutePath(), opencv_imgcodecs.IMREAD_GRAYSCALE);  // load grayscale image
            String name = image.getName().split("\\.")[0];  // get the name part
            int personId = Integer.parseInt(image.getName().split("\\.")[1]);  // extract person ID from the filename

            opencv_imgproc.resize(photo, photo, new Size(160, 160));  // resize image
            photos.put(counter, photo);   // add photo to collection
            bufferLabels.put(counter, personId);  // add corresponding label
            counter++;
        }

        // Create an LBPH face recognizer
        FaceRecognizer lbph = LBPHFaceRecognizer.create();
        // FaceRecognizer eigenFaces = EigenFaceRecognizer.create();
		// FaceRecognizer fisherFaces = FisherFaceRecognizer.create();

        // Train the classifier using the photos and labels
        lbph.train(photos, labels);
        lbph.save("C:\\FILES\\Code\\Java\\EmployeeManagementApp\\src\\main\\resources\\classifierLBPH.yml");

		// eigenFaces.train(photos, labels);
		// eigenFaces.save("C:\\FILES\\Code\\Java\\FacialRecog\\src\\resources\\classifierEigenFaces.yml");

		// fisherFaces.train(photos, labels);
		// fisherFaces.save("C:\\FILES\\Code\\Java\\FacialRecog\\src\\resources\\classifierFisherFaces.yml");

        // Notify the user that training is complete
        System.out.println("Training Done!");
    }
}
