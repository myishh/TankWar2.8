package Game;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Explode {
	int x, y;
	private boolean live = true;
	TankClient tc = null;
	
	int step = 0;
	
	static Image[] imgs = new Image[11];	
	static {
		for(int i = 0; i < imgs.length; i++) {
			imgs[i] = GameUtil.getImage("images/"+(i+1)+".gif");
			imgs[i].getHeight(null);
		}
	}
	
	public Explode(int x, int y, TankClient tc){
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		if(!live) {
			tc.explodes.remove(this);
			return;
		}
		
		if(step == imgs.length){
			live = false;
			step = 0;
			return;
		}
		
		g.drawImage(imgs[step], x, y, null);
		step++;
	}
	
}
