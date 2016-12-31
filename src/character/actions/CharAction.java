package character.actions;

import character.GameChar;

public abstract class CharAction {
	
	private GameChar actor;
	private GameChar actee;
	private int duration; //in frames
	
	public CharAction(GameChar actor, GameChar actee, int duration){
		this.actor = actor;
		this.actee = actee;
		this.duration = duration;
	}
	
	public GameChar getActor(){
		return actor;
	}
	
	public void setActor(GameChar actor){
		this.actor = actor;
	}
	
	public GameChar getActee(){
		return actee;
	}
	
	public void setActee(GameChar actee){
		this.actee = actee;
	}
	
	public int getDuration(){
		return this.duration;
	}
	
	public void decreaseDuration(){
		this.duration = this.duration-1;
	}
	
	public abstract void doAction();
	public abstract void revert();
	public abstract String toString();

}
