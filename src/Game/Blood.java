package Game;
import java.awt.*;
import java.util.*;

public class Blood {
	int x, y, width, height;
	TankClient tc = null;
	
	int step = 0;
	private boolean live = true;
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	private int[][] pos= {
			{350, 300},{350, 310},{350, 320},{350, 330},
			{350, 340},{350, 350},{350, 360},{350, 370}
	};
	
	public Blood(){
		x = pos[0][0];
		y = pos[0][1];
		width = 20;
		height = 20;
	}
	
	public void draw(Graphics g){
		if(!live) return;
		Color c = g.getColor();
		g.setColor(new Color(243, 10, 143));
		g.fillRect(x, y, width, height);
		g.setColor(c);
		
		move();
	}

	private void move() {
		if(step == pos.length) step = 0;
		x = pos[step][0];
		y = pos[step][1];
		step++;
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, width, height);
	}
	
}	
