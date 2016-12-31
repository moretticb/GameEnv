package character.actions;

import character.GameChar;

public class SlowAction extends CharAction {
	
	private static final int stepSpeed = 50;
	private static final int steps = 5;
	
	private int walkSpeed;
	private int[] idleState;
	private int[] upState;
	private int[] downState;
	private int[] leftState;
	private int[] rightState;
	
	private boolean done;
	
	public SlowAction(GameChar actor, GameChar actee){
		super(actor, actee, steps*stepSpeed);
		done = false;
	}

	@Override
	public void doAction() {
		if(!done){
			this.walkSpeed = getActee().getWalkSpeed();
			done = true;
			
			this.idleState = getActee().getSpriter().removeState("IDLE");
			this.upState = getActee().getSpriter().removeState("UP");
			this.downState = getActee().getSpriter().removeState("DOWN");
			this.leftState = getActee().getSpriter().removeState("LEFT");
			this.rightState = getActee().getSpriter().removeState("RIGHT");
			getActee().getSpriter().addState("IDLE", 37, 1);
			getActee().getSpriter().addState("DOWN", 37, 4);
			getActee().getSpriter().addState("UP", 41, 4);
			getActee().getSpriter().addState("LEFT", 45, 2);
			getActee().getSpriter().addState("RIGHT", 47, 2);
			
			getActee().setWalkSpeed(stepSpeed);
			getActee().getAnimator().setTotalFrames(stepSpeed);
			
		}
	}

	@Override
	public void revert() {
		getActee().setWalkSpeed(walkSpeed);
		getActee().getAnimator().setTotalFrames(walkSpeed);
		
		getActee().getSpriter().removeState("IDLE");
		getActee().getSpriter().removeState("UP");
		getActee().getSpriter().removeState("DOWN");
		getActee().getSpriter().removeState("LEFT");
		getActee().getSpriter().removeState("RIGHT");

		getActee().getSpriter().addState("IDLE", idleState[0]+1, idleState[1]);
		getActee().getSpriter().addState("UP", upState[0]+1,upState[1]);
		getActee().getSpriter().addState("DOWN", downState[0]+1, downState[1]);
		getActee().getSpriter().addState("LEFT", leftState[0]+1, leftState[1]);
		getActee().getSpriter().addState("RIGHT", rightState[0]+1, rightState[1]);
		
	}

	@Override
	public String toString() {
		return "Slow";
	}

}
