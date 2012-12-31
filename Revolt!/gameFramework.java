//A generic framework for a game implemented as a Java Applet

import java.applet.*; //needed to create an applet
import java.awt.*; //needed for graphics
import java.awt.event.*; //needed for event handling
import java.awt.image.BufferedImage;
import java.net.*;

import static java.lang.Math.*;

public class gameFramework extends Applet implements Runnable, KeyListener, MouseMotionListener{

	Thread thread; // used to run the game
	
	Dimension dim; // these are used for double buffering
	Image img; // the back buffer
	Graphics g; // used to draw on the back buffer
	
	Character player;
	Bird bird;
	Image background0;
	Image birdsprite;
	
	final int NUM_LEVELS = 1;
	Level[] levels;
	int currentLevel = 0, mouseX, mouseY;
	boolean levelStart = true, toggleDebug = false, debugIsVisible = false;
	
	
	
	// used to regulate the speed of the game
	long endTime, startTime, framePeriod;
	
	/**
	* This method is called when the applet is first created.
	*/
	public void init(){
		 
		System.out.println("Start Init");
		
		resize(750,600); //can be set to any dimension desired
		// INITIALIZE ANY OF YOUR OWN VARIABLES HERE
		
		player = new Character(100, 10); //initalize player
		levels = new Level[NUM_LEVELS]; //initalize level array
		
		try  //try to load level background images;
		{
			background0 = getImage(new URL(getCodeBase(), "Revolt! Lvl1 The Apartment.jpeg"));
			birdsprite = getImage(new URL(getCodeBase(), "bird.gif"));
			
		}
		catch(MalformedURLException e)
		{
			System.out.println(e);
		}
		
		
		bird = new Bird(birdsprite, 200, 400, 70, 70);
		
		levels[0] = new Level(11, 7,1, player, background0 );
		levels[0].objects[0] = bird;
		
		levels[0].startx = 0;
		levels[0].starty = 350;
		levels[0].endx = 650;
		levels[0].endy = 480;
		
		levels[0].shelves[0] = new Platform(0,284,150);
		levels[0].shelves[1] = new Platform(212,155,37);
		levels[0].shelves[2] = new Platform(203,398,46);
		levels[0].shelves[3] = new Platform(285,307,46);
		levels[0].shelves[4] = new Platform(300,166,105);
		levels[0].shelves[5] = new Platform(485,399,52);
		levels[0].shelves[6] = new Platform(488,251,49);
		levels[0].shelves[7] = new Platform(489,135,48);
		levels[0].shelves[8] = new Platform(576,365,57);
		levels[0].shelves[9] = new Platform(695,216,56);
		levels[0].shelves[10] = new Platform(698,292,61);
		
		levels[0].walls[0] = new Wall(-10, -400, 10, 1100);
		levels[0].walls[1] = new Wall(750, -400, 10, 1100);
		levels[0].walls[2] = new Wall(-10, 530, 770, 70);
		levels[0].walls[3] = new Wall(250,155,35,375);
		levels[0].walls[4] = new Wall(344,251,46,205);
		levels[0].walls[5] = new Wall(416, -100, 37, 328);
		levels[0].walls[6] = new Wall(538, 90, 40, 510);
		
		
		
		
		
	
		
		
		
		endTime=0;
		startTime=0;
		framePeriod=15; //may be adjusted to change the speed that
						//the game runs.
		addKeyListener(this); //tell the class to listen for KeyEvents
		addMouseMotionListener(this);
		dim=getSize();
		img=createImage(dim.width, dim.height);//create back buffer
		g=img.getGraphics(); //create Graphics obj to draw on back buffer
		thread=new Thread(this); //create the thread that runs game
		System.out.println("Starting Thread..");
		thread.start(); //start the thread
		
	}
	
	/**
	* This method paints the graphics of the applet.
	* @param gfx - the applet's graphics object
	*/
	public void paint(Graphics gfx){
		//System.out.println("painting");
		//########## clear canvas
		g.setColor(Color.black); //set color to clear the screen with
		g.fillRect(0,0,750,600); //clear the screen
		
		//########### draw important stuff
		levels[currentLevel].draw(g); //draw the current level
		player.draw(g);					//draw the player
		
		//########### then draw debug on top
		if (debugIsVisible == true) drawDebug(g); //draw the debug screen if debugIsVisible 
		
		//########### draw backbuffer
		gfx.drawImage(img,0,0,this); //copys back buffer onto the screen
	}
	
	/**
	* This is the method called by repaint() and makes a call to
	* paint() without clearing the screen.
	* The original method, Applet.update(), clears the screen
	* with a white rectangle and then calls paint.
	* Clearing the screen causes flickering in games, so we have
	* overriden update() to call paint without clearing.
	* We can do this because we will make paint() redraw the entire
	* screen, so clearing it first would be pointless.
	* @param gfx - the applet's graphics object
	*/
	public void update(Graphics gfx){
		paint(gfx);
	}
	
