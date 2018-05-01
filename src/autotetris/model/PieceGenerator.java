package autotetris.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PieceGenerator {
    private final ArrayList<TetrominoPrototype> allPieces;

    private ArrayList<TetrominoPrototype> pieceQueue; // Current queue of pieces. "fair bag" approach
    private int next; // Next element to take from queue

    public PieceGenerator(ArrayList<TetrominoPrototype> allPieces) {
        this.allPieces = allPieces;

        // Prime stuff
        this.pieceQueue = genQueue();
        this.next = genNext();
    }

    public TetrominoPrototype peekNext() {
        return pieceQueue.get(next);
    }

    public TetrominoPrototype getNext() {
        // Get the next piece.
        TetrominoPrototype ret = pieceQueue.get(next);
        pieceQueue.remove(next);

        // Regen queue if necessary
        if(pieceQueue.isEmpty())
            pieceQueue = genQueue();

        // Select a new next
        next = genNext();
        return ret;
    }

    // Generation functions
    private ArrayList<TetrominoPrototype> genQueue() {
        return new ArrayList<>(allPieces);
    }

    private int genNext() {
        return ThreadLocalRandom.current().nextInt(pieceQueue.size());
    }

    // Copy constructor. Gets new random (DIVERGENCE POSSIBLE IMMEDIATELY!)
    public PieceGenerator(PieceGenerator copy) {
        this.allPieces = copy.allPieces;
        this.pieceQueue = new ArrayList<>(copy.pieceQueue);
        genNext();
    }
}
