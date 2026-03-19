package view;

import model.*;
import java.awt.Color;
import java.util.ArrayList;

public interface GameView {
    /** Replace welcome screen with the game board UI. */
    void showBoard(GameState state);

    /** Refresh a single cell's background color and dice text. */
    void refreshCell(int row, int col, Player owner, int dice);

    /** Set a cell's background to the given color (used for highlighting). */
    void highlightCell(int row, int col, Color color);

    /** Display attack sums on cells, pause, then restore normal appearance. */
    void showAttackResult(AttackResult result);

    /** Refresh all player status labels at the bottom. */
    void updateStatusLabels(GameState state);

    /** Set the title label text (e.g., "YOU WON!", "Altos WINS!"). */
    void setTitleText(String text);

    /** Enable or disable the Next button. */
    void setNextButtonEnabled(boolean enabled);

    /** Print mid-tournament standings to stdout. */
    void printTournamentStandings(String winnerName, ArrayList<Player> players, int gameCount);

    /** Print tournament victory message to stdout. */
    void printTournamentVictory(String winnerName, int gameCount, ArrayList<Player> standings);
}
