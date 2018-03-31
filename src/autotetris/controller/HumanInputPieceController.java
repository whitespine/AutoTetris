package autotetris.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import autotetris.model.Model;
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
			model.moveFallingPiece(0, 0, 1);
		} else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_KP_DOWN || keyCode == KeyEvent.VK_S) {
			model.moveFallingPiece(0, 0, -1);
		} else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_KP_LEFT || keyCode == KeyEvent.VK_A) {
			model.moveFallingPiece(-1, 0, 0);
		} else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_KP_RIGHT || keyCode == KeyEvent.VK_D) {
			model.moveFallingPiece(1, 0, 0);
		} else if (keyCode == KeyEvent.VK_SPACE) {
			model.setSpeedyMode(true);
		}
		app.repaint();
	}

	@Override
	public void keyReleased(KeyEvent evt) {
		int keyCode = evt.getExtendedKeyCode();
		if (keyCode == KeyEvent.VK_SPACE) {
			model.setSpeedyMode(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
