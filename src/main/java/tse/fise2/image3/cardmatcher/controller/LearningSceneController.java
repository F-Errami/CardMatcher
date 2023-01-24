package tse.fise2.image3.cardmatcher.controller;

import java.awt.Label;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;

import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.event.ActionEvent;

import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tse.fise2.image3.cardmatcher.model.Base;
import tse.fise2.image3.cardmatcher.model.Camera;
import tse.fise2.image3.cardmatcher.model.CameraLearning;
import tse.fise2.image3.cardmatcher.util.FileUtil;
import tse.fise2.image3.cardmatcher.util.MsgUtil;
import tse.fise2.image3.cardmatcher.sift.DatabaseDescriptors;
import tse.fise2.image3.cardmatcher.sift.Sift;


/**
 * This class is used to control the learning mode.
 * Each button or functionality of this mode is directly controlled by this class.
 *
 */

public class LearningSceneController implements Initializable {
    @FXML
    private Button start_btn;
    @FXML
    private Button back_btn;
    @FXML
    private MenuItem btn_nav_menu;
    @FXML
    private MenuItem btn_nav_test;
    @FXML
    private MenuItem btn_nav_learn;
    @FXML
    private ImageView learningFrame;
    @FXML
    private ImageView menuFrame;
    @FXML
    private Label lab;
    @FXML
    private MenuItem btn_learningbase;
    @FXML
    private ImageView image_base;
    @FXML
    private ImageView img_saved = new ImageView();
    @FXML
    private ListView<String> mylistview = new ListView<String>();

    @FXML
    private javafx.scene.control.Label name_saved_card;
    @FXML
    private javafx.scene.control.Label label_base_learning;
    @FXML
    private TextField search_field;


    public Camera capture1 = new CameraLearning();
    public Base base= new Base();

    /**
     * This method allows to open then Webcam for the learning mode, clicking on the Start camera button.
     * @param event when there is an event called startCamera threw by the fxml file.
     * @throws InterruptedException
     * @throws IOException
     */
    
    @FXML
    public void startCamera(ActionEvent event) throws InterruptedException, IOException {
        boolean learningmode = true;
        capture1.setLearningmode(learningmode);
        capture1.openCamera(learningFrame,start_btn);
    }


    /**
     * This method allows to go back to the menu when we are in learning mode, clicking on the left arrow button.
     * @param actionEvent when there is an event called back threw by the fxml file.
     * @throws IOException
     * @throws InterruptedException
     */
    public void back(ActionEvent actionEvent)  throws IOException, InterruptedException{
        capture1.setCameraActive(false);
        // stop the timer
        capture1.stopAcquisition();
        Parent backLoader = FXMLLoader.load(getClass().getResource("view/Menu.fxml"));
        Stage stage = (Stage)((MenuItem) btn_nav_menu).getParentPopup().getOwnerWindow();
        stage.getScene().setRoot(backLoader);


    }

    /**
     * This method allows to go to the test mode when we are in learning mode, clicking on option Test in Navigate.
     * @param actionEvent when there is an event called gotest threw by the fxml file.
     * @throws IOException
     * @throws InterruptedException
     */
    
    public void gotest(ActionEvent actionEvent)  throws IOException, InterruptedException{
        capture1.setCameraActive(false);
        // stop the timer
        capture1.stopAcquisition();
        Parent backLoader = FXMLLoader.load(getClass().getResource("view/TestScene.fxml"));
        Stage stage = (Stage)((MenuItem) btn_nav_test).getParentPopup().getOwnerWindow();
        stage.getScene().setRoot(backLoader);
    }
    
    /**
     * This method allows to go to the learning mode. Seems useless.
     * @param actionEvent when there is an event called goLearn threw by the fxml file.
     * @throws IOException
     * @throws InterruptedException
     */
    
    @FXML
    public void goLearn(ActionEvent actionEvent)  throws IOException, InterruptedException{
        capture1.setCameraActive(false);
        // stop the timer
        capture1.stopAcquisition();
        Parent backLoader = FXMLLoader.load(getClass().getResource("view/LearningScene.fxml"));
		Stage stage = (Stage)((MenuItem) btn_nav_learn).getParentPopup().getOwnerWindow();
        stage.getScene().setRoot(backLoader);
    }
    
    /**
     * This method allows to go to the learning base when we are in learning mode, clicking on the option Base.
     * @param actionEvent when there is an event called goLearnBase threw by the fxml file.
     * @throws IOException
     * @throws InterruptedException
     */
    
    @FXML
    public void goLearnBase(ActionEvent actionEvent) throws IOException, InterruptedException{
        capture1.stopAcquisition();
        Parent backLoader = FXMLLoader.load(getClass().getResource("view/LearningBase.fxml"));
        Stage stage = (Stage)((MenuItem) btn_learningbase).getParentPopup().getOwnerWindow();
        stage.getScene().setRoot(backLoader);
    }
    
    /**
     * This method allows to import a database of photos when we are in learning mode, clicking on option Import database in Import.
     * It opens a file chooser and allows to select a folder, which is the one you want to learn the images in.
     * The descriptors of all the images are then calculated and saved in the CSV file
     * @param actionEvent when there is an event called importdatabase threw by the FXML file.
     */
    