	/**
	* This method contains the code to run the game.
	* It is executed by a thread.
	*/
	public void run(){
		
		System.out.println("Thread Started.");
		for(;;){
			//System.out.println("tick");
			startTime=System.currentTimeMillis();
			
			//############ UPDATE BLOCK
			if (toggleDebug == true)  	// if toggleDebug, toggle debug..
			{
				if (debugIsVisible == false) debugIsVisible = true;
				else if (debugIsVisible == true) debugIsVisible = false;
				toggleDebug = false;
			}				
			if(abs(player.x - levels[currentLevel].endx) < 30 && abs(player.y - levels[currentLevel].endy) < 30) //if player is near the level's end, end the level
			{	
				currentLevel++;
				if(currentLevel >= NUM_LEVELS) currentLevel = 0;
				levelStart = true;
			}
			if(levelStart)											// if level is starting, place player at the start
			{															
				player.x = levels[currentLevel].startx;	
				player.y = levels[currentLevel].starty;	
				levelStart = false;								
			}															
						
			player.update(framePeriod, levels[currentLevel].shelves, levels[currentLevel].walls); 	//update the player
			levels[currentLevel].update(framePeriod);																//update the level

			
			
			repaint();
			try{ //regulate the speed of the game
				endTime=System.currentTimeMillis();
				if(framePeriod-(endTime-startTime)>0)
					Thread.sleep(framePeriod-(endTime-startTime));
			}catch(InterruptedException e){
			}
		}
	}

	/**
	* Responds to any keys being pressed on the keyboard
	* @param e - contains information on the event (what key was
	* pressed, etc)
	*/
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode()==KeyEvent.VK_UP){
			player.isJumping = true;
		}else if(e.getKeyCode()==KeyEvent.VK_DOWN){
			player.fallThrough = true;
		}else if(e.getKeyCode()==KeyEvent.VK_LEFT){
			player.movingLeft = true;
		}else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
			player.movingRight = true;
		}else if(e.getKeyCode()==KeyEvent.VK_D){
			toggleDebug = true;
		}
	}
	
	/**
	* Responds to any keys being released on the keyboard
	* @param e - contains information on the event (what key was
	* released, etc)
	*/
	public void keyReleased(KeyEvent e){
		if(e.getKeyCode()==KeyEvent.VK_UP){
			player.isJumping = false;
		}else if(e.getKeyCode()==KeyEvent.VK_DOWN){
			player.fallThrough = false;
		}else if(e.getKeyCode()==KeyEvent.VK_LEFT) {
			player.movingLeft = false;
		}else if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
			player.movingRight = false;
		}
		
	}
	
	/**
	* This method doesn't usually need to do anything for simple games.
	* It might be used if the game involved the user typing in text.
	*/
	public void keyTyped(KeyEvent e){
	}
	
	public void mouseMoved(MouseEvent e){
		mouseX = e.getX(); //update mouse coordinates
		mouseY = e.getY(); //^
	}
	
	public void mouseDragged(MouseEvent e){
		mouseX = e.getX(); //update mouse coordinates
		mouseY = e.getY(); //^ 
	}

	public void drawDebug(Graphics g)
	{
		
				
			Color prev = g.getColor(); // save the current color of g (graphics) so we can set it back when we're done
			g.setColor(Color.green);
			
			g.drawString((int)player.x + "", 10, 10);
			g.drawString((int)player.y + "", 10, 20);
			
			g.drawString("Shelves: ID, X, Y, LENGTH", 10, 40);
			for (int i = 0; i < levels[currentLevel].shelves.length; i++)
			{
				g.drawString("S" + i, (int)levels[currentLevel].shelves[i].x, (int)levels[currentLevel].shelves[i].y);
				g.drawString("S" + i + ": (" + levels[currentLevel].shelves[i].x + "," + levels[currentLevel].shelves[i].y + ")", 10, 50 + i*10);
				g.drawString(levels[currentLevel].shelves[i].length + "", 110, 50 + i*10);
			}
			
			g.drawString("Walls: ID, X, Y, WIDTH, HEIGHT", 200, 40);
			for (int i = 0; i < levels[currentLevel].walls.length; i++)
			{
				g.drawString("W" + i, (int)levels[currentLevel].walls[i].x, (int)levels[currentLevel].walls[i].y);
				g.drawString("W" + i + ": (" + levels[currentLevel].walls[i].x + "," + levels[currentLevel].walls[i].y + ")", 200, 50 + i*10);
				g.drawString("[" + levels[currentLevel].walls[i].width + "," + levels[currentLevel].walls[i].height + "]", 340, 50 + i*10);
			}
			
			g.drawString("(" + mouseX + "," + mouseY + ")", 690, 10);
			
			
			
			//####################  addition-- draw lines where platforms are and boxes around walls ##########
			
			g.setColor(Color.blue);
			for (int i = 0; i < levels[currentLevel].shelves.length; i++)  //draw blue lines where each platform is
			{
				g.drawLine(	(int)	levels[currentLevel].shelves[i].x,
								(int)	levels[currentLevel].shelves[i].y, 
								(int)	levels[currentLevel].shelves[i].x + levels[currentLevel].shelves[i].length,
								(int)	levels[currentLevel].shelves[i].y);
			}
			
			g.setColor(Color.red);
			for (int i = 0; i < levels[currentLevel].walls.length; i++)    //draw red boxes around each of the walls
			{
				g.drawRect(	(int)	levels[currentLevel].walls[i].x,
								(int)	levels[currentLevel].walls[i].y,
								(int)	levels[currentLevel].walls[i].width,
								(int)	levels[currentLevel].walls[i].height);
			}
			//#################### end addition
			
			
			g.setColor(prev); // reset the color of g to what it was before this draw method


			
	}












}
