

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class KnowledgeBase {

    private MapCell[][] map = null;
    private int mapDim = -1;
    private final Set<MapCell> hiddenCells;
    private final Set<MapCell> revealedCells;
    private final Set<MapCell> flaggedCells;
    private final int numberOfNettels;

    /**
     * reset the internal state of the knowledge base. Resets all cells to be
     * hidden
     */
    public void reset() {

        hiddenCells.addAll(revealedCells);
        hiddenCells.addAll(flaggedCells);
        revealedCells.clear();
        flaggedCells.clear();
    }

    /**
     * Constructor. Taken and addapted from A2
     *
     * @param mapDim
     *            dimension of the world
     * @param NumberOftornados
     *            number of tornados present in the world.
     */
    public KnowledgeBase(int mapDim, int NumberOftornados) {
        this.mapDim = mapDim;
        this.numberOfNettels = NumberOftornados;
        map = new MapCell[mapDim][mapDim];
        hiddenCells = new HashSet<MapCell>();
        revealedCells = new HashSet<MapCell>();
        flaggedCells = new HashSet<MapCell>();

        for (int i = 0; i < getMapDim(); i++) {
            for (int j = 0; j < getMapDim(); j++) {
                // -8 is a random out of bounds value that should never be displayed
                map[i][j] = new MapCell(i, j, -8);
                hiddenCells.add(map[i][j]);
            }
        }

    }

    /**
     * returns a Set containing all the flagged cells
     *
     * @return Set containing all the flagged cells
     */
    public Set<MapCell> getFlaggedCells() {

        return flaggedCells;
    }

    /**
     * returns a Set containing all the revealed cells
     *
     * @return Set containing all the revealed cells
     */
    public Set<MapCell> getRevealedCells() {

        return revealedCells;
    }

    /**
     * returns the number of hidden cells left in the world
     *
     * @return the number of hidden cells left in the world
     */
    public int getNumberOfHiddenCells() {

        return hiddenCells.size();
    }

    /**
     * returns a Set containing all the hidden cells
     *
     * @return Set containing all the hidden cells
     */
    public Set<MapCell> getHiddenCells() {

        return hiddenCells;
    }

    /**
     * returns a Set containing the fronteir of the agent (i.e. all revealed
     * cells that have at least one hidden neighbour)
     *
     * @return Set containing the agent' fronteir
     */
    public Collection<MapCell> getFronteir() {

        final Collection<MapCell> answer = new HashSet<MapCell>();
        for (final MapCell cell : getRevealedCells()) {
            if (getHiddenNeighbours(cell).size() > 0 && !flaggedCells.contains(cell)) {
                answer.add(cell);
            }
        }

        return answer;
    }

    /**
     * returns a string representing a cell to be used in the displaying of the
     * map
     *
     * @param cell
     *            the cell which to print
     * @return as string containing the appropriate representation of a cell to
     *         be used in the map printing
     */
    public String toMapString(MapCell cell) {

        if (hiddenCells.contains(cell) || flaggedCells.contains(cell)) {
            if (flaggedCells.contains(cell)) {
                return "  F";
            } else {
                return "  ?";
            }
        } else {
            return String.format("%3d", cell.getNumberOfadjacentNodes());
        }
    }

    /**
     * Prints the map in a readable format. Taken and addapted from A2
     */
    public void printMap() {

        for (int i = 0; i < getMapDim(); i++) {
//            System.out.print(Main.tabs);
            for (int j = 0; j < getMapDim(); j++) {
                System.out.print(toMapString(getCellAt(i, j)));
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * returns the cell in the map at the given coordinates. Taken and addapted
     * from A2
     *
     * @param i
     *            vertical coordinate
     * @param j
     *            horizontal coordinate
     * @return the cell at the given coordinate
     */
    public MapCell getCellAt(int i, int j) {

        return map[i][j];
    }

    /**
     * @return returns the dimension of the map
     */
    public int getMapDim() {

        return mapDim;
    }


    /**
     * finds all neighbours to a given cell. Taken and addapted from A2
     *
     * @param cell
     *            the cell who's neighbours to return
     * @return a list containing the neighbours of the provided cell
     */
    public List<MapCell> getNeighbours(MapCell cell) {

        final List<MapCell> answer = new LinkedList<MapCell>();
        final int i = cell.getI();
        final int j = cell.getJ();
        for (int k = -1; k < 2; k++) { // go thorough all adjacent cells
            //HERE
            for (int l = -1; l < 2; l++) {
                if (i + k >= 0 && i + k < getMapDim() && j + l >= 0 && j + l < getMapDim() //is the neighbour in bounds?
                        && !(k == 0 && l == 0) //don't select the cell itself
                        && !(k == 1 && l == -1) //don't select the down-left cell
                        && !(k == -1 && l == 1) //don't select the top-right cell
                        ) {
                    answer.add(getCellAt(i + k, j + l));
                }
            }
        }
        return answer;
    }

    /**
     * finds all hidde nneighbours to a give cell
     *
     * @param cell
     *            the cell who's neighbours to return
     * @return a list containing the hidden neighbours of the provided cell
     */
    public List<MapCell> getHiddenNeighbours(MapCell cell) {

        //get all neighbours and remove the ones that are not hidden
        final List<MapCell> answer = getNeighbours(cell);
        for (final Iterator<MapCell> iterator = answer.iterator(); iterator.hasNext();) {
            final MapCell neighbour = iterator.next();
            if (!hiddenCells.contains(neighbour)) {
                iterator.remove();
            }
        }

        return answer;
    }

    /**
     * finds all flagged neighbours to a give cell
     *
     * @param cell
     *            the cell who's neighbours to return
     * @return a list containing the flagged neighbours of the provided cell
     */
    public List<MapCell> getFlaggedNeighbours(MapCell cell) {

        //get all neighbours and remove the ones that are not flagged
        final List<MapCell> answer = getNeighbours(cell);
        for (final Iterator<MapCell> iterator = answer.iterator(); iterator.hasNext();) {
            final MapCell neighbour = iterator.next();
            if (!flaggedCells.contains(neighbour)) {
                iterator.remove();
            }
        }

        return answer;
    }

    /**
     * @return number of tornados present in the current world
     */
    public int getNumberOfNettels() {

        return numberOfNettels;
    }

    /**
     * flag a given cell as containing a tornado
     *
     * @param cell
     *            the cell to be flagged
     */
    public void flag(MapCell cell) {

        hiddenCells.remove(cell);
        flaggedCells.add(cell);
    }

    /**
     * reveal a given cell
     *
     * @param cell
     *            the cell to be revealed
     * @param numberOfadjacentNodes
     *            the number of tornados adjacent to that cell as returned by the
     *            game
     */
    public void reveal(MapCell cell, int numberOfadjacentNodes) {

        hiddenCells.remove(cell);
        revealedCells.add(cell);
        cell.setNumberOfadjacentNodes(numberOfadjacentNodes);

    }

}
