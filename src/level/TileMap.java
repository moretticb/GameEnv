package level;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class TileMap {
	
	private int map[][];
	private int tileSize;
	private int originalTileSize;
	private BufferedImage mapImg;
	private Image tileSet;
	private Graphics mapGraphics;
	
	public TileMap(String filename, int tileSize, int originalTileSize, Image tileSet){
		this.tileSize = tileSize;
		this.originalTileSize = originalTileSize;
		this.tileSet = tileSet;
		
		InputStream level = this.getClass().getResourceAsStream(filename);
		Scanner s = new Scanner(level);
		int cols = s.nextLine().split(",").length;
		int rows = 1;
		while(s.hasNextLine()){
			rows++;
			s.nextLine();
		}
		
		mapImg = new BufferedImage(tileSize*cols, tileSize*rows, BufferedImage.TYPE_INT_RGB);
		mapGraphics = mapImg.getGraphics();
		
		map = new int[rows][];
		s = new Scanner(getClass().getResourceAsStream(filename));
		
		int ithRow = 0;
		while(s.hasNextLine()){
			String row[] = s.nextLine().split(",");
			map[ithRow] = new int[cols];
			for(int j=0;j<row.length;j++){
				map[ithRow][j] = row[j].indexOf('_')>-1?0:1;
				String offset[] = row[j].split(map[ithRow][j]==1?"-":"_");
//				mapGraphics.drawImage(tileSet,
//						j*tileSize, ithRow*tileSize, (j+1)*tileSize, (ithRow+1)*tileSize,
//						Integer.parseInt(offset[0])*tileSize, Integer.parseInt(offset[1])*tileSize, (Integer.parseInt(offset[0])+1)*tileSize, (Integer.parseInt(offset[1])+1)*tileSize,
//						null);
				
				mapGraphics.drawImage(tileSet,
						j*tileSize, ithRow*tileSize, (j+1)*tileSize, (ithRow+1)*tileSize,
						Integer.parseInt(offset[0])*originalTileSize, Integer.parseInt(offset[1])*originalTileSize, (Integer.parseInt(offset[0])+1)*originalTileSize, (Integer.parseInt(offset[1])+1)*originalTileSize,
						null);
				
			}
			ithRow++;
			
		}
		
	}
	
	public int getTileSize(){
		return this.tileSize;
	}
	
	public void setTileSize(int tileSize){
		this.tileSize = tileSize;
	}
	
	public BufferedImage getMapImg(){
		return this.mapImg;
	}
	
	public int[][] getMap(){
		return map;
	}
	
}
