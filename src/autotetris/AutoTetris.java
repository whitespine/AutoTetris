package autotetris;

import javax.swing.UIManager;

import autotetris.model.Model;
import autotetris.model.TetrisSolver;
import autotetris.view.Application;

import java.util.Random;
import java.util.stream.IntStream;

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

	private static double scoreSolver(TetrisSolver ts, int trials) {
		int[] drops = ts.countDroppedMany(trials);
		long sum = 0;
		for(int x : drops) {
			System.out.println("Total drops: " + x);
			sum += x;
		}

		double mean = (double)sum / drops.length;
		System.out.println("Avg drops: " + mean);
        double temp = 0;
        for(double a : drops)
                temp += (a-mean)*(a-mean);
        double variance = temp/(drops.length-1);
        // System.out.println("Variance: " + variance);
        System.out.println("StdDev " + Math.sqrt(variance));
        return mean;
    }

	public static void main(String[] args) {
        runGUI();
	    /*
	    int numSolvers = 100;
        int trials = 200; // Number of runs to use on each solver
        TetrisSolver bestSolver = null;
        double bestMeanDrops = -1;
	    for(int i=0; i<numSolvers; i++) {
            TetrisSolver solver = TetrisSolver.genRandomSolver();
	        double score scoreSolver(solver, trials);
        }
        */
	}

}

// 1000 avg 1: 2063.551