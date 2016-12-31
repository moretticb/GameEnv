package character.actions;

import character.GameChar;

public class FollowAction extends CharAction {

	public FollowAction(GameChar actor, GameChar actee) {
		super(actor, actee, 1);
	}

	@Override
	public void doAction() {}

	@Override
	public void revert() {}

	@Override
	public String toString() {
		return "Follow";
	}

}
