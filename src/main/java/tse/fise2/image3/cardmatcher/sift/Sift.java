package tse.fise2.image3.cardmatcher.sift;
import org.opencv.core.*;

import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.SIFT;
import tse.fise2.image3.cardmatcher.model.ScoreImage;
import tse.fise2.image3.cardmatcher.util.FileUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class implements the four main steps involved in SIFT algorithm to make descriptors and recognition
 * managing. It mainly uses the OpenCV library.
 *
 */

public class Sift {

	/**
	 * This method allows to extract and compute the SIFT descriptors of an input image.
	 * It uses the OpenCV features2D package to process the image and get its descriptors.
	 * 
	 * @param image the image we want to extract the descriptors of.
	 * @param name the name of the image
	 * @return Descriptor the descriptors of the image .
	 */

	public  static Descriptor getDescriptor(Mat image, String name)
	{

		if (image.empty()) {
			System.out.println("Erreur lors du chargement de l'image");
			return null;
		}
		SIFT detector = SIFT.create();
		SIFT extractor = SIFT.create();
		MatOfKeyPoint keypoints = new MatOfKeyPoint();

		// Detect the keypoints of the image
		detector.detect(image, keypoints);
		Mat descriptors = new Mat();
		extractor.compute(image, keypoints, descriptors);
		Descriptor des = new Descriptor(name,descriptors);

		return des;
	}

	/**
	 * Saves SIFT descriptor into a CSV file
	 * 
	 * @param desc the descriptor to be saved
	 * @throws IOException if the file cannot be written
	 */

	public static void saveDescriptor(Descriptor desc) throws IOException {

		FileUtil.CreateFile("descriptorsDB","csv");
		BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"/descriptorsDB.csv",true));

		writer.append(desc.getImageName());
		writer.append(",");

		for (int i = 0; i < desc.getDescriptor().rows(); i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j <desc.getDescriptor().cols(); j++) {
				sb.append(desc.getDescriptor().get(i, j)[0]).append(",");
			}

			// Delete the last comma
			sb.setLength(sb.length() - 1);
			writer.append(sb.toString());
			writer.newLine();
		}
		writer.close();
	}

	/**
	 * Reads SIFT descriptor from a CSV file
	 * The file is read line by line and its values are added to a list.
	 * 
	 * @return a list of descriptors
	 * @throws IOException if the file cannot be read
	 */

	public static List<Descriptor> readDescriptor() throws IOException {

		List<Descriptor> descriptorList = new ArrayList<>();


		int i=0;

		//k is here to know if the line contains image's name
		int k=0;

		boolean t=false;
		FileUtil.CreateFile("descriptorsDB","csv");
		BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/descriptorsDB.csv"));

		Descriptor des =new Descriptor();
		descriptorList.add(des);

		String line;
		List<float[]> data = new ArrayList<>();

		while ((line = reader.readLine()) != null) {

			k=0;
			// Separate the line in different numerical values
			String[] values = line.split(",");


			if ( t && !values[0].matches("-?\\d+(\\.\\d+)?")  ) {
				Mat mat = new Mat(data.size(), data.get(0).length, CvType.CV_32F);
				for (int j = 0; j < data.size(); j++) {
					mat.put(j, 0, data.get(j));
				}
				des.setDescriptor(mat);
				descriptorList.add(des);

				i++;
				des = new Descriptor();

				//Reinit data for the new descriptor
				data = new ArrayList<>();
			}

			if(!values[0].matches("-?\\d+(\\.\\d+)?"))
			{
				des.setImageName(values[0]);
				k=1;
				t=true;
			}

			// Convert every value into a float and add it to the list
			int n;
			if (k==1)
			{
				n= values.length-1;

			}else {
				n = values.length;
			}

			float[] rowData = new float[n];
			for (int j = 0; j < n; j++) {

				if(k==1) {
					rowData[j] = Float.parseFloat(values[j+1]);
				}else
					rowData[j] = Float.parseFloat(values[j]);
			}
			data.add(rowData);
		}


		// Adding the last descriptor
		if(i>0) {
			Mat mat = new Mat(data.size(), data.get(0).length, CvType.CV_32F);
			for (int j = 0; j < data.size(); j++) {
				mat.put(j, 0, data.get(j));
			}
			des.setDescriptor(mat);
			descriptorList.add(des);
		}
		reader.close();
		return descriptorList;
	}


	/**

	* This method allows to calculate the proximity score between reference descriptors and database descriptors using L1 distance
	*
	* @param referenceDescriptors the reference descriptors to compare
	* @param databaseDescriptors the database descriptors to compare
	* @return proximityScore the proximity score between the descriptors of the two images.
	*/
	
	public static double calculateProximityScore(Mat referenceDescriptors, Mat databaseDescriptors) {

		// Create a descriptor matcher
		DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_L1);
		// Match the reference descriptors to the database descriptors
		MatOfDMatch matches = new MatOfDMatch();
		matcher.match(referenceDescriptors, databaseDescriptors, matches);
		// Calculate the proximity score
		double proximityScore = 0.0;
		for (DMatch match : matches.toList()) {
			proximityScore += match.distance;
		}
		proximityScore /= matches.size().height;
		return proximityScore;
	}


	/**

	* Read the descriptor from the csv file and return the closest 3 descriptors to the
	* descriptors of the tested image.
	* 
	* @param referenceDescriptors the descriptors of the tested image.
	* @return the list of descriptor objects
	* @throws IOException if the file is not found
	*/
	
	public static List<ScoreImage> getTop3ImageBestScore(Mat referenceDescriptors) throws IOException {
		
		List<Descriptor> descriptorList = readDescriptor();
		List<ScoreImage> top3Results = new ArrayList<>();

		for (Descriptor d : descriptorList) {
			double score = calculateProximityScore(referenceDescriptors, d.getDescriptor());
			ScoreImage currentResult = new ScoreImage(d.getImageName(), score);

			// If the list top3Results is not full
			if (top3Results.size() < 3) {
				top3Results.add(currentResult);
			} else {
				// Find the bigger score in top3Results
				int worstScoreIndex = 0;
				double worstScore = top3Results.get(0).getScore();
				for (int i = 1; i < top3Results.size(); i++) {
					if (top3Results.get(i).getScore() > worstScore) {
						worstScore = top3Results.get(i).getScore();
						worstScoreIndex = i;
					}
				}
				// Replace the bigger score by currentResult if this one is too small.
				if (currentResult.getScore() < worstScore) {
					top3Results.set(worstScoreIndex, currentResult);
				}
			}
		}
		// Sort top3Results in ascending order.
		top3Results.sort(Comparator.comparingDouble(ScoreImage::getScore));
		return top3Results;
	}
}