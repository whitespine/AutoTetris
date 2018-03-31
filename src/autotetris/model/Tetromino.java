package autotetris.model;


public class Tetromino {

	public final TetrominoPrototype prototype;
	int x;
	double y;
	int orientation;
	
	public Tetromino(TetrominoPrototype prototype) {
		this.prototype = prototype;
		this.x = -100;
		this.y = -100;
		this.orientation = 0;
	}

	public Tetromino(TetrominoPrototype prototype, int x, double y) {
		this.prototype = prototype;
		this.x = x;
		this.y = y;
		this.orientation = 0;
	}
	
	public int getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public int getOrientation() {
		return orientation;
	}
	
	void move(int dx, double dy) {
		this.x += dx;
		this.y += dy;
	}
	
	void rotate(int direction) {
		orientation = (orientation + direction) % prototype.orientations.length;
		while (orientation < 0) orientation += prototype.orientations.length;
	}
}
