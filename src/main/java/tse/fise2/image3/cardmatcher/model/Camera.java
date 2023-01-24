package tse.fise2.image3.cardmatcher.model;



import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tse.fise2.image3.cardmatcher.sift.Descriptor;
import tse.fise2.image3.cardmatcher.sift.Sift;
import tse.fise2.image3.cardmatcher.util.Audio;


/**
 * This class captures video from a camera and processes the frames to get their descriptors. 
 * It allows to starts the video capture from the specified camera and runs a scheduled task
 * that grabs frames regularly. The frame is then processed and displayed on the GUI. The 
 * code also includes features for automatically detecting and recognizing cards using the 
 * SIFT algorithm and ImageUtil class.
 *
 */

public abstract class Camera{

	// A timer for acquiring the video stream
	private ScheduledExecutorService timer;
	// The OpenCV object that realizes the video capture
	private VideoCapture capture = new VideoCapture();
	// A flag to change the button behavior
	private boolean cameraActive = false;
	// The id of the camera to be used
	private static int cameraId = 0;
	// Learning mode
	private boolean learningmode;
	// Testing mode
	private boolean testingmode;
	private Boolean auto;

	private Card card= new Card();
	public Image best_image;

	//
	private Label label = new Label();
	//
	private Mat frame = new Mat();
	private ImageView imagedetection;
	private ImageView imagedetection2;
	private ImageView imagedetection3;


