package game.screens;

import engine.core.Game;
import game.logic.VoxelGame;

public class GameScreen extends LogicScreen {
    public GameScreen(Game game) {
        super(game, new VoxelGame(), true);
    }
}
