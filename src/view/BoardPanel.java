package view;

import model.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class BoardPanel extends JPanel {

    public interface AttackListener {
        void onAttackSelected(Territory attacker, Territory defender);
    }

    private JButton[][] chart;
    private final Map board;
    private Player currentPlayer;
    private Territory attackFrom = null;
    private boolean preAttack = true;
    private final AttackListener listener;

    public BoardPanel(Map board, Player currentPlayer, AttackListener listener) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.listener = listener;
        buildGrid();
    }

    private void buildGrid() {
        this.setLayout(new GridLayout(board.ROWS, board.COLUMNS));
        this.chart = new JButton[board.ROWS][board.COLUMNS];

        for (int r = 0; r < board.ROWS; r++) {
            for (int c = 0; c < board.COLUMNS; c++) {
                Territory territory = board.getTerritory(r, c);
                chart[r][c] = new JButton(Integer.toString(territory.getDice()));
                chart[r][c].setPreferredSize(new Dimension(80, 80));

                if (territory.getOwner() == null) {
                    chart[r][c].setText("");
                    chart[r][c].setContentAreaFilled(false);
                    chart[r][c].setBorderPainted(false);
                    chart[r][c].setRolloverEnabled(false);
                } else {
                    final int row = r;
                    final int col = c;
                    chart[r][c].addActionListener(e -> handleCellClick(row, col));
                    chart[r][c].setFont(ViewConstants.QTY_BUTTON_FONT);
                    chart[r][c].setFocusPainted(false);
                    chart[r][c].setBackground(territory.getOwner().getColor());
                    chart[r][c].setForeground(ViewConstants.QTY_BUTTONTEXT_COLOR);
                }
                this.add(chart[r][c]);
            }
        }
    }

    /** Called when starting a new turn so click state resets for the new player. */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
        this.attackFrom = null;
        this.preAttack = true;
    }

    /** Repaint one cell to reflect its current model state. */
    public void refreshCell(int row, int col) {
        Territory territory = board.getTerritory(row, col);
        if (territory.getOwner() != null) {
            chart[row][col].setBackground(territory.getOwner().getColor());
            chart[row][col].setForeground(ViewConstants.QTY_BUTTONTEXT_COLOR);
            chart[row][col].setFont(ViewConstants.QTY_BUTTON_FONT);
            chart[row][col].setText(Integer.toString(territory.getDice()));
        }
    }

    /** Set a cell's background (used for highlighting during attack animation). */
    public void highlightCell(int row, int col, Color color) {
        chart[row][col].setBackground(color);
        chart[row][col].update(chart[row][col].getGraphics());
    }

    /**
     * Displays dice sums on the attacking and defending cells, pauses, then
     * restores them to their normal appearance.
     */
    public void showAttackResult(AttackResult result, int flickerDelay) {
        int attackRow = result.attacker.getRow();
        int attackCol = result.attacker.getCol();
        int defendRow = result.defender.getRow();
        int defendCol = result.defender.getCol();

        chart[attackRow][attackCol].setText(Integer.toString(result.attackSum));
        chart[defendRow][defendCol].setText(Integer.toString(result.defendSum));
        chart[attackRow][attackCol].setFont(ViewConstants.SUM_BUTTON_FONT);
        chart[defendRow][defendCol].setFont(ViewConstants.SUM_BUTTON_FONT);

        Color resultColor = result.attackerWon
                ? ViewConstants.SUM_BUTTONTEXT_WIN_COLOR
                : ViewConstants.SUM_BUTTONTEXT_LOSS_COLOR;
        chart[attackRow][attackCol].setForeground(resultColor);
        chart[defendRow][defendCol].setForeground(resultColor);

        chart[attackRow][attackCol].update(chart[attackRow][attackCol].getGraphics());
        chart[defendRow][defendCol].update(chart[defendRow][defendCol].getGraphics());

        try { Thread.sleep(2L * flickerDelay); } catch (Exception ignored) {}

        refreshCell(attackRow, attackCol);
        refreshCell(defendRow, defendCol);
    }

    /** Repaint every owned cell (called after awardDice updates dice counts). */
    public void refreshAllCells() {
        for (int r = 0; r < board.ROWS; r++) {
            for (int c = 0; c < board.COLUMNS; c++) {
                if (board.getTerritory(r, c).getOwner() != null) {
                    refreshCell(r, c);
                }
            }
        }
    }

    private void handleCellClick(int clickRow, int clickCol) {
        Territory clicked = board.getTerritory(clickRow, clickCol);

        if (preAttack) {
            if (clicked.getOwner() == currentPlayer
                    && clicked.getDice() >= 2
                    && !board.getEnemyNeighbors(clicked).isEmpty()) {
                attackFrom = clicked;
                preAttack = false;
                chart[clickRow][clickCol].setBackground(currentPlayer.getClickColor());
                chart[clickRow][clickCol].update(chart[clickRow][clickCol].getGraphics());
            }
            return;
        }

        if (clicked == attackFrom) {
            chart[clickRow][clickCol].setBackground(currentPlayer.getColor());
            chart[clickRow][clickCol].update(chart[clickRow][clickCol].getGraphics());
            attackFrom = null;
            preAttack = true;
            return;
        }

        if (!board.getEnemyNeighbors(attackFrom).contains(clicked)) {
            return;
        }

        Territory from = attackFrom;
        chart[clickRow][clickCol].setBackground(clicked.getOwner().getClickColor());
        chart[clickRow][clickCol].update(chart[clickRow][clickCol].getGraphics());
        attackFrom = null;
        preAttack = true;

        listener.onAttackSelected(from, clicked);
    }
}
