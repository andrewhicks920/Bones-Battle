package view;

import model.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class StatusPanel extends JPanel {

    public interface ButtonListener {
        void onQuitClicked();
        void onNextClicked();
    }

    private final JLabel[] statusLabel;
    private final JButton nextButton;
    private final ArrayList<Player> players;
    private Map board;

    public StatusPanel(ArrayList<Player> players, Map board, ButtonListener listener) {
        this.players = players;
        this.board = board;
        this.setLayout(new BorderLayout());

        Container statusPane = new Container();
        statusPane.setLayout(new FlowLayout());
        this.add(statusPane, BorderLayout.NORTH);

        Container controlPane = new Container();
        controlPane.setLayout(new FlowLayout());
        this.add(controlPane, BorderLayout.SOUTH);

        statusLabel = new JLabel[players.size()];
        for (int i = 0; i < players.size(); i++) {
            String statusStr = buildStatusString(i);
            if (players.get(i).getName().equals("Human"))
                statusLabel[i] = new JLabel(" \u2665\u2665 " + statusStr + " ");
            else
                statusLabel[i] = new JLabel(" \u2588\u2588 " + statusStr + " ");
            statusLabel[i].setFont(ViewConstants.STAT_LABEL_FONT);
            statusLabel[i].setForeground(players.get(i).getColor());
            statusPane.add(statusLabel[i]);
        }

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> listener.onQuitClicked());
        controlPane.add(quitButton);
        controlPane.add(new JLabel("  "));

        for (int i = 0; i < players.size(); i++) {
            String playerName = players.get(i).getName();
            if (playerName.length() > 8)
                playerName = playerName.substring(0, 8);
            while (8 - playerName.length() >= 2)
                playerName = " " + playerName + " ";
            if (playerName.length() == 7)
                playerName = " " + playerName;
            JLabel nameLabel = new JLabel("   " + playerName + " ");
            nameLabel.setFont(ViewConstants.STAT_LABEL_FONT);
            nameLabel.setForeground(players.get(i).getColor());
            controlPane.add(nameLabel);
        }

        controlPane.add(new JLabel("  "));
        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> listener.onNextClicked());
        nextButton.setEnabled(false);
        controlPane.add(nextButton);
    }

    public void setBoard(Map board) {
        this.board = board;
    }

    public void updateAll(GameState state) {
        this.board = state.getBoard();
        for (int i = 0; i < players.size(); i++) {
            String statusStr = buildStatusString(i);
            if (players.get(i).getName().equals("Human"))
                statusLabel[i].setText(" \u2665\u2665 " + statusStr + " ");
            else
                statusLabel[i].setText(" \u2588\u2588 " + statusStr + " ");
            if (board.countTerritories(players.get(i)) == 0)
                statusLabel[i].setForeground(ViewConstants.ELIMINATION_COLOR);
        }
    }

    public void setActiveBorder(int playerIndex, Color color) {
        statusLabel[playerIndex].setBorder(new LineBorder(color, 2));
    }

    public void clearBorder(int playerIndex) {
        statusLabel[playerIndex].setBorder((Border) null);
    }

    public void setNextButtonEnabled(boolean enabled) {
        nextButton.setEnabled(enabled);
    }

    private String buildStatusString(int playerIndex) {
        String territoryStr = Integer.toString(board.countTerritories(players.get(playerIndex)));
        String statusStr = (territoryStr.length() < 2) ? " " + territoryStr + " (" : territoryStr + " (";
        String connectedStr = Integer.toString(board.countConnected(players.get(playerIndex)));
        statusStr += (connectedStr.length() < 2) ? " " + connectedStr + ")" : connectedStr + ")";
        return statusStr;
    }
}
