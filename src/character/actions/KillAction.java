package character.actions;

import character.GameChar;

public class KillAction extends CharAction {
	
	public KillAction(GameChar actor, GameChar actee){
		super(actor, actee, 1);
	}

	@Override
	public void doAction() {
		getActee().kill();
	}

	@Override
	public void revert() {}

	@Override
	public String toString() {
		return "Kill";
	}
	
}
