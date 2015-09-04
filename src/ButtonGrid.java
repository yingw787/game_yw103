
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
	private boolean gameWon;
	private boolean gameLost;
	public int marioLocationX;
	public int marioLocationY;
	private double difficultyFactor;
	private int numMines;
	private Set<MineButton> clickedButtons = new HashSet<MineButton>();

	public Scene init(int numRows, int numCols, int buttonWidth, int buttonHeight, double difficulty) {
		Group root = new Group();

		Scene myScene = new Scene(root, Color.WHITE);

		numberOfRows = numRows;
		numberOfColumns = numCols;
		difficultyFactor = difficulty;
		numMines = (int) ((numberOfRows * numberOfColumns) * difficultyFactor);
		gameWon = false;

		// make a grid of vboxes and hboxes to put buttons in
		for (int row = 0; row < numRows; row++) {
			MineHBox hbox = new MineHBox();
			for (int col = 0; col < numCols; col++) {
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

	public void step(double elapsedTime) {

		// keep the selected buttons down so they can't be clicked anymore
		if (!clickedButtons.isEmpty()) {
			for (MineButton button : clickedButtons) {
				button.setSelected(true);
			}
		}

		// find out which new buttons have been selected;
		MineButton button = checkForClickedButtons();
		if (button != null)
			exploreMinefield(button);
		checkGameState(button);

	}

	public MineButton checkForClickedButtons() {

		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				MineButton button = this.getMineButton(i, j);
				if (button != null && button.isSelected() && !clickedButtons.contains(button)) {
					clickedButtons.add(button);
					button.setSelected(true);
					return button;
				}
			}
		}
		return null;
	}

	public void exploreMinefield(MineButton button) {
		// if the game is starting off and you click the first button, then you
		// need to initialize the minefield

		if (button != null) {
			if (button.isMine()) {
				// losing condition; handle in separate method
				button.displayMine();
				checkGameState(button);
			}

			if (clickedButtons.size() == 1) {
				generateMinefield(button.rowIndex, button.colIndex);
			}

			cascadedExplore(button);
		}

		// check if the tile clicked is a mine;

	}

	public void checkGameState(MineButton button) {

		// MAKE SURE TO END THE GAME STATE
		if (button != null) {
			if (button.isMine()) {
				gameLost = true;
				System.out.println("Game Lost!");
			}

			if ((numberOfRows * numberOfColumns - clickedButtons.size()) == numMines) {
				gameWon = true;
				System.out.println("Game Won!");
			}
		}
	}

	public void cascadedExplore(MineButton button) {
		Queue<MineButton> cascadeExploredTiles = new PriorityQueue<MineButton>();
		if (button != null) {
			button.displayMineNeighbors();
			cascadeExploredTiles.add(button);
		}

		while (!cascadeExploredTiles.isEmpty()) {
			MineButton tempButton = cascadeExploredTiles.remove();
			if (tempButton.mineNeighbors == 0) {

				Set<MineButton> set = getLegitimateAdjacentTiles(tempButton.rowIndex, tempButton.colIndex);

				for (MineButton b : set) {

//					cascadeExploredTiles.add(b); // DO NOT UNCOMMENT THIS LINE,
													// THIS WILL BREAK THE CODE
					b.setSelected(true); // uncover them
					b.displayMineNeighbors(); // display mineNeighborNumbers
				}
			}
		}
	}

	// MINEFIELD GENERATION METHODS START HERE

	public void generateMinefield(int rowIndex, int colIndex) {
		placeMines(rowIndex, colIndex);
		calculateHints();
	}

	public void placeMines(int row, int col) {
		int minesPlaced = 0;
		Random random = new Random();

		while (minesPlaced < numMines) {
			int x = random.nextInt(numberOfRows);
			int y = random.nextInt(numberOfColumns);

			if (!getMineButton(x, y).isMine() && (x != row || y != col)) {
				getMineButton(x, y).SetMine();
				// getMineButton(x, y).setColor("red");
				minesPlaced++;
			}
		}
		// System.out.println("Done placing mines");
	}

	public void calculateHints() {
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				if (!this.getMineButton(i, j).isMine()) {
					Integer mines = minesNear(i, j);
					this.getMineButton(i, j).setMineNeighbors(mines);
					// this.getMineButton(i, j).setText(mines.toString());
				}
			}
		}
		// System.out.println("Done placing hints");
	}

	public int minesNear(int row, int col) {
		int neighborMines = 0;
		Set<MineButton> set = getLegitimateAdjacentTiles(row, col);
		for (MineButton button : set) {
			if (button.isMine())
				neighborMines++;
		}
		return neighborMines;
	}

	private Set<MineButton> getLegitimateAdjacentTiles(int row, int col) { // not
																			// null
																			// tiles
		Set<MineButton> set = new HashSet<MineButton>();

		for (int i = row - 1; i < row + 2; i++) {
			for (int j = col - 1; j < col + 2; j++) {
				MineButton tempButton = getMineButton(i, j);
				if (tempButton != null) {
					if (!tempButton.equals(getMineButton(row, col)))
						set.add(tempButton);
				}
			}
		}
		return set;
	}

	public MineButton getMineButton(int rowIndex, int colIndex) {

		if (rowIndex >= numberOfRows || colIndex >= numberOfColumns || rowIndex < 0 || colIndex < 0)
			return null;

		for (Node hbox : vbox.getChildren()) {
			MineHBox hboxCast = (MineHBox) hbox;
			for (Node button : hboxCast.getChildren()) {
				MineButton buttonCast = (MineButton) button;
				if (buttonCast.getRowIndex() == rowIndex && buttonCast.getColIndex() == colIndex)
					return buttonCast;
			}
		}
		return null;
	}

}
