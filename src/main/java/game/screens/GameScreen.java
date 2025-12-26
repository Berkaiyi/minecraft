package game.screens;

import engine.core.Game;
import engine.input.Action;
import engine.input.Input;
import engine.screen.Screen;
import game.logic.VoxelGame;

public class GameScreen implements Screen {

    private final Game game;
    private VoxelGame logic;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void handleInput(Input input) {
        if (input.isActionDown(Action.EXIT)) { game.setScreen(new MainMenuScreen(game)); }
    }

    @Override
    public void init() {
        game.getWindow().setCursorCaptured(true);
        logic = new VoxelGame();
        logic.init();
    }

    @Override
    public void update(double dt, Input input) {
        logic.update(dt, input);
    }

    @Override
    public void render(Input input) {
        logic.render(input);
    }

    @Override
    public void cleanup() {
        if (logic != null) logic.cleanup();
    }
}
