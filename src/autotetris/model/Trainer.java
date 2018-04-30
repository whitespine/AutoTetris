package autotetris.model;

import java.util.ArrayList;

public class Trainer {
    private final int populationSize;
    private final double mutationChance;
    private final int testIntensity;

    private class Organism {
        TetrisSolver solver;
        TetrisSolver.SolverScore score;
    }
    private ArrayList<Organism> population;

    /**
     * @param populationSize How many members to generate/keep between rounds
     * @param mutationChance Probability to use a random vector instead of an existing tester as a parent
     * @param testIntensity How many runs of the solver to compute before returning score.
     */
    public Trainer(int populationSize, double mutationChance, int testIntensity) {

        this.populationSize = populationSize;
        this.mutationChance = mutationChance;
        this.testIntensity = testIntensity;
    }

    /**
     * @param numTrials How many rounds of mutation/crossbreeding/etc to do
     */
    public void train(int numTrials) {
        int trials = 16; // Number of runs to use on each solver
        population = new ArrayList<>(populationSize);

        /*
        //
        double bestMeanDrops = -1;
        for(int i=0; i<numSolvers; i++) {
            TetrisSolver solver = TetrisSolver.genRandomSolver(true);
            System.out.println("\n\n\nTesting solver #" + i + "\nWith config: " + solver);

            TetrisSolver.SolverScore score = solver.scoreSelf(trials);
            double meanDrops = score.meanDrops;

            if(meanDrops > bestMeanDrops) {
                bestMeanDrops = meanDrops;


                System.out.println("^ New best solver with score " + bestMeanDrops);
            }
            else {
                System.out.println("Got score: " + meanDrops);
            }
        }

        System.out.println("Best solver with score " + bestMeanDrops);
        System.out.println(bestSolver);
        */
    }

    private void seedPopulus() {
        population = new ArrayList<>(populationSize);
        for(int i=0;;);
    }

}
