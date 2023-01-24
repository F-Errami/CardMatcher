package tse.fise2.image3.cardmatcher.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tse.fise2.image3.cardmatcher.App;


/**
 * This class contains a method which is useful to display alerts to the user
 *
 */

public class MsgUtil {
	
	/**
	 * This method allows to open an Dialog box which display an alert message.
	 * @param msg the message to be displayed
	 */
	
    public static void DisplayMsg(String msg) {

        Alert a1 = new Alert(Alert.AlertType.NONE,
                msg, ButtonType.CLOSE);
        Stage stage = (Stage) a1.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(App.class.getResourceAsStream("icons/CardMatcherLogo.png"))); // To add an icon
        a1.setTitle("CardMatcher");
        // Show the dialog
        a1.show();
    }
}