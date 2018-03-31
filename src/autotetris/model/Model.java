package autotetris.model;

import java.awt.Color;
import java.util.*;

public class Model {
	public static final int BOARD_WIDTH = 10, BOARD_HEIGHT = 6, MAX_DEBOUNCE_TICKS = 8;
	public static final double DROP_PER_TICK = 0.01;
	
	boolean board[][];
	int piecesDropped, rowScore, debounce = 0;
	ArrayList<TetrominoPrototype> allPieces;
	Tetromino fallingPiece;
	boolean speedy = false, gameOver = false;

	public Model() {
		board = new boolean[BOARD_WIDTH][BOARD_HEIGHT];
		allPieces = new ArrayList<TetrominoPrototype>();
		
		try {
			initializePieces();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setupGameState();
	}

	public boolean[][] getBoard() {
		return board;
	}
	
	public Tetromino getFallingPiece() {
		return fallingPiece;
	}
	
	public TetrominoPrototype getNextPiece() {
		return allPieces.get(piecesDropped % allPieces.size());
	}

	public void setSpeedyMode(boolean mode) {
		this.speedy = mode;
	}
	
	public int getScore() {
		return rowScore * BOARD_WIDTH * 2 + ((piecesDropped - 1) / 2);
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	public void setupGameState() {
		fallingPiece = null;
		gameOver = false;
		piecesDropped = 0;
		rowScore = 0;
		for (int i = 0; i < BOARD_WIDTH; i++)
			for (int j = 0; j < BOARD_HEIGHT; j++)
				board[i][j] = false;
		Collections.shuffle(allPieces);
	}
	
	public int tick() throws InterruptedException {
		// Check for rows
		int rowsToEliminate = 0;
		for (int y = BOARD_HEIGHT - 1; y >= 0; y--) {
			boolean andRow = true;
			for (int x = 0; x < BOARD_WIDTH; x++)
				andRow &= board[x][y];
			if (andRow)
				rowsToEliminate++;
			else
				break;
		}
		if (rowsToEliminate == BOARD_HEIGHT) {
			for (int x = 0; x < BOARD_WIDTH; x++)
				Arrays.fill(board[x], false);
		} else if (rowsToEliminate > 0) {
			for (int x = 0; x < BOARD_WIDTH; x++) {
				for (int y = BOARD_HEIGHT - 1; y >= rowsToEliminate; y--)
					board[x][y] = board[x][y - rowsToEliminate];
				for (int y = 0; y < rowsToEliminate; y++)
					board[x][y] = false;
			}
		}
		rowScore += rowsToEliminate * (rowsToEliminate + 1) / 2;
		
		// Drop a piece
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
							board[pieceX + x][pieceY - y - 1] = true;
						}
					}
				}
				fallingPiece = null;
			} else fallingPiece.move(0, DROP_PER_TICK);
				
		}
		return (int)((speedy ? 100 : 400) * DROP_PER_TICK);
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
	
	private boolean moveConflict(int dx, double dy, int direction) {
		if (fallingPiece == null) return false;
		
		boolean[][] tempBoard = new boolean[BOARD_WIDTH][BOARD_HEIGHT];
		
		int newOrientation = (fallingPiece.orientation + direction) % fallingPiece.prototype.orientations.length;
		while (newOrientation < 0) newOrientation += fallingPiece.prototype.orientations.length;
		
		for (int i = 0; i < BOARD_WIDTH; i++)
			for (int j = 0; j < BOARD_HEIGHT; j++)
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
							board[pieceX + x][pieceY - y])
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
