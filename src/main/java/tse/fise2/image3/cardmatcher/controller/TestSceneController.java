package tse.fise2.image3.cardmatcher.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import tse.fise2.image3.cardmatcher.model.Base;
import tse.fise2.image3.cardmatcher.model.Camera;

import tse.fise2.image3.cardmatcher.model.CameraTest;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is used to control the test mode in which we try to recognize the card in front of the webcam.
 * Each button or functionality of this mode is directly controlled by this class.
 *
 */

public class TestSceneController implements Initializable {

    @FXML
    private ImageView testingFrame;
    @FXML
    private Button start_btn;
    @FXML
    private Button back_btn;
    @FXML
    private MenuItem btn_testbase;
    @FXML
    private MenuItem btn_nav_learn;
    @FXML
    private MenuItem btn_nav_menu;
    @FXML
    private Label lab;
    @FXML
    private Label label_carte;
    @FXML
    private Label label_small_card;
    @FXML
    private Label label_base_app;
    @FXML
    private javafx.scene.control.Label label_base_test;
    @FXML
    private ListView<String> mylistview = new ListView<String>();
    @FXML
    private ImageView image_base = new ImageView();
    @FXML
	private ImageView small_img_card = new ImageView();
    @FXML
    private javafx.scene.control.TextField search_field = new javafx.scene.control.TextField();

    public ImageView detect_frame;
    public ImageView detect_frame2;
    public ImageView detect_frame3;
    public Camera capture1 = new CameraTest();
    public Base base= new Base();

    /**
     * This method allows to open then Webcam for the test mode, clicking on the Start camera button.
     * @param event when there is an event called startCamera threw by the fxml file.
     * @throws InterruptedException
     * @throws IOException
     */
    
    @FXML
    public void startCamera(ActionEvent event) throws InterruptedException, IOException {
        boolean testingmode = true;
        capture1.setTestingmode(testingmode);
        capture1.openCamera(testingFrame,start_btn);
        capture1.AddImageDetection(detect_frame);
        capture1.AddImageDetection2(detect_frame2);
        capture1.AddImageDetection3(detect_frame3);

    }

    /**
     * This method allows to go back to the menu when we are in test mode, clicking on the left arrow button.
     * @param actionEvent when there is an event called back threw by the fxml file.
     * @throws IOException
     * @throws InterruptedException
     */
    
    public void back(ActionEvent actionEvent)  throws IOException, InterruptedException {
        capture1.setCameraActive(false);
        // stop the timer
        capture1.stopAcquisition();
        Parent backLoader = FXMLLoader.load(getClass().getResource("view/Menu.fxml"));
        Stage stage = (Stage)((MenuItem) btn_nav_menu).getParentPopup().getOwnerWindow();
        stage.getScene().setRoot(backLoader);
    }
    
    /**
     * This method allows to go to the learning mode when we are in test mode, clicking on option Learning in Navigate.
     * @param actionEvent when there is an event called goLearn threw by the fxml file.
     * @throws IOException
     * @throws InterruptedException
     */

    public void goLearn(ActionEvent actionEvent) throws IOException, InterruptedException {
        capture1.setCameraActive(false);
        // stop the timer
        capture1.stopAcquisition();
        Parent backLoader = FXMLLoader.load(getClass().getResource("view/LearningScene.fxml"));
        Stage stage = (Stage)((MenuItem) btn_nav_learn).getParentPopup().getOwnerWindow();
        stage.getScene().setRoot(backLoader);
    }

    /**
     * This method allows to go to the test base when we are in learning mode, clicking on the option Base.
     * @param actionEvent when there is an event called goTestBase threw by the fxml file.
     * @throws IOException
     * @throws InterruptedException
     */
    
    public void goTestBase(ActionEvent actionEvent) throws IOException, InterruptedException{
        capture1.stopAcquisition();
        Parent backLoader = FXMLLoader.load(getClass().getResource("view/TestBase.fxml"));
        Stage stage = (Stage)((MenuItem) btn_testbase).getParentPopup().getOwnerWindow();
        stage.getScene().setRoot(backLoader);
    }
    
    /**
     * This method allows to go to the test mode. Seems useless.
     * @param actionEvent when there is an event called goTest threw by the fxml file.
     * @throws IOException
     * @throws InterruptedException
     */
    
    public void goTest(ActionEvent actionEvent) throws IOException, InterruptedException {
        capture1.setCameraActive(false);
        // stop the timer
        capture1.stopAcquisition();
        Parent backLoader = FXMLLoader.load(getClass().getResource("view/TestScene.fxml"));
        Stage stage = (Stage)((MenuItem) btn_nav_learn).getParentPopup().getOwnerWindow();
        stage.getScene().setRoot(backLoader);
    }

    /**
     * This method allows to display the informations when we are in test mode, clicking on option About in Help.
     * It opens a window which contains useful informations to use and understand the test mode.
     * @param actionEvent when there is an event called about threw by the FXML file.
     */
    
