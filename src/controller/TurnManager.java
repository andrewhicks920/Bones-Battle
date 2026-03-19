package controller;

import model.*;
import view.SwingGameView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.Timer;

public class TurnManager implements ActionListener {
    private final GameState state;
    private final SwingGameView view;
    private final GameConfig config;
    private final GameController controller;
    private Timer timer;

    public TurnManager(GameState state, SwingGameView view, GameConfig config, GameController controller) {
        this.state = state;
        this.view = view;
        this.config = config;
        this.controller = controller;
    }

    public void startTimer() {
        if (timer == null) {
            timer = new Timer(config.speedOfPlay, this);
            timer.setInitialDelay(config.flickerDelay);
        }

        timer.start();
    }

    public void stopTimer() {
        if (timer != null) timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Player current = state.getCurrentPlayer();
        if (current.getName().equals("Human")) {
            timer.stop();
            view.setNextButtonEnabled(true);
        }
        else doComputerTick();
    }

    private void doComputerTick() {
        Player current = state.getCurrentPlayer();
        Map board = state.getBoard();

        if (current.willAttack(board)) {
            Territory attacker = current.getAttacker();
            Territory defender = current.getDefender();

            view.highlightCell(attacker.getRow(), attacker.getCol(), attacker.getOwner().getClickColor());
            try { Thread.sleep(config.flickerDelay); } catch (Exception ignored) {}

            view.highlightCell(defender.getRow(), defender.getCol(), defender.getOwner().getClickColor());
            try { Thread.sleep(config.flickerDelay); } catch (Exception ignored) {}

            AttackResult result = state.processAttack(attacker, defender);
            view.showAttackResult(result);
            view.updateStatusLabels(state);

        }
        else if (state.isGameOver()) handleGameOver();
        else endCurrentTurn();
    }

    private void handleGameOver() {
        timer.stop();
        state.recordWin();
        Player winner = state.getCurrentPlayer();
        view.setTitleText(winner.getName() + " WINS!");

        if (config.tournament) {
            ArrayList<Player> players = new ArrayList<>(state.getPlayers());
            Collections.sort(players);

            if (state.isTournamentOver())
                view.printTournamentVictory(winner.getName(), state.getQtyGames(), players);

            else {
                view.printTournamentStandings(winner.getName(), players, state.getQtyGames());
                Collections.shuffle(state.getPlayers());
                controller.onStartClicked();
            }
        }
    }

    private void endCurrentTurn() {
        int prevIndex = state.getCurrentPlayerIndex();
        state.awardDice(state.getCurrentPlayer());
        view.clearBorder(prevIndex);
        view.notifyBoardUpdated();

        state.advanceTurn();

        Player next = state.getCurrentPlayer();
        view.setBoardCurrentPlayer(next);
        view.setActiveBorder(state.getCurrentPlayerIndex(), next.getColor());
    }

    /** Called when the human player clicks "Next". */
    public void doNextClicked() {
        view.setNextButtonEnabled(false);
        int prevIndex = state.getCurrentPlayerIndex();
        state.awardDice(state.getCurrentPlayer());
        view.clearBorder(prevIndex);
        view.notifyBoardUpdated();

        state.advanceTurn();

        Player next = state.getCurrentPlayer();
        view.setBoardCurrentPlayer(next);
        view.setActiveBorder(state.getCurrentPlayerIndex(), next.getColor());
        timer.start();
    }

    /** Called when the human selects an attack (attacker + defender chosen in BoardPanel). */
    public void doHumanAttack(Territory attacker, Territory defender) {
        AttackResult result = state.processAttack(attacker, defender);
        view.showAttackResult(result);
        view.updateStatusLabels(state);

        if (state.isGameOver()) {
            view.setTitleText("YOU WON!");
            view.setNextButtonEnabled(false);
        }
    }
}
