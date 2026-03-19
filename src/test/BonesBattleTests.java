package test;

import model.*;

import java.util.ArrayList;
import java.awt.Color;
import java.util.List;
import java.util.Objects;
import org.junit.Test;

import static org.junit.Assert.*;

public class BonesBattleTests {
    @Test
    public void testTerritoryConstructor1(){
        ArrayList<Player> dummyPlayers = new ArrayList<>();
        Player dummy = new Player("a", Color.RED);
        dummyPlayers.add(dummy);

        Map dummyMap = new Map(dummyPlayers, 4,5, 8, 9);

        Territory dummyTerritory = new Territory(dummyMap);

        // Check that the constructor actually stored all the values we put into it.
        assertEquals(dummyTerritory.getMap(), dummyMap);
        assertNull(dummyTerritory.getOwner());
        assertEquals(-1, dummyTerritory.getDice());
        assertEquals(-1, dummyTerritory.getIdNum());

    }

    @Test
    public void testTerritoryConstructor2(){
        ArrayList<Player> dummyPlayers = new ArrayList<>();
        Player dummy = new Player("a", Color.RED);
        dummyPlayers.add(dummy);

        Map dummyMap = new Map(dummyPlayers, 4,5, 8, 9);

        Territory dummyTerritory = new Territory(dummyMap, dummy, 5, 3);

        // Check that the constructor actually stored all the values we put into it.
        assertEquals(dummyTerritory.getMap(), dummyMap);
        assertEquals(dummyTerritory.getOwner(), dummy);
        assertEquals(5, dummyTerritory.getDice());
        assertEquals(3, dummyTerritory.getIdNum());

    }

    // skip testing getters because we did that earlier.

    @Test
    public void testSetDice(){
        ArrayList<Player> dummyPlayers = new ArrayList<>();
        Player dummy = new Player("a", Color.RED);
        dummyPlayers.add(dummy);

        Map dummyMap = new Map(dummyPlayers, 4,5, 8, 9);
        Territory dummyTerritory = new Territory(dummyMap, dummy, 5, 3);

        // try setting dice to new number then checking change reflected.
        dummyTerritory.setDice(38239);
        assertEquals(38239, dummyTerritory.getDice());
    }

    @Test
    public void testIdNum(){
        ArrayList<Player> dummyPlayers = new ArrayList<>();
        Player dummy = new Player("a", Color.RED);
        dummyPlayers.add(dummy);

        Map dummyMap = new Map(dummyPlayers, 4,5, 8, 9);
        Territory dummyTerritory = new Territory(dummyMap, dummy, 5, 3);

        // try setting id and seeing changes reflected
        dummyTerritory.setIdNum(38239);
        assertEquals(38239, dummyTerritory.getIdNum());
    }

    @Test
    public void testOwner(){
        ArrayList<Player> dummyPlayers = new ArrayList<>();
        Player dummy = new Player("a", Color.RED);
        Player dummy2 = new Player("b", Color.BLUE);
        dummyPlayers.add(dummy);
        dummyPlayers.add(dummy2);

        Map dummyMap = new Map(dummyPlayers, 4,5, 8, 9);
        Territory dummyTerritory = new Territory(dummyMap, dummy, 5, 3);

        // try setting owner and seeing changes reflected
        dummyTerritory.setOwner(dummy2);
        assertEquals(dummyTerritory.getOwner(), dummy2);
    }

