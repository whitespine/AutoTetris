package autotetris.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Model {
    public static final int BOARD_WIDTH = 10, BOARD_HEIGHT = 22;
    public static final int MS_PER_DROP = 500; // How long the human is given to drop.
    public static final int ACTIONS_PER_DROP = 8; // How many keypresses the AI can do per drop.

	// Represents score state
	private int piecesDropped;
    private int rowScore;
    private boolean gameOver = false;

    // Represents occupied spaces on the board.
    public boolean board[][]; // True = full

	// Handles generating new pieces
	private PieceGenerator pieceGen;

	// The current falling piece
	private Tetromino fallingPiece;

    // Timing
    private long lastDown = 0; // Last time piece was moved down



	////////////////////////////////////// Main Logic /////////////////////////////////////////////////////

	public Model() {
		board = new boolean[BOARD_HEIGHT][BOARD_WIDTH];
        this.pieceGen = new PieceGenerator(TetrominoPrototype.initializePieces());
		
		setupGameState();
	}

	public Model(Model copy) {
	    // Duplicate the board
        this.board = copy.board.clone();
        for(int row=0; row<BOARD_HEIGHT; row++) {
            this.board[row] = copy.board[row].clone();
        }

        // The score:
        this.rowScore = copy.rowScore;
        this.piecesDropped = copy.piecesDropped;
        this.gameOver = copy.gameOver;
        this.lastDown = copy.lastDown;

        // Duplicate the piecegen
        this.pieceGen = new PieceGenerator(copy.pieceGen);

        // Tetromino:
        this.fallingPiece = new Tetromino(copy.fallingPiece);
    }

	public Tetromino getFallingPiece() {
		return fallingPiece;
	}

	public TetrominoPrototype getNextPiece() {
		return pieceGen.peekNext();
	}
	
	public int getScore() {
		return rowScore * 100;
	}

	public int getTotalDrops() {
	    return piecesDropped;
    }

	public boolean isGameOver() {
		return gameOver;
	}

    public void setupGameState() {
        fallingPiece = null;
        gameOver = false;
        piecesDropped = 0;
        rowScore = 0;
        for (int i = 0; i < BOARD_HEIGHT; i++)
            for (int j = 0; j < BOARD_WIDTH; j++)
                board[i][j] = false;
        lastDown = System.currentTimeMillis();
        dropNewPiece();
    }

    // Returns which cells are expected to be filled when we bottom out.
    public ArrayList<Cell> getExpectedDrop() {
	    if(fallingPiece == null)
	        return new ArrayList<>();

	    // Go to bottom
        boolean moved = false;
	    Tetromino tCopy = new Tetromino(fallingPiece);
	    while(allFree(tCopy.project())) {
	        moved = true;
	        tCopy.move(Cell.DOWN);
        }

        //Undo last move if necessary
        if(moved)
            tCopy.move(Cell.UP);

        // Return projected spaces.
        return tCopy.project();
    }

    // Return which cells of getExpectedDrop to render by blotting those in fallingPiece
    public ArrayList<Cell> getPredictedCells() {
	    if(fallingPiece == null)
	        return new ArrayList<>();

        ArrayList<Cell> fPiece = fallingPiece.project();
        ArrayList<Cell> predicted = new ArrayList<>();
        for(Cell c : getExpectedDrop()) {
            if(fPiece.contains(c))
                continue;
            else
                predicted.add(c);
        }
        return predicted;
    }



    //////////////////////////////////// GAME API, ESSENTIALLY /////////////////////////////////////////////////////

    // Reminds the model to force a drop if necessary
    public void tick() {
        // Lower piece if we have one. Make a new piece if we don't
        if (System.currentTimeMillis() - lastDown > MS_PER_DROP) {
            tryMoveFallingPiece(Cell.DOWN, true);
        }
    }

    public void doAction(Action a) {
        switch (a) {
            case Down:
                tryMoveFallingPiece(Cell.DOWN, true);
                break;
            case Left:
                tryMoveFallingPiece(Cell.LEFT, false);
                break;
            case Right:
                tryMoveFallingPiece(Cell.RIGHT, false);
                break;
            case Spin:
                tryRotateFallingPiece(Rotation.CLOCKWISE);
                break;
            case Drop:
                drop();
                break;
        }
    }



    //////////////////////////////////// GAME LOGIC /////////////////////////////////////////////////////
    // Tries to clear all rows, if possible.
    private void tryClear() {
        int clearCount = 0;

        // Map indices
        int cursor = BOARD_HEIGHT-1;
        for(int row=BOARD_HEIGHT-1; row >= 0; row--, cursor--) {
            while (isRowFilled(cursor)) {
                cursor--;
                clearCount++;
            }
            if(cursor != row)
                copyRow(cursor, row);
        }

        // Perform rewrite
        rowScore += (clearCount * clearCount); // Quadratic for more rewarding big clears
    }


    // When a piece bottoms out, solidify it
    private void finalizeDrop() {
        // Fill in the projection of the piece.
        // If any are above row 0 (top), u lose
        for(Cell c : fallingPiece.project()) {
            if(c.row < 0) {
                gameOver = true;
            } else {
                //Otherwise just set to be filled
                board[c.row][c.col] = true;
            }
        }

        // Do clears if possible
        tryClear();

        // We're done with this faller - get a new one
        if(!gameOver)
            dropNewPiece();
    }

    private void dropNewPiece() {
        fallingPiece = new Tetromino(pieceGen.getNext(), new Cell(0, BOARD_WIDTH / 2));
        piecesDropped += 1;
        lastDown = System.currentTimeMillis();

        if(piecesDropped % 10000 == 0) {
            System.out.print(" " + piecesDropped / 10000);
        }
    }



    //////////////////////////////////// MOVEMENT LOGIC /////////////////////////////////////////////////////

    private boolean tryMoveFallingPiece(Cell deltaPosition, boolean isDown) {
        /*
		Returns true if moving the falling piece in a given direction succeeds.
		If isDown, has extra logic that finalizes the piece if movement fails.
		 */
        if(fallingPiece == null)
            return false;

        // Make a copy
        Tetromino tClone = new Tetromino(fallingPiece);

        // Move it
        tClone.move(deltaPosition);

        // If it's fine, keep it
		if (allFree(tClone.project())) {
            fallingPiece = tClone;
            // Update timer if we went down
            if(isDown)
                lastDown = System.currentTimeMillis();
            return true;
        } else {
		    if(isDown)
		        finalizeDrop();
		    return false;
        }
	}

	// Returns true if succesfully rotates
	private boolean tryRotateFallingPiece(Rotation rotation) {
        /*
		Returns true if rotating the falling piece in a given direction succeeds
		 */
        if(fallingPiece == null)
            return false;

        // Make a copy
        Tetromino tClone = new Tetromino(fallingPiece);

        // Move it
        tClone.rotate(rotation);

        // If it's fine, keep it
        if (allFree(tClone.project())) {
            fallingPiece = tClone;
            return true;
        } else {
            return false;
        }
    }

    private void drop() {
        while (tryMoveFallingPiece(Cell.DOWN, true));
    }



    //////////////////////////////////// SPACE-FILLING LOGIC /////////////////////////////////////////////////////

    // Returns true if all listed cells are free
    protected boolean allFree(ArrayList<Cell> positions) {
        for(Cell c : positions) {
            if(!isCellFree(c, true))
                return false;
        }
        return true;
    }

    // Gives true if te given row (from base) is filled
    // Gives false if its an invalid row
    private boolean isRowFilled(int row) {
	    if(row < 0 || row >= BOARD_HEIGHT)
	        return false;
        for(int col = 0; col < BOARD_WIDTH; col++) {
            if (!board[row][col])
                return false;
        }
        return true;
    }

    // Wipes the specified row, and moves all rows above, down
    private void copyRow(int from, int to) {
	    if(from < 0)
            Arrays.fill(board[to], false);
	    else
            System.arraycopy(board[from], 0, board[to], 0, BOARD_WIDTH);
    }

    // Returns true if a cell is in the board, and open.
    // If "allowTop" given, considers above the board to be free.
	protected boolean isCellFree(Cell c, boolean allowTop) {
	    if(allowTop && c.row < 0)
	        return c.col >= 0 && c.col < BOARD_WIDTH; // Only care that its within bounds
	    else
	        return      c.row >= 0 && c.row < BOARD_HEIGHT
                    &&  c.col >= 0 && c.col < BOARD_WIDTH
                    && !board[c.row][c.col];
    }
}

