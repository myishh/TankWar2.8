package Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TankClient extends Frame{
	
	public static final int GAME_WIDTH = 800, GAME_HEIGHT = 800;
	Tank myTank = new Tank(150, 100, true, Direction.STOP, this);

	List<Explode> explodes = new ArrayList<Explode>();
	List<Missile> missiles = new ArrayList<Missile>();
	List<Tank> tanks = new ArrayList<Tank>();
	
	Wall w1 = new Wall(200, 200, 25, 350, this),
		 w2 = new Wall(300, 100, 400, 25, this);
	
	Blood b = new Blood();
	
	public void lanuchFrame(){
		this.setLocation(100, 100);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("Tank War 2017");
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() { 
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setBackground(new Color(0,0,50));
		this.addKeyListener(new KeyMonitor());
		this.setVisible(true);
		new Thread(new PaintThread()).start();
		
		for(int i = 0; i < 10; i++){
			tanks.add(new Tank(200+40*(i+1), 600, false, Direction.D, this));
		}
	}
	
	@Override
	public void paint(Graphics g) {
		Color c = g.getColor();
		Font f = g.getFont();
		g.setFont(new Font("Menlo", 0, 12));
		g.setColor(new Color(255, 255, 182));
		g.drawString("Missiles count: "+ missiles.size(), 10, 50);
		g.drawString("Explodes count: "+ explodes.size(), 10, 70);
		g.drawString("Enemy num left: "+ tanks.size(), 10, 90);
		g.drawString("Your life value: "+ myTank.getLife()+"/100", 10, 110);
		g.setColor(c);
		g.setFont(f);
		
		//如果敌军死光了 重新加入
		if(tanks.size() <= 0){
			for(int i = 0; i < 10; i++){
				tanks.add(new Tank(200+50*(i+1), 600, false, Direction.D, this));
			}
		}
		
		for(int i = 0; i < missiles.size(); i++){
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(w1);
			m.hitWall(w2);
			m.draw(g);
		}

		for(int i = 0; i < explodes.size(); i++){
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		for(int i = 0; i < tanks.size(); i++){
			Tank t = tanks.get(i);
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.collidesWithTanks(tanks);
			t.draw(g);
		}
		
		myTank.draw(g);
		w1.draw(g);
		w2.draw(g);
		b.draw(g);
		myTank.eat(b);
	}
	
	Image offScreenImage = null;	
	@Override											
	public void update(Graphics g) {
		if(offScreenImage == null){ 
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(new Color(0,0,50));
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
		
	}
	
	public class PaintThread implements Runnable {
		@Override
		public void run() {
			while(true){
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class KeyMonitor extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			myTank.KeyPressed(e);
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
		
		
	}
	
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.lanuchFrame();
	}
}

/*
版本0.1 起一个frame窗口  设置location和size 设置visible 设置title
版本0.2 填加关闭窗口的事件处理; 不允许改动窗口大小
版本0.3 画出代表坦克的实心圆 想要画图需要重写paint()方法 重写之后自动调用paint()方法
版本0.4 让坦克动起来：起一个线程为重画服务 使用内部类
版本0.41 利用双缓冲解决屏幕闪烁问题
版本0.5 代码重构 将以后可能需要多处改变的量(Frame的宽度与高度)定义为常量
版本0.6 增加keyMonitor键盘监听 让坦克听指挥
版本0.7 新建Tank.java 把tank做的事交给tank自己去实现代码(面向对象的思维)
版本0.8 实现tank在8个方向上移动 (有缺陷)
版本0.9 修正了tank在8个方向上移动的缺陷, 即处理了key released事件
版本1.0 添加missile类 添加x y xspeed yspeed move() draw()等属性和方法
版本1.1 根据主战坦克的方向 打出子弹 (有缺陷)
版本1.2 解决坦克停下来打不出子弹的问题 (新增一个ptDir表示炮筒方向 子弹方向依据ptDir)
版本1.3 打出多发炮弹, 只打一发炮弹的原因是TankClient类里只有一个Missile m
版本1.4 解决炮弹不消亡的问题 解决坦克出界的问题
版本1.5 画一辆敌人坦克
版本1.6 将敌人坦克击毙
版本1.7 加入爆炸
版本1.8 添加多辆敌人Tank
版本1.9 让敌人tank动起来  不足：敌人tank看起来像热锅上的蚂蚁
版本1.92 解决1.9中的问题 让敌人tank移动的看起正常一些
版本1.93 让敌人也能发射炮弹
版本2.0 增加一堵墙 子弹和坦克不能穿墙
版本2.1 坦克相撞不能穿过
版本2.2 超级炮弹
版本2.3 加入主战坦克的生命值
版本2.4 把主战坦克的生命值图形化显示
版本2.5 添加血块 吃了血块涨血
版本2.6 敌军死光后重新加入敌人 | 我军阵亡后按F2重新开始
版本2.7 修正2.6不合理的地方：1)enum单独一个类 2)区分好坏炮弹的颜色
版本2.8 加入图片
*/