package model;

public class AttackResult {
    public final Territory attacker;
    public final Territory defender;
    public final int attackSum;
    public final int defendSum;
    public final boolean attackerWon;

    public AttackResult(Territory attacker, Territory defender, int attackSum, int defendSum, boolean attackerWon) {
        this.attacker = attacker;
        this.defender = defender;
        this.attackSum = attackSum;
        this.defendSum = defendSum;
        this.attackerWon = attackerWon;
    }
}
