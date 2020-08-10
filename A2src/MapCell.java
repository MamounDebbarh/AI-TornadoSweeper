
public class MapCell {

    private final int i;
    private final int j;
    private int numberOfadjacentNodes;

    /**
     * constructor
     *
     * @param i
     *            coordinate of the cell along the verticale axsis
     * @param j
     *            coordinate of the cell along the horizontal axsis
     * @param adjacentNodes
     */
    public MapCell(int i, int j, int adjacentNodes) {
        super();
        this.numberOfadjacentNodes = adjacentNodes;
        this.i = i;
        this.j = j;
    }

    /**
     * @return Cells coordinate along the vertical axis
     */
    public int getI() {

        return i;
    }

    @Override
    public String toString() {

        return "(" + j + "," + i + ")";
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + i;
        result = prime * result + j;
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MapCell other = (MapCell) obj;
        if (i != other.i) {
            return false;
        }
        if (j != other.j) {
            return false;
        }
        return true;
    }

    /**
     * returns the horizontal coordinate of the cell
     *
     * @return the cells coordinate along the horizontal axis
     */
    public int getJ() {

        return j;
    }

    /**
     * checks whether the provided cell is adjacent to this one
     *
     * @param other
     *            the other cell which to check for adjacentcy
     * @return true iff the other cell is adjacent to this one
     */
    public boolean isAdjacent(MapCell other) {

        return Math.max(Math.abs(getI() - other.getI()), Math.abs(getJ() - other.getJ())) == 1;
    }

    /**
     * returns the number of adjacent tornados
     *
     * @return the number of adjacent tornados
     */
    public int getNumberOfadjacentNodes() {

        return numberOfadjacentNodes;
    }

    /**
     * sets the number of adjacent tornados
     *
     * @param numberOfadjacentNodes
     *            the number of adjacent tornados
     */
    public void setNumberOfadjacentNodes(int numberOfadjacentNodes) {

        this.numberOfadjacentNodes = numberOfadjacentNodes;
    }

}
