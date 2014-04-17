import static org.lwjgl.opengl.GL11.*
;

import java.sql.Time;

public class DissapearingPlatform extends Platform{

	public boolean isThere = true, isCounting = false, a;
	private DissapearTimer timer = new DissapearTimer(this);
	
	public DissapearingPlatform(int x, int y, int width, int height) {
		super(x, y, width, height);
		setColor(1d, 0d, 1d);
	}
	
	public void startCountdown(boolean a) {
		(new Thread(timer)).start();
		isCounting = true;
		this.a= a;
		
	} 

	public void dissappear() {
		isThere = false;
	}
	
	public void reappear() {
		isThere = true;
	}
	
	@Override
	public void Draw () {
		glPushMatrix();
		
		glTranslated(x, y, 0);
		if (isThere)
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
	
	public void timerDone() {
		if (a) {
			isCounting = false;
			dissappear();
			(new Thread(timer)).start();
			isCounting = true;
			a = false;
		}
		else {
			isCounting = false;
			reappear();
		}
	}
	
	@Override
	public int getTopEdge() {
		return y + height;
	}
}
