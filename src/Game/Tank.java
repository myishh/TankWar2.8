package Game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class Tank {
	public static final int XSPEED = 5, YSPEED = 5;	
	public static final int WIDTH = 30, HEIGHT = 30;	
	TankClient tc = null;
	
	private int x, y;
	
	private int oldX, oldY;
	private boolean good;
	private boolean live = true;
	private int life = 100;
	
	private BloodBar bb = new BloodBar();
	
	public void setLife(int life) {
		this.life = life;
	}

	public int getLife() {
		return life;
	}

	public boolean isGood() {
		return good;
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	
	private static Random r = new Random();
	
	private int step = r.nextInt(12) + 3;
	
	private boolean bL = false, bU = false, bR = false, bD = false;	
	
	private Direction dir = Direction.STOP;				
	private Direction ptDir = Direction.D;					

	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
	}
	
	public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {
		this(x, y, good);
		this.dir = dir;
		this.tc = tc;
	}

	public void draw(Graphics g){
		if(!live){
			if(!good){	
				tc.tanks.remove(this);
			}
			return;
		}
		
		Color c = g.getColor();
		if(good) g.setColor(Color.RED);	
		else g.setColor(Color.BLUE);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(Color.WHITE);
		
		if(good) bb.draw(g);
		
		switch(ptDir){
		case L:
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x-3, y+Tank.HEIGHT/2);
			break;
		case LU:
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x, y);
			break;
		case U:
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+Tank.WIDTH/2, y-3);
			break;
		case RU:
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+Tank.WIDTH, y);
			break;
		case R:
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+3+Tank.WIDTH, y+Tank.HEIGHT/2);
			break;
		case RD:
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+Tank.WIDTH, y+Tank.HEIGHT);
			break;
		case D:
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+Tank.WIDTH/2, y+3+Tank.HEIGHT);
			break;
		case LD:
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x, y+Tank.HEIGHT);
			break;
		}
		
		g.setColor(c);
		move();	
	}
	
	void move(){
		this.oldX = x;
		this.oldY = y;
		switch(dir){
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			y -= YSPEED;
			x += XSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			y += YSPEED;
			x -= XSPEED;
			break;
		case STOP:
			break;
		}
		if(this.dir != Direction.STOP) this.ptDir = dir;
		
		if(x < 0) x = 0;
		if(y < 30) y = 30;
		if(x+Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if(y+Tank.HEIGHT>TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;

		if(!good){
			Direction[] dirs = Direction.values();
			if(step == 0){
				step = r.nextInt(12)+3;
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}
			step--;
			if(r.nextInt(40) > 41) this.fire();
		}
		
	}
	
	public void KeyPressed(KeyEvent e){
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_CONTROL:
			break;
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		//按下f2我军原地复活 满血
		case KeyEvent.VK_F2:
			if(!this.live && good) {
				this.live = true;
				this.life = 100;
			}
			break;
		default:
			break;
		}
		locateDireciton();
	}
	
	void locateDireciton(){
		if(bL && !bU && !bR && !bD) dir = Direction.L;	
		else if(bL && bU && !bR && !bD) dir = Direction.LU;	
		else if(!bL && bU && !bR && !bD) dir = Direction.U;	
		else if(!bL && bU && bR && !bD) dir = Direction.RU;	
		else if(!bL && !bU && bR && !bD) dir = Direction.R;	
		else if(!bL && !bU && bR && bD) dir = Direction.RD;	
		else if(!bL && !bU && !bR && bD) dir = Direction.D;	
		else if(bL && !bU && !bR && bD) dir = Direction.LD;	
		else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;	
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_CONTROL:
			fire();
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		case KeyEvent.VK_A:
			superFire();
			break;
		default:
			break;
		}
		locateDireciton();
	}
	
	public Missile fire(){
		if(!live) return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, this.good, ptDir, this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	public Missile fire(Direction dir){
		if(!live) return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, this.good, dir, this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	public void superFire(){
		Direction[] dirs = Direction.values();
		for(int i = 0; i < 8; i++){
			fire(dirs[i]);
		}
	}

	public Rectangle getRect(){
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean collidesWithWall(Wall w){
		if(this.live && this.getRect().intersects(w.getRect())){
			this.stay();
			return true;
		}
		return false;
	}

	private void stay(){
		x = oldX;
		y = oldY;
	}
	
	public boolean collidesWithTanks(List<Tank> tanks){
		for(int i = 0; i < tanks.size(); i++){
			Tank t = tanks.get(i);
			if(this!=t && this.live && t.isLive()
					&& this.getRect().intersects(t.getRect())){
					this.stay();
					t.stay();
					return true;
			}
		}
		return false;
	}
	
	private class BloodBar{
		public void draw(Graphics g){
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y-10, WIDTH, 5);
			int w = WIDTH * life / 100;
			g.fillRect(x, y-10, w, 5);
			g.setColor(c);
		}
	}
	
	
	//tank里新增eat方法来涨血
	public boolean eat(Blood b){
		if(this.live && b.isLive() && this.getRect().intersects(b.getRect())){
			this.life = 100;
			//吃完之后让血死掉
			b.setLive(false);
			return true;
		}
		return false;
	}
	
}
