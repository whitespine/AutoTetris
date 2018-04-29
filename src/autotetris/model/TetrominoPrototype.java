package autotetris.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public class TetrominoPrototype {
	public final String name;
	public final Color color;
	// Length reasonably between 1 and 4.
	public final Cell[][] orientations; // In order. 0 = no rotation, 1 = 1 clockwise, 2 = 2 clockwise, etc.

	
	public TetrominoPrototype(String name, Color base, boolean rotate, Cell[] positions) {
		this.name = name;
		this.color = base;

		if(rotate) {
            this.orientations = new Cell[4][];

            // Validate all are tetrominos
            assert positions.length == 4;

            // Generate orientations
            Cell[] o = positions.clone();
            for (int i = 0; i < 4; i++) {
                // Save the current orientation
                orientations[i] = o.clone();

                // Rotate each cell
                for(int cellIndex=0; cellIndex<o.length; cellIndex++)
                    o[cellIndex] = o[cellIndex].rotateClockwise(null);
            }
        }
        else {
		    // Special case for square. Don't want it to awkwardly orbit itself.
		    this.orientations = new Cell[][]{
		            positions.clone()
            };
        }
	}
	
	public Cell[][] getOrientations() {
		return orientations;
	}
	
	public String toString() {
		return String.format("TetrominoPrototype['%s']", name);
	}


    public static ArrayList<TetrominoPrototype> initializePieces() {
        TetrominoPrototype I = new TetrominoPrototype("I", Color.CYAN, true, new Cell[]{
                new Cell(-1, 0),
                new Cell(0, 0),
                new Cell(1, 0),
                new Cell(2, 0)});
        TetrominoPrototype J = new TetrominoPrototype("J", Color.BLUE, true, new Cell[]{
                new Cell(-1, 0),
                new Cell(0, 0),
                new Cell(1, 0),
                new Cell(1, -1)});
        TetrominoPrototype L = new TetrominoPrototype("L", Color.ORANGE, true, new Cell[]{
                new Cell(-1, 0),
                new Cell(0, 0),
                new Cell(1, 0),
                new Cell(1, 1)});
        TetrominoPrototype O = new TetrominoPrototype("O", Color.YELLOW, false, new Cell[]{
                new Cell(0, 0),
                new Cell(0, 1),
                new Cell(1, 0),
                new Cell(1, 1)});
        TetrominoPrototype S = new TetrominoPrototype("S", Color.GREEN, true, new Cell[]{
                new Cell(0, 0),
                new Cell(0, 1),
                new Cell(1, 0),
                new Cell(1, -1)});
        TetrominoPrototype T = new TetrominoPrototype("T", new Color(128, 0, 255), true, new Cell[]{
                new Cell(-1, 0),
                new Cell(0, -1),
                new Cell(0, 0),
                new Cell(0, 1)});
        TetrominoPrototype Z = new TetrominoPrototype("Z", Color.RED, true, new Cell[]{
                new Cell(0, -1),
                new Cell(0, 0),
                new Cell(1, 0),
                new Cell(1, 1)});

        ArrayList<TetrominoPrototype> allPieces = new ArrayList<>();
        allPieces.add(I);
        allPieces.add(J);
        allPieces.add(L);
        allPieces.add(O);
        allPieces.add(S);
        allPieces.add(T);
        allPieces.add(Z);
        return allPieces;
    }
}
