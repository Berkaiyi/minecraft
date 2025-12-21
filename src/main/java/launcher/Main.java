package launcher;

import engine.core.Game;
import game.MyGame;

public class Main {
    public static void main(String[] args) {
        Game game = new Game(new MyGame());
        game.run();
    }
}
