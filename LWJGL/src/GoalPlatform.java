import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;


public class GoalPlatform extends Platform {
	
	private Texture texture;
	
	public GoalPlatform(int x, int y, int width, int height) {
		super(x, y, width, height);
		

		
	}
	
	@Override
	public void Draw() {
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
		
		glEnable(GL_TEXTURE_2D);
		
		
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/door.png")));
		        // Replace PNG with your file extension
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		if (texture != null) {
			
			glBegin(GL_QUADS);
			texture.bind();
			{
				
				glTexCoord2f(1,1);
				glVertex2d(50, 16);
				glTexCoord2f(0,1);
				glVertex2d(50+32, 16);
				glTexCoord2f(0,0);
				glVertex2d(50+32, 16+32);
				glTexCoord2f(1,0);
				glVertex2d(50, 16+32);
			}
			texture.release();
			glEnd();
			
		}
		else {
			System.out.println("FATAL EXCEPTION: TEXTURE FAILED TO LOAD");
			System.exit(0);
		}
		glDisable(GL_TEXTURE_2D);
		glPopMatrix();
	}

}
