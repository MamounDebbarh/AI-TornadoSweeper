
public class Main {


    private char[][] world;
    public static boolean gameOver = false;
    public static boolean failed = false;

    Agent agent;
    private int numberOftornados;

    private int runsUntillSucess = 0;
    private final int maxRuns = 1000;



    @SuppressWarnings("javadoc")
    public static boolean hasFailed() {

        return failed;
    }

    @SuppressWarnings("javadoc")
    public static void setFailed(boolean failed) {

        Main.failed = failed;
    }


    public void startGame() {

        do {
            System.out.println("\t" + "Starting new game");
            agent.reset();
            agent.firstMove();
            agent.uncoverWorld();
            runsUntillSucess++;
        } while (failed && runsUntillSucess < maxRuns);
        System.out.println("run: " + runsUntillSucess);

    }


    /**
     * probes a cell and returns the appropriate number
     *
     * @param cell the cell to be probed
     * @return the number of tornados adjacent ot the provided cell.
     */
    public int probe(MapCell cell) {

        char numbOftornados = world[cell.getI()][cell.getJ()];
        if (numbOftornados == 't') {
            failed = true;
            agent.setDone(true);
        }

        return Character.getNumericValue(numbOftornados);

    }

    /**
     * @param agent            the agent that will reveal the world
     * @param world            the world the agent will traverse
     * @param numberOftornados number of tornados present in the world
     * @param verbose          should the agin print the steps it takes?
     */
    public Main(Agent agent, char[][] world, int numberOftornados, boolean verbose) {
        this.world = world;
        this.numberOftornados = numberOftornados;
        this.agent = agent;

    }

    public static void main(String[] args) {
        try {
            // argument handler
            String Choice = args[0];
            String world = args[1];
            char[][] map = World.valueOf(world).map;
            int mapDim = map.length;
            int numberOfTornados = 0;
            switch (mapDim) {
                case 3:
                    numberOfTornados = 3;
                    break;
                case 5:
                    numberOfTornados = 5;
                    break;
                case 7:
                    numberOfTornados = 10;
                    break;
                case 11:
                    numberOfTornados = 28;
                    break;
                default:
                    System.out.println("wrong world format");
                    System.exit(1);
            }


            KnowledgeBase kb = new KnowledgeBase(mapDim, numberOfTornados);
            Strategy strat = null;
            switch (args[0]) {
                case "RPX":
                    strat = new RandomGuessStrategy(kb);
                    break;
                case "SPX":
                    strat = new SmartStrat(kb);
                    break;
                case "SATX":
                    System.out.println("SAT solver not implemented");
                    System.exit(1);
                default:
                    System.out.println("wrong world format");
                    System.exit(1);
            }
            Agent agent = new Agent(kb, numberOfTornados);
            Main game = new Main(agent, map, numberOfTornados, true);
            Board board = new Board(map);
            board.printBoard();
            agent.setGame(game);
            agent.setStrat(strat);
            game.startGame();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}
