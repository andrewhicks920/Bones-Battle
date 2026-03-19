package model;

public class MilquetoastStrategy implements Strategy {
    Player player;

    public MilquetoastStrategy() {}

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean willAttack(Map board) {
        return false;
    }

    public Territory getAttacker() {
        return null;
    }

    public Territory getDefender() {
        return null;
    }
}
