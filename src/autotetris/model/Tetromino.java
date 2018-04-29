package autotetris.model;


import java.util.ArrayList;
import java.util.Arrays;

public class Tetromino {
	// Represents a tetromino on the board, in terms of position, underlying prototype, and its orientation.
	public final TetrominoPrototype prototype;
	private Cell position; // Origin position of the prototype.
	private int orientation;

	// Construct a tetromino at an arbitrary off-board location.
	public Tetromino(TetrominoPrototype prototype) {
		this.prototype = prototype;
		this.position = new Cell(-100, -100);
		this.orientation = 0;
	}

	// Construct a tetromino at the specified position
	public Tetromino(TetrominoPrototype prototype, Cell position) {
		this.prototype = prototype;
		this.position = position;
		this.orientation = 0;
	}

    // Construct a tetromino at the specified position + orientation
    public Tetromino(TetrominoPrototype prototype, Cell position, int orientation) {
        this.prototype = prototype;
        this.position = position;
        this.orientation = orientation % prototype.orientations.length;
    }

	// Construct from another Tetromino. Cache assumed to be valid
    public Tetromino(Tetromino other) {
	    this.prototype = other.prototype;
	    this.position = other.position;
	    this.orientation = other.orientation;
	    this.cacheValid = other.cacheValid;
	    this.cachedProjection = other.cachedProjection;
    }

    // Rotate tetromino in the specified direction. Invalidates cache.
	public void rotate(Rotation r) {
	    if(r == Rotation.CLOCKWISE) {
	        orientation = orientation + 1;
	        if (orientation == prototype.orientations.length)
	            orientation = 0;
        }
        else if(r == Rotation.COUNTERCLOCKWISE) {
	        orientation = orientation - 1;
	        if(orientation < 0)
	            orientation = prototype.orientations.length - 1;
        }
        this.cacheValid = false;
	}

	// Simple wrapper around movement. Invalidates cache.
	public void move(Cell deltaPosition) {
	    this.position = this.position.add(deltaPosition);
	    this.cacheValid = false;
    }

    // Gets this tetromino's cells in board-space
    private ArrayList<Cell> cachedProjection;
    boolean cacheValid = false;
    public ArrayList<Cell> project() {
        // Return cache if available
        if(cacheValid)
            return cachedProjection;

        // Copy our prototype orientation, and offset each cell by position
        int n = prototype.orientations[orientation].length;
        ArrayList<Cell> projection = new ArrayList<>(n);
	    for(int i=0; i<n; i++){
	        projection.add(prototype.orientations[orientation][i].add(position));
        }

        // Save to cache, and return
        cachedProjection = projection;
        cacheValid = true;
	    return projection;
    }

    public Cell getPosition() {
        return position;
    }

    public int getOrientation() {
        return orientation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tetromino tetromino = (Tetromino) o;

        if (orientation != tetromino.orientation) return false;
        if (prototype != tetromino.prototype) return false;
        return position.equals(tetromino.position);
    }

    @Override
    public int hashCode() {
        int result = prototype.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + orientation;
        return result;
    }
}
