package model;

/*
Strategy interface, where any player strategy must be able to:
    1. Determine if the player will attack, given current board
    2. Produce reference to player's territory that will be the source of the attack
    3. Produce reference to enemy territory that the attacker is to attack

It is expected that willAttack(Map) is called before getAttacker() and getDefender() are called.
 */
public interface Strategy {
    public void setPlayer(Player whom);
    public boolean willAttack(Map board);
    public Territory getAttacker();
    public Territory getDefender();
}
