package autotetris.controller;

import javax.swing.JOptionPane;

import autotetris.model.Model;
import autotetris.view.Application;

public class AIConfiguratorController {
	Model model;
	Application app;
	
	public AIConfiguratorController(Model model, Application app) {
		this.model = model;
		this.app = app;
	}
 	
	public void fillConfigurator() {
		app.getAIConfigurator().getIterSpinner().setValue(model.getTrainingIterations());
		app.getAIConfigurator().getTotalHeight_wtfield().setText("" + model.getSolver().getTotalHeightWeight());
		app.getAIConfigurator().getRows_wtfield().setText("" + model.getSolver().getCompleteLinesWeight());
		app.getAIConfigurator().getHoles_wtfield().setText("" + model.getSolver().getHolesWeight());
		app.getAIConfigurator().getHeightvar_wtfield().setText("" + model.getSolver().getHeightVarianceWeight());
	}

	public boolean writeConfiguration(Object iterations, String totalHeight_wts, String completeLines_wts, String holes_wts, String heightVariation_wts) {
		double totalHeight_wt, completeLines_wt, holes_wt, heightVariation_wt;
		try {
			totalHeight_wt = Double.parseDouble(totalHeight_wts);
			completeLines_wt = Double.parseDouble(completeLines_wts);
			holes_wt = Double.parseDouble(holes_wts);
			heightVariation_wt = Double.parseDouble(heightVariation_wts);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(app, "Provided weights need to be valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if ((iterations instanceof Integer) == false)
			return false;
		
		model.getSolver().setCompleteLinesWeight(completeLines_wt);
		model.getSolver().setHeightVarianceWeight(heightVariation_wt);
		model.getSolver().setHolesWeight(holes_wt);
		model.getSolver().setTotalHeightWeight(totalHeight_wt);
		Model.setTrainingIterations((Integer)iterations);
		return true;
	}
}
