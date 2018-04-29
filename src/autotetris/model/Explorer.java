package autotetris.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

// Handles exploration.
// Note: Gets pretty messy with what calls what. Be careful!
public class Explorer {
    // Order in which to consider actions
    private static final Action[] ACTION_PRIORITY = {
            Action.Spin,
            Action.Left,
            Action.Right,
            Action.Down
    };

    // The model to analyze
    private Model model;

    // Exploration state
    private HashMap<PotentialState, RouteNode> stateMap; // Represents <State, stepsTaken>. Won't go to a state we've accessed more swiftly
    private LinkedList<PotentialState> frontier;

    public Explorer(Model m) {
        this.model = m;
    }

    // EXPENSIVE! Probably
    public List<ActionSequence> exploreAllActions() {
        // Create our statemap
        stateMap = new HashMap<>();

        // Make the queue.
        frontier = new LinkedList<>();

        // Enqueue the current position, and map it to the statemap
        Tetromino fallingPiece = model.getFallingPiece();
        PotentialState currPos = new PotentialState(fallingPiece.getPosition(), fallingPiece.getOrientation());
        stateMap.put(currPos, new RouteNode(0, null, null));
        frontier.push(currPos);

        // Until frontier exhausted, enqueue potential
        while (!frontier.isEmpty()) {
            // Get our state, and explore it
            PotentialState next = frontier.pop();
            next.queueExploreAction();
        }

        // Check for terminal locations, and build back from them
        return stateMap.keySet().stream()
                .filter(PotentialState::isTerminal)
                .map(PotentialState::howArrived)
                .collect(Collectors.toList());
    }

    private class PotentialState {
        Cell pos;
        int orientation;
        PotentialState(Cell pos, int orientation) {
            this.pos = pos;
            this.orientation = orientation;
        }

        Tetromino asTetromino() {
            return new Tetromino(model.getFallingPiece().prototype, pos, orientation);
        }

        void queueExploreAction() {
            int stepsToHere = stateMap.get(this).stepsToArrive;
            for (Action action : ACTION_PRIORITY) {
                if(action == Action.Drop) continue;

                // Create the tetromino, and perform action on it
                Tetromino t = asTetromino();
                switch (action) {
                    case Spin:
                        t.rotate(Rotation.CLOCKWISE);
                        break;
                    case Left:
                        t.move(Cell.LEFT);
                        break;
                    case Right:
                        t.move(Cell.RIGHT);
                        break;
                    case Down:
                        t.move(Cell.DOWN);
                        break;
                }

                // Generate a new state, check its validity, and merge into the map.
                PotentialState newState = new PotentialState(t.getPosition(), t.getOrientation());

                // Compute existing route to the given node
                RouteNode existingRoute = stateMap.get(newState);

                // If we're a faster route to neighbor, override. Don't need to check if allFree if someone else already has.
                // Also don't need to re-explore.
                if (existingRoute != null) {
                    if (existingRoute.stepsToArrive > stepsToHere + 1)
                        stateMap.put(newState, new RouteNode(stepsToHere + 1, action, this));
                }
                // Otherwise, check if it's even possible.
                else if (model.allFree(t.project())) {
                    stateMap.put(newState, new RouteNode(stepsToHere + 1, action, this));
                    if(!frontier.contains(newState))
                        frontier.push(newState);
                }
            }
        }

        boolean isTerminal() {
            Tetromino t = asTetromino();
            t.move(Cell.DOWN);
            return !model.allFree(t.project());
        }

        ActionSequence howArrived() {
            ActionSequence ret = new ActionSequence();
            RouteNode rn = stateMap.get(this);
            while (rn.from != null) {
                ret.actions.push(rn.actionToArrive);
                rn = stateMap.get(rn.from);
            }
            return ret;
        }

        @Override
        public boolean equals(Object o) {
            PotentialState that = (PotentialState) o;

            if (orientation != that.orientation) return false;
            return pos.equals(that.pos);
        }

        @Override
        public int hashCode() {
            int result = pos.hashCode();
            result = 31 * result + orientation;
            return result;
        }

        @Override
        public String toString() {
            return "PotentialState{" +
                    "pos=" + pos +
                    ", orientation=" + orientation +
                    '}';
        }
    }

    private class RouteNode {
        int stepsToArrive;
        Action actionToArrive;
        PotentialState from;

        public RouteNode(int stepsToArrive, Action actionToArrive, PotentialState from) {
            this.stepsToArrive = stepsToArrive;
            this.actionToArrive = actionToArrive;
            this.from = from;
        }
    }

    public class ActionSequence {
        public LinkedList<Action> actions = new LinkedList<>();

        // Returns what the board looks like with this model
        public Model rolloutModel(Model m) {
            m = new Model(m);
            for(Action a : actions) {
                m.doAction(a);
            }

            // Finalize with a down
            m.doAction(Action.Down);

            return m;
        }
    }
}
