import java.awt.Color;


public class Board {
	public final static int PLAYING = 0;
	public final static int DEFEAT = 1;
	public final static int VICTORY = 2;
	
	public final static Color BURNT_ORANGE = new Color(191,87,0);

	public final static double CELL_SIZE = 45;
	public final static double BUFFER_SIZE = 10;
	public final static double MARGIN_PERCENT = .05;

	private Cell[][] cell;
	private int columns;
	private int rows;
	private int gameStatus;
	private int bombsInGame;
	private int flaggedBombs;
	private int cellsInGame;
	private int revealedCells;
	private boolean firstRound;


	public Board(int columns, int rows, double probOfBomb) {
		firstRound = true;
		this.columns = columns;
		this.rows = rows;
		cellsInGame = rows * columns;

		createBombs(probOfBomb);
		drawBoard();
	}
	
	public void createBombs(double probOfBomb) {
		cell = new Cell[rows][columns];
		
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < columns; c++) {
				cell[r][c] = new Cell(probOfBomb, r, c);
				
				if(cell[r][c].isBomb()) {
					bombsInGame++;
				}
			}
		}
	}

	public void setNeighborValue(Cell tgtCell) {
		if(!tgtCell.isBomb()) {
			int neighborringBombs = 0;

			for(int row = tgtCell.getRow() - 1; row <= tgtCell.getRow() + 1; row++) {
				for(int column = tgtCell.getColumn() - 1; column <= tgtCell.getColumn() + 1; column++) {
					if(neighborIsBomb(row, column, tgtCell)) {
						neighborringBombs++;
					}
				}
			}

			tgtCell.setValue(neighborringBombs);
		}
	}
	
	public void setAllNeighborValues() {
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < columns; c++) {
				setNeighborValue(cell[r][c]);
			}
		}
	}

	public boolean inbounds(int row, int column) {
		return (0 <= row && row < rows && 0 <= column && column < columns);
	}

	public boolean neighborIsBomb(int r, int c, Cell tgtCell) {
		return (inbounds(r, c) && cell[r][c].isBomb());
	}

	public String getCellValue(int r, int c) {
		if(cell[r][c].isBomb()) {
			return " * ";
		} else if (cell[r][c].getValue() == 0){ 
			return "   ";
		}else {
			return " " + cell[r][c].getValue() + " ";
		}
	}

	public void updateBoardByMouse(double mouseX, double mouseY, boolean flagMode) {
		Cell clickedCell = findClickedCell(mouseX, mouseY);
		if(clickedCell != null) {
			if (flagMode) {
				flagCell(clickedCell);
			} else {
				revealCell(clickedCell);
			}
		}

		gameStatus = updateGameStatus(gameStatus);
	}

	public Cell findClickedCell(double mouseX, double mouseY) {
		Cell clickedCell = null;
		
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < columns; c++) {
				if(isClickedCell(mouseX, mouseY, cell[r][c])) {
					clickedCell = cell[r][c];
				}
			}
		}

		return clickedCell;
	}

	public boolean isClickedCell(double mouseX, double mouseY, Cell tgtCell) {
		return ((mouseX >= (tgtCell.getPosX() - (tgtCell.getCellSize()))) && (mouseX <= (tgtCell.getPosX() + (tgtCell.getCellSize()))) &&
				(mouseY >= (tgtCell.getPosY() - (tgtCell.getCellSize()))) && (mouseY <= (tgtCell.getPosY() + (tgtCell.getCellSize()))));
	}
	
	public int updateGameStatus(int gameStatus) {

		if(flaggedBombs == bombsInGame && cellsInGame == (flaggedBombs + revealedCells)) {
			System.out.println("BITCHES!!!");
			gameStatus = VICTORY;
		}
		
		return gameStatus;
	}
	
	public void flagCell(Cell tgtCell) {
		if(!tgtCell.isRevealed()) {
			updateColorAndBombCount(tgtCell);
			tgtCell.setFlagged(!tgtCell.isFlagged());
			StdDraw.filledRectangle(tgtCell.getPosX(), tgtCell.getPosY(), tgtCell.getCellSize(), tgtCell.getCellSize());
		}
	}
	
	public void updateColorAndBombCount(Cell tgtCell) {
		if(tgtCell.isFlagged()) {
			if(tgtCell.isBomb()) { flaggedBombs--; }
			
			StdDraw.setPenColor(BURNT_ORANGE);
		} else {
			if(tgtCell.isBomb()) { flaggedBombs++; }
			
			StdDraw.setPenColor(StdDraw.RED);
		}
	}

	public void revealCell(Cell tgtCell) {
		if((!tgtCell.isBomb() && !tgtCell.isRevealed() && !tgtCell.isFlagged() && !firstRound) || (firstRound)) {
			firstRoundPlay(tgtCell);
			
			updateRevealedCell(tgtCell);
			revealedCells++;

			if(tgtCell.getValue() == 0) {
				revealNeighborCells(tgtCell);
			}
		} else if (tgtCell.isBomb()) {
			gameStatus = DEFEAT;
		}
	}
	
	public void firstRoundPlay(Cell tgtCell) {
		if(firstRound) {
			firstRound = false;
			setCellNotAsBomb(tgtCell);
			
			setAllNeighborValues();
		}
	}
	
	public void setCellNotAsBomb(Cell tgtCell) {
		tgtCell.setValue(0);
	}
	
	public void revealNeighborCells(Cell tgtCell) {
		for(int r = tgtCell.getRow() - 1; r <= tgtCell.getRow() + 1; r++) {
			for(int c = tgtCell.getColumn() - 1; c <= tgtCell.getColumn() + 1; c++) {
				if(inbounds(r, c)) {
					revealCell(cell[r][c]);
				}
			}
		}
	}

	public void updateRevealedCell(Cell tgtCell) {
		tgtCell.setRevealed(true);

		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.filledRectangle(tgtCell.getPosX(), tgtCell.getPosY(), tgtCell.getCellSize(), tgtCell.getCellSize());

		StdDraw.setPenColor(BURNT_ORANGE);
		StdDraw.rectangle(tgtCell.getPosX(), tgtCell.getPosY(), tgtCell.getCellSize(), tgtCell.getCellSize());
		StdDraw.text(tgtCell.getPosX(), tgtCell.getPosY(), getCellValue(tgtCell.getRow(), tgtCell.getColumn()));

	}

	public int getGameStatus() {
		return gameStatus;
	}

	public void revealBoard() {
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < columns; c++) {
				updateRevealedCell(cell[r][c]);
			}
		}
	}

	public void drawBoard() {
		double totalX = rows * (CELL_SIZE + BUFFER_SIZE);
		double totalY = columns * (CELL_SIZE + BUFFER_SIZE);
		double bufferSizePercent = (BUFFER_SIZE / totalX);
		double cellSizePercent = (CELL_SIZE / totalX);
		double posX = ((cellSizePercent + (bufferSizePercent / 2)/2)) - MARGIN_PERCENT;
		double posY = 1 - (MARGIN_PERCENT);
		
		//set Canvas up
		StdDraw.setCanvasSize((int) totalX,(int) totalY);
		StdDraw.setPenColor(BURNT_ORANGE);
		
		cell[0][0].setCellSize(cellSizePercent / 2);
		
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < columns; c++) {
				StdDraw.filledRectangle(posX, posY, cellSizePercent / 2, cellSizePercent / 2);
				cell[r][c].setPosX(posX);
				cell[r][c].setPosY(posY);
				
				posX += ((bufferSizePercent + cellSizePercent));
			}
			posX = ((cellSizePercent + (bufferSizePercent / 2)/2)) - MARGIN_PERCENT;
			posY -= ((bufferSizePercent + cellSizePercent));
		}
	}
}
