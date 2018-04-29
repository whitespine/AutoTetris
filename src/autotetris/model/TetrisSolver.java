package autotetris.model;

import autotetris.view.Application;

import java.util.List;

public class TetrisSolver {
	public double totalHeightWeight, completeLinesWeight, holesWeight, heightVarianceWeight;

	public TetrisSolver() {
	    // Higher = better
		totalHeightWeight = -100;
		completeLinesWeight = 10;
		holesWeight = -1000;
		heightVarianceWeight = -1;
	}

	private double score(Evaluation ev) {
		return totalHeightWeight * ev.getTotalHeight()
				+ completeLinesWeight * ev.getCompleteLines()
				+ holesWeight * ev.getHoles()
				+ heightVarianceWeight * ev.getHeightVariance();
	}

	private Application targetApp;
	private Model targetModel;

	public void execute(Application parent, Model target) {
		Actor a = new Actor();
		targetModel = target;
		targetApp = parent;
		a.start();
	}

	private class Actor extends Thread {
	    boolean done = false;

        @Override
        public void run() {
            while(!done) {
                Explorer explorer = new Explorer(targetModel);

                // Get all possible terminae
                List<Explorer.ActionSequence> sequences = explorer.exploreAllActions();

                // Map to models, and rate each
                Explorer.ActionSequence bestSequence = null;
                double bestScore = 0;
                for(int i=0; i<sequences.size(); i++) {
                    // Get the model
                    Model m = sequences.get(i).rolloutModel(targetModel);

                    // Evaluate and compare
                    Evaluation eval = new Evaluation(m.board);
                    double evalScore = score(eval);
                    if (bestSequence == null || evalScore > bestScore) {
                        bestScore = evalScore;
                        bestSequence = sequences.get(i);
                    }
                }

                // Now that we have best sequence, play it out super fast
                for(Action a : bestSequence.actions) {
                    targetModel.doAction(a);
                    targetApp.repaint();
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted in tetrissolver for some reason");
                    }
                }
                // finalize with a down
                targetModel.doAction(Action.Down);
                targetApp.repaint();
            }
        }
    }
}
