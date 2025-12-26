package game.screens;

import engine.core.Game;
import engine.input.Input;
import engine.screen.Screen;
import game.logic.ScreenLogic;

public class LogicScreen implements Screen {
    protected final Game game;
    protected final ScreenLogic logic;
    private final boolean captureCursor;

    private boolean pendingMouseReset = false;

    public LogicScreen(Game game, ScreenLogic logic, boolean captureCursor) {
        this.game = game;
        this.logic = logic;
        this.captureCursor = captureCursor;
    }

    @Override
    public void init() {
        game.getWindow().setCursorCaptured(captureCursor);
        pendingMouseReset = captureCursor;
        logic.init(game);
    }

    @Override
    public void handleInput(Input input) {
        if (pendingMouseReset) {
            input.resetMouse();
            pendingMouseReset = false;
        }

        if (game.getWindow().isResize()) {
            logic.onResize(game);
        }

        logic.handleInput(game, input);
    }

    @Override
    public void update(double dt, Input input) {
        logic.update(dt, input);
    }

    @Override
    public void render(Input input) {
        logic.render(game, input);
    }

    @Override
    public void cleanup() {
        logic.cleanup();
    }
}
