package engine.core;

public interface GameLogic {
    void init();
    void update(double dt);
    void render();
    void cleanup();
}
