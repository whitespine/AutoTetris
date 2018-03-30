package autotetris.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import autotetris.model.Model;


public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final Color DROPPED_SQUARE_COLOR = Color.LIGHT_GRAY;
	private Application app;

	public GamePanel(Application app) {
		this.app = app;
	}
	
	public void paintComponent(Graphics g) {
		Dimension size = this.getSize();
		int xUnitSize = size.width / Model.BOARD_WIDTH, yUnitSize = size.height / Model.BOARD_HEIGHT,
				xoff = size.width - Model.BOARD_WIDTH * xUnitSize,
				yoff = size.height - Model.BOARD_HEIGHT * yUnitSize;
		
		g.setColor(Application.BACKGROUND_COLOR);
		g.fillRect(0, 0, size.width, size.height);
		
		g.setColor(DROPPED_SQUARE_COLOR);
		for (int x = 0; x < Model.BOARD_WIDTH; x++) {
			for (int y = 0; y < Model.BOARD_HEIGHT; y++) {
				if (app.model.getBoard()[x][y])
					g.fillRect(x * xUnitSize + xoff / 2, y * yUnitSize + yoff, xUnitSize - 1, yUnitSize - 1);
			}
		}
	}
}
