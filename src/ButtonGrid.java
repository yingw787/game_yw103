
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
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
		checkForClickedButtons();
		exploreMinefield();
	
	}
	
	public void checkForClickedButtons(){
		IntegerIntegerPair rowColNumPair = findPositionOfClickedMineButton(); 		
		if(rowColNumPair != null){
//			System.out.printf("%d, %d\n", rowColNumPair.x, rowColNumPair.y);
			clickedButtons.add(this.getMineButton(rowColNumPair.x, rowColNumPair.y));
			if(clickedButtons.size() == 1){ // if beginning to explore the minefield with first click
				initializeGame(rowColNumPair.x, rowColNumPair.y);
			}	
		}
	}
	
	public void initializeGame(int x, int y){
		marioDeployed = true; // Mario has been deployed
		deployMario(x, y);
		generateMinefield(x, y); // dynamically generate the minefield 
	}
	
	public void deployMario(int x, int y){
		this.getMineButton(x, y).createMario();
	}
	
	public void exploreMinefield(){
		for(MineButton button : clickedButtons){
			keepMineButtonEnabled(button);
			if(button.isMine()){
				button.displayMine();
				button.hitMine();
			}
			else{
				Queue<MineButton> cascadeExploredTiles = new PriorityQueue<MineButton>();
				button.displayMineNeighbors();
				cascadeExploredTiles.add(button);
				
				while(!cascadeExploredTiles.isEmpty()){
					MineButton tempButton = cascadeExploredTiles.remove();
					if(tempButton.mineNeighbors == 0){
						
						Set<MineButton> set = getLegitimateAdjacentTiles(tempButton.rowIndex, tempButton.colIndex);
						for(MineButton b: set){
//							cascadeExploredTiles.add(b); // DO NOT UNCOMMENT THIS LINE, THIS WILL BREAK THE CODE
							b.setSelected(true); // uncover them
							b.displayMineNeighbors(); // display mineNeighborNumbers
						}
					}
				}
				
			}
		}
	}
	
	// resets the minefield 
	public void clearMinefield(){
		for(int i = 0; i < numberOfRows; i++){
			for(int j = 0; j < numberOfColumns; j++){
				this.getMineButton(i, j).reset();
			}
		}
	}
	
	// generates the complete minefield dynamically upon the first click
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
//		System.out.println("Done placing mines");
	}
	
	public void calculateHints(){
		for(int i = 0; i < numberOfRows; i++){
			for(int j = 0; j < numberOfColumns; j++){
				if(!this.getMineButton(i, j).isMine()){
					int mines = minesNear(i, j);
					this.getMineButton(i, j).setMineNeighbors(mines);
				}
			}
		}
//		System.out.println("Done placing hints");
	}
	
	public int minesNear(int row, int col){
		int neighborMines = 0; 
		
		Set<MineButton> set = getLegitimateAdjacentTiles(row, col);
		for(MineButton button: set){
			if(button.isMine())
				neighborMines++;
		}
		
		return neighborMines;
	}
	
	private Set<MineButton> getLegitimateAdjacentTiles(int row, int col){ // not null tiles
		Set<MineButton> set = new HashSet<MineButton>();
		
		MineButton tempButton1 = getMineButton(row - 1, col - 1);
		MineButton tempButton2 = getMineButton(row - 1, col);
		MineButton tempButton3 = getMineButton(row - 1, col + 1);
		MineButton tempButton4 = getMineButton(row, col - 1);
		MineButton tempButton5 = getMineButton(row, col + 1);
		MineButton tempButton6 = getMineButton(row + 1, col - 1);
		MineButton tempButton7 = getMineButton(row + 1, col);
		MineButton tempButton8 = getMineButton(row + 1, col + 1);

		if(tempButton1 != null)
			set.add(tempButton1);
		if(tempButton2 != null)
			set.add(tempButton2);
		if(tempButton3 != null)
			set.add(tempButton3);
		if(tempButton4 != null)
			set.add(tempButton4);
		if(tempButton5 != null)
			set.add(tempButton5);
		if(tempButton6 != null)
			set.add(tempButton6);
		if(tempButton7 != null)
			set.add(tempButton7);
		if(tempButton8 != null)
			set.add(tempButton8);
		
		return set;
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
		
		if(rowIndex >= numberOfRows || colIndex >= numberOfColumns || rowIndex < 0 || colIndex < 0){
			return null;
		}
		
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
