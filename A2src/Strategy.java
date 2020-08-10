import java.util.List;

public interface Strategy {

    /**
     * Deterimne the next cells to be probed or flagged according to the
     * relevant strategy
     *
     * @return a list of cells to either probe or flag
     */
    public List<MapCell> deterimeMove();


    /**
     * increases the random guess counter by 1
     */
    public void incrRandomGuessCounter();

    /**
     * Make a random move
     *
     * @return a list containing one cell to probe
     */
    public List<MapCell> randomMove();

    /**
     * returns whether the list of returned cells is to be probed or flagged
     *
     * @return true iff the agent should probe
     */
    public boolean shouldProbe();

    /**
     * Reset the internal state of the strategy (i.e. the random guess counter)
     */
    public void reset();

}