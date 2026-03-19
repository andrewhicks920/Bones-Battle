package model;

import java.util.ArrayList;

public class GameState {
    private final ArrayList<Player> players;
    private final GameConfig config;
    private Map board;
    private int turnCounter;
    private int currentPlayerIndex;
    private Player currentPlayer;
    private int qtyGames;

    public GameState(ArrayList<Player> players, GameConfig config) {
        this.players = players;
        this.config = config;
        this.qtyGames = 0;
    }

    public void newGame() {
        this.board = new Map(players, GameConfig.MAP_ROWS, GameConfig.MAP_COLUMNS,
                             config.mapGaps, GameConfig.MAX_DICE);
        this.turnCounter = 0;
        this.currentPlayerIndex = 0;
        this.currentPlayer = players.get(0);
    }

    public Map getBoard() { return board; }
    public ArrayList<Player> getPlayers() { return players; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    public int getTurnCounter() { return turnCounter; }
    public int getQtyGames() { return qtyGames; }

    public boolean isGameOver() {
        return board.countTerritories(currentPlayer) == board.OCCUPIED;
    }

    public boolean isTournamentOver() {
        return currentPlayer.getWins() == config.victory;
    }

    /**
     * Resolves one attack: rolls dice, updates Territory ownership and dice
     * counts, and returns an AttackResult for the view to animate.
     */
    public AttackResult processAttack(Territory attacker, Territory defender) {
        int attackSum = rollDice(attacker.getDice());
        int defendSum = rollDice(defender.getDice());
        boolean attackerWon = attackSum > defendSum;

        if (attackerWon) {
            defender.setOwner(attacker.getOwner());
            defender.setDice(attacker.getDice() - 1);
            attacker.setDice(1);
        } else {
            attacker.setDice(1);
        }

        return new AttackResult(attacker, defender, attackSum, defendSum, attackerWon);
    }

    private int rollDice(int numDice) {
        int sum = 0;
        for (int i = 0; i < numDice; i++) {
            sum += (int) (Math.random() * 6.0 + 1.0);
        }
        return sum;
    }

    /**
     * Awards end-of-turn dice to the given player based on their largest
     * connected territory cluster.
     */
    public void awardDice(Player player) {
        int connected = board.countConnected(player);
        int diceToAward = board.MAXDICE * board.countTerritories(player) - board.countDice(player);
        ArrayList<Territory> territories = board.getPropertyOf(player);

        if (connected >= diceToAward) {
            for (Territory territory : territories) {
                territory.setDice(board.MAXDICE);
            }
        } else {
            for (int i = 0; i < connected; i++) {
                Territory territory;
                do {
                    int randIdx = (int) (Math.random() * territories.size());
                    territory = territories.get(randIdx);
                } while (territory.getDice() >= board.MAXDICE);
                territory.setDice(territory.getDice() + 1);
            }
        }
    }

    /**
     * Advances turnCounter past any players with 0 territories.
     */
    public void advanceTurn() {
        do {
            ++turnCounter;
            currentPlayerIndex = turnCounter % players.size();
            currentPlayer = players.get(currentPlayerIndex);
        } while (board.countTerritories(currentPlayer) < 1);
    }

    /**
     * Records a win for the current player and increments the game counter.
     */
    public void recordWin() {
        ++qtyGames;
        currentPlayer.incrementWins();
    }
}
