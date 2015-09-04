import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application{

	public static void main(String[] args){
		launch(args);
	}
	
	private ButtonGrid myButtonGrid;
	private static int numRows = 10; 
	private static int numCols = 10; 
	private static int buttonWidth = 25; 
	private static int buttonHeight = 15;
	private static double difficultyFactor = 0.05; // between 0 and 1 
	
	public static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	
	public void start(Stage stage) {
		
		myButtonGrid = new ButtonGrid();
		stage.setTitle("Mario Meets Minesweeper!");
		
		Scene scene = myButtonGrid.init(numRows, numCols, buttonWidth, buttonHeight, difficultyFactor);
		
		
//		myButtonGrid.generateMinefield(numMines);
		
		
		
		stage.setScene(scene);
		stage.show();
		
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                e -> myButtonGrid.step(SECOND_DELAY));
		Timeline animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}

}
