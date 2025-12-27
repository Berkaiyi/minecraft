package game.screens;

import engine.core.Game;
import engine.input.Input;
import engine.screen.Screen;
import engine.util.Log;
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
        Log.info("Screen", "init(): %s (cursorCaptured=%s)",
                logic.getClass().getSimpleName(), captureCursor);

        game.getWindow().setCursorCaptured(captureCursor);
        pendingMouseReset = captureCursor;
        logic.init(game);
    }

    @Override
    public void handleInput(Input input) {
        if (pendingMouseReset) {
            Log.debug("Screen", "Mouse reset for %s", logic.getClass().getSimpleName());
            input.resetMouse();
            pendingMouseReset = false;
        }

        if (game.getWindow().isResize()) {
            Log.debug("Screen", "Resize seen by %s (fb=%dx%d)",
                    logic.getClass().getSimpleName(),
                    game.getWindow().getFbWidth(),
                    game.getWindow().getFbHeight());
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
        Log.info("Screen", "cleanup: %s", logic.getClass().getSimpleName());
        logic.cleanup();
    }
}
