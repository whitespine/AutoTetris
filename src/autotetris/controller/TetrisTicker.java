package autotetris.controller;

import autotetris.model.Model;
import autotetris.view.Application;

public class TetrisTicker extends Thread {
	Model model;
	Application app;
	private boolean paused;
	
	public TetrisTicker(Model model, Application app) {
		this.model = model;
		this.app = app;
		this.paused = false;
	}
	
	public void pause() {
		this.paused = true;
	}
	
	public void unpause() {
		this.paused = false;
	}
	
	public void kill() {
		this.interrupt();
	}
	
	public boolean isPaused() {
		return this.paused;
	}
	
	public void run() {
		try {
			model.setupGameState();
			app.repaint();
			
			while (!this.isInterrupted()) {
				int sleepTime = 10;
				if (!paused) {
					sleepTime = model.tick();
					app.showScore(model.getScore());
					app.repaint();
					if (sleepTime < 0)
						break;
				}
				Thread.sleep(sleepTime);
			}
		} catch (InterruptedException e) {
			return;
		}
	}
}
