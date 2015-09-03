
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class ButtonGrid {
	
	private MineVBox vbox = new MineVBox(); 
	private int numberOfRows; 
	private int numberOfColumns; 
	private boolean marioDeployed;
	private double difficultyFactor; 
	private int numMines; 
	private Set<MineButton> clickedButtons = new HashSet<MineButton>();
	
	public Scene init(int numRows, int numCols, int buttonWidth, int buttonHeight, double difficulty){
		Group root = new Group(); 
		
		Scene myScene = new Scene(root, Color.WHITE);
		
		numberOfRows = numRows;
		numberOfColumns = numCols;
		difficultyFactor = difficulty;
		numMines = (int) ((numberOfRows*numberOfColumns)*difficultyFactor);
		
		for(int row = 0; row < numRows; row++){
			MineHBox hbox = new MineHBox(); 
			for(int col = 0; col < numCols; col++){
				MineButton button = new MineButton(row, col); 
				button.setPrefHeight(buttonHeight);
				button.setPrefWidth(buttonWidth);
				hbox.getChildren().add(button);
			}
			vbox.getChildren().add(hbox);
		}
		root.getChildren().add(vbox);
		return myScene;
	}
	
	public void step(double elapsedTime){
		
		
		// find out which tiles are selected; 
		IntegerIntegerPair rowColNumPair = findPositionOfClickedMineButton(); 
//		Integer[] array = new Integer[]{rowColNumPair.x, rowColNumPair.y};
		
		if(rowColNumPair != null){
//			System.out.printf("%d, %d\n", rowColNumPair.x, rowColNumPair.y);
			clickedButtons.add(this.getMineButton(rowColNumPair.x, rowColNumPair.y));
			if(clickedButtons.size() == 1){
				marioDeployed = true;
				generateMinefield(rowColNumPair.x, rowColNumPair.y);
			}
			
		}
		
		for(MineButton button : clickedButtons){
			keepMineButtonEnabled(button);
		}
		
		
		
	}
	
	
	public void generateMinefield(int rowIndex, int colIndex){
		placeMines(rowIndex, colIndex);
		calculateHints();
	}
	
	public void placeMines(int row, int col){
		int minesPlaced = 0; 
		Random random = new Random();
		
		while(minesPlaced < numMines){
			int x = random.nextInt(numberOfRows);
			int y = random.nextInt(numberOfColumns);
			
			if(!getMineButton(x, y).isMine() && (x != row || y != col)){
				getMineButton(x, y).SetMine();
				minesPlaced++;
			}
		}
		System.out.println("Done placing mines");
	}
	
	public void calculateHints(){
		for(int i = 0; i < numberOfRows; i++){
			for(int j = 0; j < numberOfColumns; j++){
				if(!this.getMineButton(i, j).isMine()){
					int mines = minesNear(i, j);
					this.getMineButton(i, j).setMineNeighbors(mines);
					this.getMineButton(i, j).displayMineNeighbors();
				}
			}
		}
	}
	
	public int minesNear(int row, int col){
		int neighborMines = 0; 
		if(getMineButton(row - 1, col - 1) != null && getMineButton(row - 1, col - 1).isMine()){
			neighborMines++; 
		}
		if(getMineButton(row - 1, col) != null && getMineButton(row - 1, col).isMine()){
			neighborMines++; 
		}
		if(getMineButton(row - 1, col + 1) != null && getMineButton(row - 1, col + 1).isMine()){
			neighborMines++; 
		}
		if(getMineButton(row, col - 1) != null && getMineButton(row, col - 1).isMine()){
			neighborMines++; 
		}
		if(getMineButton(row, col + 1) != null && getMineButton(row, col + 1).isMine()){
			neighborMines++; 
		}
		if(getMineButton(row + 1, col - 1) != null && getMineButton(row + 1, col - 1).isMine()){
			neighborMines++; 
		}
		if(getMineButton(row + 1, col) != null && getMineButton(row + 1, col).isMine()){
			neighborMines++; 
		}
		if(getMineButton(row + 1, col + 1) != null && getMineButton(row + 1, col + 1).isMine()){
			neighborMines++; 
		}
		
		
		return neighborMines;
	}
	
	
	
	private void keepMineButtonEnabled(MineButton button){
		button.setSelected(true);
	}
	
	public IntegerIntegerPair findPositionOfClickedMineButton(){
		for(int i = 0; i < numberOfRows; i++){
			for(int j = 0; j < numberOfColumns; j++){
				if(this.getMineButton(i, j).isSelected() && !clickedButtons.contains(getMineButton(i, j))){
					IntegerIntegerPair Pair = new IntegerIntegerPair(i, j);
					return Pair;
				}
			}
		}
		return null; 
		
	}
	
	
	public MineButton getMineButton(int rowIndex, int colIndex){
		
//		if(rowIndex >= numberOfRows || colIndex >= numberOfColumns || rowIndex < 0 || colIndex < 0){
//			throw new IndexOutOfBoundsException(); 
//		}
		
		for(Node hbox : vbox.getChildren()){
			MineHBox hboxCast = (MineHBox) hbox; 
			for(Node button : hboxCast.getChildren()){
				MineButton buttonCast = (MineButton) button; 
				if(buttonCast.getRowIndex() == rowIndex && buttonCast.getColIndex() == colIndex){
//					System.out.println(rowIndex);
//					System.out.println(colIndex);
					return buttonCast;
					
				}
			}
		}
		return null;
		
	}
	

	
	
}
