package view;

import controller.GameController;
import model.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class SwingGameView extends JFrame implements GameView {

    private final ArrayList<Player> players;
    private GameController controller;
    private Container mainPane;
    private JLabel titleLabel;
    private BoardPanel boardPanel;
    private StatusPanel statusPanel;

    public SwingGameView(ArrayList<Player> players) {
        this.players = players;
        this.setTitle("Bones Battle");
        this.setSize(ViewConstants.WINDOW_WIDTH, ViewConstants.WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainPane = this.getContentPane();
        this.mainPane.setLayout(new BorderLayout(0, 0));

        buildTopBorder();
        buildSideBorders();

        JTextArea directionsArea = new JTextArea(getDirectionsText(), 20, 80);
        directionsArea.setFont(ViewConstants.DIRECTIONS_FONT);
        directionsArea.setEditable(false);
        this.mainPane.add(directionsArea, BorderLayout.CENTER);

        JButton startButton = new JButton("Start!");
        startButton.setFont(ViewConstants.START_BUTTON_FONT);
        startButton.setFocusPainted(false);
        startButton.setPreferredSize(new Dimension(755, 75));
        startButton.addActionListener(e -> { if (controller != null) controller.onStartClicked(); });
        this.mainPane.add(startButton, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    // --- GameView interface ---

    @Override
    public void showBoard(GameState state) {
        mainPane.removeAll();
        mainPane.setLayout(new BorderLayout(50, 0));
        buildTopBorder();
        buildSideBorders();

        boardPanel = new BoardPanel(state.getBoard(), state.getCurrentPlayer(),
            (attacker, defender) -> {
                if (controller != null) controller.onAttackSelected(attacker, defender);
            });
        mainPane.add(boardPanel, BorderLayout.CENTER);

        statusPanel = new StatusPanel(players, state.getBoard(),
            new StatusPanel.ButtonListener() {
                public void onQuitClicked() { if (controller != null) controller.onQuitClicked(); }
                public void onNextClicked() { if (controller != null) controller.onNextClicked(); }
            });
        mainPane.add(statusPanel, BorderLayout.SOUTH);

        mainPane.validate();
        mainPane.repaint();
    }

    @Override
    public void refreshCell(int row, int col, Player owner, int dice) {
        if (boardPanel != null) boardPanel.refreshCell(row, col);
    }

    @Override
    public void highlightCell(int row, int col, Color color) {
        if (boardPanel != null) boardPanel.highlightCell(row, col, color);
    }

    @Override
    public void showAttackResult(AttackResult result) {
        if (boardPanel != null && controller != null)
            boardPanel.showAttackResult(result, controller.getFlickerDelay());
    }

    @Override
    public void updateStatusLabels(GameState state) {
        if (statusPanel != null) statusPanel.updateAll(state);
    }

    @Override
    public void setTitleText(String text) {
        if (titleLabel != null) titleLabel.setText(text);
    }

    @Override
    public void setNextButtonEnabled(boolean enabled) {
        if (statusPanel != null) statusPanel.setNextButtonEnabled(enabled);
    }

    @Override
    public void printTournamentStandings(String winnerName, ArrayList<Player> players, int gameCount) {
        System.out.println("Game " + gameCount + " won by " + winnerName + ".");
        System.out.println("\tStandings: ");
        for (Player p : players) {
            System.out.print(p.getName() + ": " + p.getWins() + "  ");
        }
        System.out.println();
    }

    @Override
    public void printTournamentVictory(String winnerName, int gameCount, ArrayList<Player> standings) {
        System.out.println("\n\tThe Tournament has a victor: " + winnerName + "!");
        System.out.println("\nFinal Standings after " + gameCount + " games:");
        for (Player p : standings) {
            System.out.println(p.getWins() + " wins for " + p.getName());
        }
    }

    // --- Extra methods called by TurnManager / GameController ---

    public void setActiveBorder(int playerIndex, Color color) {
        if (statusPanel != null) statusPanel.setActiveBorder(playerIndex, color);
    }

    public void clearBorder(int playerIndex) {
        if (statusPanel != null) statusPanel.clearBorder(playerIndex);
    }

    /** Repaint all board cells (called after awardDice updates dice counts). */
    public void notifyBoardUpdated() {
        if (boardPanel != null) boardPanel.refreshAllCells();
    }

    /** Update the board panel's click state for the new current player. */
    public void setBoardCurrentPlayer(Player player) {
        if (boardPanel != null) boardPanel.setCurrentPlayer(player);
    }

    // --- Private layout helpers ---

    private void buildTopBorder() {
        Container topPane = new Container();
        topPane.setLayout(new FlowLayout());
        mainPane.add(topPane, BorderLayout.NORTH);

        JLabel spacer = new JLabel("");
        spacer.setPreferredSize(new Dimension(0, 75));
        topPane.add(spacer);

        JLabel[] diceIcons = new JLabel[6];
        for (int i = 0; i < 6; i++) {
            diceIcons[i] = new JLabel(new ImageIcon("images/face" + (i + 1) + "s.png"));
            topPane.add(diceIcons[i]);
        }

        titleLabel = new JLabel(" Bones Battle ");
        titleLabel.setFont(Font.decode("Serif-36"));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPane.add(titleLabel);

        for (int i = 5; i >= 0; i--) {
            diceIcons[i] = new JLabel(new ImageIcon("images/face" + (i + 1) + "s.png"));
            topPane.add(diceIcons[i]);
        }
    }

    private void buildSideBorders() {
        mainPane.add(new JLabel(""), BorderLayout.WEST);
        mainPane.add(new JLabel(""), BorderLayout.EAST);
    }

    private String getDirectionsText() {
        return """

          In this RISK-like dice game, you control the green \
        territories while the computer plays the
          remaining \
        territories.  Each territory is labeled with a quantity of six-sided dice, ranging
          from 1 through 8.  Initially, each player occupies six randomly selected territories
          and is given 18 dice, one per territory plus a dozen scattered randomly. A turn consists of
          0 or more attacks, each launched from a territory  with 2 or more dice toward an adjacent
          enemy territory. To launch an attack, click on a suitable green territory, then on an
          adjacent enemy territory.  The computer will (fairly!) roll and sum your territory's dice and,
          separately, the enemy territory's dice.  If the total of your dice exceeds that of the enemy,
          you win the enemy's territory, and all but one of your territory's dice are moved to it.  If not,
          you retain the attacking territory but just one die.  When you are done attacking, click `Next'.
          A quantity of dice equal to the number of territories in your largest territory cluster will be
          randomly added to your territories, and play continues.  The winner is the player who
          captures all of the territories.  You can see each player's territory and connected cluster
          totals at the bottom of the game window.

          When you are ready to play, click 'Start!', below.""";
    }
}
