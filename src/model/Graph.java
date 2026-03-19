package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Graph {
    private final int numVertices;
    private int[][] adjacencyMatrix;
    private Set<Integer> inactiveVertices;

    /*
    Constructor for initializing a Graph object, n number of vertices. Given
    that all vertices will be considered inactive at the start, we'll add each
    one to a 'master' set-list, which keeps track of inactive vertices

    Pre-condition: numVertices is a positive integer
    Post-condition: Graph will be an abstraction of a 2d-matrix with numVertices
                    rows and numVertices columns, and will have each of its
                    vertices considered 'inactive'

    Parameters:
    numVertices (int):

    Return: None
     */
    public Graph(int numVertices) {
        this.numVertices = numVertices;
        this.adjacencyMatrix = new int[numVertices][numVertices];
        this.inactiveVertices = new HashSet<>();

        for (int i = 0; i < numVertices; i++)
            inactiveVertices.add(i);
    }

    public ArrayList<Integer> getUnusedVertices() {
        return new ArrayList<>(inactiveVertices);
    }

    public boolean isEdge(int source, int destination) {
        boolean wayOne = adjacencyMatrix[source][destination] == 1;
        boolean wayTwo = adjacencyMatrix[destination][source] == 1;
        return wayOne && wayTwo && isInGraph(source) && isInGraph(destination);
    }

    public void addEdge(int source, int destination) {
        inactiveVertices.remove(source);
        inactiveVertices.remove(destination);
        adjacencyMatrix[source][destination] = 1;
        adjacencyMatrix[destination][source] = 1;
    }

    public void removeEdge(int source, int destination) {
        adjacencyMatrix[source][destination] = 0;
        adjacencyMatrix[destination][source] = 0;
    }

    public boolean isInGraph(int vertex) {
        return !inactiveVertices.contains(vertex);
    }

    public void removeVertex(int vertex) {
        inactiveVertices.add(vertex);
        for (int i = 0; i < adjacencyMatrix.length; i++)
            adjacencyMatrix[i][vertex] = 0;
        for (int i = 0; i < adjacencyMatrix[0].length; i++)
            adjacencyMatrix[0][i] = 0;
    }

    public ArrayList<Integer> getAdjacent(int vertex) {
        ArrayList<Integer> adjacentVertices = new ArrayList<>();
        if (inactiveVertices.contains(vertex))
            return adjacentVertices;

        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if (adjacencyMatrix[vertex][i] == 1 && !inactiveVertices.contains(i))
                adjacentVertices.add(i);
        }
        return adjacentVertices;
    }

    public int degree(int vertex) {
        if (inactiveVertices.contains(vertex))
            return 0;
        int degree = 0;
        for (int i = 0; i < adjacencyMatrix[vertex].length; i++)
            degree += adjacencyMatrix[vertex][i];
        return degree;
    }

    public boolean connected() {
        int startVertex = -1;
        for (int i = 0; i < numVertices; i++) {
            if (!inactiveVertices.contains(i)) {
                startVertex = i;
                break;
            }
        }
        if (startVertex == -1)
            return false;

        Set<Integer> visited = new HashSet<>();
        dfs(startVertex, visited);

        for (int i = 0; i < numVertices; i++) {
            if (!inactiveVertices.contains(i) && !visited.contains(i))
                return false;
        }
        return true;
    }

    private void dfs(int vertex, Set<Integer> visited) {
        visited.add(vertex);
        for (int i = 0; i < numVertices; i++) {
            boolean edgeCheck = adjacencyMatrix[vertex][i] == 1;
            if (edgeCheck && !inactiveVertices.contains(i) && !visited.contains(i))
                dfs(i, visited);
        }
    }
}
