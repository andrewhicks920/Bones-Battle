package model;

import java.util.ArrayList;

public class ComputerStrategy implements Strategy {
    Player player;
    Territory attacker = null;
    ArrayList<TPair> targetPairs = null;

    public ComputerStrategy() {}

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean willAttack(Map board) {
        this.targetPairs = new ArrayList<>();
        ArrayList<Territory> territories = board.getPropertyOf(this.player);

        for (Territory ownedTerritory : territories) {
            ArrayList<Territory> enemies = board.getEnemyNeighbors(ownedTerritory);
            for (Territory enemy : enemies) {
                if (ownedTerritory.getDice() > 1 && ownedTerritory.getDice() >= enemy.getDice()) {
                    TPair pair = new TPair();
                    pair.attacker = ownedTerritory;
                    pair.defender = enemy;
                    this.targetPairs.add(pair);
                }
            }
        }

        return !this.targetPairs.isEmpty();
    }

    public Territory getAttacker() {
        if (this.targetPairs == null) {
            return null;
        }
        int idx = (int) (Math.random() * this.targetPairs.size());
        return this.attacker = this.targetPairs.get(idx).attacker;
    }

    public Territory getDefender() {
        if (this.attacker != null) {
            for (TPair pair : this.targetPairs) {
                if (pair.attacker == this.attacker)
                    return pair.defender;
            }
        }
        return null;
    }

    static class TPair {
        public Territory attacker;
        public Territory defender;
    }
}