    @Test
    public void testGetRow(){
        /*
         Warning! This isn't a comprehensive check, it's just a spot check. Probably if you pass this test though,
         you've got the row formula correct.
        */

        ArrayList<Player> dummyPlayers = new ArrayList<>();
        Player dummy = new Player("a", Color.RED);
        Player dummy2 = new Player("b", Color.BLUE);
        dummyPlayers.add(dummy);
        dummyPlayers.add(dummy2);

        Map dummyMap = new Map(dummyPlayers, 5,8, 8, 9);

        Territory dummyTerritory = new Territory(dummyMap, dummy, 5, 3);

        assertEquals(0, dummyTerritory.getRow());

        dummyTerritory.setIdNum(10);
        assertEquals(1, dummyTerritory.getRow());

        dummyTerritory.setIdNum(20);
        assertEquals(2, dummyTerritory.getRow());

        dummyTerritory.setIdNum(31);
        assertEquals(3, dummyTerritory.getRow());

        dummyTerritory.setIdNum(39);
        assertEquals(4, dummyTerritory.getRow());
    }

    @Test
    public void testGetCol() {
        /*
         Warning! This isn't a comprehensive check, it's just a spot check. Probably if you pass this test though,
         you've got the row formula correct.
        */

        ArrayList<Player> dummyPlayers = new ArrayList<>();
        Player dummy = new Player("a", Color.RED);
        Player dummy2 = new Player("b", Color.BLUE);
        dummyPlayers.add(dummy);
        dummyPlayers.add(dummy2);

        Map dummyMap = new Map(dummyPlayers, 5,8, 8, 9);

        Territory dummyTerritory = new Territory(dummyMap, dummy, 5, 3);

        assertEquals(3, dummyTerritory.getCol());

        dummyTerritory.setIdNum(10);
        assertEquals(2, dummyTerritory.getCol());

        dummyTerritory.setIdNum(20);
        assertEquals(4, dummyTerritory.getCol());

        dummyTerritory.setIdNum(31);
        assertEquals(7, dummyTerritory.getCol());

        dummyTerritory.setIdNum(39);
        assertEquals(7, dummyTerritory.getCol());
    }

    @Test
    public void testMapConstructor(){
        ArrayList<Player> dummyPlayers = new ArrayList<>();
        Player dummy = new Player("a", Color.RED);
        Player dummy2 = new Player("b", Color.BLUE);
        dummyPlayers.add(dummy);
        dummyPlayers.add(dummy2);
        Map dummyMap = new Map(dummyPlayers, 5,8, 8, 9);

        // Test constants and instance variables initalized correctly.
        assertEquals(5, dummyMap.ROWS);
        assertEquals(8, dummyMap.COLUMNS);
        assertEquals(8, dummyMap.VICTIMS);
        assertEquals(40, dummyMap.NUMTERRITORIES);
        assertEquals(32, dummyMap.OCCUPIED);
        assertEquals(9, dummyMap.MAXDICE);

        // Test that game board initialized with territories
        for(int i = 0; i < dummyMap.getMap().length; i++){
            for(int j = 0; j < dummyMap.getMap()[0].length; j++){
                assertNotNull(dummyMap.getMap()[i][j]);
            }
        }

        // Spot check that ids are correctly assigned.
        assertEquals(0, dummyMap.getMap()[0][0].getIdNum());
        assertEquals(8, dummyMap.getMap()[1][0].getIdNum());
        assertEquals(19, dummyMap.getMap()[2][3].getIdNum());
        assertEquals(39, dummyMap.getMap()[4][7].getIdNum());

        // Spot check ids using getTerritory
        assertEquals(0, dummyMap.getTerritory(0, 0).getIdNum());
        assertEquals(8, dummyMap.getTerritory(1,0).getIdNum());
        assertEquals(19, dummyMap.getTerritory(2,3).getIdNum());
        assertEquals(39, dummyMap.getTerritory(4,7).getIdNum());

        // Check that A graph was created (not that it's correct)
        assertNotNull(dummyMap.getGraph());

    }

    // no need to test getters and setters since we called all of them above.