    public void importdatabase(ActionEvent actionEvent){
        try {

            Stage primaryStage = new Stage();
            DirectoryChooser directoryChooser = new DirectoryChooser();

            directoryChooser.setInitialDirectory(new File("src"));

            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory!=null) {
                FileUtil.copyfolder(selectedDirectory.getAbsolutePath());
                DatabaseDescriptors.extractAndSaveDescriptors(selectedDirectory.getAbsolutePath());
                MsgUtil.DisplayMsg("Import and learning of the database success !");
            }
        }
        catch (IOException e)
        {
            MsgUtil.DisplayMsg("Import failed !");
        }
    }

    /**
     * This method allows to import a picture when we are in learning mode, clicking on option Import picture in Import.
     * It opens a file chooser and allows to select a file, which is the one you want to learn the image of.
     * The descriptors of the image are then calculated and saved in the CSV file
     * @param actionEvent when there is an event called importpicture threw by the FXML file.
     */
    
    public void importpicture(ActionEvent actionEvent){
        try {

            Stage primaryStage = new Stage();
            FileChooser fileChooser = new FileChooser();

            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile!=null) {
                FileUtil.copyfile(selectedFile.getAbsolutePath());
                File file = new File(selectedFile.getAbsolutePath());
                Mat image = Imgcodecs.imread(file.getAbsolutePath());
                String name = file.getName();
                String updatedname = name.replaceAll(".png", "");
                Sift.saveDescriptor(Sift.getDescriptor(image, updatedname));
                MsgUtil.DisplayMsg("Import and learning of the picture success !");
            }
        }
        catch (IOException e)
        {
            MsgUtil.DisplayMsg("Import failed !");
        }
    }
    
    /**
     * This method allows to display the informations when we are in learning mode, clicking on option About in help.
     * It opens a window which contains useful informations to use and understand the learning mode.
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
    	Text importText = new Text();
    	Group root = new Group();
        
        //Setting the text to be added. 
        title.setText("About learning mode"); 
        title.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20)); 
        //setting the position of the text 
        title.setX(50); 
        title.setY(50);
        
        presentationText.setText("The CardMatcher learning mode is a mode where you can grab pictures of cards to fill a learning database. This database will be useful to recognize which card you show to the webcam in the test mode.");
        presentationText.setWrappingWidth(500);
        presentationText.setTextAlignment(TextAlignment.JUSTIFY);
        
        presentationText.setX(50); 
        presentationText.setY(90);
        
        title2.setText("Grabbing a picture"); 
        title2.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20)); 
        //setting the position of the text 
        title2.setX(50); 
        title2.setY(160);
        
        grabText.setText("When you are ready to grab your card, just click on the Start camera button to make your webcam start. Then, just place your card in the red rectangle, and click on the Capture button. All that was inside the red rectangle is saved as a picture in your computer. Then a small window appears, to rename your .jpg file writing a new name for it.");
        grabText.setWrappingWidth(500);
        grabText.setTextAlignment(TextAlignment.JUSTIFY);
        
        grabText.setX(50); 
        grabText.setY(190);
        
        title3.setText("Importing files"); 
        title3.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20)); 
        //setting the position of the text 
        title3.setX(50); 
        title3.setY(270);
        
        importText.setText("In learning mode, you can import either a learning database or just a picture of a card, instead of grabbing all the pictures of the card one by one. To do that, just click on the 'File' menu button, then 'Import' and finally 'Import picture' or 'Import database'. Choose the file or the folder you want to import and select it.");
        importText.setWrappingWidth(500);
        importText.setTextAlignment(TextAlignment.JUSTIFY);
        
        importText.setX(50); 
        importText.setY(300);
        
        root.getChildren().add(title);
        root.getChildren().add(presentationText);
        root.getChildren().add(title2);
        root.getChildren().add(grabText);
        root.getChildren().add(title3);
        root.getChildren().add(importText);

        Scene scene = new Scene(root, 600, 400);   
        stage.setTitle("About Learning Mode");  
        stage.setScene(scene); 
        stage.show(); 
     }

    public void aboutLearningBase(ActionEvent actionEvent) {       
    	Stage stage = new Stage();
    	
    	//Creating a Text object 
    	Text title = new Text(); 
    	Text presentationText = new Text();
    	Group root = new Group();
        
        //Setting the text to be added. 
        title.setText("About learning base"); 
        title.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20)); 
        //setting the position of the text 
        title.setX(50); 
        title.setY(50);
        
        presentationText.setText("The CardMatcher learning base is a mode where you can see the images of all the cards you have already learnt."
        		+ "You can browse your database either with your mouse or up and down keys.");
        presentationText.setWrappingWidth(500);
        presentationText.setTextAlignment(TextAlignment.JUSTIFY);
        
        presentationText.setX(50); 
        presentationText.setY(90);
        
        root.getChildren().add(title);
        root.getChildren().add(presentationText);

        Scene scene = new Scene(root, 600, 150);   
        stage.setTitle("About Learning Mode");  
        stage.setScene(scene); 
        stage.show(); 
     }
    
    /**
     * This method allows to update the display of the previous picture saved.
     * It also updates the display of the list of the learning base.
     */

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (start_btn!=null){
            start_btn.setOnMouseClicked(new EventHandler() {
                @Override
                public void handle(Event event) {
                    String card_name= capture1.getCard().getName();
                    String path1 = System.getProperty("user.dir")+ "/apprentissage/";
                    File dir  = new File(path1);
                    File[] liste = dir.listFiles();
                    if (liste.length==0){
                        String path = System.getProperty("user.dir")+ "/src/main/resources/tse/fise2/image3/cardmatcher/images/fond_carte.jpg";
                        base.displayImage(path, img_saved);
                    }
                    else {
                    	String path = System.getProperty("user.dir")+ "/apprentissage/"+ card_name + ".png";
                    	base.displayImage(path, img_saved);
                    	name_saved_card.setText(card_name);
                    }
                }
            });
        }
        base.initializeList(arg0, arg1, mylistview,null,null, label_base_learning, image_base,null,"apprentissage");
        //add Listener to filterInput TextField
        if (search_field!=null) {
            base.searchFieldProperty(search_field, mylistview,"apprentissage");
        }
    }
}