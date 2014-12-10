import javax.swing.JOptionPane;


public class minesweeper {
	//game status
	public final static int PLAYING = 0;
	public final static int DEFEAT = 1;
	public final static int VICTORY = 2;
	//game board
	public final static int DEFAULT_ROWS = 9;
	public final static int DEFAULT_COLUMNS = 9;	
	public final static double DEFAULT_PROBABILITY = .1;
	//User Settings
	public final static String[] SIZE_CHOICES = {"4x4","5x5","6x6","7x7","8x8","9x9","10x10","11x11","12x12","13x13","14x14"};
	public final static String[] PROB_CHOICES = {"10%", "20%","30%","40%","50%","60%", "70%","80%","90%"};

	public static int gameRows = DEFAULT_ROWS;
	public static int gameColumns = DEFAULT_COLUMNS;
	public static double gameProb = DEFAULT_PROBABILITY;


	public static void main(String[] args) {
		runGame();

		checkButtonsAtEnd();
	}

	public static void runGame() {
		int gameStatus = PLAYING;
		Board minesweeperBoard = new Board(gameRows, gameColumns, gameProb);

		while(gameStatus == PLAYING) {
			checkButtons();

			updateGameForMouseInput(minesweeperBoard);

			gameStatus = minesweeperBoard.getGameStatus();
		}

		showEndGameMessage(gameStatus, minesweeperBoard);
	}

	public static void showEndGameMessage(int gameStatus, Board minesweeperBoard) {
		if(gameStatus == DEFEAT) {
			JOptionPane.showMessageDialog(StdDraw.frame, "You lose!");
			minesweeperBoard.revealBoard();
		} else if (gameStatus == VICTORY) {
			JOptionPane.showMessageDialog(StdDraw.frame, "You won!");
		}
	}

	public static void updateGameForMouseInput(Board minesweeperBoard) {
		if(StdDraw.mouseReleased()) {
			minesweeperBoard.updateBoardByMouse(StdDraw.mouseX(), StdDraw.mouseY(), StdDraw.getIsFlagged());
			StdDraw.codeIsDone();
		}

	}

	public static void checkButtons() {
		if(isPlayAgainButton()) {
			StdDraw.gameRestarting();
			runGame();
		} else if(isSizeSettingButton()) {
			sizeUpdated();
		} else if(isProbSettingButton()) {
			probabilityUpdated();
		}
	}

	public static void checkButtonsAtEnd() {
		boolean gameOverScreenShown = true;

		while(gameOverScreenShown){
			gameOverScreenShown = false;
			checkButtons();
		}
	}

	public static void sizeUpdated() {
		StdDraw.sizeDone();

		String sizeSetting = (String)JOptionPane.showInputDialog(
				StdDraw.frame,
				"Pick which board you want:",
				"Customize size of board",
				JOptionPane.PLAIN_MESSAGE,
				null,
				SIZE_CHOICES,
				gameRows + "x" + gameColumns);

		getUserSetting(sizeSetting, "x", true);

		runGame();
	}

	public static void probabilityUpdated() {
		StdDraw.probabilityDone();

		String probSetting = (String)JOptionPane.showInputDialog(
				StdDraw.frame,
				"Pick which board you want:",
				"Customize probability of bombs",
				JOptionPane.PLAIN_MESSAGE,
				null,
				PROB_CHOICES,
				gameProb * 100 + "%");

		getUserSetting(probSetting, "%", false);

		runGame();
	}

	public static void getUserSetting(String userSetting, String breakPoint, boolean sizeSetting) {
		int result = 0;

		if ((userSetting != null) && (userSetting.length() > 0)) {
			result = Integer.parseInt(userSetting.substring(0, userSetting.indexOf(breakPoint)));

			updateGameSettings(sizeSetting, result);

			runGame();
		}
	}

	public static void updateGameSettings(boolean sizeSetting, int result) {
		if(sizeSetting) {
			gameRows = result;
			gameColumns = gameRows;
		} else {
			gameProb = (double) result / 100;
		}
	}

	public static boolean isPlayAgainButton() { return StdDraw.getPlayAgainValue(); }
	public static boolean isSizeSettingButton() { return StdDraw.isSizeShown(); }
	public static boolean isProbSettingButton() { return StdDraw.isProbabilityShown(); }
}
