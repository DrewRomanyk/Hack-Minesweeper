
public class Board {
	public final static int PLAYING = 0;
	public final static int DEFEAT = 1;
	public final static int VICTORY = 2;

	public final static double cellSize = 45;

	private Cell[][] cell;
	private int columns;
	private int rows;
	private int gameStatus;
	private int bombsInGame;
	private int flaggedBombs;
	private int cellsInGame;
	private int revealedCells;
	private boolean firstPlay;


	public Board(int columns, int rows, double probablityOfBomb) {
		firstPlay = true;
		this.columns = columns;
		this.rows = rows;

		//Create Bombs
		cell = new Cell[rows][columns];
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < columns; c++) {
				cell[r][c] = new Cell(probablityOfBomb, r, c);
				
				if(cell[r][c].isBomb()) {
					bombsInGame++;
				}
			}
		}
		cellsInGame = rows * columns;

		//Set neighbor
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < columns; c++) {
				setNeighborValue(cell[r][c]);
			}
		}

	}

	public void setNeighborValue(Cell tgtCell) {
		if(!tgtCell.isBomb()) {
			int neighborringBombs = 0;

			for(int row = tgtCell.getRow() - 1; row <= tgtCell.getRow() + 1; row++) {
				for(int column = tgtCell.getColumn() - 1; column <= tgtCell.getColumn() + 1; column++) {
					if(validNeighbor(row, column, tgtCell)) {
						neighborringBombs++;
					}
				}
			}

			tgtCell.setValue(neighborringBombs);
		}
	}

	public boolean inbounds(int row, int column) {
		return (0 <= row && row < rows && 0 <= column && column < columns);
	}

	public boolean validNeighbor(int r, int c, Cell tgtCell) {
		return (inbounds(r, c) && cell[r][c].isBomb());
	}

	public String getCellValue(int r, int c) {
		if(cell[r][c].isBomb()) {
			return " * ";
		} else {
			return " " + cell[r][c].getValue() + " ";
		}
	}

	@Override
	public String toString() {
		String overall = "";

		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < columns; c++) {
				if(cell[r][c].isBomb()) {
					overall += " * ";
				} else {
					overall += " " + cell[r][c].getValue() + " ";
				}
			}
			overall += "\n";
		}

		return overall;
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

		if(flaggedBombs == bombsInGame && cellsInGame == (flaggedBombs + revealedCells)) {
			gameStatus = VICTORY;
		}
	}

	public Cell findClickedCell(double mouseX, double mouseY) {
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < columns; c++) {
				if(isClickedCell(mouseX, mouseY, cell[r][c])) {
					return cell[r][c];
				}
			}
		}

		return null;
	}

	public boolean isClickedCell(double mouseX, double mouseY, Cell tgtCell) {
		return ((mouseX >= (tgtCell.getPosX() - (cellSize / 1000))) && (mouseX <= (tgtCell.getPosX() + (cellSize / 1000))) &&
				(mouseY >= (tgtCell.getPosY() - (cellSize / 1000))) && (mouseY <= (tgtCell.getPosY() + (cellSize / 1000))));
	}
	
	public void flagCell(Cell tgtCell) {
		if(!tgtCell.isRevealed()) {
			
			if(tgtCell.isFlagged()) {
				if(tgtCell.isBomb()) {
					flaggedBombs--;
				}
				tgtCell.setFlagged(false);
				
				StdDraw.setPenColor(StdDraw.BOOK_BLUE);
				StdDraw.filledRectangle(tgtCell.getPosX(), tgtCell.getPosY(), cellSize/1000, cellSize/1000);
			} else {
				if(tgtCell.isBomb()) {
					flaggedBombs++;
				}
				tgtCell.setFlagged(true);
				
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.filledRectangle(tgtCell.getPosX(), tgtCell.getPosY(), cellSize/1000, cellSize/1000);
			}
		}
	}

	public void revealCell(Cell tgtCell) {
		if((!tgtCell.isBomb() && !tgtCell.isRevealed() && !tgtCell.isFlagged() && !firstPlay) || (tgtCell.isBomb() && firstPlay)) {
			updateRevealedCell(tgtCell);
			revealedCells++;

			if(tgtCell.getValue() == 0) {
				for(int r = tgtCell.getRow() - 1; r <= tgtCell.getRow() + 1; r++) {
					for(int c = tgtCell.getColumn() - 1; c <= tgtCell.getColumn() + 1; c++) {
						if(inbounds(r, c)) {
							revealCell(cell[r][c]);
						}
					}
				}
			}
		} else if (tgtCell.isBomb()) {
			gameStatus = DEFEAT;
		}
	}

	public void updateRevealedCell(Cell tgtCell) {
		tgtCell.setRevealed(true);

		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.filledRectangle(tgtCell.getPosX(), tgtCell.getPosY(), cellSize/1000, cellSize/1000);

		StdDraw.setPenColor(StdDraw.BOOK_BLUE);
		StdDraw.rectangle(tgtCell.getPosX(), tgtCell.getPosY(), cellSize/1000, cellSize/1000);
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
		StdDraw.setCanvasSize(rows * 55, columns * 55);
		StdDraw.setPenColor(StdDraw.BOOK_BLUE);
		//StdDraw.filledCircle(.5, .5, .1);


		//double posX = 1.0/columns;
		//double posY =  1 - (1.0/rows);
		double posX = .05;
		double posY = .95;
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < columns; c++) {
				StdDraw.filledRectangle(posX, posY, cellSize/1000, cellSize/1000);
				//StdDraw.rectangle(posX, posY, cellSize/1000, cellSize/1000);
				//StdDraw.text(posX, posY, getCellValue(r, c));
				cell[r][c].setPosX(posX);
				cell[r][c].setPosY(posY);
				posX = (.05 + (c + 1.0)/columns);
			}

			posX = .05;
			posY = (.95 - ((r + 1.0)/rows));
		}
	}
}
