package game.screens;

import engine.core.Game;
import game.logic.MainMenu;

public class MainMenuScreen extends LogicScreen {
    public MainMenuScreen(Game game) {
        super(game, new MainMenu(), false);
    }
}
