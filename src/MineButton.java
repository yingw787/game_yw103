import javafx.scene.control.ToggleButton;

public class MineButton extends ToggleButton{

	public int colIndex; 
	public int rowIndex; 
	private boolean isMine; 
	public Integer mineNeighbors; 
	
	public MineButton(int row, int col) {
		rowIndex = row; 
		colIndex = col; 
		isMine = false; 
	}
	
	public void reset(){
		isMine = false;
		mineNeighbors = 0; 
	}
	
	public int getRowIndex(){
		return rowIndex; 
	}
	
	public int getColIndex(){
		return colIndex; 
	}
	
	public void SetMine(){
		isMine = true; 
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
	
	public void setColor(String color){
		this.setStyle("-fx-background-color: " + color);
	}
	
}
