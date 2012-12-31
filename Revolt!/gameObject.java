import java.awt.Graphics;

//	Interface to be used by the level class to communicate with 
//	objects used in the level


public interface gameObject 
{
	public void draw(Graphics g);										//method for drawing object
	public void update(long ms);										//method for updating object's position or other variables
	
	public boolean checkCollision(Character player);			//method for checking for collision with player
	public void onCollide(Character player, Level level);		//method called when a collision with player is detected
	
	public double getX();												//method to get x-coordinate of position
	public double getY();												//method to get y-coordinate of position
}