    @Test
    public void testGetTerritoryId(){
        ArrayList<Player> dummyPlayers = new ArrayList<>();
        Player dummy = new Player("a", Color.RED);
        Player dummy2 = new Player("b", Color.BLUE);
        dummyPlayers.add(dummy);
        dummyPlayers.add(dummy2);
        Map dummyMap = new Map(dummyPlayers, 5,8, 8, 9);

        assertEquals(0, dummyMap.getTerritoryId(0, 0));
        assertEquals(8, dummyMap.getTerritoryId(1,0));
        assertEquals(19, dummyMap.getTerritoryId(2,3));
        assertEquals(39, dummyMap.getTerritoryId(4,7));

    }

    @Test
    public void testCountTerritories(){
        ArrayList<Player> dummyPlayers = new ArrayList<>();
        Player dummy = new Player("a", Color.RED);
        Player dummy2 = new Player("b", Color.BLUE);
        dummyPlayers.add(dummy);
        dummyPlayers.add(dummy2);
        Map dummyMap = new Map(dummyPlayers, 5,8, 8, 9);

        // 2 players, 32 active territories, so each player should have 16
        assertEquals(16, dummyMap.countTerritories(dummy));
        assertEquals(16, dummyMap.countTerritories(dummy2));

    }

    @Test
    public void testCountTerritoriesOddSplit(){
        ArrayList<Player> dummyPlayers = new ArrayList<>();
        Player dummy = new Player("a", Color.RED);
        Player dummy2 = new Player("b", Color.BLUE);
        dummyPlayers.add(dummy);
        dummyPlayers.add(dummy2);
        Map dummyMap = new Map(dummyPlayers, 5,8, 9, 9);

        // 2 players, 31 active territories, so each player should have 15 or 16
        assertTrue(dummyMap.countTerritories(dummy) == 16 || dummyMap.countTerritories(dummy) == 15);
        assertTrue(dummyMap.countTerritories(dummy2) == 16 || dummyMap.countTerritories(dummy2) == 15);
    }

    @Test
    public void testCountDice(){
        ArrayList<Player> dummyPlayers = new ArrayList<>();
        Player dummy = new Player("a", Color.RED);
        Player dummy2 = new Player("b", Color.BLUE);
        dummyPlayers.add(dummy);
        dummyPlayers.add(dummy2);
        Map dummyMap = new Map(dummyPlayers, 5,8, 8, 9);

        // 16 active territories each, so 48 per player.
        assertEquals(48, dummyMap.countDice(dummy));
        assertEquals(48, dummyMap.countDice(dummy2));
    }

    @Test
    public void testGetPropertyOf(){
        ArrayList<Player> dummyPlayers = new ArrayList<>();
        Player dummy = new Player("a", Color.RED);
        Player dummy2 = new Player("b", Color.BLUE);
        dummyPlayers.add(dummy);
        dummyPlayers.add(dummy2);
        Map dummyMap = new Map(dummyPlayers, 5,8, 8, 9);

        assertEquals(16, dummyMap.getPropertyOf(dummy).size());
        assertEquals(16, dummyMap.getPropertyOf(dummy2).size());

        for(Territory territory:dummyMap.getPropertyOf(dummy)){
            assertEquals(territory.getOwner(), dummy);
        }

        for(Territory territory:dummyMap.getPropertyOf(dummy2)){
            assertEquals(territory.getOwner(), dummy2);
        }
    }

