package Game;
import java.awt.*;

public class Wall {
	
	int x, y;
	int width, height;
	TankClient tc = null;
	
	
	
	public Wall(int x, int y, int width, int height, TankClient tc) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.tc = tc;
	}

	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(new Color(255, 128, 0));
		g.fillRect(x, y, width, height);
		g.setColor(c);
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, width, height); 
	}
	
	

}
