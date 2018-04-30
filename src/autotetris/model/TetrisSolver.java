package autotetris.model;

import autotetris.view.Application;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class TetrisSolver {
	public double squaredHeightWeight, completeLinesWeight, holesWeight, heightStdevWeight, maxHeightWeight;

	public TetrisSolver() {
	    // Higher = better
		squaredHeightWeight = -(1<<3);
		completeLinesWeight = (1<<3);
		holesWeight = -(1<<2);
		heightStdevWeight = -(1<<1);
		maxHeightWeight = -10; // Applied to square

	}

	public static TetrisSolver genRandomSolver() {
        TetrisSolver solver = new TetrisSolver();
        solver.squaredHeightWeight = -genRandomWeight();
        solver.completeLinesWeight = genRandomWeight();
        solver.holesWeight = -genRandomWeight();
        solver.heightStdevWeight = -genRandomWeight();
        solver.maxHeightWeight = -genRandomWeight();
        return  solver;
    }

    private static Random rng = new Random();
    private static double genRandomWeight() {
        double base = 2;
        double maxExponent = 20;
        return Math.pow(base, rng.nextDouble() * maxExponent);
    }

	private double score(Evaluation ev) {
		return squaredHeightWeight * ev.getTotalHeight()
				+ completeLinesWeight * ev.getCompleteLines()
				+ holesWeight * ev.getHoles()
				+ heightStdevWeight * Math.sqrt(ev.getHeightVariance())
                + maxHeightWeight * ev.getMaxHeight() * ev.getMaxHeight();
	}


	public void execute(Application parent, Model target) {
		Actor a = new Actor(parent, target, 5);
		a.start();
	}

	// Executes the game on numTrials times, and returns an array of the scores
	public int[] countDroppedMany(int numTrials) {
        return IntStream.range(0, numTrials)
                .parallel()
                .map(whocares -> {
                    Model m = new Model();
                    Actor a = new Actor(null, m, 0);
                    a.start();
                    try {
                        a.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return a.targetModel.getTotalDrops();
                })
                .toArray();
    }

    // Executes a game till completion
    // If targetApp is supplied, will repaint after each action on model
    // If delay > 0, will sleep for that long between actions
	private class Actor extends Thread {
	    boolean done = false;
        private Application targetApp;
        Model targetModel;
        private int delay;

        public Actor(Application targetApp, Model targetModel, int delay) {
            this.targetApp = targetApp;
            this.targetModel = targetModel;
            this.delay = delay;
        }

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
                    if(targetApp != null)
                        targetApp.repaint();
                    try {
                        if(delay > 0)
                            sleep(delay);
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted in tetrissolver for some reason");
                    }
                }
                // finalize with a down
                targetModel.doAction(Action.Down);
                if(targetApp != null)
                    targetApp.repaint();

                // Exit if done
                if(targetModel.isGameOver())
                    done = true;
            }
        }
    }
}
