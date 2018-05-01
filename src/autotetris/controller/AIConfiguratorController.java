package autotetris.controller;

import javax.swing.JOptionPane;

import autotetris.model.TetrisSolver;
import autotetris.view.AIConfigurator;
import autotetris.view.Application;

public class AIConfiguratorController {
	private Application app;
	
	public AIConfiguratorController(Application app) {
		this.app = app;
	}
 	
	public void fillConfigurator() {
		//app.getAIConfigurator().getIterSpinner().setValue(model.getTrainingIterations());
        AIConfigurator config = app.getAIConfigurator();
        config.getTotalHeight_wtfield().setText("" + app.getSolver().totalHeightWeight);
        config.getRows_wtfield().setText("" + app.getSolver().completeLinesWeight);
		config.getMaxheight_wtfield().setText("" + app.getSolver().squaredMaxHeightWeight);
        config.getHoles_wtfield().setText("" + app.getSolver().holesWeight);
        config.getHeightDev_wtfield().setText("" + app.getSolver().heightStdevWeight);
	}

	public boolean writeConfiguration(String totalHeight_wts, String completeLines_wts, String holes_wts, String heightVariation_wts, String maxHeight_wts) {
		double totalHeight_wt, completeLines_wt, holes_wt, heightVariation_wt, maxHeight_wt;
		try {
			totalHeight_wt = Double.parseDouble(totalHeight_wts);
			completeLines_wt = Double.parseDouble(completeLines_wts);
			holes_wt = Double.parseDouble(holes_wts);
			heightVariation_wt = Double.parseDouble(heightVariation_wts);
            maxHeight_wt = Double.parseDouble(maxHeight_wts);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(app, "Provided weights need to be valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

        TetrisSolver solver = app.getSolver();
        solver.completeLinesWeight = completeLines_wt;
        solver.heightStdevWeight = heightVariation_wt;
        solver.holesWeight = holes_wt;
        solver.totalHeightWeight = totalHeight_wt;
        solver.squaredMaxHeightWeight = maxHeight_wt;
		return true;
	}
}
