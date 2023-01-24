package tse.fise2.image3.cardmatcher.util;

import javafx.scene.effect.GaussianBlur;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

/**
*
* The ImageUtil class contains a utility method to detect a card in an image.
*/

public class ImageUtil {
	
	/**
	* Detects if there is a card in an image.
	*
	* @param frame the image to be processed
	* @return returns true if the image contains a card and false otherwise
	*/
    public static Boolean detectCard(Mat frame) {
        Mat bin= new Mat();
        Mat mGray= new Mat();
        Imgproc.cvtColor(frame,mGray,Imgproc.COLOR_RGB2GRAY);
        Imgproc.threshold(mGray,bin,180,255,Imgproc.THRESH_BINARY);
        int n=bin.rows();
        int m=bin.cols();
        int nb= 0;
        int  N= m*n;
        for (int i =0; i<n;i++)
            for (int j =0; j<m;j++) {
            	
                //White pixels detection with a margin of error of 10
                if ( bin.get(i, j)[0]== 255)
                   nb++;
            }
        	if (Double.valueOf(nb)/(Double.valueOf(N))*100 > 90)
            return true;
            else return false;
    }
}