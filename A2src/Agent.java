
import java.util.HashSet;
import java.util.Set;


public class Agent {

    private KnowledgeBase kb;
    private Strategy strat = null;
    private Main game = null;
    private int probeCounter = 0;
    private final int numberOftornados;
    private boolean done = false;


    /**
     * @param kb              the knowledge base of the agent
     * @param numberOfNettels number of tornados present in the world
     */
    public Agent(KnowledgeBase kb, int numberOfNettels) {
        this.kb = kb;
        this.numberOftornados = numberOfNettels;
    }
    /**
     * resets internal state of the agent.
     */
    public void reset() {

        kb.reset();
        strat.reset();
        probeCounter = 0;
        done = false;
    }

    /**
     * set the reference to the game. Needed for probing. Not set in the
     * constructor to avoid circular dependency in agent creation
     *
     * @param game
     */
    public void setGame(Main game) {

        this.game = game;
    }

    /**
     * @param strat strategy to use in solving the problem. needed to avoid
     *              circular dependency in agent creation
     */
    public void setStrat(Strategy strat) {

        this.strat = strat;
    }

    /**
     * as per the spec, the first more is always on 0,0
     */
    public void firstMove() {

        probe(kb.getCellAt(0, 0));
        probe(kb.getCellAt(kb.getMapDim() / 2, kb.getMapDim() / 2));
    }

    /**
     * repeadedly makes moves to uncover the world.
     */
    public void uncoverWorld() {

        while (!done) {
            if (kb.getFlaggedCells().size() == numberOftornados) {
                // we found every tornado, so reveal all the hidden cells
                Set<MapCell> tempSet = new HashSet<MapCell>();
                tempSet.addAll(kb.getHiddenCells());
                for (MapCell cell : tempSet) {
                    probe(cell);
                }
            }
            if (kb.getHiddenCells().size() == (numberOftornados - kb.getFlaggedCells().size())) {
                // all hidden cells must be tornados, so flag all of them
                Set<MapCell> tempSet = new HashSet<MapCell>();
                tempSet.addAll(kb.getHiddenCells());
                for (MapCell cell : tempSet) {
                    flag(cell);
                }
            }

            // if there are no more hidden cells we are done
            if (kb.getNumberOfHiddenCells() == 0) {
                done = true;
                Main.setFailed(false);
                return;
            }

            // determine move according to strategy
            for (MapCell move : strat.deterimeMove()) {
                if (Main.gameOver) {
                    return;
                }
                if (strat.shouldProbe()) {
                    probe(move);
                } else {
                    flag(move);
                }
            }
        }
    }

    /**
     * increases probecouner by 1
     */
    public void incrProbeCounter() {

        probeCounter += 1;
    }

    /**
     * Flag the cell as containing a tornado
     *
     * @param cell the cell to be flagged
     */
    public void flag(MapCell cell) {

        System.out.println("\t" + "Flagging: " + cell.toString());
        kb.flag(cell);
        kb.printMap();


    }

    /**
     * same as probe but doesn't increase the count. mainly to be used for
     * uncovering neigbours with a cell with value 0
     *
     * @param cell the cell to be revealed
     */
    public void reveal(MapCell cell) {

        System.out.println("\t" + "Revealing: " + cell.toString());
        int numberOftornados = game.probe(cell);
        kb.reveal(cell, numberOftornados);
        kb.printMap();

        //should we reveal any neighbours?
        if (numberOftornados == 0) {
            for (MapCell safeNeighbour : kb.getHiddenNeighbours(cell)) {
                reveal(safeNeighbour);
            }

        }
    }

    /**
     * Reveals the cell, increases the probe counter and records the number of
     * tornados
     *
     * @param cell the cell to be probed
     */
    public void probe(MapCell cell) {

        // don't probe revealed cells
        if (kb.getRevealedCells().contains(cell)) {
            return;
        }

        System.out.println("\t" + "Probing: " + cell.toString());

        incrProbeCounter();
        int numb = game.probe(cell);
        //update the knowledge base
        kb.reveal(cell, numb);
        kb.printMap();


        // should we reveal any neighbours?
        if (numb == 0) {
            for (MapCell safeNeighbour : kb.getHiddenNeighbours(cell)) {
                reveal(safeNeighbour);
            }

        }

    }

    /**
     * sets the done variable to the given value.
     *
     * @param b boolean to set the done value to
     */
    public void setDone(boolean b) {

        done = b;

    }

}
