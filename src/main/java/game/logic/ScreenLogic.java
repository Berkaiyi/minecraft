package game.logic;

import engine.core.Game;
import engine.input.Input;

public interface ScreenLogic {
    void init(Game game);
    void handleInput(Game game, Input input);
    void update(double dt, Input input);
    void render(Game game, Input input);
    void cleanup();

    default void onResize(Game game) {}
}
