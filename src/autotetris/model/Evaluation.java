package autotetris.model;

import com.sun.org.apache.xpath.internal.operations.Mod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

// Gives statistics about a given final board state
public class Evaluation {
    private int maxHeight; // minimize
	private int totalHeight; // minimize
	private int completeLines; // maximize
	private int holes; // minimize
	private int heightVariance; // minimize

	public Evaluation(boolean[][] board) {
		// treat potential as actual
		int[] heights = new int[Model.BOARD_WIDTH];
		Arrays.fill(heights, 0);

		int hits = 0; // Track how many top-blocks we've hit
        totalHeight = 0; // Track total height
        maxHeight = 0;
		for (int row = 0; row < Model.BOARD_HEIGHT; row++) {
		    int height = Model.BOARD_HEIGHT - row;
			// find minimum value of col such that board[col][row], since top is 0
			for (int col = 0; col < Model.BOARD_WIDTH; col++) {
				if (board[row][col] && heights[col] == 0) {
                    heights[col] = height;
                    totalHeight += height;
                    if(height > maxHeight)
                        maxHeight = height;
                    hits++;


                    // If we have everything, exit early
                    if(hits == Model.BOARD_WIDTH) {
                        row = Model.BOARD_HEIGHT;
                        break;
                    }
                }
			}
		}

		// Count how many lines are full
        completeLines = 0;
        for (int row = 0; row < Model.BOARD_HEIGHT; row++) {
            int col;
            for (col = 0; col < Model.BOARD_WIDTH; col++) {
                if (!board[row][col])
                    break;
            }
            if (col == Model.BOARD_WIDTH)
                completeLines++;
        }

        // Measure hole variance
		holes = 0;
		for (int row = 1; row < Model.BOARD_HEIGHT; row++) {
			for (int x = 0; x < Model.BOARD_WIDTH; x++) {
				if (board[row][x] == false && board[row-1][x] == true)
				    holes++;
			}
		}

		// Measure variance
        heightVariance = 0;
		for (int x = 1; x < Model.BOARD_WIDTH; x++) {
		    double dh = heights[x] - heights[x-1];
			heightVariance += dh*dh;
		}
	}

	public String toString() {
		return String.format("Evaluation[%d,%d,%d,%d]", totalHeight, completeLines, holes, heightVariance);
	}

    public int getTotalHeight() {
        return totalHeight;
    }

    public int getCompleteLines() {
        return completeLines;
    }

    public int getHoles() {
        return holes;
    }

    public int getHeightVariance() {
        return heightVariance;
    }

    public int getMaxHeight() {
        return maxHeight;
    }
}
