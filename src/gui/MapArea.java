package gui;

import img.ImageManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JPanel;

import animation.Spriter;

import level.TileMap;

import path.AStar;
import path.Cell;

import character.GameChar;
import character.GameComputer;
import character.GamePlayer;

public class MapArea extends JPanel {
	
	private int tileSize;
	private int tilesX;
	private int tilesY;
	
	private GamePlayer player;
	private List<GameComputer> computer;
	
	private AStar as;
	
	private TileMap tileMap;
	
	public MapArea(int tileSize, int originalTileSize, int marginX, int marginY, GamePlayer player, List<GameComputer> computer, String levelFile){
		this.tileMap = new TileMap(levelFile, tileSize, originalTileSize, ImageManager.getImage(GameWindow.mapTileset)); //remover args tilesX e tilesY
		this.tileSize = tileSize;
		this.tilesX = tileMap.getMap()[0].length;
		this.tilesY = tileMap.getMap().length;
		
		this.player = player;
		this.computer = computer;
		
		setBounds(marginX/2, marginY/2, tileSize*tilesX, tileSize*tilesY);
		setDoubleBuffered(true);
		
	}

	public TileMap getTileMap(){
		return this.tileMap;
	}
	
	public AStar getAS(){
		return this.as;
	}
	
	public void setAS(AStar as){
		this.as=as;
	}
	
	public int getTileSize(){
		return this.tileSize;
	}
	
	private void drawCharName(GameChar character, Graphics g, Point pos){
		pos = new Point(pos.x, pos.y);
		g.setFont(new Font("Courier new", Font.BOLD, (28*tileSize)/64));
		Rectangle2D bounds = g.getFont().getStringBounds(character.toString(), g.getFontMetrics().getFontRenderContext());
		int border = (int) (g.getFont().getSize()*0.1);
		pos.setLocation(pos.x+(tileSize/2)-(bounds.getWidth()/2), pos.y);
		
		
		if(pos.x-1 < 0)
			pos.setLocation(0, pos.y);
		if(pos.x+bounds.getWidth()+border > getWidth())
			pos.setLocation(getWidth()-bounds.getWidth()+border, pos.y);
		if(pos.y-1-bounds.getHeight() < 0)
			pos.setLocation(pos.x, 1+bounds.getHeight()/2);
		
		
		g.setColor(Color.BLACK);
		g.drawString(character.toString(), pos.x-1, pos.y-1);
		g.drawString(character.toString(), pos.x+border, pos.y+border);
		Color nameColor = new Color(240, 240, 240);
		if(character.isAlive()){
			if(character.getActs().size() > 0)
				nameColor = Color.YELLOW;
		} else {
			nameColor = new Color(253,128,128);
		}
		g.setColor(nameColor);
		g.drawString(character.toString(), pos.x, pos.y);
	}
	
	public GameChar getCharByCell(Cell c){
		for(int i=0;i<computer.size();i++){
			GameChar comp = computer.get(i);
			if(c.equals(comp.getPosition()) && comp.isAlive())
				return comp;
		}
		if(c.equals(player.getPosition()) && player.isAlive())
			return player;
		return null;
	}
	
	public void paint(Graphics g){
//		g.drawImage(ImageManager.getImage("scene.png"), 0, 0, null);
		g.drawImage(tileMap.getMapImg(),0,0,null);
		
//		g.setColor(Color.BLUE);
//		for(int i=0;i<map[0].length;i++){
//			for(int j=0;j<map.length;j++){
//				if(map[j][i]==1)
//					g.drawRect(tileSize*i, tileSize*j, tileSize, tileSize);
//			}
//		}
		
		g.setColor(Color.WHITE);
		Point pos;
		for(int i=0;i<computer.size();i++){
			GameComputer comp = computer.get(i);
//			g.fillRect(tileSize*comp.getPosition().getX(),tileSize*comp.getPosition().getY(), tileSize, tileSize);
			
			if(comp.getAnimator().getCurrentFrame()==0){
				pos = new Point(tileSize*comp.getPosition().getX(),tileSize*comp.getPosition().getY());
			} else {
				pos = new Point(
						(int)(tileSize*comp.getPosition().getX()+(comp.getWalkDirection()==GameChar.LEFT?-1:comp.getWalkDirection()==GameChar.RIGHT?1:0)*comp.getAnimator().getEasingValue(1)*tileSize),
						(int)(tileSize*comp.getPosition().getY()+(comp.getWalkDirection()==GameChar.BOTTOM?-1:comp.getWalkDirection()==GameChar.TOP?1:0)*comp.getAnimator().getEasingValue(1)*tileSize)
				);
			}
//			g.fillRect(pos.x,pos.y, tileSize, tileSize);
			g.drawImage(comp.getSpriter().getCurrentFrame().getScaledInstance(tileSize, tileSize, BufferedImage.SCALE_DEFAULT), pos.x, pos.y, null);
			drawCharName(comp, g, pos);
		}
		
		g.setColor(Color.RED);
		if(player.getAnimator().getCurrentFrame()==0){
			pos = new Point(tileSize*player.getPosition().getX(),tileSize*player.getPosition().getY());
		} else {
			pos = new Point(
					(int)(tileSize*player.getPosition().getX()+(player.getWalkDirection()==GameChar.LEFT?-1:player.getWalkDirection()==GameChar.RIGHT?1:0)*player.getAnimator().getEasingValue(1)*tileSize),
					(int)(tileSize*player.getPosition().getY()+(player.getWalkDirection()==GameChar.BOTTOM?-1:player.getWalkDirection()==GameChar.TOP?1:0)*player.getAnimator().getEasingValue(1)*tileSize)
			);
		}
//		g.fillRect(pos.x,pos.y, tileSize, tileSize);
		g.drawImage(player.getSpriter().getCurrentFrame().getScaledInstance(tileSize, tileSize, BufferedImage.SCALE_DEFAULT), pos.x, pos.y, null);
		drawCharName(player, g, pos);
		
//		List<Cell> wayp = computer.get(0).getWaypoints();
//		for(int i=0;i<wayp.size();i++){
//			g.setColor(Color.GREEN);
//			g.drawRect(tileSize*wayp.get(i).getX(), tileSize*wayp.get(i).getY(), tileSize, tileSize);
//		}
	}

}
