import static org.lwjgl.opengl.GL11.*;


public class Platform {

	protected int x, y;
	protected int height, width;
	public boolean isFloor;
	protected double r = 0, g = 1,  b = 0.1;
	
	public Platform (int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
	}
	
	public void Draw () {
		glPushMatrix();
		
		glTranslated(x, y, 0);
		
		{
			/*
			 * For a quad the coords are:
			 * vertex 1 = 0, 0
			 * vertex 2 = width, 0
			 * vertex 3 = width, height
			 * vertex 4 = 0, height
			 */
			glBegin(GL_QUADS);
			glColor3d(r, g, b);
			glVertex2d(0, 0);
			
			glVertex2d(width, 0);
			
			glVertex2d(width, height);
			
			glVertex2d(0, height);
			
			glEnd();
		}
		
		glPopMatrix();
	}
	
	public int getTopEdge() {
		return y+height;
	}
	
	public int getLeftEdge() {
		return x;
	}
	
	public int getRightEdge() {
		return x+width;
	}
	
	public int getBottomEdge(){
		return y;
	}
	
	public void setColor(Double r, Double g, Double b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
}


