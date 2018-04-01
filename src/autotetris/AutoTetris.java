package autotetris;

import autotetris.controller.ExecutionController;
import autotetris.model.Model;
import autotetris.view.Application;

public class AutoTetris {
	public static void main(String[] args) {
		Model m = new Model();
		final Application app = new Application(m);

		app.setExecutionController(new ExecutionController(m, app));		
		app.setVisible(true);
	}
}