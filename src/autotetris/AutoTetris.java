package autotetris;

import javax.swing.UIManager;

import autotetris.model.Model;
import autotetris.model.TetrisSolver;
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

	private static double calcMeanDrops(TetrisSolver ts, int trials) {
		int[] drops = ts.countDroppedMany(trials);
		long sum = 0;
		for(int x : drops) {
			sum += x;
		}

		double mean = (double)sum / drops.length;
		/*
		System.out.println("Avg drops: " + mean);
        double temp = 0;
        for(double a : drops)
                temp += (a-mean)*(a-mean);
        double variance = temp/(drops.length-1);
        System.out.println("Variance: " + variance);
        System.out.println("StdDev " + Math.sqrt(variance));
        */
        return mean;
    }

    private static void train() {
        int numSolvers = 500;
        int trials = 16; // Number of runs to use on each solver
        TetrisSolver bestSolver = null;
        double bestMeanDrops = -1;
        for(int i=0; i<numSolvers; i++) {
            TetrisSolver solver = TetrisSolver.genRandomSolver();
            System.out.println("\n\n\nTesting solver #" + i + "\nWith config: " + solver);

            double meanDrops = calcMeanDrops(solver, trials);

            if(meanDrops > bestMeanDrops) {
                bestMeanDrops = meanDrops;
                bestSolver = solver;

                System.out.println("^ New best solver with score " + bestMeanDrops);
            }
            else {
                System.out.println("Got score: " + meanDrops);
            }
        }

        System.out.println("Best solver with score " + bestMeanDrops);
        System.out.println(bestSolver);
    }

	public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("masstrain"))
            train();
	    else
	        runGUI();


	}

}

// 1000 avg 1: 2063.551