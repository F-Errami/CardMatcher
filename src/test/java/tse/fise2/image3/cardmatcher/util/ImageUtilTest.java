package tse.fise2.image3.cardmatcher.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


class ImageUtilTest {


    @Test
    void detectCard() throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
         String userHome = System.getProperty("user.dir")+"/TestResources";;

        Imgcodecs imageCodecs = new Imgcodecs();
        Mat matrix = imageCodecs.imread(userHome+"/purple.png");
        assertEquals(false,ImageUtil.detectCard(matrix));
        matrix = imageCodecs.imread(userHome+"/white.png");
        assertEquals(true,ImageUtil.detectCard(matrix));
        matrix = imageCodecs.imread(userHome+"/mix.png");
        assertEquals(true,ImageUtil.detectCard(matrix));
    }
}
