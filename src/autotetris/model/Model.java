package autotetris.model;

import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import autotetris.view.Application;

public class Model {
	public static final int BOARD_WIDTH = 20, BOARD_HEIGHT = 22, MAX_DEBOUNCE_TICKS = 8;
	double DROP_PER_TICK = 0.01;
	
	BoardItem board[][];
	int piecesDropped, rowScore, debounce = 0;
	ArrayList<TetrominoPrototype> allPieces;
	Tetromino fallingPiece;
	boolean gameOver = false;
	TetrisSolver ai;
	public static TetrisTrainer trainer = new TetrisTrainer();

	public Model() {
		board = new BoardItem[BOARD_HEIGHT][BOARD_WIDTH];
		allPieces = new ArrayList<TetrominoPrototype>();
		trainer.iterations = 500;
		ai = new TetrisSolver();
		
		try {
			initializePieces();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setupGameState();
	}

	public BoardItem[][] getBoard() {
		return board;
	}
	
	public Tetromino getFallingPiece() {
		return fallingPiece;
	}
	
	public TetrominoPrototype getNextPiece() {
		return allPieces.get(piecesDropped % allPieces.size());
	}
	
	public int getScore() {
		return rowScore * BOARD_WIDTH + ((piecesDropped - 1) / 2);
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	public int getTrainingIterations() {
		return trainer.iterations;
	}

	public static void setTrainingIterations(int trainingIterations) {
		Model.trainer.iterations = trainingIterations;
	}
	
	public TetrisSolver getSolver() {
		return ai;
	}

	public void setupGameState() {
		fallingPiece = null;
		gameOver = false;
		piecesDropped = 0;
		rowScore = 0;
		for (int i = 0; i < BOARD_HEIGHT; i++)
			for (int j = 0; j < BOARD_WIDTH; j++)
				board[i][j] = BoardItem.Open;
	}
	
	public int tick() throws InterruptedException {
		// Clear rows
		List<Boolean> filledRows = Arrays.stream(board).map(row -> 
				Arrays.stream(row).map(it -> it == BoardItem.Filled).reduce(true, (a, b) -> a && b).booleanValue()).collect(Collectors.toList());
		if (filledRows.contains(true)) {
			int rowMove = 0;
			for (int y = BOARD_HEIGHT - 1; y >= 0; y--) {
				if (filledRows.get(y))
					rowMove++;
				if (y >= rowMove)
					board[y] = Arrays.copyOf(board[y - rowMove], board[y - rowMove].length);
			}
			for (int i = 0; i < rowMove; i++)
				Arrays.fill(board[i], BoardItem.Open);
			rowScore += rowMove * (rowMove + 1);
		}
		
		// Drop piece
		if (fallingPiece == null) {
			fallingPiece = new Tetromino(getNextPiece(), BOARD_WIDTH / 2, 0);
			if (piecesDropped++ % allPieces.size() == 0)
				Collections.shuffle(allPieces);
		} else {
			if (moveConflict(0, DROP_PER_TICK, 0)) {
				Thread.sleep(10);
				int pieceX = fallingPiece.x, pieceY = (int)Math.ceil(fallingPiece.y + DROP_PER_TICK),
						pieceSize = fallingPiece.prototype.orientations[0].length;
				boolean[][] currentOrientation = fallingPiece.prototype.orientations[fallingPiece.orientation];
				for (int x = 0; x < pieceSize; x++) {
					for (int y = 0; y < pieceSize; y++) {
						if (currentOrientation[y][x]) {
							if (pieceY - y - 1 < 0) {
								gameOver = true;
								return -1;
							}
							board[pieceY - y - 1][pieceX + x] = BoardItem.Filled;
						}
					}
				}
				fallingPiece = null;
			} else fallingPiece.move(0, DROP_PER_TICK);
				
		}
		return (int)(400 * DROP_PER_TICK);
	}
	
	public BoardItem[][] generateRolloutBoard(BoardItem[][] cBoard, Tetromino piece) {
		if (cBoard == null) {
			cBoard = new BoardItem[BOARD_HEIGHT][BOARD_WIDTH];
			for (int i = 0; i < BOARD_HEIGHT; i++)
				for (int j = 0; j < BOARD_WIDTH; j++)
					cBoard[i][j] = board[i][j];
		}
		if (piece == null)
			return cBoard;
		int pieceX = fallingPiece.x, pieceY = (int)Math.ceil(fallingPiece.y),
				pieceSize = fallingPiece.prototype.orientations[0].length;
		boolean[][] currentOrientation = fallingPiece.prototype.orientations[fallingPiece.orientation];
		
		// drop piece until collision
		int dy = 0;
		while (!moveConflict(0, dy, 0)) dy++;
		pieceY += dy;

		for (int y = 0; y < pieceSize; y++) {
			for (int x = 0; x < pieceSize; x++) {
				if (currentOrientation[y][x]) {
					if (pieceY - y - 1 >= 0 && pieceY - y - 1 < BOARD_HEIGHT)
						cBoard[pieceY - y - 1][pieceX + x] = BoardItem.Potential;
				}
			}
		}
		return cBoard;
	}
	
	public void applyCurrentRolloutBoard() {
		BoardItem[][] roBoard = this.generateRolloutBoard(board, fallingPiece);
		for (int i = 0; i < roBoard.length; i++)
			for (int j = 0; j < roBoard[i].length; j++)
				if (roBoard[i][j] == BoardItem.Potential)
					roBoard[i][j] = BoardItem.Filled;
		fallingPiece = null;
	}
	
	public boolean moveFallingPiece(int dx, double dy, int direction) {
		if (moveConflict(dx, dy, direction))
			return false;
		else {
			fallingPiece.move(dx, dy);
			fallingPiece.rotate(direction);
			return true;
		}
	}

	public void train(Application app) {
		Model self = this;
		new Thread() {
			public void run() {
				TetrisSolver ts = Model.trainer.train();
				if (ts != null)
					self.ai = ts;
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						JOptionPane.showMessageDialog(app, 
								ts == null ? "Solver could not be trained." : "Solver was successfully trained.", "Training Status", 
								ts == null ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
						app.enableControls();
					}
				});
			}
		}.start();
	}
	
