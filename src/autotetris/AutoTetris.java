package autotetris;

import autotetris.controller.TetrisTicker;
import autotetris.model.Model;
import autotetris.view.Application;

public class AutoTetris {
	public static void main(String[] args) {
		Model m = new Model();
		final Application app = new Application(m);
		
		TetrisTicker tt = new TetrisTicker(m, app);
		tt.start();
		
		app.setVisible(true);
	}
}