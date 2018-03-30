package autotetris.model;

import java.awt.Color;
import java.util.Arrays;

public class TetrominoPrototype {
	public final String name;
	public final Color base;
	boolean[][][] orientations;
	
	public TetrominoPrototype(String name, Color base, boolean[][] defaultOrientation) throws Exception {
		this.name = name;
		this.base = base;
		this.orientations = new boolean[4][][];
		
		if (defaultOrientation != null) {
			// first check that default orientation is square
			for (int i = 0; i < defaultOrientation.length; i++) {
				if (defaultOrientation[i].length != defaultOrientation.length)
					throw new Exception("Tetromino orientations must be square.");
			}
			
			
			// Generate orientations
			boolean[][] orientation = defaultOrientation;
			for (int i = 0; i < 4; i++) {
				this.orientations[i] = orientation;
				boolean[][] new_orientation = new boolean[orientation.length][orientation.length];                                                                                                                                                        
				for (int x = 0; x < orientation.length; x++) {
					for (int y = 0; y < orientation.length; y++) {
						new_orientation[x][y] = orientation[y][orientation.length - 1 - x];
					}
				}
				
				orientation = new_orientation;
			}
			
			// Fix orientations
			for (int i = 0; i < 4; i++) {
				orientation = orientations[i];
				int first_nonempty_row = 0;
				for (int j = 0; j < orientation.length; j++) {
					boolean or = false;
					for (int k = 0; k < orientation[j].length; k++) or |= orientation[j][k];
					if (or) {
						first_nonempty_row = j;
						break;
					}
				}
				
				if (first_nonempty_row > 0) {
					// shift up
					for (int j = first_nonempty_row; j < orientation.length; j++) {
						orientation[j - first_nonempty_row] = Arrays.copyOf(orientation[j], orientation[j].length);
					}
					for (int j = 0; j < first_nonempty_row; j++) {
						Arrays.fill(orientation[orientation.length - j - 1], false);
					}
				}
			}
		}
	}
	
	public boolean[][][] getOrientations() {
		return orientations;
	}
	
	public String toString() {
		return String.format("TetrominoPrototype['%s']", name);
	}
}
