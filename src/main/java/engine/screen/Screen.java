package engine.screen;

import engine.input.Input;

public interface Screen {
    void init();
    void handleInput(Input input);
    void update(double dt, Input input);
    void render(Input input);
    void cleanup();
}
