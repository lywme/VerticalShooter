package shoot;

import java.util.Random;

//С�۷䣬�Ƿ����Ҳ�ǽ���
public class Bee extends FlyingObject implements Award{
	private int xSpeed=1; //x�����ƶ�����
	private int ySpeed=2; //y�����ƶ�����
	private int awardType; //����������(0��1)
	
	public Bee()
	{
		image=ShootGame.bee;
		width=image.getWidth();
		height=image.getHeight();
		Random rand=new Random();
		x=rand.nextInt(ShootGame.WIDTH-this.width+1);
		y=-this.height;
		//y=200;
		awardType=rand.nextInt(2); //0��1֮�䣬������2��0�������ֵ��1������
	}
	public int getType()
	{
		return awardType;  //���ؽ�������(0��1)
	}
	
	public void step()
	{
		x=x+xSpeed;
		y=y+ySpeed;
		if(x>=ShootGame.WIDTH-width)//�����ұ߽�
		{
			xSpeed=-1;
		}
		if(x<=0)
		{//������߽�
			xSpeed=1;
		}
	}
	
	public boolean outOfBounds()
	{
		return this.y>=ShootGame.HEIGHT;
	}
}
