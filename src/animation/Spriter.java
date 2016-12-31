package animation;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Spriter {
	
	private int frameSize;
	private int totalFrames;
	private int initialFrame;
	private int framesPerRow;
	
	private int currentFrame;
	
	private BufferedImage sprite;
	
	private Map<String, int[]> states;
	private String currentState;
	
	public Spriter(int frameSize, int totalFrames, int initialFrame, int framesPerRow, BufferedImage sprite){
		this.frameSize = frameSize;
		this.totalFrames = totalFrames;
		this.initialFrame = initialFrame-1;
		this.framesPerRow = framesPerRow;
		this.sprite = sprite;
		
		this.states = new  HashMap<String, int[]>();
		this.currentState = "IDLE";
		addState(currentState, initialFrame, totalFrames);
	}
	
	public BufferedImage getCurrentFrame(){
		int[] s = states.get(currentState);
		if(s.length == 2)
			return sprite.getSubimage((initialFrame+currentFrame)%framesPerRow*frameSize, (initialFrame+currentFrame)/framesPerRow*frameSize, frameSize, frameSize);
		return sprite.getSubimage(s[currentFrame]%framesPerRow*frameSize, (s[currentFrame])/framesPerRow*frameSize, frameSize, frameSize);
	}
	
	public void nextFrame(){
		currentFrame = (++currentFrame)%totalFrames;
	}
	
	public BufferedImage getSprite(){
		return sprite;
	}
	
	public void addState(String stateName, int initialFrame, int totalFrames){
		states.put(stateName, new int[]{initialFrame-1, totalFrames});
	}
	
	public void addState(String stateName, int... frames){
		for(int i=0;i<frames.length;i++){
			frames[i] = frames[i]-1;
		}
		states.put(stateName, frames);
	}
	
	public int[] removeState(String stateName){
		return states.remove(stateName);
	}
	
	public int[] getState(String state){
		return states.get(state);
	}
	
	public boolean setState(String s){
		if(s=="IDLE" && currentState!="UP" && currentState!="DOWN" && currentState!="LEFT" && currentState!="RIGHT" && currentState!="IDLE")
			return true;
		int[] state = states.get(s);
		if(state == null)
			return false;
		
		if(!s.equals(currentState)){
			currentState = s;
			currentFrame = 1;
			if(state.length==2){
				initialFrame = state[0];
				totalFrames = state[1];
			} else {
				initialFrame = 0;
				totalFrames = state.length;
			}
		}
		
		return true;
	}
	
	public String getCurrentState(){
		return currentState;
	}

}
