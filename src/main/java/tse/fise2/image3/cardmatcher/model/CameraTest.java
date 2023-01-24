package tse.fise2.image3.cardmatcher.model;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import tse.fise2.image3.cardmatcher.util.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * This class is used to grab a picture in test mode in order to recognize thanks to the database.
 * It is inherited from the class Camera.
 * 
 */

public class CameraTest  extends Camera {
    
	/**
	 * This class is used to grab a picture in test mode in order to recognize it.
	 * It grabs the picture in front of the webcam, saves the image and its SIFT descriptors, 
	 * and then match it with the descriptors of the pictures in the database.
	 * 
	 * @throws IOException 
	 */
	
	@Override
    public void saveImage() {


        String userHome = System.getProperty("user.dir"); // return c:\Users\${current_user_name}
        String folder = userHome + "/test";
        FileUtil.CreateFolder(folder);
        String pictureName = super.getLabel().getText();
        String file = folder + "/" + super.getCard().getName();
        int i = 1;
        while(new File(file + "Test" + i + ".png").exists()){
            i++;
        }
        file += "Test" + i + ".png";
        String SE = System.getProperty("os.name").toLowerCase();
        
        // Different crop boundaries for Mac or windows users.
        if (SE.indexOf("win") >= 0) {
        	Rect rectCrop = new Rect(new Point(202, 82), new Point(438, 398));
        	Mat crop_frame = new Mat(super.getFrame(),rectCrop);
        	// Saves the image into the database
        	Imgcodecs.imwrite(file, crop_frame);
        }
        else {
        	Rect rectCrop = new Rect(new Point(202, 52), new Point(598, 648));
        	Mat crop_frame = new Mat(super.getFrame(),rectCrop);
        	Imgcodecs.imwrite(file, crop_frame);
        }
    }
}