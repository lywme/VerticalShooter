package shoot;

import java.util.Random;

public class Bullet extends FlyingObject{
	private int speed=3; //移动的步数、速度
	
	public Bullet(int x,int y)
	{
		image=ShootGame.bullet;
		width=image.getWidth();
		height=image.getHeight();
		Random rand=new Random();
		this.x=x;
		this.y=y;
	}
	
	public void step()
	{
		y=y-speed;
	}
	
	public boolean outOfBounds()
	{
		return this.y<=-this.height;
	}
}
