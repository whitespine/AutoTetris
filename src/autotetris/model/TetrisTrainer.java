package autotetris.model;

import java.util.*;

public class TetrisTrainer {
	public static final int CANDIDATES = 40;
	public static final int DROPPED_PIECES = 500;
	public static final Random RNG = new Random();
	
	int iterations;
	
	public TetrisSolver train() {
		/*
		// Apparently since i need a vector of things, I should normalize it and rotate it around a sphere of fixed length.
		List<Model> games = new ArrayList<Model>();
		for (int i = 0; i < CANDIDATES; i++) {
			Model m = new Model();
			double[] weights = new double[4];
			double sum = 0;
			for (int j = 0; j < 4; j++) {
				weights[j] = RNG.nextDouble() * (j == 0 ? -1 : 1);
				sum += weights[j] * weights[j];
			}
			for (int j = 0; j < 4; j++)
				weights[j] /= Math.sqrt(sum);
			
			m.getSolver().completeLinesWeight = weights[0];
			m.getSolver().heightVarianceWeight = weights[1];
			m.getSolver().holesWeight = weights[2];
			m.getSolver().totalHeightWeight = weights[3];
			games.add(m);
		}
		
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new TetrisSolver();
		*/
		return null;
	}
}

class TetrisTrainerThread extends Thread {
	Model model;

	public TetrisTrainerThread(Model m) {
		this.model = m;
	}
	
	public void run() {
	}
}