    @Test
    public void testGetNeighbors(){
        // WARNING this is not comprehensive, we only check adjacency middle and corners

        ArrayList<Player> dummyPlayers = new ArrayList<>();
        Player dummy = new Player("a", Color.RED);
        dummyPlayers.add(dummy);

        // create a map where dummy will own all territories, so we can check adjacency easily
        Map dummyMap = new Map(dummyPlayers, 3,3, 0, 9);

        // corners
        ArrayList<Territory> adj = dummyMap.getNeighbors(dummyMap.getTerritory(0,0));
        assertTrue(adj.contains(dummyMap.getTerritory(0, 1)));
        assertTrue(adj.contains(dummyMap.getTerritory(1, 0)));
        assertEquals(2, adj.size());

        adj =  dummyMap.getNeighbors(dummyMap.getTerritory(0,2));
        assertTrue(adj.contains(dummyMap.getTerritory(0, 1)));
        assertTrue(adj.contains(dummyMap.getTerritory(1, 2)));
        assertEquals(2, adj.size());

        adj =  dummyMap.getNeighbors(dummyMap.getTerritory(2,2));
        assertTrue(adj.contains(dummyMap.getTerritory(2, 1)));
        assertTrue(adj.contains(dummyMap.getTerritory(1, 2)));
        assertEquals(2, adj.size());

        adj =  dummyMap.getNeighbors(dummyMap.getTerritory(2,0));
        assertTrue(adj.contains(dummyMap.getTerritory(1, 0)));
        assertTrue(adj.contains(dummyMap.getTerritory(2, 1)));
        assertEquals(2, adj.size());

        adj =  dummyMap.getNeighbors(dummyMap.getTerritory(1,1));
        assertTrue(adj.contains(dummyMap.getTerritory(0, 1)));
        assertTrue(adj.contains(dummyMap.getTerritory(1, 2)));
        assertTrue(adj.contains(dummyMap.getTerritory(1, 0)));
        assertTrue(adj.contains(dummyMap.getTerritory(2, 1)));
        assertEquals(4, adj.size());

    }

    @Test
    public void testGetEnemyNeighbors(){
        // WARNING this is not comprehensive.

        ArrayList<Player> dummyPlayers = new ArrayList<>();
        Player dummy = new Player("a", Color.RED);
        Player dummy2 = new Player("b", Color.BLUE);
        dummyPlayers.add(dummy);

        // create a map where dummy will own all territories so we can check adjacency easily
        Map dummyMap = new Map(dummyPlayers, 3,3, 0, 9);

        // set middle to be owned by dummy2
        dummyMap.getTerritory(1,1).setOwner(dummy2);

        // corners
        ArrayList<Territory> adj =  dummyMap.getEnemyNeighbors(dummyMap.getTerritory(0,0));
        assertEquals(0, adj.size());

        adj =  dummyMap.getEnemyNeighbors(dummyMap.getTerritory(0,2));
        assertEquals(0, adj.size());

        adj =  dummyMap.getEnemyNeighbors(dummyMap.getTerritory(2,2));
        assertEquals(0, adj.size());

        adj =  dummyMap.getEnemyNeighbors(dummyMap.getTerritory(2,0));
        assertEquals(0, adj.size());

        adj =  dummyMap.getEnemyNeighbors(dummyMap.getTerritory(1,1));
        assertTrue(adj.contains(dummyMap.getTerritory(0, 1)));
        assertTrue(adj.contains(dummyMap.getTerritory(1, 2)));
        assertTrue(adj.contains(dummyMap.getTerritory(2, 1)));
        assertTrue(adj.contains(dummyMap.getTerritory(1, 0)));
        assertEquals(4, adj.size());

    }

