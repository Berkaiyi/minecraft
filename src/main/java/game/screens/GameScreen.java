package game.screens;

import engine.core.Game;
import engine.input.Action;
import engine.input.Input;
import engine.screen.Screen;
import game.MyGame;

public class GameScreen implements Screen {

    private final Game game;
    private MyGame myGame;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void init() {
        game.getWindow().setCursorCaptured(true);
        myGame = new MyGame();
        myGame.init();
    }

    @Override
    public void update(double dt, Input input) {
        myGame.update(dt, input);

        if (input.isActionDown(Action.EXIT)) game.setScreen(new MainMenuScreen(game));
    }

    @Override
    public void render(Input input) {
        myGame.render(input);
    }

    @Override
    public void cleanup() {
        if (myGame != null) myGame.cleanup();
    }
}
