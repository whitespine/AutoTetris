package autotetris.model;

import java.util.*;

import javax.swing.JOptionPane;

import autotetris.view.Application;

public class Model {
    public int MS_PER_DROP = 500;
	public static final int BOARD_WIDTH = 20, BOARD_HEIGHT = 22;

	// Represents occupied spaces on the board.
	public boolean board[][]; // True = full

	// Represents scorestate
	int piecesDropped, rowScore;
    private boolean gameOver = false;

	// Handles generating new pieces
	private PieceGenerator pieceGen;

	// The current falling piece
	private Tetromino fallingPiece;

	// The AI
	private TetrisSolver ai;
	public static TetrisTrainer trainer = new TetrisTrainer();

    // Timing
    private long lastDown = 0; // Last time piece was moved down

	////////////////////////////////////// Main Logic /////////////////////////////////////////////////////

	public Model() {
		board = new boolean[BOARD_HEIGHT][BOARD_WIDTH];
		trainer.iterations = 500;
		ai = new TetrisSolver();

        this.pieceGen = new PieceGenerator(TetrominoPrototype.initializePieces());
		
		setupGameState();
	}

	public Tetromino getFallingPiece() {
		return fallingPiece;
	}

	public TetrominoPrototype getNextPiece() {
		return pieceGen.peekNext();
	}
	
	public int getScore() {
		return rowScore * BOARD_WIDTH + ((piecesDropped - 1) / 2);
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
        dropPiece();
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

    // Gets this board's state if you just hit space like, right now.
    public boolean[][] getPredictedBoardState() {
        // Duplicate the board
	    boolean[][] ret = new boolean[BOARD_WIDTH][];
        for(int row=0; row<BOARD_HEIGHT; row++)
            ret[row] = board[row].clone();

        // Slot in predicts
        for(Cell c : getExpectedDrop())
            ret[c.row][c.col] = true;
        return ret;
    }

    //////////////////////////////////// STEPPING GAME STATE /////////////////////////////////////////////////////
    // Tries to clear all rows, if possible.
    private void tryClear() {
        // Clear rows
        int clearCount = 0;
        for(int r=0; r < BOARD_HEIGHT; r++) {
            if (isRowFilled(r)) {
                clearRow(r);
                rowScore++;

                // Do the same r again
                r--;
            }
        }
        rowScore += (clearCount * (clearCount+1)); // Quadratic for more rewarding big clears
    }

    // Reminds the model to force a drop if necessary
    public void tick() {
        // Lower piece if we have one. Make a new piece if we don't
        if (System.currentTimeMillis() - lastDown > MS_PER_DROP) {
            tryMoveFallingPiece(Cell.DOWN, true);
        }
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
        dropPiece();
    }

    private void dropPiece() {
        fallingPiece = new Tetromino(pieceGen.getNext(), new Cell(0, BOARD_WIDTH / 2));
        piecesDropped += 1;
        lastDown = System.currentTimeMillis();
    }

    public void drop() {
        while (tryMoveFallingPiece(Cell.DOWN, true));
    }

	//////////////////////////////////// AI SHIT /////////////////////////////////////////////////////

	public int getTrainingIterations() {
		return trainer.iterations;
	}

	public static void setTrainingIterations(int trainingIterations) {
		Model.trainer.iterations = trainingIterations;
	}
	
	public TetrisSolver getSolver() {
		return ai;
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


    //////////////////////////////////// MOVEMENT LOGIC /////////////////////////////////////////////////////
    public boolean tryMoveFallingPiece(Cell deltaPosition, boolean isDown) {
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
	public boolean tryRotateFallingPiece(Rotation rotation) {
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

    // Returns true if all listed cells are free
    private boolean allFree(ArrayList<Cell> positions) {
	    for(Cell c : positions) {
	        if(!isCellFree(c, true))
	            return false;
        }
        return true;
    }

    //////////////////////////////////// SPACE-FILLING LOGIC /////////////////////////////////////////////////////

    // Gives true if te given row (from base) is filled
    private boolean isRowFilled(int row) {
        for(int col = 0; col < BOARD_WIDTH; col++) {
            if (!board[row][col])
                return false;
        }
        return true;
    }

    // Wipes the specified row, and moves all rows above, down
    private void clearRow(int row) {
        // Do all except topmost row
	    for(; row > 0; row--) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (row == 0)
                    board[row][col] = false;
                else
                    board[row][col] = board[row - 1][col];
            }
        }
    }

    // Returns true if a cell is in the board, and open.
    // If "allowTop" given, considers above the board to be free.
	private boolean isCellFree(Cell c, boolean allowTop) {
	    if(allowTop && c.row < 0)
	        return c.col >= 0 && c.col < BOARD_WIDTH; // Only care that its within bounds
	    else
	        return      c.row >= 0 && c.row < BOARD_HEIGHT
                    &&  c.col >= 0 && c.col < BOARD_WIDTH
                    && !board[c.row][c.col];
    }

}

