package character.actions;

import path.Cell;
import character.GameChar;

public class StunAction extends CharAction {
	
	private static final int duration = 60;
	
	public StunAction(GameChar actor, GameChar actee){
		super(actor, actee, duration);
		
	}

	@Override
	public void doAction() {
		getActee().stop();
		getActee().getSpriter().setState("STUN");
	}

	@Override
	public void revert() {}

	@Override
	public String toString() {
		return "Stun";
	}

}
