
import java.awt.*;

public class Character
{
	public double x, y, xvel, yvel;
	public boolean isGrounded = false, movingLeft = false, movingRight = false, isJumping = false, fallThrough = false;
	final int fallThroughTicks = 5;
	final int height = 40;
	final int width = 30;
	final double gravity = 50;
	final double speed = 200;
	final double jumpVelocity = 1000;
	final double descendVelocity = 200;
	
	
	Character(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics g)
	{
		
		if(movingRight && movingLeft) g.setColor(Color.magenta);
		else if(movingLeft) g.setColor(Color.blue);
		else if(movingRight)g.setColor(Color.red);
		else g.setColor(Color.green);
		
		g.fillRect((int)(x-5), (int)(y-5), width, height);
	}
	
	
	public void update(long ms, Platform[] shelfs, Wall[] walls)
	{
		if(movingLeft && movingRight) xvel = 0;
		else if(movingLeft) xvel = -speed;
		else if(movingRight) xvel = speed;
		else xvel = 0;
		if(fallThrough && isGrounded) yvel += descendVelocity;
		
		if(isJumping) jump(jumpVelocity); 
		
		double time = (double)ms/1000;
		x += xvel* time;
		y += yvel* time;
		
		boolean isCollidingDown = false;
		
		if(yvel >= 0 && !fallThrough)
		{
			for(int i = 0; i < shelfs.length; i ++)
			{
				if(  y + height < shelfs[i].y + 1 && y + height + yvel* time > shelfs[i].y)
				{
					if( x > shelfs[i].x - width && x < shelfs[i].x + shelfs[i].length + width)
					{
						isCollidingDown = true;
						y = shelfs[i].y - height;
						yvel = 0;
					}
				}
			}
		}
		
		for(int i = 0; i < walls.length; i++)
		{
			if(x + width > walls[i].x && x < walls[i].x + walls[i].width)
			{
				//System.out.println("a");
				if(walls[i].y < y + height && y < walls[i].y)
				
				{
					
					
						//System.out.println("b");
						isCollidingDown = true;
						y = walls[i].y - height;
						yvel = 0;
					
				}
				else if( y < walls[i].y + walls[i].height && y + height > walls[i].y)
				{
				//	System.out.println("c");
					//xvel = 0;
					if( xvel > 0) x = walls[i].x - width;
					else if(xvel < 0) x = walls[i].x + walls[i].width;
					else if (xvel == 0) 
					{
						y = walls[i].y + walls[i].height;
						yvel = 0;
					}
						
				}
				
			}
		}
		isGrounded = isCollidingDown;
		
		if(!isGrounded)
		{
			yvel += gravity;
		}
		if(isGrounded) yvel = 0;
		//if(isGrounded) System.out.println("Grounded!");
	}
	
	public void jump( double velocity)
	{
		if(isGrounded) yvel -= velocity;
	}
	
}
	
	
	