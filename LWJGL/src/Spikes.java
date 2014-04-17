import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;


public class Spikes extends Mob{
	
	private Texture texture;

	public Spikes(double x_, double y_, double xSpeed_, double ySpeed_) {
		super(x_, y_, xSpeed_, ySpeed_);
		height = 32;
	}
	
	@Override
	public void draw() {
		glPushMatrix();
		glTranslated(x, y, 0);
		
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND); glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/spikes.png")));
		        // Replace PNG with your file extension
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		int sm = 1;
		
		if (texture != null) {
			
			glBegin(GL_QUADS);
			texture.bind();
			{
				
				/*
				 * For a quad the coords are:
				 * vertex 1 = 0, 0
				 * vertex 2 = width, 0
				 * vertex 3 = width, height
				 * vertex 4 = 0, height
				 */
				glColor4f(1, 1, 1, 1);
				glTexCoord2f(1,1);
				glVertex2d(0, 0);
				glTexCoord2f(0,1);
				glVertex2d(width, 0);
				glTexCoord2f(0,0);
				glVertex2d(width, height);
				glTexCoord2f(1,0);
				glVertex2d(0, height);
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
