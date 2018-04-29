package autotetris.model;

// Represents a position on the board for purposes of easy offsets, rotation, etc.
public class Cell {
    public static final Cell ORIGIN = new Cell(0, 0);
    public static final Cell UP = new Cell(-1, 0);
    public static final Cell DOWN = new Cell(1, 0);
    public static final Cell LEFT = new Cell(0, -1);
    public static final Cell RIGHT = new Cell(0, 1);

	public final int row; // Descending from top
	public final int col; // Left->right
	
	public Cell(int row, int col) {
		this.row = row;
		this.col = col;
	}

    // Shift by delta-row, delta-col
    public Cell offset(int delta_row, int delta_col) {
        return new Cell(this.row + delta_row, this.col + delta_col);
    }

    // Element-wise addition of two cells
	public Cell add(Cell other){
        return this.offset(other.row, other.col);
    }

    // Element-wise subtraction of two cells
    public Cell subtract(Cell other){
	    return this.offset(-other.row, -other.col);
    }

    // Rotate about a specific point (can be null to just do position)
    public Cell rotateClockwise(Cell aboutPoint) {
        if(aboutPoint == null){
            aboutPoint = ORIGIN;
        }
        // position relative to origin
        Cell rel = this.subtract(aboutPoint);
        // Perform the actual rotation on rel
        Cell rel_rotated = new Cell(col, -row);
        return rel_rotated.add(aboutPoint);
	}

    // Rotate about a specific point (can be null to just do position)
    public Cell rotateCounterClockwise(Cell aboutPoint) {
        if(aboutPoint == null){
            aboutPoint = ORIGIN;
        }
        // position relative to origin
        Cell rel = this.subtract(aboutPoint);
        // Perform the actual rotation on rel
        Cell rel_rotated = new Cell(-col, row);
        return rel_rotated.add(aboutPoint);
    }

    // generalization of above two. "about" can be null
    public Cell rotate(Rotation r, Cell about) {
        switch (r){
            case CLOCKWISE:
                return rotateClockwise(about);
            case COUNTERCLOCKWISE:
                return rotateCounterClockwise(about);
        }
        throw new NullPointerException();
    }

    // Duplicate
    public Cell(Cell c){
        this.row = c.row;
        this.col = c.col;
    }

	public String toString() {
		return String.format("(%d, %d)", row, col);
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (row != cell.row) return false;
        return col == cell.col;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }
}
