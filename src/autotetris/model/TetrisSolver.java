package autotetris.model;

import autotetris.view.Application;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class TetrisSolver {
	public double totalHeightWeight, completeLinesWeight, holesWeight, heightStdevWeight, squaredMaxHeightWeight;
	public int actionDelay;

	public TetrisSolver() {
	    // Higher = better
        // The below parameters had an average survival of ~40000 pieces
        totalHeightWeight       = -0.0940682412;
        completeLinesWeight     = +0.0260657235;
        holesWeight             = -0.9789442818;
        heightStdevWeight       = -0.1792715434;
        squaredMaxHeightWeight  = -0.0012453843;
        actionDelay = 1;
	}

	public TetrisSolver(TetrisSolver copyTarg) {
	    this.totalHeightWeight = copyTarg.totalHeightWeight;
	    this.completeLinesWeight = copyTarg.completeLinesWeight;
	    this.holesWeight = copyTarg.holesWeight;
	    this.heightStdevWeight = copyTarg.heightStdevWeight;
	    this.squaredMaxHeightWeight = copyTarg.squaredMaxHeightWeight;
	    this.actionDelay = copyTarg.actionDelay;
    }

	public static TetrisSolver genRandomSolver(boolean expWeights) {
	    // If expWeights, random weights will be 2**<random> instead of just <random>.
        // Can help make certain characteristics stand out, but risks missing fine tuning. (probably) good for initial generation!
        TetrisSolver solver = new TetrisSolver();
        solver.totalHeightWeight = -genRandomWeight(expWeights);
        solver.completeLinesWeight = genRandomWeight(expWeights);
        solver.holesWeight = -genRandomWeight(expWeights);
        solver.heightStdevWeight = -genRandomWeight(expWeights);
        solver.squaredMaxHeightWeight = -genRandomWeight(expWeights);
        solver.unitVectorize();
        return solver;
    }

    public static TetrisSolver crossBreed(TetrisSolver a, TetrisSolver b) {
	    // Copy a and b so we can safely unitvectorize
        a = new TetrisSolver(a);
        b = new TetrisSolver(b);
        a.unitVectorize();
        b.unitVectorize();

        // Create weights of each
        double wta = ThreadLocalRandom.current().nextDouble();
        double wtb = 1 - wta;

        // Create a combination of them
	    TetrisSolver t = new TetrisSolver();
        t.totalHeightWeight         = wta * a.totalHeightWeight       + wtb * b.totalHeightWeight;
        t.completeLinesWeight       = wta * a.completeLinesWeight     + wtb * b.completeLinesWeight;
        t.holesWeight               = wta * a.holesWeight             + wtb * b.holesWeight;
	    t.heightStdevWeight         = wta * a.heightStdevWeight       + wtb * b.heightStdevWeight;
        t.squaredMaxHeightWeight    = wta * a.squaredMaxHeightWeight  + wtb * b.squaredMaxHeightWeight;

        t.unitVectorize();
        return t;
    }

    private static double genRandomWeight(boolean expWeights) {
        double base = 2;
        double maxExponent = 20;
        if(expWeights)
            return Math.pow(base, ThreadLocalRandom.current().nextDouble() * maxExponent);
        else
            return ThreadLocalRandom.current().nextDouble();
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
        if(s == 0) s = 0.0001;
        totalHeightWeight /= s;
        completeLinesWeight /= s;
        holesWeight /= s;
        heightStdevWeight /= s;
        squaredMaxHeightWeight /= s;
    }

    private Actor a = null;
	public void execute(Application parent, Model target) {
	    if(a != null && a.isAlive())
	        return;
		a = new Actor(parent, target, true);
		a.start();
	}

    // Executes a game till completion
    // If targetApp is supplied, will repaint after each action on model
    // If delay > 0, will sleep for that long between actions
	private class Actor extends Thread {
	    boolean done = false;
        private Application targetApp;
        Model targetModel;
        private boolean delay;

        public Actor(Application targetApp, Model targetModel, boolean delay) {
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

                    // If it lost, discard
                    if(m.isGameOver())
                        continue;

                    // Evaluate and compare
                    Evaluation eval = new Evaluation(m.board);
                    double evalScore = score(eval);
                    if (bestSequence == null || evalScore > bestScore) {
                        bestScore = evalScore;
                        bestSequence = sequences.get(i);
                    }
                }

                // If bestSequence still null, we can't do anything (doomed state).
                if(bestSequence != null){
                    // Now that we have best sequence, play it out super fast
                    for (Action a : bestSequence.actions) {
                        targetModel.doAction(a);
                        if (targetApp != null)
                            targetApp.repaint();
                        try {
                            if (delay)
                                sleep(actionDelay);
                        } catch (InterruptedException e) {
                            System.out.println("Interrupted in tetrissolver for some reason");
                        }
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
                ";\n\tcompleteLinesWeight   = " + df.format(completeLinesWeight) +
                ";\n\tholesWeight           = " + df.format(holesWeight) +
                ";\n\theightStdevWeight     = " + df.format(heightStdevWeight) +
                ";\n\tsquaredMaxHeightWeight= " + df.format(squaredMaxHeightWeight) +
                ";\n}";
    }

    public SolverScore scoreSelf(int numTrials) {
        // Run self numTrials times
        ConcurrentLinkedQueue<Integer> _drops = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Integer> _scores = new ConcurrentLinkedQueue<>();
        IntStream.range(0, numTrials)
        .parallel()
        .mapToObj(whocares -> {
            Model m = new Model();
            Actor a = new Actor(null, m, false);
            a.start();
            try {
                a.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return m;
        }).forEach(m -> {
            _drops.add(m.getTotalDrops());
            _scores.add(m.getScore());
        });

        // Bring to lists
        ArrayList<Integer> drops = new ArrayList<>(_drops);
        ArrayList<Integer> scores = new ArrayList<>(_scores);

        // Do some calculations
        SolverScore s = new SolverScore();
        long sumDrops = 0;
        long sumScores = 0;
        for(int x : drops)
            sumDrops += x;
        for(int x : scores)
            sumScores += x;
        s.meanDrops = (double)sumDrops  / drops.size();
        s.meanScore = (double)sumScores / scores.size();

        double varDrops = 0;
        double varScore = 0;
        for(double x : drops)
            varDrops += Math.pow(x - s.meanDrops, 2);
        for(double x : scores)
            varScore += Math.pow(x - s.meanScore, 2);
        s.devDrops = Math.sqrt(varDrops / (drops.size()-1));
        s.devScore = Math.sqrt(varScore / (scores.size()-1));

        s.numTrials = numTrials;
        return s;
    }

    public static class SolverScore {
        public int numTrials;
        public double meanDrops; // Average # pieces dropped across trials
        public double meanScore; // Average score across trials
        public double devDrops; // Std deviation of # pieces dropped across trials
        public double devScore; // Std deviation of score dropped across trials
    }
}
