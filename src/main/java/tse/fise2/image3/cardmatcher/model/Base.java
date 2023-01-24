package tse.fise2.image3.cardmatcher.model;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.opencv.core.Core;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.SIFT;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import tse.fise2.image3.cardmatcher.util.FileUtil;

/**
 * This class is focused on the navigation in the databases for both learning and test modes.
 * We can find many methods managing the image displayed after clicking on the buttons in these modes.
 *
 */

public class Base {

	private String path;
	public boolean correspondance=false;
	

	public boolean isCorrespondance() {
		return correspondance;
	}

	public void setCorrespondance(boolean correspondance) {
		this.correspondance = correspondance;
	}

	public String getPath() {
		return path;
	}
	
	/**
	 * Displays an image in a given ImageView.
	 * This method is used to display the card of the database we want to focus on.
	 * @param path The path to the image file.
	 * @param img The ImageView in which to display the image.
	 */
	
    public void displayImage(String path, ImageView img) {
        File file = new File(path);
        String localUrl;
        try {
            localUrl = file.toURI().toURL().toString();
            Image myimage = new Image(localUrl);
            img.setImage(myimage);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the files in a given folder.
     * @param baseName the name of the folder
     * @return list of the files in the folder
     */
    
    private ObservableList<String> displayBase(String baseName) {
        String userHome = System.getProperty("user.dir");
        String folder = userHome + "/"+baseName;
        //System.out.println(folder);
        File dir  = new File(folder);
        File[] liste = dir.listFiles();
        ObservableList<String> listeArray = FXCollections.observableArrayList();
        for(File item : liste){
            if(item.isFile())
            {
                String name = item.getName();
                listeArray.add(name);
            }
            else if(item.isDirectory())
            {
                System.out.format("Nom du répertoir: %s%n", item.getName());
            }
        }
        return listeArray;
    }

    /**
     * Filters a ListView of strings based on the text entered by the user.
     * @param oldValue The previous search text.
     * @param newValue The current search text.
     * @param mylistview The ListView to be filtered.
     * @param baseName The name of the folder where the images are stored
     */
    
    private void filterText(String oldValue, String newValue, ListView<String> mylistview,String baseName) {
        ObservableList<String> listeArray = displayBase(baseName);
        mylistview.setItems(listeArray);
        ObservableList<String> subentries = FXCollections.observableArrayList();
        if (oldValue != null && (newValue.length() < oldValue.length())) {
            mylistview.setItems(subentries);
            filterText(newValue, newValue, mylistview,baseName);
        }
        else {
            newValue = newValue.toUpperCase();
            for(String input : mylistview.getItems()) {
                String filterText = input;
                if(filterText.toUpperCase().contains(newValue)) {
                    subentries.add(input);
                }
            }
            mylistview.setItems(subentries);
        }
    }

    /**
     * Initializes a ListView of strings and sets up event handlers to display an image 
     * and update labels when an item is selected.
	 * 
     * @param mylistview The ListView to be initialized.
     * @param label_title The label where the title of the selected image is displayed
     * @param label_small_card The label where the name of the corresponding image is displayed
     * @param label_current_card The label where the name of the selected image is displayed
     * @param image_base The ImageView where the selected image is displayed
     * @param small_img_card The ImageView where the corresponding image is displayed
     * @param baseName The name of the folder where the images are stored
     */
    
    public void initializeList(URL arg0, ResourceBundle arg1, ListView<String> mylistview,Label label_title, Label label_small_card,Label label_current_card, ImageView image_base, ImageView small_img_card,String baseName) {
        FileUtil.CreateFolder(baseName);
        ObservableList<String> listeArray = displayBase(baseName);
        mylistview.getItems().addAll(listeArray);
        // Add listener to display image on click
        mylistview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                path = System.getProperty("user.dir")+ "/"+baseName+"/"+ mylistview.getSelectionModel().getSelectedItem();
                correspondance=false;
                displayImage(path, image_base);
                //label_current_card.setText(mylistview.getSelectionModel().getSelectedItem());
				if (label_title!=null) {
					label_title.setText("Carte Selectionnée");
					displayCorres(label_title,label_small_card,label_current_card,image_base,small_img_card,mylistview);			
					}
				}            
        });
    };
    
	public void displayCorres(Label label_title, Label label_small_card,Label label_current_card, ImageView image_base, ImageView small_img_card, ListView<String> mylistview) {
    	if (correspondance) {
        	displayImage(getPath(),small_img_card);
        	label_small_card.setText("Selected Card");       	
        	label_title.setText("Matched Card"); 
        	
        	// Display best correspondance
        	String corres_card_name = mylistview.getSelectionModel().getSelectedItem().substring(0, mylistview.getSelectionModel().getSelectedItem().length()-9);
        	String path_corres_card = System.getProperty("user.dir")+ "/apprentissage/"+ corres_card_name + ".png";
        	this.displayImage(path_corres_card, image_base);
        	label_current_card.setText(corres_card_name);
    	}
    	else {
    		small_img_card.setImage(null);
    		label_title.setText("Selected card");
    		label_small_card.setText(null); 
    	}
    	

	}
	
	/**
	 * Displays the keypoints of the selected image and of the recognized image..
	 * 
	 * @param label_title The label where the title of the selected image is displayed
	 * @param label_small_card The label where the name of the corresponding image is displayed
	 * @param label_current_card The label where the name of the selected image is displayed
	 * @param image_base The ImageView where the selected image is displayed
	 * @param small_img_card The ImageView where the corresponding image is displayed
	 */
	
	public void displayPtsInteretsCard(Label label_current_card, Label label_small_card, ImageView small_img_card, ImageView image_base ,ListView<String> mylistview) {
	    if (this.isCorrespondance()) {
	    	
	    	
	    	String corres_card_name = mylistview.getSelectionModel().getSelectedItem().substring(0, mylistview.getSelectionModel().getSelectedItem().length()-9);
        	String path_corres_card = System.getProperty("user.dir")+ "/apprentissage/"+ corres_card_name + ".png";
        	String imgPath = Paths.get(System.getProperty("user.home"), "image2.png").toString();
        	compareImages(this.getPath(), path_corres_card,imgPath);
        	this.displayImage(imgPath, image_base);
	    	
    		
    		label_current_card.setText(null);
    		label_small_card.setText(null);
    		small_img_card.setImage(null);
	    	


	    } else {
	    	String imgPath = Paths.get(System.getProperty("user.home"), "image1.png").toString();
	        displaySIFTKeypoints(this.getPath(),imgPath);

	        displayImage(imgPath,image_base);

	        label_current_card.setText("Card with keypoints");
	        //label_small_card.setText("Grabbed card");
 
	    }
       
	    
	}


    
    
	public static void displaySIFTKeypoints(String imgPath, String savePath) {
		// Read the image file
		Mat img = Imgcodecs.imread(imgPath);
		// Initialize SIFT feature detector
		SIFT detector = SIFT.create();

		// Find keypoints and descriptors for the image
		MatOfKeyPoint keypoints = new MatOfKeyPoint();
		Mat descriptors = new Mat();
		detector.detectAndCompute(img, new Mat(), keypoints, descriptors);

		// Draw keypoints on the image
		Mat output = new Mat();
		Features2d.drawKeypoints(img, keypoints, output);

		// Save the image in png format
		Imgcodecs.imwrite(savePath, output);
	}


	/**
	 * Displays the points of interest of the selected image and the comparison 
	 * of the selected image with the corresponding image
	 * @param label_current_card The label where the name of the selected image is displayed
	 * @param label_small_card The label where the name of the corresponding image is displayed
	 * @param small_img_card The ImageView where the corresponding image is displayed
	 * @param image_base The ImageView where the selected image or the comparison of the images is displayed
	 */
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void searchFieldProperty(TextField search_field, ListView<String> mylistview,String baseName) {
        search_field.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                mylistview.setItems(null);
                filterText((String) oldValue, (String) newValue,mylistview,baseName);
            }
        });
    }
    
    
    
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void compareImages(String img1Path, String img2Path, String savePath) {
    	Mat img1 = Imgcodecs.imread(img1Path);
    	Mat img2 = Imgcodecs.imread(img2Path);
        // Initialize SIFT feature detector
        SIFT detector = SIFT.create();

        // Find keypoints and descriptors for both images
        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        Mat descriptors1 = new Mat();
        detector.detectAndCompute(img1, new Mat(), keypoints1, descriptors1);

        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
        Mat descriptors2 = new Mat();
        detector.detectAndCompute(img2, new Mat(), keypoints2, descriptors2);

        // Match the descriptors
        BFMatcher matcher = BFMatcher.create();
        MatOfDMatch matches = new MatOfDMatch();
        matcher.match(descriptors1, descriptors2, matches);

        // Get the coordinates of the matched keypoints
        List<Point> matchedPoints1 = new ArrayList<>();
        List<Point> matchedPoints2 = new ArrayList<>();
        DMatch[] matchArray = matches.toArray();
        for (DMatch match : matchArray) {
            matchedPoints1.add(keypoints1.toList().get(match.queryIdx).pt);
            matchedPoints2.add(keypoints2.toList().get(match.trainIdx).pt);
        }

        // Draw lines between matched points on output image
        Mat output = new Mat();
        MatOfPoint2f points1 = new MatOfPoint2f();
        points1.fromList(matchedPoints1);
        MatOfPoint2f points2 = new MatOfPoint2f();
        points2.fromList(matchedPoints2);
        Features2d.drawMatches(img1, keypoints1, img2, keypoints2, matches, output);

        // Save the output image in png format
        Imgcodecs.imwrite(savePath, output);
    }
 



}
