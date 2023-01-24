package tse.fise2.image3.cardmatcher;


import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tse.fise2.image3.cardmatcher.controller.StartController;


/**
 * The main class of the application.
 * Extends the JavaFx Application class and overrides the start method.
 * It loads the FXML file, sets the scene and shows the stage.
 * It also loads the native library required by openCV
 */

public class App extends Application {

	/**
     * This method is called when the application is launched.
     * It sets up the primary stage and loads the FXML file.
     * @param primaryStage The primary stage of the application
     * @throws InvocationTargetException if an exception is thrown by the underlying method
     */
	
    @Override
    public void start(Stage primaryStage) throws InvocationTargetException {
        try {

            URL fxmlLocation = getClass().getResource("controller/view/start.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);

            VBox root = (VBox) loader.load();
            StartController controller = loader.getController();
            Scene scene =  new Scene(root);

            primaryStage.setTitle("CardMatcher");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("icons/CardMatcherLogo.png")));
            primaryStage.show();
        }
        catch (Exception e) {
            // generic exception handling
            e.printStackTrace();
        }
    }

    /**
     * The main method of the application
     * @param args the innate argument for main method.
     */
    
    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }
}
