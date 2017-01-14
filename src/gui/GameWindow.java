package gui;

import img.ImageManager;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.RepaintManager;

import level.TileMap;

import animation.Animator;
import animation.Spriter;

import character.GameChar;
import character.GameComputer;
import character.GamePlayer;
import character.actions.CharAction;
import character.actions.FollowAction;
import character.actions.KillAction;
import character.actions.SlowAction;
import character.actions.StunAction;
import path.AStar;
import path.Cell;

public class GameWindow extends JFrame {
	
	public static final String mapTileset = "pokeTileset.png";
	public static final String charSprite = "charSprite.png";
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 550;
	private static final int FPS = 18;
	
	private static final int btnSize = 25;
	private static final Rectangle exitBtn = new Rectangle(5,5,btnSize,btnSize);
	
	private GamePlayer player;
	private List<GameComputer> computer;
	private Thread gameLogic;
	private MapArea mapArea;
	
	private CharAction actionToDo;
	private GameChar selectedActee;
	
	private int pressedKey;
	
//	private static final int[][] map = {
//		{1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
//		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0},
//		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0},
//		{0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1},
//		{0,0,0,0,0,0,0,1,1,1,0,1,1,1,0,0,0,0,1,1,1,1,1},
//		{0,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,1,1,0,0,0},
//		{1,1,1,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,1,1,0,0,0},
//		{1,1,1,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0},
//		{0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
//		{0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
//		{0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
//		{0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0},
//		{0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0},
//		{0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0},
//		{0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0}
//	};
	
	
	public GameWindow(){
		super("Game");

		MouseL mouseListener = new MouseL();
		KeyL keyListener = new KeyL();
		
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
		addKeyListener(keyListener);
		
		actionToDo = null;
		pressedKey = GameChar.IDLE;
		
		player = new GamePlayer("myself", new Cell(5,6), false);
		player.setWalkSpeed(5);
		player.setAnimator(new Animator(FPS, player.getWalkSpeed(), Animator.EASE_IN));
		
		computer = new ArrayList<GameComputer>();
		computer.add(new GameComputer("Comp0", new Cell(16,16),false));
		computer.add(new GameComputer("Comp1", new Cell(10,4),false));
		computer.add(new GameComputer("Comp2", new Cell(11,0),false));
		List<Cell> waypoints = new ArrayList<Cell>();
		waypoints.add(new Cell(5,5));
		waypoints.add(new Cell(12,11));
		waypoints.add(new Cell(13,5));
		for(int i=0;i<computer.size();i++){
			GameComputer comp = computer.get(i);
			comp.setWaypoints(waypoints);
			comp.setAnimator(new Animator(FPS, comp.getWalkSpeed(), Animator.EASE_IN));
		}
		
		
		int[] margin = {35,85};
		this.mapArea = new MapArea(32, 16, margin[0], margin[1], player, computer, "pallet.csv");
		int map[][] = mapArea.getTileMap().getMap();
		AStar as = new AStar(map, new Cell(1,1), new Cell(1,1));
		mapArea.setAS(as);
		mapArea.addMouseListener(mouseListener);
		mapArea.addMouseMotionListener(mouseListener);
		getContentPane().add(mapArea);
		getContentPane().setBackground(Color.BLACK);
		
		setBounds(0, 0, margin[0]+mapArea.getWidth(), margin[0]*2+mapArea.getHeight());
		new Rectangle(WIDTH-btnSize,0,btnSize,btnSize);
		setUndecorated(true);
		
		setLayout(null);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		gameLogic = new Thread(new GameLogic());
		gameLogic.run();
		
	}
	
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.RED);
		g.drawRect(exitBtn.x, exitBtn.y, exitBtn.width, exitBtn.height);
		g.drawLine(exitBtn.x, exitBtn.y, exitBtn.x+exitBtn.width, exitBtn.y+exitBtn.height);
		g.drawLine(exitBtn.x+exitBtn.width, exitBtn.y, exitBtn.x, exitBtn.y+exitBtn.height);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Courier new",Font.PLAIN,20));
		g.drawString("www.moretticb.com", 10, getHeight()-10);
		g.setFont(new Font("Courier new",Font.PLAIN,25));
		g.drawString("Game environment test - "+FPS+" FPS", exitBtn.x+exitBtn.width+10, exitBtn.x/2+exitBtn.width);
		
		String strToDraw = "";
		if(actionToDo != null){
			strToDraw += "Select a character to "+actionToDo.toString();
		}
		if(player.getAction() != null){
			if(strToDraw.length() > 0)
				strToDraw += " | ";
			strToDraw += String.format("%s -> %s", player.getAction().toString(), player.getTargetChar().toString());
		}
		if(strToDraw.length() > 0)
			g.drawString(strToDraw, getWidth() - 10 - (int) strBounds(strToDraw, g).getWidth(), getHeight()-10);
		
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(0, 0, getWidth(), 0);
		g.drawLine(0, 0, 0, getHeight());
		g.setColor(Color.DARK_GRAY);
		g.drawLine(0, getHeight()-1, getWidth()-1, getHeight()-1);
		g.drawLine(getWidth()-1, 0, getWidth()-1, getHeight()-1);
		
		Cell mCell = ((MouseL)(mapArea.getMouseMotionListeners()[0])).getMouseCell();
		if(actionToDo != null && mCell != null){
			int tileSize = mapArea.getTileSize();
			g.setColor(mapArea.getCharByCell(mCell)!=null?Color.GREEN:Color.RED);
			((Graphics2D) g).setStroke(new BasicStroke(3));  
			g.drawRect(mapArea.getX()+mCell.getX()*tileSize, mapArea.getY()+mCell.getY()*tileSize, tileSize, tileSize);
		}
	}
	
	private Rectangle2D strBounds(String s, Graphics g){
		return g.getFont().getStringBounds(s, g.getFontMetrics().getFontRenderContext()); 
	}
	
	public MapArea getMapArea(){
		return this.mapArea;
	}
	
	public CharAction getActionToDo(){
		return this.actionToDo;
	}
	
	public void setActionToDo(CharAction actionToDo){
		this.actionToDo = actionToDo;
	}
	
	//*** PRIVATE CLASSES (LISTENERS AND THREADS) ***//
	
	private class GameLogic implements Runnable {

		@Override
		public void run() {
			
			long elapsed = System.currentTimeMillis();
			String[] walkSprStates = new String[]{"IDLE","DOWN","RIGHT","UP","LEFT"};
			
			while(true){
				long now = System.currentTimeMillis();
				AStar as = mapArea.getAS();
				if(now-elapsed >= 1000/FPS){
					elapsed = now;

					if(player.isAlive()){
						
						if(pressedKey != GameChar.IDLE){
							Cell pos;
							if(player.isBusy()){
								pos = player.getNextStep();
							} else {
								pos = player.getPosition();
							}
							if(pos != null){
								if(pressedKey == GameChar.LEFT && pos.getX() > 0){
									player.goToCell(new Cell(pos.getX()-1, pos.getY()));
								}
								if(pressedKey == GameChar.RIGHT && pos.getX() < mapArea.getTileMap().getMap()[0].length-1){
									player.goToCell(new Cell(pos.getX()+1, pos.getY()));
								}
								if(pressedKey == GameChar.TOP && pos.getY() > 0){
									player.goToCell(new Cell(pos.getX(), pos.getY()-1));
								}
								if(pressedKey == GameChar.BOTTOM && pos.getY() < mapArea.getTileMap().getMap().length-1){
									player.goToCell(new Cell(pos.getX(), pos.getY()+1));
								}
							}
						}
						
						if(player.isFollowing()){
							if(player.getTargetChar() != null && AStar.manhattan(player.getPosition(), player.getTargetChar().getPosition())==1 || (int)(AStar.euclidean(player.getPosition(), player.getTargetChar().getPosition())*10) < 15){
								//System.out.println("TAKE ACTION!!!!!!!!!!!");
								if(player.getAction() != null){
									if(!(player.getAction() instanceof FollowAction)){
										player.getAction().getActee().getActs().add(player.getAction());
										player.setAction(null);
										player.stop();
	//									player.setFollowing(false);
									}
								}
							} else {
								player.follow(1);
							}
						}
						
						if(player.getPathToGo() != null && player.getAnimator().getCurrentFrame() == 1){
							as.setOrigin(player.getPosition());
							as.setTarget(player.getPathToGo());
							player.setPathToGo(null);
							player.newPath(as.getPath(false));
							player.setBusy(true);
						}
						
						if(player.isBusy()){
							if(player.getAnimator().nextFrame()){ //true if animation has finished (back to first frame)
								player.walkOnPath();
							}
						}
						if(pressedKey == GameChar.IDLE || (pressedKey != GameChar.IDLE && player.getWalkDirection() != GameChar.IDLE))
							player.getSpriter().setState(walkSprStates[player.getWalkDirection()]);
						player.updateActions();
					} else {
						player.getSpriter().setState("DEAD");
					}
					player.getSpriter().nextFrame();
					
					for(int i=0;i<computer.size();i++){
						GameComputer comp = computer.get(i);

						if(comp.isAlive()){
							if(comp.isFollowing())
								comp.follow(1);
							
							if(comp.getPathToGo() != null && comp.getAnimator().getCurrentFrame() == 1){
								as.setOrigin(comp.getPosition());
								as.setTarget(comp.getPathToGo());
								comp.setPathToGo(null);
								comp.newPath(as.getPath(false));
								comp.setBusy(true);
							}
							comp.getSpriter().setState(walkSprStates[comp.getWalkDirection()]);
							if(comp.isBusy()){
								if(comp.getAnimator().nextFrame()){ //true if animation has finished (back to first frame)
									comp.walkOnPath();
								}
							} else {
								comp.nextWaypoint();
							}
							comp.updateActions();
						} else {
							comp.getSpriter().setState("DEAD");
						}
						comp.getSpriter().nextFrame();
					}
					repaint();
				}
				
			}
			
		}
		
	}
	
	private class MouseL implements MouseListener, MouseMotionListener {
		
		private Point clickPoint;
		private Point mousePos;

		@Override
		public void mouseClicked(MouseEvent evt) {
			if(exitBtn.contains(evt.getPoint()))
				System.exit(0);
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {}
		@Override
		public void mouseExited(MouseEvent arg0) {}
		@Override
		public void mousePressed(MouseEvent arg0) {}

		@Override
		public void mouseReleased(MouseEvent evt) {
			if(evt.getSource() == mapArea){
				if(actionToDo == null){
					Cell c = new Cell(evt.getX()/mapArea.getTileSize(), evt.getY()/mapArea.getTileSize());
					player.goToCell(c);
				} else {
					GameChar selected = mapArea.getCharByCell(getMouseCell()); 
					if(selected != null){
						actionToDo.setActee(selected);
						player.act(actionToDo);
						actionToDo = null;
					}
				}
			} else if(evt.getSource() instanceof GameWindow){
				clickPoint = null;				
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent evt) {
			if(evt.getSource() instanceof GameWindow){
				if(clickPoint == null)
					clickPoint = evt.getPoint();
				
				setLocation(new Point(getLocation().x-clickPoint.x+evt.getPoint().x,getLocation().y-clickPoint.y+evt.getPoint().y));
			}
		}
		@Override
		public void mouseMoved(MouseEvent evt) {
			if(evt.getSource() == mapArea){
				if(actionToDo != null){
					mousePos = evt.getPoint();
					repaint();
				} else {
					mousePos = null;
				}
			}
		}
		
		public Cell getMouseCell(){
			if(mousePos == null)
				return null;
			int tileSize = mapArea.getTileSize();
			return new Cell(mousePos.x/tileSize, mousePos.y/tileSize);
		}
	}
	
	private class KeyL implements KeyListener {
		
		private int lastPressed;

		@Override
		public void keyPressed(KeyEvent evt) {
			
			lastPressed = evt.getKeyCode();
			if(lastPressed == KeyEvent.VK_RIGHT){
				pressedKey = GameChar.RIGHT;
			} else if(lastPressed == KeyEvent.VK_LEFT){
				pressedKey = GameChar.LEFT;
			} else if(lastPressed == KeyEvent.VK_UP){
				pressedKey = GameChar.TOP;
			} else if(lastPressed == KeyEvent.VK_DOWN){
				pressedKey = GameChar.BOTTOM;
			}
		}

		@Override
		public void keyReleased(KeyEvent evt) {
			if(lastPressed == evt.getKeyCode())
				pressedKey = GameChar.IDLE;
			
			
			if(evt.getKeyCode() == KeyEvent.VK_ESCAPE){
				System.exit(0);
			}
			
			
			if(evt.getKeyCode() == KeyEvent.VK_SPACE){
				if(actionToDo != null){
					actionToDo = null;
				} else {
					player.stop();
					pressedKey = GameChar.IDLE;
				}
			}else if(evt.getKeyCode() == KeyEvent.VK_K){
//				player.act(new KillAction(player, computer.get(0)));
				actionToDo = new KillAction(player, player);
			} else if(evt.getKeyCode() == KeyEvent.VK_F){
//				player.setTargetChar(computer.get(0));
//				player.follow(1);
				actionToDo = new FollowAction(player, player);
			} else if(evt.getKeyCode() == KeyEvent.VK_S){
//				player.act(new SlowAction(player, computer.get(0)));
				actionToDo = new SlowAction(player, player);
			} else if(evt.getKeyCode() == KeyEvent.VK_T){
//				player.act(new StunAction(player, computer.get(0)));
				actionToDo = new StunAction(player, player);
			}
		}

		@Override
		public void keyTyped(KeyEvent arg0) {}
		
	}
	
}
