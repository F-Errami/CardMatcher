package tse.fise2.image3.cardmatcher.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is used to control the menu.
 * Each button of this menu is directly controlled by this class.
 *
 */

public class MenuController {
    @FXML
    private Button btn_test;
    @FXML
    private Button btn_app;

    /**
     * This method allows to go to learning mode, clicking on Learning button.
     * @param event when there is an event called onClickedApp threw by the fxml file.
     * @throws IOException
     */
    
    @FXML
    public void onClickedApp(ActionEvent event) throws IOException {


        Parent modeApp = FXMLLoader.load(getClass().getResource("view/LearningScene.fxml"));

        btn_app.getScene().setRoot(modeApp);


    }

    /**
     * This method allows to go to test mode, clicking on Test button.
     * @param event when there is an event called onClickedtest threw by the fxml file.
     * @throws IOException
     */
    
    public void onClickedtest (ActionEvent actionEvent)throws IOException {


        Parent modeTest = FXMLLoader.load(getClass().getResource("view/TestScene.fxml"));

        btn_test.getScene().setRoot(modeTest);
    }
    
    /**
     * This method allows to display general informations about the app, clicking on Help button.
     * @param event when there is an event called about threw by the fxml file.
     */
    
    public void about(ActionEvent actionEvent) {       
    	Stage stage = new Stage();
    	final VBox vb = new VBox();
    	//Creating a Text object 
    	Text title = new Text(); 
    	Text title2 = new Text();
    	Text title3 = new Text();
    	Text presentationText = new Text();
    	Text contribText = new Text();
    	Text technoText = new Text();
    	ScrollBar s = new ScrollBar();
    	Group root = new Group();
    	Scene scene = new Scene(root, 600, 400);
    	root.getChildren().addAll(vb, s);
        
    	vb.setLayoutX(50);
    	vb.setLayoutY(50);
        vb.setSpacing(20);
 
        s.setLayoutX(scene.getWidth()-s.getWidth());
        s.setMin(-50);
        s.setOrientation(Orientation.VERTICAL);
        s.setPrefHeight(400);
        s.setMax(160);  
        
        //Setting the text to be added. 
        title.setText("About CardMatcher"); 
        title.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20)); 
        //setting the position of the text 
        title.setX(50); 
        title.setY(50);
        
        presentationText.setText("CardMatcher is an app capable of learning to recognize playing cards. CardMatcher can be used very easily, without any knowledge in image recognition and with any standard playing card set and a webcam.");
        presentationText.setWrappingWidth(500);
        presentationText.setTextAlignment(TextAlignment.JUSTIFY);
        
        presentationText.setX(50); 
        presentationText.setY(90);
        
        title2.setText("Authors"); 
        title2.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20)); 
        //setting the position of the text 
        title2.setX(50); 
        title2.setY(160);
        
        contribText.setText("This app has been made as part of the IT project of FISE2 class in Télécom Saint-Etienne. Here is a list of the main contributors to this project :\n\n Developers : \n - Coue Mélodie \n - Hidalgo Sarah\n - Errami Fatima Ezzahrae \n - Tardy Xavier\n - Chemani Nabil\n - Pontiggia Valentin \n\nProfessors :\n - Product Owner : Alimoussa Mohammed\n - Scrum Master : Peyrard Frédéric\n - Java expert : Jean Drevet Pierre\n - Image expert : Alimoussa Mohammed");
        contribText.setWrappingWidth(500);
        contribText.setTextAlignment(TextAlignment.JUSTIFY);
        
        contribText.setX(50); 
        contribText.setY(190);
        
        title3.setText("Technologies"); 
        title3.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20)); 
        //setting the position of the text 
        title3.setX(50); 
        title3.setY(470);
        
        technoText.setText("We made this app with Java, using the OpenCV library and the JavaFX framework.");
        technoText.setWrappingWidth(500);
        technoText.setTextAlignment(TextAlignment.JUSTIFY);
        
        technoText.setX(50); 
        technoText.setY(490);
        
        vb.getChildren().add(title);
        vb.getChildren().add(presentationText);
        vb.getChildren().add(title2);
        vb.getChildren().add(contribText);
        vb.getChildren().add(title3);
        vb.getChildren().add(technoText);  
        
        //Setting title to the Stage 
        stage.setTitle("About Learning Mode"); 
           
        //Adding scene to the stage 
        stage.setScene(scene); 
        
        s.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    vb.setLayoutY(-new_val.doubleValue());
            }
        });
        
        //Displaying the contents of the stage 
        stage.show(); 
     }
    /**
     * @param actionEvent
     * Terminating the main thread
     */
    public void quit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
