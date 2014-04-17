import static org.lwjgl.opengl.GL11.*;


public class Mob {

	private double x = 500, y = 38, xSpeed = 0, ySpeed = 0, xSave =0, ySave = 0;
	
	public void update() {
		logic();
		draw();
	}
	
	public Mob(double x_, double y_, double xSpeed_, double ySpeed_) {
		x=x_;
		y=y_;
		xSpeed=xSpeed_;
		xSave=xSpeed_;
		ySpeed=ySpeed_;
		ySave=ySpeed_;
	}
	
	public void logic() {
		x+=xSpeed;
		y+=ySpeed;
		
		if (x> Reign.WIDTH) {
			xSpeed = -xSave;
		}		else if (x < 0) {
			xSpeed = xSave;
		}
		
	}
	
	public void draw() {
		glPushMatrix();
		glTranslated(x, y, 0);
		
		
		int sm = 2;
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
