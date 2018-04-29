package autotetris.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import autotetris.model.Cell;
import autotetris.model.Model;
import autotetris.model.Tetromino;


public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final Color DROPPED_SQUARE_COLOR = Color.LIGHT_GRAY;
	public static final Color POTENTIAL_SQUARE_COLOR = Color.DARK_GRAY;
	private Application app;

	public GamePanel(Application app) {
		this.app = app;
	}
	
	public void paintComponent(Graphics g) {
        // Get the board
        boolean[][] board = app.model.board;

        // Compute how big to draw squares
		Dimension size = this.getSize();
		int xUnitSize = size.width / Model.BOARD_WIDTH, yUnitSize = size.height / Model.BOARD_HEIGHT,
				xoff = size.width - Model.BOARD_WIDTH * xUnitSize,
				yoff = size.height - Model.BOARD_HEIGHT * yUnitSize;

		//Draw background
		g.setColor(Application.BACKGROUND_COLOR);
		g.fillRect(xoff / 2, 0, Model.BOARD_WIDTH * xUnitSize, size.height);

		// Draw dropped stuff
		g.setColor(DROPPED_SQUARE_COLOR);
		for (int y = 0; y < Model.BOARD_HEIGHT; y++) {
			for (int x = 0; x < Model.BOARD_WIDTH; x++) {
				if (board[y][x])
					g.fillRect(x * xUnitSize + xoff / 2, y * yUnitSize + yoff, xUnitSize - 1, yUnitSize - 1);
			}
		}

		// Draw potentials
		g.setColor(POTENTIAL_SQUARE_COLOR);
		for(Cell c : app.model.getPredictedCells()) {
		    int y = c.row;
		    int x = c.col;
            g.fillRect(x * xUnitSize + xoff / 2, y * yUnitSize + yoff, xUnitSize - 1, yUnitSize - 1);
		}

		// Draw falling piece
		Tetromino fallingPiece = app.model.getFallingPiece();
		if (fallingPiece != null) {
			g.setColor(fallingPiece.prototype.color);
			for(Cell c : fallingPiece.project()) {
                int y = c.row;
                int x = c.col;
                g.fillRect(x * xUnitSize + xoff / 2, y * yUnitSize + yoff, xUnitSize - 1, yUnitSize - 1);
			}
		}
		
		if (app.model.isGameOver()) {
			g.setFont(new Font("Helvetica", Font.BOLD, 18));
			int desiredWidth = this.getWidth(), desiredHeight = this.getHeight();

			Rectangle2D sBounds = g.getFontMetrics().getStringBounds("Game Over", g);
			double actualWidth = sBounds.getWidth(), actualHeight = sBounds.getHeight();
			double scaleFactor = Math.min(desiredWidth / actualWidth, desiredHeight / actualHeight);
			g.setFont(new Font(g.getFont().getName(), g.getFont().getStyle(), (int) (g.getFont().getSize() * scaleFactor * 0.9)));
			sBounds = g.getFontMetrics().getStringBounds("Game Over", g);
			
			g.setColor(Color.RED);
			g.drawString("Game Over", (int)((getWidth() - sBounds.getWidth()) / 2), (int)((getHeight() + sBounds.getHeight()) / 2));
		}
	}
}
