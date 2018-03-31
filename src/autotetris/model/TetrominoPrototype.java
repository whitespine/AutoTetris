package autotetris.model;

import java.awt.Color;

public class TetrominoPrototype {
	public final String name;
	public final Color color;
	boolean[][][] orientations;
	
	public TetrominoPrototype(String name, Color base, boolean[][] defaultOrientation) throws Exception {
		this.name = name;
		this.color = base;
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
		}
	}
	
	public boolean[][][] getOrientations() {
		return orientations;
	}
	
	public String toString() {
		return String.format("TetrominoPrototype['%s']", name);
	}
}
