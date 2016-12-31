package animation;

public class Animator {
	
	public static final int EASE_IN = 1;
	public static final int EASE_OUT = 2;
	
	private int fps;
	private int totalFrames;
	private int currentFrame;
	private int easing;
	
	public Animator(int fps, int totalFrames, int easing){
		this.fps = fps;
		this.totalFrames = totalFrames;
		this.currentFrame = 0;
		this.easing = easing;
	}
	
	public void goTo(int frame){
		this.currentFrame = frame;
	}
	
	public boolean nextFrame(){
		currentFrame = ++currentFrame%totalFrames;
		return currentFrame==0;
	}
	
	public double getEasingValue(int order){
		return Math.pow((double)currentFrame/(double)totalFrames, easing==EASE_IN?(double)order:(double)1/(double)order);
	}
	
	public int getCurrentFrame(){
		return this.currentFrame+1;
	}
	
	public int getTotalFrames(){
		return this.totalFrames;
	}
	
	public void setTotalFrames(int totalFrames){
		this.totalFrames = totalFrames;
	}
	
	public void setFPS(int fps){
		this.fps=fps;
	}
	
	public int getFPS(){
		return fps;
	}

}
