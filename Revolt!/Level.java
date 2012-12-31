import java.awt.Graphics;
import java.awt.Image;

public class Level
{
	public Platform[] shelves;
	public Wall[] walls;
	public gameObject[] objects;
	public boolean isComplete = false;
	
	
	
	Character levelPlayer;
	int endx, endy;
	int startx, starty;
	
	public Image background;
	
	
	Level(int numPlatforms, int numWalls, int numObjects, Character character)
	{
		shelves = new Platform[numPlatforms];
		walls = new Wall[numWalls];
		objects = new gameObject[numObjects];
		levelPlayer  = character;
		
		for( int i = 0; i < shelves.length; i++) shelves[i] = new Platform(0, 0, 0); //initalize platforms
		for( int i = 0; i < walls.length; i++) walls[i] = new Wall(0, 0, 0, 0); //initalize walls
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(background, 0, 0, null); // draws the background image
		for(int i = 0; i < objects.length; i++) objects[i].draw(g); //draws each of the gameObjects

		
		
	}
	
	public void update(long ms)
	{
		for(int i = 0; i < objects.length; i++)//for each gameObject in objects...
		{ 
			objects[i].update(ms); //update
			if(objects[i].checkCollision(levelPlayer))objects[i].onCollide(levelPlayer, this); //check collision with player, and if there is call the collide method of the object
		}	
		
	}
		
}