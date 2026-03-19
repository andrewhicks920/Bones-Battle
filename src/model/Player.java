package model;

import java.awt.Color;

public class Player implements Comparable<Player> {
    private static boolean DEBUG = false;
    public static int playerQty = 0;
    private int id;
    private String name;
    private Strategy strategy = null;
    private Color color;
    private Color clickColor;
    private int wins;

    public Player(String name, Color color) {
        this.id = playerQty++;
        this.name = name;
        this.color = color;
        this.clickColor = color.darker();
        this.strategy = new MilquetoastStrategy();
        this.strategy.setPlayer(this);
        this.wins = 0;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Strategy getStrategy() {
        return this.strategy;
    }

    public Color getColor() {
        return this.color;
    }

    public Color getClickColor() {
        return this.clickColor;
    }

    public int getWins() {
        return this.wins;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setClickColor(Color clickColor) {
        this.clickColor = clickColor;
    }

    public void incrementWins() {
        ++this.wins;
    }

    public boolean willAttack(Map board) {
        return this.strategy.willAttack(board);
    }

    public Territory getAttacker() {
        return this.strategy.getAttacker();
    }

    public Territory getDefender() {
        return this.strategy.getDefender();
    }

    public int compareTo(Player other) {
        return this.name.compareTo(other.getName());
    }
}