	public void openCamera(ImageView crframe, Button btn) throws InterruptedException, IOException {
		if (!this.cameraActive)
		{
			this.capture.open(cameraId);

			// If the video stream is available
			if (this.capture.isOpened())
			{
				this.cameraActive = true;

				// Grabs a frame every 33 ms (30 frames/sec)
				Runnable frameGrabber = new Runnable() {

					@Override
					public void run()
					{
						// Grab and process a single frame
						frame = grabFrame();
						Scalar scalar = new Scalar(182, 74, 108);
						if (learningmode) {
							// Definition of a red rectangle for learning mode
							scalar= new Scalar(225, 114, 141);
						}
						else{
							// Definition of a green rectangle for learning mode
							scalar= new Scalar(255, 224, 185);
						}
						String SE = System.getProperty("os.name").toLowerCase();
						// Change size of the rectangle according to OS.
						if (SE.indexOf("win") >= 0) {
							Imgproc.rectangle(frame,new Point(200, 80), new Point(440, 400),scalar, 1);
						}
						else {
							Imgproc.rectangle(frame,new Point(200, 50), new Point(600, 650),scalar, 1);
						}

						MatOfByte buffer1 = new MatOfByte();
						Imgcodecs.imencode(".png", frame, buffer1);
						Image imageToShow = new Image(new ByteArrayInputStream(buffer1.toArray()));
						Platform.runLater(new Runnable() {
							@Override public void run() {
								crframe.setImage(imageToShow);
							}
						});
					}
				};

				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

				if (this.learningmode || this.testingmode) {
					btn.setText("Capture");
				}

				Stage stage =(Stage)(btn.getScene().getWindow());
				stage.setOnCloseRequest((new EventHandler<WindowEvent>() {
					public void handle(WindowEvent we)
					{
						try {
							stopAcquisition();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}));
			}
			else
			{
				System.err.println("Impossible to open the camera connection...");
			}
		}


		else
		{
			// The camera is not active at this point
			this.cameraActive = false;

			this.stopAcquisition();
			if (this.learningmode || this.testingmode) {
				btn.setText("Restart");
				Audio.play_sound(getClass().getResource("media/shot_sound.wav"));
				// Name the capture and save it in a folder
				this.showInputTextDialog();
				AddImageDetection(crframe);
			}
		}
	}

	/**
	 *  Add the ImageView for displaying the image detection.
	 * @param det_frame ImageView object for displaying the image detection
	 */

	public void AddImageDetection(ImageView  det_frame)
	{

		imagedetection = det_frame;
	}

	/**
	 *  Add the ImageView for displaying the 2nd image detection.
	 * @param det_frame ImageView object for displaying the 2nd image detection
	 */

	public void AddImageDetection2(ImageView  det_frame)
	{

		imagedetection2 = det_frame;
	}

	/**
	 *  Add the ImageView for displaying the 3rd image detection.
	 * @param det_frame ImageView object for displaying the 3rd image detection
	 */

	public void AddImageDetection3(ImageView  det_frame)
	{

		imagedetection3 = det_frame;
	}

	/**
	 *  Sets the testing mode.
	 * @param testingmode flag for testing mode
	 */

	public void setTestingmode(boolean testingmode) {
		this.testingmode = testingmode;
	}

	/**
	 *  Saves the image of the card.
	 */

	public abstract void saveImage( ) throws IOException;

	/**
	 *  Returns the Card object.
	 * @return  the Card object
	 */

	public Card getCard() {
		return card;
	}

	/**
	 *  Sets the Card object.
	 * @param card the Card object to be set
	 */

	public void setCard(Card card) {
		this.card = card;
	}

	/**
	 *  Show input text dialog to get the name of the picture
	 *  @throws InterruptedException when the thread can not wake up from the Thread.sleep method.
	 *  @throws IOException in case of issue with input or output between the app and the user.
	 */

	private void showInputTextDialog() throws InterruptedException, IOException {
		if(learningmode) {
			TextInputDialog dialog = new TextInputDialog("Write here");
			dialog.setTitle("Save picture");
			dialog.setHeaderText("Enter the name of the picture ");
			dialog.setContentText("Name:");

			Optional<String> result = dialog.showAndWait();

			result.ifPresent(name -> {
				card.setName(name);
				this.label.setText(name);
				try {
					this.saveImage();
				} catch (IOException e) {
					e.printStackTrace();
				}

			});

		}else if(testingmode)
		{
			Thread.sleep(2000);

			Rect rectCrop = new Rect(new Point(202, 82), new Point(438, 398));
			Mat crop_frame = new Mat(getFrame(),rectCrop);

			Descriptor desc = Sift.getDescriptor(crop_frame, "");
			// Name of the three cards with the best proximity score
			List<ScoreImage> l_scoreImage= Sift.getTop3ImageBestScore(desc.getDescriptor());
			desc.setImageName(l_scoreImage.get(0).getImageName());
			setDescCard(desc);
			card.setName(l_scoreImage.get(0).getImageName());


			//MsgUtil.DisplayMsg("this card belongs to class "+card.getName()+" with the proximity score  "+l_scoreImage.get(0).getScore() );
			this.saveImage();

			InputStream stream = null;
			InputStream stream2 = null;
			InputStream stream3 = null;
			try {

				String userHome = System.getProperty("user.dir"); // return c:\Users\${current_user_name}
				String folder = userHome + "/apprentissage";
				stream = new FileInputStream(folder+"/"+l_scoreImage.get(0).getImageName()+".png");
				stream2= new FileInputStream(folder+"/"+l_scoreImage.get(1).getImageName()+".png");
				stream3= new FileInputStream(folder+"/"+l_scoreImage.get(2).getImageName()+".png");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			imagedetection.setImage( new Image(stream));
			imagedetection2.setImage( new Image(stream2));
			imagedetection3.setImage( new Image(stream3));
		}

	}

	/**
	 *  Grabs a single frame from the video capture.
	 *  @return the grabbed frame
	 */

	private Mat grabFrame() {
		Mat frame = new Mat();

		if (this.capture.isOpened()) {
			try {
				this.capture.read(frame);
			} catch (Exception e) {
				System.err.println("Exception during the image elaboration: " + e);
			}
		}
		return frame;

	}

	/**
	 *  Returns the Label object.
	 * @return  the Label object
	 */

	public Label getLabel() {
		return label;
	}

	/**
	 *  Returns the Mat object for the current frame.
	 * @return  the Mat object for the current frame
	 */

	public Mat getFrame() {
		return frame;
	}


	/**
	 *  Stops the video capture and releases the camera.
	 *  @throws InterruptedException if interrupted while waiting.
	 */

	public void stopAcquisition() throws InterruptedException {
		if (this.timer != null && !this.timer.isShutdown()) {
			try {
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
				this.capture.release();
			} catch (InterruptedException e) {
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
		}
		if (this.capture.isOpened()) {
			// release the camera
			this.capture.release();
		}
	}

	/**
	 * Returns whether the camera is active or not.
	 * @return true if the camera is active, false otherwise
	 */

	public boolean isCameraActive() {
		return cameraActive;
	}

	/**
	 * Sets the camera active status.
	 * @param cameraActive true if the camera is active, false otherwise
	 */

	public void setCameraActive(boolean cameraActive) {
		this.cameraActive = cameraActive;
	}


	/**
	 * Returns whether the learning mode is active or not.
	 * @return true if the learning mode is active, false otherwise
	 */

	public boolean isLearningmode() {
		return learningmode;
	}

	/**
	 * Sets the learning mode status.
	 * @param learningmode true if the learning mode is active, false otherwise
	 */

	public void setLearningmode(boolean learningmode) {
		this.learningmode = learningmode;
	}

	/**
	 * Sets the descriptor for the card.
	 * @param descCard the descriptor of the card
	 */

	public void setDescCard(Descriptor descCard) {
	}

	/**
	 * returns whether the auto mode is on or off
	 * @return true if the auto mode is on, false otherwise
	 */

	public Boolean isAuto()
	{
		return auto;
	}
}

