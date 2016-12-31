package character;

import gui.GameWindow;
import img.ImageManager;

import java.util.ArrayList;

import animation.Spriter;


import path.Cell;

public class GamePlayer extends GameChar {
	
	public GamePlayer(String name, Cell position, boolean busy) {
		super(name, position, busy);
		setBusy(false);
		setSpriter(new Spriter(16, 1, 1, 4, ImageManager.getImage(GameWindow.charSprite)));
		Spriter spr = getSpriter();
		spr.addState("DOWN", 1, 4);
		spr.addState("UP", 5, 4);
		spr.addState("LEFT", 9, 2);
		spr.addState("RIGHT", 11, 2);
		spr.addState("STUN", 1, 9, 5, 11);
		spr.addState("DEAD", 50);
	}

}
