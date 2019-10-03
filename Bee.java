package shoot;

import java.util.Random;

//小蜜蜂，是飞行物，也是奖励
public class Bee extends FlyingObject implements Award{
	private int xSpeed=1; //x坐标移动步数
	private int ySpeed=2; //y坐标移动步数
	private int awardType; //奖励的类型(0或1)
	
	public Bee()
	{
		image=ShootGame.bee;
		width=image.getWidth();
		height=image.getHeight();
		Random rand=new Random();
		x=rand.nextInt(ShootGame.WIDTH-this.width+1);
		y=-this.height;
		//y=200;
		awardType=rand.nextInt(2); //0到1之间，不包含2。0代表火力值，1代表命
	}
	public int getType()
	{
		return awardType;  //返回奖励类型(0或1)
	}
	
	public void step()
	{
		x=x+xSpeed;
		y=y+ySpeed;
		if(x>=ShootGame.WIDTH-width)//超出右边界
		{
			xSpeed=-1;
		}
		if(x<=0)
		{//超出左边界
			xSpeed=1;
		}
	}
	
	public boolean outOfBounds()
	{
		return this.y>=ShootGame.HEIGHT;
	}
}
