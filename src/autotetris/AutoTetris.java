package autotetris;

import javax.swing.UIManager;

import autotetris.model.Model;
import autotetris.view.Application;

public class AutoTetris {
	public static void main(String[] args) {
		
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

		app.setExecutionController(new autotetris.controller.ExecutionController(m, app));		
		app.setVisible(true);
	}
}