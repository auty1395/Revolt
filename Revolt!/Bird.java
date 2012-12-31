import java.awt.Graphics;
import java.awt.Image;
import static java.lang.Math.*;


//Simple bird to test the gameObject interface
public class Bird implements gameObject
{
	double x, y, startx, starty;  //Real positions and inital positions
	int width, height;				//Width and height
	Image sprite;
	
	long timer = 0;					//timer for oscillating motion
	double peakheight = 300;		//height achieved when bird is disrupted by player
	double offset = 0;				//variable to store the offset position as the bird is rising to and falling from peak
	final double tick = .5;			//rate at which offset changes per tick
	boolean rising = false;			//boolean to store whether bird should be rising or falling
	
//######## commented out because the image.getWidth() and image.getHeight() methods don't work as expected #### 
//###### TODO: find other way to determine image width
//
//	public Bird(Image image, double x, double y) 
//	{
//		startx = x;
//		starty = y;
//		this.x = startx;
//		this.y = starty;
//		sprite = image;
//		width = sprite.getWidth(null);
//		height = sprite.getHeight(null);
//	}
//################################
	
	public Bird(Image image, double x, double y, int width, int height) //constructor
	{
		startx = x;
		starty = y;
		this.x = startx;
		this.y = starty;
		this.width = width;
		this.height = height;
		sprite = image;
	}
	
	
	public void draw(Graphics g)
	{
		g.drawImage(sprite,(int)x,(int) y, width, height, null);    //draw the bird sprite at its location with set width and height
	}
		
	public void update(long ms)
	{
		timer += ms;             									//advance timer
		if( timer >314159) timer = ms;							//loop timer
		
		if(rising && offset < peakheight) offset += tick;  //when rising to peak, increase offset by tick
		if(rising && offset >= peakheight) rising = false;	//when bird has reached its peak, set rising to false
		if(!rising && offset > 0) offset -= tick;				//when bird has an offset but is not rising, then it must be falling. decrease offset by tick
		if(offset < 0) offset = 0;									//should offset ever end up negative, change it to be zero
		
		y = starty - offset + 30.0 * sin( (double) timer / 700.0 );  //add simple sine oscillation to position
				
	}
	
	
	public boolean checkCollision(Character player)
	{
		if(player.x + player.width >= x && player.x < x + width) // if touching x-wise
		{
			if( player.y + player.height >= y && player.y < y + height) //are touching y-wise
			{
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	
	
	
	public void onCollide(Character player, Level level)
	{
		if(!rising) rising = true; 								//when collision is detected, set rising to true
	}
	
	public double getY()
	{
		return y;
	}
	public double getX()
	{
		return x;
	}
}