    public void about(ActionEvent actionEvent) {       
    	Stage stage = new Stage();
    	
    	//Creating a Text object 
    	Text title = new Text(); 
    	Text title2 = new Text();
    	Text title3 = new Text();
    	Text presentationText = new Text();
    	Text grabText = new Text();
    	Text matchText = new Text();
    	Group root = new Group();
        
        //Setting the text to be added. 
        title.setText("About test mode"); 
        title.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20)); 
        //setting the position of the text 
        title.setX(50); 
        title.setY(50);
        
        presentationText.setText("The CardMatcher test mode is a mode where you can grab a picture of a card to recognize it. Then, the app will show in output the name of the card showed\r\n"
        		+ "to the camera and the picture of a card in learning database with the highest proximity score.");
        presentationText.setWrappingWidth(500);
        presentationText.setTextAlignment(TextAlignment.JUSTIFY);
        
        presentationText.setX(50); 
        presentationText.setY(90);
        
        title2.setText("Grabbing a picture"); 
        title2.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20)); 
        //setting the position of the text 
        title2.setX(50); 
        title2.setY(160);
        
        grabText.setText("When you are ready to grab your card, just click on the Start camera button to make your webcam start. Then, just place your card in the green rectangle, and click again on the Capture button. All that was inside the green rectangle is saved as a picture in your computer, and then matched with the pictures lying in your learning database.");
        grabText.setWrappingWidth(500);
        grabText.setTextAlignment(TextAlignment.JUSTIFY);
        
        grabText.setX(50); 
        grabText.setY(190);
        
        title3.setText("Matching and proximity score"); 
        title3.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20)); 
        //setting the position of the text 
        title3.setX(50); 
        title3.setY(270);
        
        matchText.setText("In this mode, you will be able to compare the picture you are grabbing with the pictures contained in your learning database. The matching functionality is done with the calcul of points of interest il all the pictures. The more points of interest of the tested card match those of one of the learning database cards, the more high proximity score you will get. In output, the app will display the card in your learning database with the highest proximity score, which is the same card as the one you tested.");
        matchText.setWrappingWidth(500);
        matchText.setTextAlignment(TextAlignment.JUSTIFY);
        
        matchText.setX(50); 
        matchText.setY(300);
        
        root.getChildren().add(title);
        root.getChildren().add(presentationText);
        root.getChildren().add(title2);
        root.getChildren().add(grabText);
        root.getChildren().add(title3);
        root.getChildren().add(matchText);

        Scene scene = new Scene(root, 600, 420);  
        stage.setTitle("About Test Mode"); 
        stage.setScene(scene);
        stage.show(); 
     }
    
    public void aboutTestBase(ActionEvent actionEvent) {       
    	Stage stage = new Stage();
    	
    	//Creating a Text object 
    	Text title = new Text(); 
    	Text title2 = new Text();
    	Text title3 = new Text();
    	Text presentationText = new Text();
    	Text grabText = new Text();
    	Text matchText = new Text();
    	Group root = new Group();
        
        //Setting the text to be added. 
        title.setText("About test base"); 
        title.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20)); 
        //setting the position of the text 
        title.setX(50); 
        title.setY(50);
        
        presentationText.setText("The CardMatcher test base is a mode where you can select a card between the one you have already tested. Then, you can either print its keypoints or try to match it with the cards you learnt.\r\n");
        presentationText.setWrappingWidth(500);
        presentationText.setTextAlignment(TextAlignment.JUSTIFY);
        
        presentationText.setX(50); 
        presentationText.setY(90);
        
        title2.setText("Keypoints"); 
        title2.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20)); 
        //setting the position of the text 
        title2.setX(50); 
        title2.setY(160);
        
        grabText.setText("You can print the main keypoints of the selected card, computed by the SIFT algorithm. These keypoints represent the image main features but not all of them are printed for aesthetic reasons."
        		+ "Each keypoint is a special structure which has many attributes like its (x,y) coordinates, size of the meaningful neighbourhood, angle which specifies its orientation, response that specifies strength of keypoints etc.");
        grabText.setWrappingWidth(500);
        grabText.setTextAlignment(TextAlignment.JUSTIFY);
        
        grabText.setX(50); 
        grabText.setY(190);
        
        title3.setText("Match"); 
        title3.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20)); 
        //setting the position of the text 
        title3.setX(50); 
        title3.setY(300);
        
        matchText.setText("This button allows to compare the picture you are grabbing with the pictures contained in your learning database. In output, the app will display the card in your learning database with the highest proximity score, and it will draw the lines of the recognized keypoints.");
        matchText.setWrappingWidth(500);
        matchText.setTextAlignment(TextAlignment.JUSTIFY);
        
        matchText.setX(50); 
        matchText.setY(330);
        
        root.getChildren().add(title);
        root.getChildren().add(presentationText);
        root.getChildren().add(title2);
        root.getChildren().add(grabText);
        root.getChildren().add(title3);
        root.getChildren().add(matchText);

        Scene scene = new Scene(root, 600, 420);  
        stage.setTitle("About Test Mode"); 
        stage.setScene(scene);
        stage.show(); 
     }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        base.initializeList(arg0, arg1, mylistview, label_carte, label_small_card, label_base_test, image_base, small_img_card,"test");
        //add Listener to filterInput TextField
        if (search_field !=null) {
            base.searchFieldProperty(search_field, mylistview,"test");
        }
    }
	public void displayCorrespondance(ActionEvent actionEvent) {
		base.setCorrespondance(true);
		base.displayCorres(label_carte, label_small_card, label_base_test, image_base, small_img_card,mylistview);

    }
	public void displayPtsInterets(ActionEvent actionEvent) {
		base.displayPtsInteretsCard(label_base_test, label_small_card, small_img_card, image_base,mylistview);
	}
    


}