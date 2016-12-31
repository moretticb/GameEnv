package character;

import gui.MapArea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import character.actions.CharAction;


import animation.Animator;
import animation.Spriter;

import path.AStar;
import path.Cell;

public class GameChar {
	
	//clockwise ordering
	public static final int IDLE = 0;
	public static final int TOP = 1;
	public static final int RIGHT = 2;
	public static final int BOTTOM = 3;
	public static final int LEFT = 4;
	
	private String name;
	private boolean alive;
	
	private List<Cell> waypoints;
	
	private Cell[] path;
	private int pathOffset;
	private Cell pathToGo;
	
	private Cell position;
	private boolean busy;
	private boolean following;
	
	private GameChar targetChar;
	
	private Spriter spriter;
	private Animator animator;
	private int walkSpeed; //in frames
	
	private CharAction action;
	private List<CharAction> acts;
	
	public GameChar(String name, Cell position, boolean busy){
		this.name = name;
		this.alive = true;
		this.position=position;
		this.busy=busy;
		this.following=false;
		this.targetChar = null;
		this.waypoints = new ArrayList<Cell>();
		this.walkSpeed = 15; //default
		this.acts = new ArrayList<CharAction>();
	}
	
	public void act(CharAction a){
		action = a;
		targetChar = a.getActee();
		
		following = false;
		follow(3);
	}
	
	public void updateActions(){
		for(int i=0;i<acts.size();i++){
			CharAction a = acts.get(i);
			
			if(a.getDuration() > 0){
				a.doAction();
				a.decreaseDuration();
			} else {
				a.revert();
				acts.remove(i);
			}
		}
	}
	
	public Spriter getSpriter(){
		return this.spriter;
	}
	
	public void setSpriter(Spriter spriter){
		this.spriter = spriter;
	}
	
	public int getWalkSpeed(){
		return this.walkSpeed;
	}
	
	public void setWalkSpeed(int walkSpeed){
		this.walkSpeed=walkSpeed;
	}
	
	public Animator getAnimator(){
		return this.animator;
	}
	
	public void setAnimator(Animator animator){
		this.animator=animator;
	}
	
	public Cell getPosition(){
		return position;
	}
	
	public boolean isBusy(){
		return this.busy;
	}
	
	public void setBusy(boolean busy){
		this.busy=busy;
	}
	
	public List<Cell> getWaypoints(){
		return this.waypoints;
	}
	
	public void setWaypoints(List<Cell> waypoints){
		this.waypoints = waypoints;
	}
	
	public void addWaypoint(Cell c){
		waypoints.add(c);
	}
	
	public void clearWaypoints(){
		waypoints.clear();
	}
	
	public void nextWaypoint(){
		if(waypoints != null && waypoints.size() > 0){
			this.pathToGo = waypoints.get((int) Math.round(Math.random()*(waypoints.size()-1)));
		} else {
			this.pathToGo = null;
		}
	}
	
	public void newPath(Cell[] path){
		pathOffset = 0;
		if(following)
			this.path = Arrays.copyOf(path,path.length-1);
		else
			this.path = path;
	}
	
	public void setPathToGo(Cell pathToGo){
		this.pathToGo = pathToGo;
	}
	
	public Cell getPathToGo(){
		return this.pathToGo;
	}
	
	public Cell getNextStep(){
		if(path != null && path.length-1 >= pathOffset+1)
			return path[pathOffset+1];
		return null;
	}
	
	public void walkOnPath(){
		if(pathOffset>path.length-2){
			setBusy(false);
		} else {
			Cell nextStep = path[++pathOffset];
			getPosition().setX(nextStep.getX());
			getPosition().setY(nextStep.getY());
		}
	}
	
	public void setTargetChar(GameChar c){
		targetChar = c;
	}
	
	public GameChar getTargetChar(){
		return targetChar;
	}
	
	public void stop(){
		goToCell(position);
	}
	
	public void goToCell(Cell c){
		action = null;
		following = false;
		pathToGo = c;
	}
	
	public void follow(int updateThresh){
//		if(!following || pathOffset >= updateThresh || AStar.manhattan(getPosition(), targetChar.getPosition()) > 2 || (int) (AStar.euclidean(getPosition(), targetChar.getPosition())) > 1){
		if(!following || pathOffset >= updateThresh){
			pathToGo = targetChar.getPosition();
		}
		following = true;
	}
	
	public boolean isFollowing(){
		return following;
	}
	
	public void setFollowing(boolean following){
		this.following = following;
	}
	
	public int getWalkDirection(){
		if(path == null || pathOffset>=path.length-1)
			return IDLE;
		else if(path[pathOffset].getX()-path[pathOffset+1].getX() > 0)
			return LEFT;
		else if(path[pathOffset].getX()-path[pathOffset+1].getX() < 0)
			return RIGHT;
		else if(path[pathOffset].getY()-path[pathOffset+1].getY() > 0)
			return BOTTOM;
		else if(path[pathOffset].getY()-path[pathOffset+1].getY() < 0)
			return TOP;
		return IDLE;
	}
	
	public CharAction getAction(){
		return action;
	}
	
	public void setAction(CharAction action){
		this.action = action;
	}
	
	public List<CharAction> getActs(){
		return acts;
	}
	
	public void kill(){
		getAnimator().goTo(0);
		this.alive = false;
	}
	
	public boolean isAlive(){
		return this.alive;
	}
	
	public String toString(){
		return this.name;
	}
	
}
