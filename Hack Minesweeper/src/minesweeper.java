import javax.swing.JOptionPane;


public class minesweeper {
	public final static int PLAYING = 0;
	public final static int DEFEAT = 1;
	public final static int VICTORY = 2;
	public final static double cellSize = 45;
	//MIN is 4; MAX is 11
	public static int MAX_ROWS = 9;
	public static int MAX_COLUMNS = 9;	
	public static double PROBABILITY = .1;
	
	public static boolean playAgain = true;
	
	
	public static void main(String[] args) {
		runGame();
		while(playAgain) {
			playAgain = false;
			willrestartGame();
		}
	}
	
	public static void runGame() {
		Board minesweeperBoard = new Board(MAX_ROWS,MAX_COLUMNS, PROBABILITY);
		int gameStatus = PLAYING;
		
		minesweeperBoard.drawBoard();
		
		while(gameStatus == PLAYING) {
			//end game to new game
			willrestartGame();
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
	
	public static void willrestartGame() {
		if(StdDraw.getPlayAgainValue()) {
			StdDraw.gameRestarting();
			runGame();
		}
	}
}
