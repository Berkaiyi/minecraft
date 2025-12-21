package game;

import engine.core.GameLogic;

public class MyGame implements GameLogic {
    @Override
    public void init() {
        System.out.println("Spiel startet");
    }

    @Override
    public void update(double dt) {
        // Spiellogik
    }

    @Override
    public void render() {
        // Rendering
    }

    @Override
    public void cleanup() {
        System.out.println("Spiel endet");
    }
}
