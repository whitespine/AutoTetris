package autotetris.view;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.*;

import autotetris.model.TetrominoPrototype;

public class NextPiecePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final int PADDING = 3;
	private Application app;

	public NextPiecePanel(Application app) {
		this.app = app;
	}

	public void paintComponent(Graphics g) {
		TetrominoPrototype tp = app.model.getNextPiece();
		if (tp != null) {
			// find square
			Dimension size = this.getSize();
			int xoff = (size.width > size.height ? (size.width - size.height) / 2 : 0),
					yoff = (size.height > size.width ? (size.height - size.width) / 2 : 0),
					totalSize = Math.min(size.width - 2 * PADDING, size.height - 2 * PADDING),
					squareSize = (totalSize - tp.getOrientations()[0].length + 1) / tp.getOrientations()[0].length;
			g.setColor(Application.BACKGROUND_COLOR);
			g.fillRect(0, 0, size.width, size.height);
			g.setColor(tp.color);
			for (int y = 0; y < tp.getOrientations()[0].length; y++) {
				for (int x = 0; x < tp.getOrientations()[0][0].length; x++) {
					if (tp.getOrientations()[0][y][x])
						g.fillRect(PADDING + xoff + (squareSize + 1) * x, PADDING + yoff + (squareSize + 1) * y, squareSize, squareSize);
				}
			}
		}
	}

}
