package autotetris.model;

import java.awt.Color;
import java.util.*;

public class Model {
	public static final int BOARD_WIDTH = 10, BOARD_HEIGHT = 22;
	
	boolean board[][];
	int piecesDropped, rowScore;
	ArrayList<TetrominoPrototype> allPieces;
	Tetromino fallingPiece;

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
	
	public void setupGameState() {
		fallingPiece = null;
		piecesDropped = 0;
		rowScore = 0;
		for (int i = 0; i < BOARD_WIDTH; i++)
			Arrays.fill(board[i], false);
		Collections.shuffle(allPieces);
	}
	
	public void tick() {
		if (piecesDropped % allPieces.size() == 0)
			Collections.shuffle(allPieces);
		
		if (fallingPiece == null) {
			fallingPiece = new Tetromino(getNextPiece(), BOARD_WIDTH / 2, BOARD_HEIGHT);
		} else {
			fallingPiece.move(0, -0.01);
		}
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
