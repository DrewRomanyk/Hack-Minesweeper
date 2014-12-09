import javax.swing.JOptionPane;


public class minesweeper {
	public final static int PLAYING = 0;
	public final static int DEFEAT = 1;
	public final static int VICTORY = 2;
	public final static double cellSize = 45;
	//MIN is 6; MAX is 11; Need to optimize the creation of the cells and their placement for more
	public static int MAX_ROWS = 9;
	public static int MAX_COLUMNS = 9;	
	public static double PROBABILITY = .1;
	
	public static boolean playAgain = true;
	
	
	public static void main(String[] args) {
		runGame();
		while(playAgain) {
			playAgain = false;
			checkButtons();
		}
	}
	
	public static void runGame() {
		Board minesweeperBoard = new Board(MAX_ROWS,MAX_COLUMNS, PROBABILITY);
		int gameStatus = PLAYING;
		
		minesweeperBoard.drawBoard();
		
		while(gameStatus == PLAYING) {
			//can end game to new game
			checkButtons();
			//mouse button pressed
			if(StdDraw.mouseReleased()) {
				//System.out.println("Mouse X: " + StdDraw.mouseX() + " Mouse Y: " + StdDraw.mouseY());
				minesweeperBoard.updateBoardByMouse(StdDraw.mouseX(), StdDraw.mouseY(), StdDraw.getIsFlagged());
				
				StdDraw.codeIsDone();
				
				gameStatus = minesweeperBoard.getGameStatus();
				
				
			}
		}
		
		if(gameStatus == DEFEAT) {
			JOptionPane.showMessageDialog(StdDraw.frame, "You lose!");
			minesweeperBoard.revealBoard();
		} else if (gameStatus == VICTORY) {
			JOptionPane.showMessageDialog(StdDraw.frame, "You won!");
		}
		
		playAgain = true;
	}
	
	public static void checkButtons() {
		if(StdDraw.getPlayAgainValue()) {
			StdDraw.gameRestarting();
			runGame();
		} else if(StdDraw.isSizeShown()) {
			StdDraw.sizeDone();
			sizeUpdated();
		} else if(StdDraw.isProbabilityShown()) {
			StdDraw.probabilityDone();
			probabilityUpdated();
		}
	}
	
	public static void sizeUpdated() {
		Object[] possibilities = {"4x4","6x6", "7x7","8x8","9x9","10x10","11x11","12x12","13x13","14x14"};
		String s = (String)JOptionPane.showInputDialog(
		                    StdDraw.frame,
		                    "Pick which board you want:",
		                    "Customize size of board",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    possibilities,
		                    "9x9");

		//If a string was returned, say so.
		int numForRC = 9;
		if ((s != null) && (s.length() > 0)) {
			int breakPoint = s.indexOf("x");
		    System.out.println(s.substring(0,breakPoint));
		    numForRC = Integer.parseInt(s.substring(0,breakPoint));
		}
		MAX_COLUMNS = numForRC;
		MAX_ROWS = numForRC;
		runGame();
	}
	
	public static void probabilityUpdated() {
		Object[] possibilities = {"10%", "20%","30%","40%","50%","60%", "70%","80%","90%"};
		String s = (String)JOptionPane.showInputDialog(
		                    StdDraw.frame,
		                    "Pick which board you want:",
		                    "Customize probability of bombs",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    possibilities,
		                    "10%");

		//If a string was returned, say so.
		double numForProb = .1;
		if ((s != null) && (s.length() > 0)) {
			int breakPoint = s.indexOf("%");
		    System.out.println(s.substring(0,breakPoint));
		    numForProb = Double.parseDouble(s.substring(0,breakPoint)) / 100;
		}
		PROBABILITY = numForProb;
		runGame();
	}
}
