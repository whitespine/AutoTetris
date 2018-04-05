package autotetris.model;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Evaluation {
	public final int totalHeight; // minimize
	public final int completeLines; // maximize
	public final int holes; // minimize
	public final int heightVariance; // minimize
	
	public Evaluation(BoardItem[][] _board) {
		// treat potential as actual
		boolean[][] board = new boolean[Model.BOARD_HEIGHT][Model.BOARD_WIDTH];
		for (int y = 0; y < Model.BOARD_HEIGHT; y++)
			for (int x = 0; x < Model.BOARD_WIDTH; x++)
				board[y][x] = _board[y][x] != BoardItem.Open;
		
		int[] heights = new int[Model.BOARD_WIDTH];
		Arrays.fill(heights, Model.BOARD_HEIGHT);
		
		for (int y = 0; y < Model.BOARD_HEIGHT; y++) {
			// find minimum value of y such that board[y][x], since top is 0
			for (int x = 0; x < Model.BOARD_WIDTH; x++) {
				if (board[y][x] && heights[x] == Model.BOARD_HEIGHT) heights[x] = y;
			}
		}		
		heights = Arrays.stream(heights).map(ypos -> Model.BOARD_HEIGHT - ypos).toArray();
		
		totalHeight = Arrays.stream(heights).reduce(0, (a, b) -> a + b);

		completeLines = (int) IntStream.range(0, Model.BOARD_HEIGHT)
							.mapToObj(i -> board[i])
							.map(row -> IntStream.range(0, Model.BOARD_WIDTH).mapToObj(i -> row[i]).reduce(true, (a, b) -> a && b).booleanValue())
							.filter(isComplete -> isComplete)
							.count();
		
		int tHoles = 0, tHeightVariance = 0;
		for (int y = 1; y < Model.BOARD_HEIGHT; y++) {
			for (int x = 0; x < Model.BOARD_WIDTH; x++) {
				if (board[y][x] == false && board[y-1][x] == true) tHoles++;
			}
		}
		holes = tHoles;
		
		for (int x = 1; x < Model.BOARD_WIDTH; x++) {
			tHeightVariance += Math.abs(heights[x] - heights[x-1]);
		}
		heightVariance = tHeightVariance;
	}
	
	public String toString() {
		return String.format("Evaluation[%d,%d,%d,%d]", totalHeight, completeLines, holes, heightVariance);
	}
}
