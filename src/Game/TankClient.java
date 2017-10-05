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
		
		//����о������� ���¼���
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
�汾0.1 ��һ��frame����  ����location��size ����visible ����title
�汾0.2 ��ӹرմ��ڵ��¼�����; ������Ķ����ڴ�С
�汾0.3 ��������̹�˵�ʵ��Բ ��Ҫ��ͼ��Ҫ��дpaint()���� ��д֮���Զ�����paint()����
�汾0.4 ��̹�˶���������һ���߳�Ϊ�ػ����� ʹ���ڲ���
�汾0.41 ����˫��������Ļ��˸����
�汾0.5 �����ع� ���Ժ������Ҫ�ദ�ı����(Frame�Ŀ����߶�)����Ϊ����
�汾0.6 ����keyMonitor���̼��� ��̹����ָ��
�汾0.7 �½�Tank.java ��tank�����½���tank�Լ�ȥʵ�ִ���(��������˼ά)
�汾0.8 ʵ��tank��8���������ƶ� (��ȱ��)
�汾0.9 ������tank��8���������ƶ���ȱ��, ��������key released�¼�
�汾1.0 ���missile�� ���x y xspeed yspeed move() draw()�����Ժͷ���
�汾1.1 ������ս̹�˵ķ��� ����ӵ� (��ȱ��)
�汾1.2 ���̹��ͣ�����򲻳��ӵ������� (����һ��ptDir��ʾ��Ͳ���� �ӵ���������ptDir)
�汾1.3 ����෢�ڵ�, ֻ��һ���ڵ���ԭ����TankClient����ֻ��һ��Missile m
�汾1.4 ����ڵ������������� ���̹�˳��������
�汾1.5 ��һ������̹��
�汾1.6 ������̹�˻���
�汾1.7 ���뱬ը
�汾1.8 ��Ӷ�������Tank
�汾1.9 �õ���tank������  ���㣺����tank���������ȹ��ϵ�����
�汾1.92 ���1.9�е����� �õ���tank�ƶ��Ŀ�������һЩ
�汾1.93 �õ���Ҳ�ܷ����ڵ�
�汾2.0 ����һ��ǽ �ӵ���̹�˲��ܴ�ǽ
�汾2.1 ̹����ײ���ܴ���
�汾2.2 �����ڵ�
�汾2.3 ������ս̹�˵�����ֵ
�汾2.4 ����ս̹�˵�����ֵͼ�λ���ʾ
�汾2.5 ���Ѫ�� ����Ѫ����Ѫ
�汾2.6 �о���������¼������ | �Ҿ�������F2���¿�ʼ
�汾2.7 ����2.6������ĵط���1)enum����һ���� 2)���ֺû��ڵ�����ɫ
�汾2.8 ����ͼƬ
*/