import static org.lwjgl.opengl.GL11.*;


public class Mob {

	private double x = 500, y = 38, xSpeed = 1, ySpeed = 0;
	
	public void update() {
		logic();
		draw();
	}
	
	public void logic() {
		x+=xSpeed;
		y+=ySpeed;
	}
	
	public void draw() {
		glPushMatrix();
		glTranslated(x, y, 0);
		
		
		int sm = 4;
		glBegin(GL_QUADS);
		{
			glColor3d(1, 0, 0);
			glVertex2d(-8*sm, 0*sm);
			glVertex2d(8*sm, 0*sm);
			glVertex2d(8*sm, 16*sm);
			glVertex2d(-8*sm, 16*sm);
		}
		glEnd();
		glPopMatrix();
		
	}
	
}