    @Test
    public void testPartition(){
        ArrayList<Player> dummyPlayers = new ArrayList<>();
        Player dummy = new Player("a", Color.RED);
        Player dummy2 = new Player("b", Color.BLUE);
        dummyPlayers.add(dummy);
        dummyPlayers.add(dummy2);
        Map dummyMap = new Map(dummyPlayers, 5,8, 8, 9);

        ArrayList<Integer> whoOwnsWhatById = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 8; j++){
                if(dummyMap.getTerritory(i,j).getOwner() != null)
                    whoOwnsWhatById.add(dummyMap.getTerritory(i,j).getOwner().getId());
            }
        }

        // should have a different partition because random assign each time
        dummyMap = new Map(dummyPlayers, 5,8, 8, 9);
        ArrayList<Integer> whoOwnsWhatById2 = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 8; j++){
                if(dummyMap.getTerritory(i,j).getOwner() != null){
                    whoOwnsWhatById2.add(dummyMap.getTerritory(i,j).getOwner().getId());
                }
            }
        }

        boolean same = true;
        for(int i = 0; i < whoOwnsWhatById.size(); i++){
            if (!Objects.equals(whoOwnsWhatById.get(i), whoOwnsWhatById2.get(i))) {
                same = false;
                break;
            }
        }
        assertFalse(same);
    }

    @Test
    public void testDistributeDice(){
        ArrayList<Player> dummyPlayers = new ArrayList<>();
        Player dummy = new Player("a", Color.RED);
        Player dummy2 = new Player("b", Color.BLUE);
        dummyPlayers.add(dummy);
        dummyPlayers.add(dummy2);
        Map dummyMap = new Map(dummyPlayers, 5,8, 8, 9);

        ArrayList<Integer> diceCountsForDummy1 = new ArrayList<>();

        for(Territory dummyterritory: dummyMap.getPropertyOf(dummy)){
            diceCountsForDummy1.add(dummyterritory.getDice());
        }

        dummyMap = new Map(dummyPlayers, 5,8, 8, 9);
        ArrayList<Integer> diceCountsForDummy1_2 = new ArrayList<>();

        for(Territory dummyterritory: dummyMap.getPropertyOf(dummy)){
            diceCountsForDummy1_2.add(dummyterritory.getDice());
        }

        boolean same = true;
        for (int i = 0; i < diceCountsForDummy1.size(); i++){
            if (!Objects.equals(diceCountsForDummy1.get(i), diceCountsForDummy1_2.get(i))) {
                same = false;
                break;
            }
        }
        assertFalse(same);

        for(Territory dummyterritory: dummyMap.getPropertyOf(dummy)){
            assertTrue(dummyterritory.getDice() >= 1 && dummyterritory.getDice() <= dummyMap.MAXDICE);
        }
        for(Territory dummyterritory: dummyMap.getPropertyOf(dummy2)){
            assertTrue(dummyterritory.getDice() >= 1 && dummyterritory.getDice() <= dummyMap.MAXDICE);
        }
    }

    @Test
    public void testCountConnected(){
        ArrayList<Player> dummyPlayers = new ArrayList<>();
        Player dummy = new Player("a", Color.RED);
        Player dummy2 = new Player("b", Color.BLUE);
        dummyPlayers.add(dummy);
        dummyPlayers.add(dummy2);
        Map dummyMap = new Map(dummyPlayers, 3,3, 0, 9);

        dummyMap.getTerritory(0,0).setOwner(dummy);
        dummyMap.getTerritory(1,0).setOwner(dummy);
        dummyMap.getTerritory(2,0).setOwner(dummy);
        dummyMap.getTerritory(2,2).setOwner(dummy);

        dummyMap.getGraph().removeVertex(1);
        dummyMap.getGraph().removeVertex(2);
        dummyMap.getGraph().removeVertex(7);

        dummyMap.getTerritory(0,1).setOwner(null);
        dummyMap.getTerritory(0,2).setOwner(null);
        dummyMap.getTerritory(2,1).setOwner(null);

        dummyMap.getTerritory(1,1).setOwner(dummy2);
        dummyMap.getTerritory(1,2).setOwner(dummy2);

        assertEquals(3, dummyMap.countConnected(dummy));
        assertEquals(2, dummyMap.countConnected(dummy2));
    }

    @Test
    public void testConstructGraph(){
        ArrayList<Player> dummyPlayers = new ArrayList<>();
        Player dummy = new Player("a", Color.RED);
        Player dummy2 = new Player("b", Color.BLUE);
        dummyPlayers.add(dummy);
        dummyPlayers.add(dummy2);
        Map dummyMap = new Map(dummyPlayers, 5,8, 8, 9);

        assertEquals(40 - dummyMap.getGraph().getUnusedVertices().size(), dummyMap.OCCUPIED);

        List<Integer> idsUnused = dummyMap.getGraph().getUnusedVertices();

        dummyMap = new Map(dummyPlayers, 5,8, 8, 9);
        List<Integer> idsUnused2 = dummyMap.getGraph().getUnusedVertices();

        assertFalse(idsUnused.containsAll(idsUnused2) && idsUnused2.containsAll(idsUnused));

        assertTrue(dummyMap.getGraph().connected());
    }

    @Test
    public void testGraphConstructor(){
        Graph test = new Graph(10);

        for (int i = 0; i < 10; i++)
            assertEquals(0, test.getAdjacent(i).size());

        test.addEdge(0, 1);
        test.addEdge(0, 2);
        test.addEdge(0, 3);
        test.addEdge(0, 4);
        test.addEdge(0, 5);
        test.addEdge(0, 6);
        test.addEdge(0, 7);
        test.addEdge(0, 8);
        test.addEdge(0, 9);
        test.removeVertex(3);
        assertEquals(1, test.getUnusedVertices().size());

        assertEquals(3, (int) test.getUnusedVertices().getFirst());
    }

    @Test
    public void testIsEdge(){
        Graph test = new Graph(10);

        test.addEdge(1,2);
        assertTrue(test.isEdge(2,1));
        test.addEdge(2,1);
        assertTrue(test.isEdge(1,2));
        test.removeEdge(1,2);
        assertFalse(test.isEdge(1,2));
    }

    @Test
    public void testIsInGraph(){
        Graph test = new Graph(10);

        test.addEdge(0, 1);
        test.addEdge(0, 2);
        test.addEdge(0, 3);
        test.addEdge(0, 4);
        test.addEdge(0, 5);
        test.addEdge(0, 6);
        test.addEdge(0, 7);
        test.addEdge(0, 8);
        test.addEdge(0, 9);

        for(int i = 0; i < 10; i++)
            assertTrue(test.isInGraph(i));

        test.removeVertex(3);

        for(int i = 0; i < 10; i++){
            if(i != 3) assertTrue(test.isInGraph(i));
            else assertFalse(test.isInGraph(i));
        }
    }

    @Test
    public void testRemoveVertex(){
        Graph test = new Graph(10);

        test.removeVertex(3);

        assertFalse(test.isInGraph(3));
        assertEquals(0, test.getAdjacent(3).size());
        assertEquals(0, test.degree(3));
    }

    @Test
    public void testGetAdjacent() {
        Graph test = new Graph(10);
        test.addEdge(1,2);
        test.addEdge(1,3);
        test.removeVertex(3);

        assertEquals(2, (int) test.getAdjacent(1).getFirst());
        assertEquals(1, test.getAdjacent(1).size());
    }

    @Test
    public void testDegree(){
        Graph test = new Graph(10);
        test.addEdge(1,2);
        test.addEdge(1,3);

        assertEquals(2, test.degree(1));

        test.removeVertex(1);

        assertEquals(0, test.degree(1));
    }

    @Test
    public void testConnected(){
        // We're only going to test one connected, one unconnected here.

        Graph connectedTest = new Graph(10);
        connectedTest.addEdge(1,2);
        connectedTest.addEdge(2,3);
        connectedTest.addEdge(1,5);
        connectedTest.addEdge(3,4);
        connectedTest.removeVertex(0);
        connectedTest.removeVertex(6);
        connectedTest.removeVertex(7);
        connectedTest.removeVertex(8);
        connectedTest.removeVertex(9);
        assertTrue(connectedTest.connected());

        Graph disconnectedTest = new Graph(10);
        disconnectedTest.addEdge(1,2);
        disconnectedTest.addEdge(2,3);
        disconnectedTest.addEdge(1,5);
        disconnectedTest.addEdge(4,7);
        disconnectedTest.removeVertex(0);
        disconnectedTest.removeVertex(6);
        disconnectedTest.removeVertex(8);
        disconnectedTest.removeVertex(9);
        assertFalse(disconnectedTest.connected());
    }
}
