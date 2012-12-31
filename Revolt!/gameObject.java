import java.awt.Graphics;

public interface gameObject 
{
	public void draw(Graphics g);
	public void update(long ms);
	public boolean checkCollision(Character player);
	public void onCollide(Character player, Level level);
}
	