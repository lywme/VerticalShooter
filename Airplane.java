package shoot;
import java.util.Random;

//敌机，是飞行物，也是敌人
public class Airplane extends FlyingObject implements Enemy {
	private int speed=2; //敌机移动的步数
	
	public Airplane()
	{
		image=ShootGame.airplane;
		width=image.getWidth();
		height=image.getHeight();
		Random rand=new Random();
		x=rand.nextInt(ShootGame.WIDTH-this.width+1);
		y=-this.height;
		//y=200;
	}
	public int getScore()
	{
		return 5;
	} 
	
	public void step()
	{
		y=y+speed;
	}
	
	public boolean outOfBounds()
	{
		return this.y>=ShootGame.HEIGHT;
		
	}
}
