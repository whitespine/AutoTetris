package autotetris.controller;

import autotetris.model.Model;
import autotetris.view.Application;

public class ExecutionController {
	private TetrisTicker ticker;
	private Application app;
	private Model model;
	
	public ExecutionController(Model m, Application app) {
		this.model = m;
		this.app = app;
		this.ticker = null;
	}
	
	public void reset() {
		if (ticker != null) {
			ticker.kill();
			try {
				ticker.join();
			} catch (InterruptedException e) {
			}
		}
		model.setupGameState();
		ticker = null;
		app.repaint();
	}
	
	public void play() {
		if (ticker == null) {
			ticker = new TetrisTicker(model, app);
			
			ticker.start();
		} else if (ticker.isPaused()) {
			ticker.unpause();
		}
	}
	
	public void pause() {
		if (ticker != null && !ticker.isPaused())
			ticker.pause();
	}
}
