package character;

import gui.GameWindow;
import img.ImageManager;

import java.util.List;
import java.util.ArrayList;

import animation.Spriter;


import path.Cell;

public class GameComputer extends GameChar {
	
	public GameComputer(String name, Cell position, boolean busy) {
		super(name, position, busy);
		nextWaypoint();
		setSpriter(new Spriter(16, 1, 13, 4, ImageManager.getImage(GameWindow.charSprite)));
		Spriter spr = getSpriter();
		spr.addState("DOWN", 13, 4);
		spr.addState("UP", 17, 4);
		spr.addState("LEFT", 21, 2);
		spr.addState("RIGHT", 23, 2);
		spr.addState("STUN", 13, 21, 17, 23);
		spr.addState("DEAD", 50);
	}
	
}
