import java.awt.*;

public class Level
{
	public Platform[] shelves;
	public Wall[] walls;
	
	int endx, endy;
	int startx, starty;
	
	public Image background;
	
	
	Level(int numPlatforms, int numWalls)
	{
		shelves = new Platform[numPlatforms];
		walls = new Wall[numWalls];
		
		for( int i = 0; i < shelves.length; i++) shelves[i] = new Platform(0, 0, 0);
		for( int i = 0; i < walls.length; i++) walls[i] = new Wall(0, 0, 0, 0);
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(background, 0, 0, null);
	}
	
}