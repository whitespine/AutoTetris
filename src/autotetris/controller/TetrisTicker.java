package autotetris.controller;

import autotetris.model.Model;
import autotetris.view.Application;

public class TetrisTicker extends Thread {
	Model model;
	Application app;
	
	public TetrisTicker(Model model, Application app) {
		this.model = model;
		this.app = app;
	}
	
	public void run() {
		try {
			while (true) {
				int sleepTime = model.tick();
				app.showScore(model.getScore());
				app.repaint();
				if (sleepTime < 0)
					break;
				else
					Thread.sleep(sleepTime);
			}
		} catch (InterruptedException e) {
			return;
		}
	}
}
