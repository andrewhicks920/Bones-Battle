package controller;

import model.*;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class PlayerFactory {

    private PlayerFactory() {}

    /**
     * Creates and returns the shuffled player list based on the parsed config.
     * Loads Strategy instances via StrategyLoader for named computer players,
     * or falls back to ComputerStrategy when no .class file exists (default mode).
     */
    public static ArrayList<Player> createPlayers(GameConfig config) {
        ArrayList<String> nameList = config.playerNames;
        ArrayList<Player> players = new ArrayList<>();

        ArrayList<Color> colorPool = new ArrayList<>();
        colorPool.add(Color.blue);
        colorPool.add(Color.RED.darker());
        colorPool.add(new Color(238, 118, 0));
        colorPool.add(new Color(178, 58, 238));
        colorPool.add(new Color(139, 90, 43));

        int colorIndex = 0;

        for (String name : nameList) {
            if (name.equals("Human")) {
                Player human = new Player(name, Color.GREEN.darker().darker());
                human.setStrategy(null);
                players.add(human);
            } else {
                Player computer = new Player(name, colorPool.get(colorIndex));
                players.add(computer);
                File classFile = new File("./" + name + ".class");
                try {
                    if (classFile.exists()) {
                        StrategyLoader loader = new StrategyLoader();
                        Strategy stratInstance = (Strategy) loader.loadClass(name).newInstance();
                        computer.setStrategy(stratInstance);
                    } else {
                        computer.setStrategy(new ComputerStrategy());
                    }
                    computer.getStrategy().setPlayer(computer);
                } catch (Exception e) {
                    System.out.println("ERROR:  Strategy for " + name + " not loaded due to " + e);
                    System.exit(1);
                }
                colorIndex++;
            }
        }

        Collections.shuffle(players);
        return players;
    }
}
