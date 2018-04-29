package autotetris.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import autotetris.model.Cell;
import autotetris.model.Model;
import autotetris.model.Rotation;
import autotetris.view.Application;

public class HumanInputPieceController implements KeyListener {

	private Model model;
	private Application app;

	public HumanInputPieceController(Model model, Application app) {
		this.model = model;
		this.app = app;
	}

	@Override
	public void keyPressed(KeyEvent evt) {
		int keyCode = evt.getExtendedKeyCode();
		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_KP_UP || keyCode == KeyEvent.VK_W) {
			model.tryRotateFallingPiece(Rotation.CLOCKWISE);
		} else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_KP_DOWN || keyCode == KeyEvent.VK_S) {
			// model.tryRotateFallingPiece(Rotation.COUNTERCLOCKWISE);
			model.tryMoveFallingPiece(Cell.DOWN, true);
		} else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_KP_LEFT || keyCode == KeyEvent.VK_A) {
			model.tryMoveFallingPiece(Cell.LEFT, false);
		} else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_KP_RIGHT || keyCode == KeyEvent.VK_D) {
			model.tryMoveFallingPiece(Cell.RIGHT, false);
		} else if (keyCode == KeyEvent.VK_SPACE) {
			model.drop();
		}
		app.repaint();
	}

	@Override
	public void keyReleased(KeyEvent evt) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

}
