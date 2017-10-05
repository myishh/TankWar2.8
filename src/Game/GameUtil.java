package Game;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * Common method in game development(such as load image)
 * @author Myishh
 *
 */
public class GameUtil {
	
	//Game Utility is a tool class. All methods are static. So we don't need
	//to new an object of GameUtil. Thus, set the constructor to be private
	private GameUtil() {};
	
	public static Image getImage(String path) {		
		URL u = GameUtil.class.getClassLoader().getResource(path);
		BufferedImage img = null;
		try {
			img = ImageIO.read(u);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

}
