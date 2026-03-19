package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class Map {
    private Territory[][] map;
    private Graph graph;
    private ArrayList<Player> players;

    /*
    If we imagine a point in a Graph or Territory[][] as the center of the
    entire unit in a 2D-plane (0,0), we can leverage cardinal directionality to
    help find neighbors or enemies
     */
    private static final int[][] DIRECTIONS = {
            {0, 1},  // Right
            {1, 0},  // Down
            {0, -1}, // Left
            {-1, 0}  // Up
    };

    public final int ROWS;
    public final int COLUMNS;
    public final int VICTIMS;
    public final int NUMTERRITORIES;
    public final int OCCUPIED;
    public final int MAXDICE;

    public Map(ArrayList<Player> players, int rows, int columns, int victims, int maxDice) {
        this.players = players;
        this.ROWS = rows;
        this.COLUMNS = columns;
        this.VICTIMS = victims;
        this.MAXDICE = maxDice;
        this.NUMTERRITORIES = ROWS * COLUMNS;
        this.OCCUPIED = NUMTERRITORIES - VICTIMS;

        this.map = new Territory[ROWS][COLUMNS];

        do { partitionTerritories(); }
        while (!areTerritoriesConnected(map));

        this.graph = constructGraph(ROWS, COLUMNS, VICTIMS);
        distributeDice();
    }

    public Territory[][] getMap() {
        return map;
    }

    public Graph getGraph() {
        return graph;
    }

    public Territory getTerritory(int row, int column) {
        return map[row][column];
    }

    public int getTerritoryId(int row, int column) {
        return row * COLUMNS + column;
    }

    public int countTerritories(Player player) {
        int count = 0;
        for (Territory[] row : map) {
            for (Territory territory : row) {
                if (territory.getOwner() == player)
                    count++;
            }
        }
        return count;
    }

    public int countDice(Player player) {
        int count = 0;
        for (Territory[] row : map) {
            for (Territory territory : row) {
                if (territory.getOwner() == player)
                    count += territory.getDice();
            }
        }
        return count;
    }

    public ArrayList<Territory> getNeighbors(Territory cell) {
        ArrayList<Territory> neighbors = new ArrayList<>();
        int row = cell.getRow();
        int col = cell.getCol();

        for (int[] direction : DIRECTIONS) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLUMNS) {
                Territory neighbor = map[newRow][newCol];
                if (graph.isInGraph(neighbor.getIdNum()) && (neighbor.getOwner() != null))
                    neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    public ArrayList<Territory> getEnemyNeighbors(Territory cell) {
        ArrayList<Territory> enemyNeighbors = getNeighbors(cell);
        for (int i = enemyNeighbors.size() - 1; i >= 0; i--) {
            Territory temp = enemyNeighbors.get(i);
            if ((temp.getOwner() == cell.getOwner()))
                enemyNeighbors.remove(temp);
        }
        return enemyNeighbors;
    }

    public ArrayList<Territory> getPropertyOf(Player player) {
        ArrayList<Territory> properties = new ArrayList<>();
        for (Territory[] row : map) {
            for (Territory territory : row) {
                if (territory.getOwner() == player)
                    properties.add(territory);
            }
        }
        return properties;
    }

    private void partitionTerritories() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                map[row][col] = null;
            }
        }

        Random random = new Random();
        Set<Integer> totalTerritories = new HashSet<>();

        int territoriesPerPlayer = OCCUPIED / players.size();
        int remainder = OCCUPIED % players.size();

        for (Player player : players) {
            Set<Integer> uniqueVertices = new HashSet<>();

            while (uniqueVertices.size() < territoriesPerPlayer) {
                int randomIndex = random.nextInt(NUMTERRITORIES);
                if (!totalTerritories.contains(randomIndex)) {
                    totalTerritories.add(randomIndex);
                    uniqueVertices.add(randomIndex);
                }
            }

            for (Integer vertex : uniqueVertices) {
                int row = vertex / COLUMNS;
                int col = vertex % COLUMNS;
                if (map[row][col] == null)
                    map[row][col] = new Territory(this, player, 0, vertex);
            }
        }

        while (remainder > 0) {
            Player randomPlayer = players.get(random.nextInt(players.size()));
            int randomVertex = random.nextInt(NUMTERRITORIES);

            while (totalTerritories.contains(randomVertex))
                randomVertex = random.nextInt(NUMTERRITORIES);

            totalTerritories.add(randomVertex);
            int row = randomVertex / COLUMNS;
            int col = randomVertex % COLUMNS;
            map[row][col] = new Territory(this, randomPlayer, 0, randomVertex);
            remainder--;
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (map[row][col] == null) {
                    int id = row * COLUMNS + col;
                    map[row][col] = new Territory(this, null, -1, id);
                }
            }
        }
    }

    private void distributeDice() {
        Random random = new Random();
        for (Player player : players) {
            ArrayList<Territory> territories = getPropertyOf(player);
            int totalDice = 3 * countTerritories(player);

            for (Territory territory : territories) {
                territory.setDice(1);
                totalDice -= 1;
            }

            while (totalDice > 0) {
                boolean addedDice = false;
                for (Territory territory : territories) {
                    if (territory.getDice() < MAXDICE && totalDice > 0) {
                        int diceToAdd = random.nextInt(Math.min(MAXDICE - territory.getDice(), totalDice)) + 1;
                        territory.setDice(territory.getDice() + diceToAdd);
                        totalDice -= diceToAdd;
                        addedDice = true;
                    }
                }
                if (!addedDice) break;
            }
        }
    }

    public int countConnected(Player player) {
        ArrayList<Integer> playerTerritories = new ArrayList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                Territory temp = map[r][c];
                if (temp.getOwner() == player)
                    playerTerritories.add(temp.getIdNum());
            }
        }

        if (playerTerritories.isEmpty())
            return 0;

        Set<Integer> visited = new HashSet<>();
        int largestClusterSize = 0;

        for (int id : playerTerritories) {
            if (!visited.contains(id)) {
                int clusterSize = componentSize(id, playerTerritories, visited);
                largestClusterSize = Math.max(largestClusterSize, clusterSize);
            }
        }

        return largestClusterSize;
    }

    private int componentSize(int startId, ArrayList<Integer> playerTerritories, Set<Integer> visited) {
        Stack<Integer> stack = new Stack<>();
        Set<Integer> playerTerritoriesSet = new HashSet<>(playerTerritories);
        stack.push(startId);
        visited.add(startId);

        int componentSize = 0;

        while (!stack.isEmpty()) {
            int current = stack.pop();
            componentSize++;

            ArrayList<Integer> neighbors = graph.getAdjacent(current);
            for (int neighbor : neighbors) {
                if (playerTerritoriesSet.contains(neighbor) && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    stack.push(neighbor);
                }
            }
        }

        return componentSize;
    }

    public Graph constructGraph(int rows, int cols, int victims) {
        int totalTerritories = rows * cols;
        int invisibleTerritories = totalTerritories - victims;

        Graph graph = new Graph(totalTerritories);

        int activeIndex = 0;
        int[] activeIds = new int[ROWS * COLUMNS];

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                Territory temp = map[i][j];
                if (temp.getOwner() != null)
                    activeIds[activeIndex++] = temp.getIdNum();
            }
        }

        for (int i = 0; i < activeIndex; i++) {
            for (int j = 0; j < activeIndex; j++) {
                if (i != j)
                    graph.addEdge(activeIds[i], activeIds[j]);
            }
        }

        return graph;
    }

    public boolean areTerritoriesConnected(Territory[][] territories) {
        int rows = territories.length;
        int cols = territories[0].length;

        boolean[][] visited = new boolean[rows][cols];
        boolean foundTerritory = false;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (territories[r][c].getOwner() != null) {
                    dfs(territories, visited, r, c);
                    foundTerritory = true;
                    break;
                }
            }
            if (foundTerritory)
                break;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if ((territories[i][j].getOwner() != null) && !visited[i][j])
                    return false;
            }
        }

        return true;
    }

    private void dfs(Territory[][] territories, boolean[][] visited, int row, int col) {
        if (row < 0 || row >= territories.length || col < 0 || col >= territories[0].length)
            return;
        if (visited[row][col] || (territories[row][col].getOwner() == null))
            return;

        visited[row][col] = true;

        for (int[] direction : DIRECTIONS)
            dfs(territories, visited, row + direction[0], col + direction[1]);
    }
}
