package img;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.xml.transform.Source;

public class ImageManager {
	
	public static BufferedImage getImage(String filename){
		BufferedImage img; 
		try {
			img = ImageIO.read(ImageManager.class.getResource(filename));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return img;
	}

}
