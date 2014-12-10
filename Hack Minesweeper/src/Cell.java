
public class Cell {
	private final static int BOMB = -1;
	private static double cellSize; //in percentage

	private boolean reveal;
	private int value; // -1 is bomb, then the rest is the amount of neighbors
	private boolean flagged;
	private int row;
	private int column;
	private double posX;
	private double posY;

	public Cell(double probablityOfBomb, int row, int column) {
		reveal = false;
		value = setBomb(probablityOfBomb);
		this.row = row;
		this.column = column;

	}

	public double getCellSize() { return cellSize; }

	public void setCellSize(double cellSize) { Cell.cellSize = cellSize; }

	public boolean isRevealed() {
		return reveal;
	}

	public void setRevealed(boolean reveal) {
		this.reveal = reveal;
	}

	public boolean isFlagged() {
		return flagged;
	}

	public void setFlagged(boolean flagged) {
		this.flagged = flagged;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getRow() { return row; }

	public int getColumn() { return column; }

	public double getPosX() { return posX; }

	public double getPosY() { return posY; }

	public void setPosX(double posX) { this.posX = posX; }

	public void setPosY(double posY) { this.posY = posY; }

	private int setBomb(double probablityOfBomb) {
		return (probablityOfBomb > Math.random()) ? BOMB : 0;
	}

	public boolean isBomb() { return (value == BOMB); }

}