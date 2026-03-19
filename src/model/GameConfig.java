package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class GameConfig {
    public final ArrayList<String> playerNames;
    public final boolean tournament;
    public final int victory;
    public final int speedOfPlay;
    public final int flickerDelay;
    public final int mapGaps;

    public static final int MAP_ROWS = 5;
    public static final int MAP_COLUMNS = 8;
    public static final int MAX_DICE = 8;

    private GameConfig(ArrayList<String> playerNames, boolean tournament, int victory,
                       int speedOfPlay, int mapGaps) {
        this.playerNames = playerNames;
        this.tournament = tournament;
        this.victory = victory;
        this.speedOfPlay = speedOfPlay;
        this.flickerDelay = speedOfPlay / 5;
        this.mapGaps = mapGaps;
    }

    public static GameConfig fromArgs(String[] args) {
        ArrayList<String> nameList = new ArrayList<>();
        boolean speedSet = false;
        boolean tournament = true;
        int speedOfPlay = 20;
        int victory = 16;

        for (String arg : args) {
            if (arg.equalsIgnoreCase("human")) {
                nameList.add("Human");
                tournament = false;
            } else if (arg.startsWith("wins=")) {
                victory = Integer.parseInt(arg.substring(5));
            } else if (arg.startsWith("speed=")) {
                speedOfPlay = Integer.parseInt(arg.substring(6));
                speedSet = true;
            } else if (arg.endsWith(".class")) {
                nameList.add(arg.substring(0, arg.length() - 6));
            } else {
                nameList.add(arg);
            }
        }

        // Remove duplicate "Human" entries, keeping only the first.
        boolean humanFound = false;
        ArrayList<Integer> duplicateIndices = new ArrayList<>();
        for (int i = 0; i < nameList.size(); i++) {
            String name = nameList.get(i);
            if (name.equals("Human") && !humanFound) {
                humanFound = true;
            } else if (name.equals("Human")) {
                duplicateIndices.add(i);
            }
        }
        for (int i = duplicateIndices.size() - 1; i >= 0; i--) {
            nameList.remove((int) duplicateIndices.get(i));
        }

        // If a human player is included and speed wasn't explicitly set, use a slower pace.
        if (!speedSet) {
            for (String name : nameList) {
                if (name.equals("Human")) {
                    speedOfPlay = 2000;
                    break;
                }
            }
        }

        if (nameList.isEmpty()) {
            nameList.add("Human");
            tournament = false;
            if (!speedSet) speedOfPlay = 2000;

            String[] defaultNames = {"Altos", "Amstrad", "Bondwell", "Cromemco",
                                     "Heathkit", "Kaypro", "Osborne", "Sinclair", "Tandy"};
            ArrayList<String> computerNames = new ArrayList<>();
            Collections.addAll(computerNames, defaultNames);
            Collections.shuffle(computerNames);
            nameList.add(computerNames.get(0));
            nameList.add(computerNames.get(1));
            nameList.add(computerNames.get(2));
            nameList.add(computerNames.get(3));

        } else if (nameList.size() >= 2 && nameList.size() <= 5) {
            boolean allValid = true;
            for (String stratName : nameList) {
                if (stratName.equals("Human")) continue;
                File classFile = new File("./" + stratName + ".class");
                if (!classFile.exists() || !classFile.canRead()) {
                    allValid = false;
                    System.out.println("ERROR: Strategy " + stratName
                            + ".class either doesn't exist or can't be read.");
                }
            }
            if (!allValid) System.exit(1);
        } else {
            System.out.println("\nERROR:  Argument list is invalid.\n");
            System.out.println("Arguments are to be names of strategy .class files.\n"
                    + "Include 'human' if you wish to play against the computer players.\n"
                    + "List 2, 3, 4, or 5 names.  No arguments defaults\n"
                    + " human playing against 4 computer players.\n\n"
                    + "Tournament Mode: List only .class file names.  First to 10\n"
                    + " wins is the winner.  Use arg. of 'wins=#' to change.\n");
            System.exit(1);
        }

        int mapGaps = (nameList.size() == 4) ? 8 : 10;
        return new GameConfig(nameList, tournament, victory, speedOfPlay, mapGaps);
    }
}
