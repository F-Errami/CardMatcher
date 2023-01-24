package tse.fise2.image3.cardmatcher.sift;

import org.junit.jupiter.api.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SiftTest {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private final String testImage =  System.getProperty("user.dir")+"/TestResources/1coeur.png";
    private final String testImageName = "test_image";


    @Test
    void getDescriptor() {

        Mat testMat = Imgcodecs.imread(testImage);
        Descriptor descriptor = Sift.getDescriptor(testMat, testImageName);
        assertNotNull(descriptor);
        assertEquals(testImageName, descriptor.getImageName());
        assertNotNull(descriptor.getDescriptor());

    }

    @Test
    void saveDescriptor() throws IOException {

        Mat testMat = Imgcodecs.imread(testImage);
        Descriptor descriptor = Sift.getDescriptor(testMat, testImageName);
        Sift.saveDescriptor(descriptor);
        File descriptorFile = new File("descriptorsDB.csv");
        assertTrue(descriptorFile.exists());
        descriptorFile.delete();

    }



    @Test
    void calculateProximityScore() {

        Descriptor d1= Sift.getDescriptor(Imgcodecs.imread("/Users/mac/Desktop/reference.png"),"name");

        Descriptor d2= Sift.getDescriptor(Imgcodecs.imread("/Users/mac/Desktop/2coeur.png"),"picture");
        System.out.println(Sift.calculateProximityScore(d1.getDescriptor(),d2.getDescriptor()));

        assertNotEquals(0,Sift.calculateProximityScore(d1.getDescriptor(),d2.getDescriptor()));
    }

}
