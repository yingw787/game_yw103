import javax.swing.text.View;

import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MineButton extends ToggleButton{

	public int colIndex; 
	public int rowIndex; 
	private boolean isMine; 
	public Integer mineNeighbors; 
	public boolean triggeredButton; 
	
	public MineButton(int row, int col) {
		rowIndex = row; 
		colIndex = col; 
		isMine = false; 
	}

	public void onClick(View v){
		triggeredButton = true; 
		this.setSelected(true);
		
	}
	
	public void reset(){
		isMine = false;
		mineNeighbors = 0; 
		triggeredButton = false;
	}
	
	public int getRowIndex(){
		return rowIndex; 
	}
	
	public int getColIndex(){
		return colIndex; 
	}
	
	public void SetMine(){
		isMine = true; 
		//this.setStyle("-fx-background-color: slateblue");
	}
	
	public boolean isMine(){
		return isMine; 
	}
	
	public void displayMine(){
		this.setText("X");
	}
	
	public void setMineNeighbors(int numberOfNearbyMines){
		if(this != null){
			mineNeighbors = numberOfNearbyMines;
		}
	}
	
	public void displayMineNeighbors(){
		if(this != null){
			this.setText(mineNeighbors.toString());
		}
	}
	
	public void hitMine(){
		this.setStyle("-fx-background-color: red");
	}
	
	public void createMario(){
//		Image marioImage = new Image(getClass().getClassLoader().getResourceAsStream("mario.png"));
//		this.setGraphic(new ImageView(marioImage));
		// CANT LAYER IN TIME, just make Mario a color
		
		this.setStyle("-fx-background-color: slateblue");
		
		
	}
	
}