	private boolean moveConflict(int dx, double dy, int direction) {
		if (fallingPiece == null) return false;
		
		BoardItem[][] tempBoard = new BoardItem[BOARD_HEIGHT][BOARD_WIDTH];
		
		int newOrientation = (fallingPiece.orientation + direction) % fallingPiece.prototype.orientations.length;
		while (newOrientation < 0) newOrientation += fallingPiece.prototype.orientations.length;
		
		for (int i = 0; i < BOARD_HEIGHT; i++)
			for (int j = 0; j < BOARD_WIDTH; j++)
				tempBoard[i][j] = board[i][j];
		
		int pieceX = fallingPiece.x + dx, pieceY = (int)Math.ceil(fallingPiece.y + dy),
				pieceSize = fallingPiece.prototype.orientations[0].length;
		
		
		for (int x = 0; x < pieceSize; x++) {
			for (int y = 0; y < pieceSize; y++) {
				if (fallingPiece.prototype.orientations[newOrientation][y][x]) {
					// Check for collision with left and right sides and the bottom
					if ((pieceX + x) < 0 || (pieceX + x) >= BOARD_WIDTH || (pieceY - y) >= BOARD_HEIGHT)
						return true;
					// Check for collision with other pieces on the board
					if ((pieceX + x) >= 0 && (pieceX + x) < BOARD_WIDTH && 
							(pieceY - y) >= 0 && (pieceY - y) < BOARD_HEIGHT && 
							board[pieceY - y][pieceX + x] == BoardItem.Filled)
						return true;
				}
			}
		}
		
		return false;
	}
	
	private void initializePieces() throws Exception {
		TetrominoPrototype I = new TetrominoPrototype("I", Color.CYAN, new boolean[][]{
					{false, false, true, false}, 
					{false, false, true, false}, 
					{false, false, true, false}, 
					{false, false, true, false}}),
				J = new TetrominoPrototype("J", Color.BLUE, new boolean[][]{
					{false, true, true}, 
					{false, true, false}, 
					{false, true, false}}),
				L = new TetrominoPrototype("L", Color.ORANGE, new boolean[][]{
					{false, true, false}, 
					{false, true, false}, 
					{false, true, true}}),
				O = new TetrominoPrototype("O", Color.YELLOW, null), // special case
				S = new TetrominoPrototype("S", Color.GREEN, new boolean[][]{
					{false, true, true}, 
					{true, true, false}, 
					{false, false, false}}),
				T = new TetrominoPrototype("T", new Color(128, 0, 255), new boolean[][]{
					{false, true, false}, 
					{true, true, true}, 
					{false, false, false}}),
				Z = new TetrominoPrototype("Z", Color.RED, new boolean[][]{
					{true, true, false}, 
					{false, true, true}, 
					{false, false, false}});
		
		boolean[][] o_orientation = new boolean[][]{{true, true}, {true, true}};
		for (int i = 0; i < 4; i++)
			O.orientations[i] = o_orientation;
		
		allPieces.add(I);
		allPieces.add(J);
		allPieces.add(L);
		allPieces.add(O);
		allPieces.add(S);
		allPieces.add(T);
		allPieces.add(Z);
	}
}
