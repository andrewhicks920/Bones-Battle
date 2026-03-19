# Bones Battle

A Java implementation of the Flash game Dice Wars. Unlike the original, computer players in this version don't cheat!

---

## How to Play

You control the **green** territories. Each territory displays the number of six-sided dice it holds (1–8).

**On your turn:**
1. Click one of your territories with 2+ dice that borders an enemy territory to select it as the attacker.
2. Click an adjacent enemy territory to launch the attack.
3. The game rolls and sums both sides' dice. If your total **exceeds** the enemy's, you capture the territory and all but one of your dice move into it. If not, your territory is reduced to 1 die.
4. Repeat attacks as desired, then click **Next** to end your turn.

**After each turn**, you receive bonus dice equal to the size of your largest connected territory cluster, distributed randomly across your territories.

**Win condition:** Capture all territories.

The status bar at the bottom shows each player's territory count and largest connected cluster size in the format `territories (cluster)`.

---

## Running the Game

Compile all `.java` files in `src/` (including subdirectories) and run `Main`. Command-line arguments control who plays:

```bash
# Default: one human vs. four randomly named computers
java Main

# Human vs. specific strategy files
java Main human MyStrategy.class AnotherStrategy.class

# Tournament mode (no human — first to N wins)
java Main StrategyA.class StrategyB.class StrategyC.class wins=5

# Adjust animation speed (milliseconds per tick)
java Main human speed=500
```

**Argument rules:**
- Include `human` to play yourself; omit it for a fully automated tournament.
- List 2–5 total players.
- `wins=N` sets the number of wins needed to claim the tournament (default: 16).
- `speed=N` sets the animation tick speed in ms (default: 2000 when a human is present, 20 for tournaments).

---

## Writing a Computer Strategy

Implement the `Strategy` interface from the `model` package:

```java
import model.Strategy;
import model.Player;
import model.Map;
import model.Territory;

public class MyStrategy implements Strategy {
    void setPlayer(Player player);
    boolean willAttack(Map board); // Return true if the player wants to attack
    Territory getAttacker(); // Called after willAttack returns true
    Territory getDefender(); // Called after getAttacker
}
```

`willAttack` is always called first. After it returns `true`, `getAttacker` and `getDefender` are called to retrieve the chosen territories. Compile your strategy to a `.class` file in the same directory as the other `.class` files and pass its name as a command-line argument.

Two built-in strategies are included:

| Strategy              | Behavior                                                                                             |
|-----------------------|------------------------------------------------------------------------------------------------------|
| `ComputerStrategy`    | Attacks whenever it has a territory with dice ≥ an adjacent enemy. Picks a random valid attack pair. |
| `MilquetoastStrategy` | Never attacks. Used as the default placeholder strategy assigned to new `Player` objects.            |

---

## Project Structure

### `src/model/` — Game logic, no UI dependencies

| File                       | Description                                                                                                |
|----------------------------|------------------------------------------------------------------------------------------------------------|
| `AttackResult.java`        | Value object returned by `GameState.processAttack` — holds dice sums and the outcome of one attack.        |
| `ComputerStrategy.java`    | Default computer strategy. Attacks when it has a numerical advantage.                                      |
| `GameConfig.java`          | Immutable configuration object parsed from command-line arguments.                                         |
| `GameState.java`           | Mutable game state machine. Owns `processAttack`, `awardDice`, `advanceTurn`, and win tracking.            |
| `Graph.java`               | Adjacency-matrix graph used to track active territories and check connectivity.                            |
| `Map.java`                 | Game board. Handles territory layout, partitioning, dice distribution, neighbor lookups, and connectivity. |
| `MilquetoastStrategy.java` | Passive strategy — never attacks.                                                                          |
| `Player.java`              | Holds a player's name, color, strategy, and win count.                                                     |
| `Strategy.java`            | Interface that all computer strategies must implement.                                                     |
| `StrategyLoader.java`      | Custom `ClassLoader` that loads strategy `.class` files from the working directory at runtime.             |
| `Territory.java`           | Represents a single cell on the board — owner, dice count, and position.                                   |

### `src/view/` — Swing UI code

| File                 | Description                                                                                      |
|----------------------|--------------------------------------------------------------------------------------------------|
| `BoardPanel.java`    | `JPanel` holding the `JButton` grid. Manages the two-click (attacker → defender) selection flow. |
| `GameView.java`      | Interface the controller uses to update the UI. Decouples the controller from Swing.             |
| `StatusPanel.java`   | `JPanel` showing per-player territory/cluster counts plus the Quit and Next buttons.             |
| `SwingGameView.java` | `JFrame` that implements `GameView`. Composes `BoardPanel` and `StatusPanel`.                    |
| `ViewConstants.java` | All shared font, color, and dimension constants.                                                 |

### `src/controller/` — Wires model and view together

| File                  | Description                                                                                                  |
|-----------------------|--------------------------------------------------------------------------------------------------------------|
| `GameController.java` | Top-level orchestrator. Receives all user events (start, quit, next, cell click) and delegates to the model. |
| `PlayerFactory.java`  | Creates and shuffles the player list. Loads custom strategy `.class` files via `StrategyLoader`.             |
| `TurnManager.java`    | Owns the Swing `Timer`. Drives computer turns, handles animation, and routes human attacks.                  |

### `src/` — Entry point and tests

| File                         | Description                                                                              |
|------------------------------|------------------------------------------------------------------------------------------|
| `Main.java`                  | Entry point. Parses args, wires together model/view/controller, and launches the window. |
| `test.BonesBattleTests.java` | JUnit 4 test suite covering `Territory`, `Map`, and `Graph`.                             |

---

## Requirements

- Java 21+
- JUnit 4 (for running tests)
