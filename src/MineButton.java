import javax.swing.text.View;

import javafx.scene.control.ToggleButton;

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
	
	public int getRowIndex(){
		return rowIndex; 
	}
	
	public int getColIndex(){
		return colIndex; 
	}
	
	public void SetMine(){
		isMine = true; 
		this.setStyle("-fx-background-color: slateblue");
	}
	
	public boolean isMine(){
		return isMine; 
	}
	
	public void displayMine(){
		this.setText("X");
	}
	
	public void setMineNeighbors(int numberOfNearbyMines){
		mineNeighbors = numberOfNearbyMines;
	}
	
	public void displayMineNeighbors(){
		this.setText(mineNeighbors.toString());
	}
	
	
}
