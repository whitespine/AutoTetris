package autotetris;

import javax.swing.UIManager;

import autotetris.model.Model;
import autotetris.model.TetrisSolver;
import autotetris.model.Trainer;
import autotetris.view.Application;

public class AutoTetris {

	private static void runGUI() {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		Model m = new Model();
		final Application app = new Application(m);

		app.bindExecutionController(new autotetris.controller.ExecutionController(m, app));
		app.setVisible(true);
	}

	public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("masstrain")) {
            Trainer t = new Trainer(20, 0.4d, 50);
            t.train(20);
        }
	    else
	        runGUI();
	}

}

// 1000 avg 1: 2063.551