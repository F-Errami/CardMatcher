package tse.fise2.image3.cardmatcher.model;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import tse.fise2.image3.cardmatcher.sift.Descriptor;
import tse.fise2.image3.cardmatcher.sift.Sift;
import tse.fise2.image3.cardmatcher.util.FileUtil;

import java.io.IOException;


/**
 * This class is used to grab a picture in learning mode in order to recognize it in test mode.
 * It is inherited from the class Camera.
 * 
 */


public class CameraLearning extends Camera {

	/**
	 * This class is used to grab a picture in learning mode in order to recognize it in test mode.
	 * It grabs the picture in front of the webcam, saves the image and its SIFT descriptors into databases.
	 * 
	 * @throws IOException 
	 */
	
    public void saveImage() throws IOException {
        String userHome = System.getProperty("user.dir");
        String folder = userHome + "/apprentissage";
        
        FileUtil.CreateFolder(folder);
        String pictureName = super.getLabel().getText();
        String file = folder + "/" + super.getCard().getName()+".png";
        String SE = System.getProperty("os.name").toLowerCase();
        
        // Different crop boundaries for Mac or windows users.
        if (SE.indexOf("win") >= 0) {
        	Rect rectCrop = new Rect(new Point(202, 82), new Point(438, 398));
        	Mat crop_frame = new Mat(super.getFrame(),rectCrop);
            Descriptor desc = Sift.getDescriptor(crop_frame,super.getCard().getName());
            Sift.saveDescriptor(desc);
            
            // Saves the image into the database
            Imgcodecs.imwrite(file, crop_frame); 
        }
        
        else {
        	Rect rectCrop = new Rect(new Point(202, 52), new Point(598, 648));
        	Mat crop_frame = new Mat(super.getFrame(),rectCrop);
            Descriptor desc = Sift.getDescriptor(crop_frame,super.getCard().getName());
            Sift.saveDescriptor(desc);
            Imgcodecs.imwrite(file, crop_frame);
        }  
    }
}
