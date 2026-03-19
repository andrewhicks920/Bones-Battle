import controller.*;
import model.*;
import view.*;

import java.util.ArrayList;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException |
                 InstantiationException | IllegalAccessException ignored) {}

        SwingUtilities.invokeLater(() -> {
            GameConfig config = GameConfig.fromArgs(args);
            ArrayList<Player> players = PlayerFactory.createPlayers(config);
            GameState state = new GameState(players, config);
            SwingGameView view = new SwingGameView(players);
            GameController controller = new GameController(state, view, config);
            view.setController(controller);
        });
    }
}
