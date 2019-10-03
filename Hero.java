package shoot;
import java.awt.image.BufferedImage;
import java.util.Random;
//英雄机
public class Hero extends FlyingObject{
	private int life; //命
	private int doubleFire; //火力值
	private BufferedImage[] images;  //BufferedImage类型图片数组
	private int index;  //协助图片切换
	
	public Hero()
	{
		image=ShootGame.hero0;
		width=image.getWidth();
		height=image.getHeight();
		Random rand=new Random();
		x=150;
		y=400;
		life=3;
		doubleFire=0;
		images=new BufferedImage[]{ShootGame.hero0,ShootGame.hero1};
		index=0;
	}
	
	public void step()
	{
		index++;
		int a=index/10;
		int b=a%2;
		image=images[b];
	}
	
	public Bullet[] shoot()
	{
		int xStep=this.width/4;
		int yStep=20;
		if(doubleFire>0)
		{
			Bullet[] bs=new Bullet[2];
			bs[0]=new Bullet(this.x+xStep,this.y-yStep);
			bs[1]=new Bullet(this.x+3*xStep,this.y-yStep);
			doubleFire-=2;  //发射一次火力值减2
			return bs;
		}else
		{
			Bullet[] bs=new Bullet[1];
			bs[0]=new Bullet(this.x+xStep*2,this.y-yStep);
			return bs;
		}
	}
	
	public void moveTo(int x,int y)
	{
		this.x=x-this.width/2;
		this.y=y-this.height/2;
	}
	
	public boolean outOfBounds()
	{
		return false;
	}
	
	public void addLife()
	{
		this.life++;
	}
	
	public void addDoubleFire()
	{
		this.doubleFire+=40;
	}
	
	public int getLifes()
	{
		return this.life;
	}
	
	public boolean hit(FlyingObject obj)
	{
		int x1=obj.x-this.width/2;
		int x2=obj.x+obj.width+this.width/2;
		int y1=obj.y-this.height/2;
		int y2=obj.y+obj.height+this.height/2;
		
		int x=this.x+this.width/2;
		int y=this.y+this.height/2;
		
		return x>x1&&x<x2
				&&
			   y>y1&&y<y2;
			   
	}
	
	public void subtractLife()
	{
		this.life--;
	}
	
	public void clearDoubleFire()
	{
		this.doubleFire=0;
	}
}
