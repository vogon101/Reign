import static  org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

public class Player {
	
	private double x, y, xSpeed, ySpeed, floor, runtime_fwd, runtime_bk;
	private int jumptimer;
	private boolean jump,newFloor;
	public boolean win;
	
	private static Texture texture;
	
	public Player() {
		x= 100;
		y= 38;
		floor = 37;
		newFloor = false;
		win = false;
		runtime_fwd = 0;
		runtime_bk = 0;
	}
	
	public void logic() {

		x += xSpeed;
		y += ySpeed;
		
		
		ArrayList<Platform> plats = Reign.getPlatforms();
		
		for (Platform plat : plats) {
			if (x > plat.getLeftEdge() && x < plat.getRightEdge()){
				if (y == plat.gettopEdge() || y == plat.gettopEdge()+1) {
					floor = plat.gettopEdge();
					plat.isFloor = true;
					newFloor = true;
					if (plat.getClass() == GoalPlatform.class) {
						win = true;
					}
				}
			}
			else if ((x < plat.getLeftEdge() || x > plat.getRightEdge()) && plat.isFloor){
				floor = 37;
				plat.isFloor = false;
				newFloor = false;
			}
		}
		
		
		if (!jump) {
			if (y > floor) {
				ySpeed = -1;
			}
			else {
				ySpeed = 0;
			}
		}
		else {
			jumptimer++;
			if (jumptimer < 50) {
				ySpeed = 2;
			}
			else if (jumptimer <70) {
				ySpeed = 1;
			}
			else {
				jump = false;
			}
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W) && y <= floor){
			jump = true;
			jumptimer = 0;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S) && y >= floor || Keyboard.isKeyDown(Keyboard.KEY_S) && y >= floor+1){
			jump = false;
			jumptimer = 0;
			ySpeed = -3;
		}
		
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			if (runtime_fwd > 50) {
				xSpeed = 2;
			}
			else if (runtime_fwd > 150) {
				xSpeed = 3;
			}
			else if (runtime_fwd > 250) {
				xSpeed = 4;
			}
			else if (runtime_fwd > 350) {
				xSpeed = 5;
			}
			else {
				xSpeed = 1;
			}
			runtime_fwd += 2;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			if (runtime_bk > 50) {
				xSpeed = -2;
			}
			else if (runtime_bk > 150) {
				xSpeed = -3;
			}
			else if (runtime_bk > 250) {
				xSpeed = -4;
			}
			else if (runtime_bk > 350) {
				xSpeed = -5;
			}
			else {
				xSpeed = -1;
			}
			runtime_bk += 2;
		}
		else {
			xSpeed = 0;
			runtime_fwd = 0;
			runtime_bk  = 0;
		}
		
		
	}
	
	public void update() {
		
		logic();
		draw();
		
	}
	
	public void draw () {
		
		glPushMatrix();
		glTranslated(x, y, 0);
		{
			glBegin(GL_QUADS);
			glColor3d(1, 0, 0);
			glVertex2d(-8, 0);
			
			glColor3d(0, 1, 0);
			glVertex2d(8, 0);
			
			glColor3d(0, 0, 1);
			glVertex2d(8, 16);
			
			glColor3d(1, 1, 0.01);
			glVertex2d(-8, 16);
			
			glEnd();
		}
		
		glPopMatrix();
	}
	
	public void reset () {
		x = 100;
		y= 100;
		floor = 37;
		jump = false;
	}
	
	
	
}


/*
glColor4f(1.0f,1.0f,1.0f,1.0f);
glEnable (GL_BLEND);

glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

glEnable(GL_TEXTURE_2D);

try {
	texture = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/char.png")));
        // Replace PNG with your file extension
} catch (FileNotFoundException e) {
	e.printStackTrace();
} catch (IOException e) {
	e.printStackTrace();
	System.exit(1);
}

int sm = 4;


if (texture != null) {
	
	glBegin(GL_QUADS);
	texture.bind();
	{
		
		glTexCoord2f(1,1);
		glVertex2d(-8*sm, 0*sm);
		glTexCoord2f(0,1);
		glVertex2d(8*sm, 0*sm);
		glTexCoord2f(0,0);
		glVertex2d(8*sm, 16*sm);
		glTexCoord2f(1,0);
		glVertex2d(-8*sm, 16*sm);
	}
	texture.release();
	glEnd();
	glDisable (GL_BLEND);
}
else {
	System.out.println("FATAL EXCEPTION: TEXTURE FAILED TO LOAD");
	System.exit(0);
}
glDisable(GL_TEXTURE_2D);

*/
/*
glColor4f(1.0f,1.0f,1.0f,1.0f);
glEnable (GL_BLEND);
glPushMatrix();
glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
glTranslated(x, y, 0);
glEnable(GL_TEXTURE_2D);
		
TextureLoaderCustom tl  = new TextureLoaderCustom();
try {
	tl.getTexture("res/char.png");
} catch (IOException e) {
	e.printStackTrace();
	System.exit(0);
}

int sm = 4;

glBegin(GL_QUADS);
texture.bind();
{
	
	glTexCoord2f(1,1);
	glVertex2d(-8*sm, 0*sm);
	glTexCoord2f(0,1);
	glVertex2d(8*sm, 0*sm);
	glTexCoord2f(0,0);
	glVertex2d(8*sm, 16*sm);
	glTexCoord2f(1,0);
	glVertex2d(-8*sm, 16*sm);
}
glEnd();
glDisable (GL_BLEND);
*/
