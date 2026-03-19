package controller;

import model.*;
import view.SwingGameView;

public class GameController {

    private final GameState state;
    private final SwingGameView view;
    private final GameConfig config;
    private final TurnManager turnManager;

    public GameController(GameState state, SwingGameView view, GameConfig config) {
        this.state = state;
        this.view = view;
        this.config = config;
        this.turnManager = new TurnManager(state, view, config, this);
    }

    public int getFlickerDelay() {
        return config.flickerDelay;
    }

    /** Called when the Start button is clicked (also called to restart in tournament mode). */
    public void onStartClicked() {
        state.newGame();
        view.showBoard(state);
        view.setActiveBorder(state.getCurrentPlayerIndex(),
                             state.getCurrentPlayer().getColor());
        turnManager.startTimer();
    }

    /** Called when the Quit button is clicked. */
    public void onQuitClicked() {
        System.exit(0);
    }

    /** Called when the human player clicks "Next" to end their turn. */
    public void onNextClicked() {
        turnManager.doNextClicked();
    }

    /** Called by BoardPanel when the human has selected an attacker and defender. */
    public void onAttackSelected(Territory attacker, Territory defender) {
        turnManager.doHumanAttack(attacker, defender);
    }
}
