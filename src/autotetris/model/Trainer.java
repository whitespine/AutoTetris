package autotetris.model;
import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Trainer {
    private final int populationSize;
    private final double mutationChance;
    private final int testIntensity;
    private final int spawnsPerRound;
    private final Random rng = new Random();

    private class Organism implements Comparable<Organism> {
        TetrisSolver solver;
        TetrisSolver.SolverScore score;

        public Organism(TetrisSolver solver) {
            this.solver = solver;
            this.score = solver.scoreSelf(testIntensity);
        }

        @Override
        public int compareTo(Organism o) {
            return Double.compare(score.meanDrops, o.score.meanDrops);
        }
    }
    private ArrayList<Organism> population;

    /**
     * @param populationSize How many members to generate/keep between rounds
     * @param mutationChance Probability to use a random vector instead of an existing tester as a parent
     * @param testIntensity How many runs of the solver to compute before returning score.
     */
    public Trainer(int populationSize, double mutationChance, int testIntensity, int spawnsPerRound) {
        this.spawnsPerRound = spawnsPerRound;
        this.populationSize = populationSize;
        this.mutationChance = mutationChance;
        this.testIntensity = testIntensity;
    }

    /**
     * @param numTrials How many rounds of mutation/crossbreeding/etc to do
     */
    public void train(int numTrials) {
        System.out.println("Seeding populus");
        if(population == null)
            seedPopulus();

        // Do eet
        for(int i=0; i<numTrials; i++) {
            System.out.println(String.format("Running trial #%d / %d", i+1, populationSize));
            // Generate new set of populae
            ArrayList<Organism> newMembers = new ArrayList<>();
            for(int j=0; j<spawnsPerRound; j++) {
                newMembers.add(breedNewOrganism());
                System.out.println(String.format("Bred organism #%d / %d", j+1, populationSize));
            }

            // Add into existing list, and sort
            population.addAll(newMembers);
            population.sort(Comparator.naturalOrder());

            // Truncate to best
            while (population.size() > populationSize)
                population.remove(0);

            // Print supremo
            Organism currBest = population.get(population.size() - 1);
            System.out.println("Best solver with score " + currBest.score.meanDrops);
            System.out.println(currBest.solver);
        }
    }

    private void seedPopulus() {
        population = new ArrayList<>(populationSize);
        for(int i=0; i < populationSize; i++) {
            TetrisSolver solver = TetrisSolver.genRandomSolver(true); // Initially want some extremes
            Organism organism = new Organism(solver);
            population.add(organism);
            System.out.println(String.format("Bred organism #%d / %d", i+1, populationSize));
        }
    }

    private Organism breedNewOrganism() {
        // Spawns and tests a single new member of the populus
        TetrisSolver parent1, parent2;
        if(rng.nextDouble() <= mutationChance) {
            int i = rng.nextInt(population.size());
            parent2 = population.get(i).solver;
            parent1 = TetrisSolver.genRandomSolver(false);
        } else {
            int i = rng.nextInt(population.size());
            int j =  rng.nextInt(population.size());
            j = (i != j) ? j : ((j + 1) % population.size()); // Prevent them being the same member
            parent1 = population.get(i).solver;
            parent2 = population.get(j).solver;
        }

        return new Organism(TetrisSolver.crossBreed(parent1, parent2));
    }
}
