import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class Reign {
	
	private static Player player;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	
	private static Level level   = new Level();
	private int levelNum = 0;
	
	public static void main (String[] args) throws Exception {
		//Set screen size
		Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
		Display.create();
		Display.setTitle("Reign - The Platform Game | Pre Alpha 1.0.1 (DEV 1a)");
		//Player instance
		player = new Player();
		
		
		
		
		//Mainloop
		while (!Display.isCloseRequested())
		{	
			//Call main render method
			setCamera();
			//Draw the Background
			drawBG();
			//update and draw the player
			player.update();
			//Draw Platforms
			for (Platform platform : level.getPlatforms()) {
				platform.Draw();
			}
			//Draw Goal
			level.getGP().Draw();
			//Update screen
			Display.update();
			//60fps
			Display.sync(60);
			
			
			if (player.win) {
				Thread.sleep(1000);
				level = new Level();
				player.win = false;
				player.reset();
			}
		}
		Display.destroy();
	}
	
	
	//important Opengl shit
	public static void setCamera()
	{
		glClearColor(1f, 1f, 1f, 1.0f);
		//Clear
		glClear(GL_COLOR_BUFFER_BIT);
		//Modify projection matrix - 2d projection
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,1280, 0, 720, -1, 1);
		
		//Modify modelview matrix
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}
	
	
	public static void drawBG() {
		//static sky
		{
			glBegin(GL_QUADS);
			glColor3d(0.3, 0.4, 0.6);
			glVertex2d(0,  0);
			glVertex2d(1280, 0);
			
			glColor3d(0.1, 0.2, 0.5);
			glVertex2d(1280,  720);
			glVertex2d(0, 720);
			glEnd();
		}
		//static ground
		{
			//mud
			{
				glBegin(GL_QUADS);
				glColor3d(0.6, 0.2, 0.1);
				glVertex2d(0,  0);
				glVertex2d(1280, 0);
				
				glVertex2d(1280,  32);
				glVertex2d(0, 32);
				glEnd();
			}
			//grass
			{
				glBegin(GL_QUADS);
				glColor3d(0.2, 0.75, 0.2);
				glVertex2d(0,  32);
				glVertex2d(1280, 32);
				
				glVertex2d(1280,  37);
				glVertex2d(0, 37);
				glEnd();
			}
		}
	}
	
	//TODO: Needs to be modified to return array list of platforms
	public static ArrayList<Platform> getPlatforms() {
		return level.getPlatforms();
	}
}