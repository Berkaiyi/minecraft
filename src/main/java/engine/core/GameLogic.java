package engine.core;

import engine.input.Input;

public interface GameLogic {
    void init();
    void update(double dt, Input input);
    void render(Input input);
    void cleanup();
}
