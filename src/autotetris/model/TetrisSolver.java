package autotetris.model;

import autotetris.view.Application;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class TetrisSolver {
	public double totalHeightWeight, completeLinesWeight, holesWeight, heightStdevWeight, squaredMaxHeightWeight;
	public int actionDelay;

	public TetrisSolver() {
	    // Higher = better
        // The below parameters had an average survival of ~37300 pieces
		totalHeightWeight       = -0.0000337604;
		completeLinesWeight     = +0.0189894317;
		holesWeight             = -0.9972973465;
		heightStdevWeight       = -0.0709744758;
		squaredMaxHeightWeight  = -0.0001637646;
        actionDelay = 20;
	}

	public static TetrisSolver genRandomSolver() {
        TetrisSolver solver = new TetrisSolver();
        solver.totalHeightWeight = -genRandomWeight();
        solver.completeLinesWeight = genRandomWeight();
        solver.holesWeight = -genRandomWeight();
        solver.heightStdevWeight = -genRandomWeight();
        solver.squaredMaxHeightWeight = -genRandomWeight();
        solver.unitVectorize();
        return solver;
    }

    private static Random rng = new Random();
    private static double genRandomWeight() {
        double base = 2;
        double maxExponent = 20;
        return Math.pow(base, rng.nextDouble() * maxExponent);
    }

	private double score(Evaluation ev) {
		return totalHeightWeight * ev.getTotalHeight()
				+ completeLinesWeight * ev.getCompleteLines()
				+ holesWeight * ev.getHoles()
				+ heightStdevWeight * Math.sqrt(ev.getHeightVariance())
                + squaredMaxHeightWeight * ev.getMaxHeight() * ev.getMaxHeight();
	}

	private void unitVectorize() {
        double s = 0;
        s += totalHeightWeight * totalHeightWeight;
        s += completeLinesWeight * completeLinesWeight;
        s += holesWeight * holesWeight;
        s += heightStdevWeight * heightStdevWeight;
        s += squaredMaxHeightWeight * squaredMaxHeightWeight;
        s = Math.sqrt(s);
        totalHeightWeight /= s;
        completeLinesWeight /= s;
        holesWeight /= s;
        heightStdevWeight /= s;
        squaredMaxHeightWeight /= s;
    }

	public void execute(Application parent, Model target) {
		Actor a = new Actor(parent, target, actionDelay);
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

            this.setDaemon(true);
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

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(10);
        df.setPositivePrefix("+");
        df.setMaximumFractionDigits(10);
        return "TetrisSolver{" +
                "\n\ttotalHeightWeight      = " + df.format(totalHeightWeight) +
                ",\n\tcompleteLinesWeight   = " + df.format(completeLinesWeight) +
                ",\n\tholesWeight           = " + df.format(holesWeight) +
                ",\n\theightStdevWeight     = " + df.format(heightStdevWeight) +
                ",\n\tsquaredMaxHeightWeight= " + df.format(squaredMaxHeightWeight) +
                '}';
    }
}
