package shoot;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Font;


//��������
public class ShootGame extends JPanel{
	public static final int WIDTH=400;
	public static final int HEIGHT=654;
	public static BufferedImage background;
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	
	public static final int START=0;
	public static final int RUNNING=1;
	public static final int PAUSE=2;
	public static final int GAME_OVER=3;
	public int state=START;
	
	private Hero hero=new Hero(); //Ӣ�ۻ�����
	private FlyingObject[] flyings={};//��������
	private Bullet[] bullets={};
	

	
	static{
		try{
			background=ImageIO.read(ShootGame.class.getResource("background.png"));
			start=ImageIO.read(ShootGame.class.getResource("start.png"));
			pause=ImageIO.read(ShootGame.class.getResource("pause.png"));
			gameover=ImageIO.read(ShootGame.class.getResource("gameover.png"));
			airplane=ImageIO.read(ShootGame.class.getResource("airplane.png"));
			bee=ImageIO.read(ShootGame.class.getResource("bee.png"));
			bullet=ImageIO.read(ShootGame.class.getResource("bullet.png"));
			hero0=ImageIO.read(ShootGame.class.getResource("hero0.png"));
			hero1=ImageIO.read(ShootGame.class.getResource("hero1.png"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public FlyingObject nextOne()
	{
		Random rand=new Random();
		int result=rand.nextInt(20);
		if(result<=4)
		{
			return new Bee();
		}
		else
		{
			return new Airplane();
		}
	}
	
	public void action()
	{
		MouseAdapter l=new MouseAdapter(){
			public void mouseMoved(MouseEvent e)
			{
				if(state==RUNNING)
				{
					int x=e.getX();
					int y=e.getY();
					hero.moveTo(x,y);
				}
			}
			public void mouseClicked(MouseEvent e)
			{
				switch(state)
				{
				case START:
					state=RUNNING;
					break;
				case GAME_OVER:
					hero=new Hero();
					flyings=new FlyingObject[0];
					bullets=new Bullet[0];
					state=START;
					break;
				}
			}
			public void mouseExited(MouseEvent e)
			{
				if(state==RUNNING)
				{
					state=PAUSE;
				}
			}
			public void mouseEntered(MouseEvent e)
			{
				if(state==PAUSE)
				{
					state=RUNNING;
				}
			}
		};
		this.addMouseListener(l);
		this.addMouseMotionListener(l);
		
		Timer timer=new Timer();
		int interval=10;
		timer.schedule(new TimerTask(){
			public void run()
			{
				if(state==RUNNING)
				{
					enterAction(); //�л�С�۷��볡
					stepAction(); //�ƶ�
					shootAction(); //�ӵ��볡
					outOfBoundsAction(); //ɾ��Խ��ĵ��˺��ӵ�
					bangAction(); //�ж��ӵ��͵��˵���ײ
					checkGameOverAction(); //�����Ϸ�Ƿ����
				}
				repaint();
			}
		},interval,interval);
	}
	
	int flyingCount=0;
	public void enterAction()
	{
		flyingCount++;
		if(flyingCount%40==0)
		{
			FlyingObject fly=nextOne();
			flyings=Arrays.copyOf(flyings,flyings.length+1);
			flyings[flyings.length-1]=fly;
		}
	}
	
	public void stepAction()
	{
		hero.step();
		for(int i=0;i<bullets.length;i++)
		{
			bullets[i].step();
		}
		for(int i=0;i<flyings.length;i++)
		{
			flyings[i].step();
		}
	}
	
	//�ӵ��볡��Ӣ�ۻ������ӵ�
	int shootIndex=0;
	public void shootAction()//10��������һ��
	{
		shootIndex++; //ÿ10����+1
		if(shootIndex%30==0) //ÿ300���루10x30��һ��
		{
			Bullet[] bs=hero.shoot(); //��ȡ�ӵ�����
			bullets=Arrays.copyOf(bullets, bullets.length+bs.length); //����
			System.arraycopy(bs,0,bullets,bullets.length-bs.length,bs.length);
			//�����׷��
		}
	}
	
	public void outOfBoundsAction()
	{
		//�л�С�۷�Խ��ɾ��
		int index=0;
		FlyingObject[] flyingLive=new FlyingObject[flyings.length];
		for(int i=0;i<flyings.length;i++)
		{
			FlyingObject f=flyings[i];
			if(!f.outOfBounds())
			{
				//��Խ��
				flyingLive[index]=f;
				index++;
			}	
		}
		flyings=Arrays.copyOf(flyingLive,index);
		//�ӵ�Խ��ɾ��
		int bindex=0;
		Bullet[] bulletsLive=new Bullet[bullets.length];
		for(int i=0;i<bullets.length;i++)
		{
			Bullet b=bullets[i];
			if(!b.outOfBounds())
			{
				//�ӵ���Խ��
				bulletsLive[bindex]=b;
				bindex++;
			}	
		}
		bullets=Arrays.copyOf(bulletsLive,bindex);
	}
	
	public void bangAction()
	{
		for(int i=0;i<bullets.length;i++)
		{
			Bullet b=bullets[i];
			bang(b);//����һ���ӵ������е��˵���ײ
		}
	}
	
	int score=0; //�÷�
	public void bang(Bullet b)
	{
		int index=-1;//��ײ���˵��±�
		for(int i=0;i<flyings.length;i++)
		{
			FlyingObject fly=flyings[i];
			if(fly.shootBy(b))  //ײ����
			{
				index=i;
				break;
			}
		}
		if(index!=-1)
		{//ײ����
			FlyingObject one=flyings[index];
			//�ж�fly������
			if(one instanceof Enemy)
			{
				Enemy e=(Enemy)one;
				//�������ͣ���Ҽӷ�
				score+=e.getScore();
			}
			if(one instanceof Award)
			{
				Award a=(Award)one;
				int award=a.getType();
				switch(award)
				{
				case Award.DOUBLE_FIRE:
					hero.addDoubleFire();
					break;
				case Award.LIFE:
					hero.addLife();
					break;
				}
			}
			//ɾ����ײ�ĵ��ˣ��ѱ�ײ�����Ƶ��������һ��
			FlyingObject temp=flyings[index];
			flyings[index]=flyings[flyings.length-1];
			flyings[flyings.length-1]=temp;
			
			//ɾ�������һ������
			flyings=Arrays.copyOf(flyings, flyings.length-1);
		}
	}
	
	public void checkGameOverAction()
	{
		if(isGameOver())
		{
			state=GAME_OVER;
		}
	}
	
	//��Ϸ��������true
	public boolean isGameOver()
	{
		for(int i=0;i<flyings.length;i++)
		{
			FlyingObject f=flyings[i];
			if(hero.hit(f))//����л�������
			{
				hero.subtractLife();
				hero.clearDoubleFire();
				//������л����������һ������
				
				FlyingObject temp=flyings[i];
				flyings[i]=flyings[flyings.length-1];
				flyings[flyings.length-1]=temp;
				
				flyings=Arrays.copyOf(flyings, flyings.length-1);
			}
		}
		return hero.getLifes()<=0;
	}
	
	//��д�����paint()    g����
	public void paint(Graphics g)
	{
		//������ͼ
		g.drawImage(background,0,0,null);
		paintHero(g);
		paintFlyingObjects(g);
		paintBullets(g);
		paintStrings(g);
		paintState(g);
	}
	public void paintHero(Graphics g)
	{
		g.drawImage(hero.image,hero.x,hero.y,null);
	}
	public void paintFlyingObjects(Graphics g)
	{
		for(int i=0;i<flyings.length;i++)
		{
			FlyingObject fly=flyings[i];
			g.drawImage(fly.image,fly.x,fly.y,null);
		}
	}
	public void paintBullets(Graphics g)
	{
		for(int i=0;i<bullets.length;i++)
		{
			Bullet b=bullets[i];
			g.drawImage(b.image,b.x,b.y,null);
		}
	}
	public void paintStrings(Graphics g)
	{
		g.setColor(new Color(0xFF0000));
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		g.drawString("SCORE :"+this.score, 10, 25);
		g.drawString("LIFE  :"+hero.getLifes(),10,45);
	}
	
	public void paintState(Graphics g)
	{
		switch(state)
		{
		case START:
			g.drawImage(start,0,0,null);
			break;
		case PAUSE:
			g.drawImage(pause,0,0,null);
			break;
		case GAME_OVER:
			g.drawImage(gameover,0,0,null);
			break;
		}
	}
	
	public static void main(String[] args) {
		JFrame frame=new JFrame("Fly");
		ShootGame game=new ShootGame();
		frame.add(game);
		frame.setSize(WIDTH,HEIGHT);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true); //����1>���ô��ڿɼ�  2>������� paint()����
		game.action();
	}

}
