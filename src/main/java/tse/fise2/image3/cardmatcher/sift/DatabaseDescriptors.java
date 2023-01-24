package tse.fise2.image3.cardmatcher.sift;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * Class for extracting and saving into a database the SIFT descriptors of a set of images.
 */

public class DatabaseDescriptors {
	
	 /**
     * Extracts and saves the SIFT descriptors of all images in a given input folder.
     * @param inputFolder the path of the input folder containing the images
     * @throws IOException if an error occurs while saving the descriptors
     */
	
	public static void extractAndSaveDescriptors(String inputFolder) throws IOException {
        File folder = new File(inputFolder);
        File[] files = folder.listFiles();

        // Create a list to store the SIFT descriptors of the images
        List<Descriptor> descriptorsList = new ArrayList<>();

        // Get the SIFT descriptors of each image
        for (File file : files) {
            Mat image = Imgcodecs.imread(file.getAbsolutePath());
            if (!image.empty()) {
                String name = file.getName();
                String updatedname = name.replaceAll(".png", "");
                Descriptor descriptor = Sift.getDescriptor(image, updatedname);
                descriptorsList.add(descriptor);
            }
        }

        // Write the SIFT descriptors to the CSV file
        for (Descriptor descriptors : descriptorsList) {
            Sift.saveDescriptor(descriptors);
        }
    }
}
