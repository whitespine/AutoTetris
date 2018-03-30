package autotetris.model;

public class Tuple<X, Y> {
	public final X x;
	public final Y y;
	
	public Tuple(X x, Y y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return String.format("(%s, %s)", x.toString(), y.toString());
	}
}
