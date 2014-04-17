import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.PNGDecoder;
import static org.lwjgl.opengl.GL11.*;

public class Reign {

	private static Player player;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	private static Level level = new Level();
	public static int levelNum = 0;

	private static int fontTexture;

	public static void main(String[] args) throws Exception {
		// Set screen size
		Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
		Display.create();
		Display.setTitle("Reign - The Platform Game | Pre Alpha 1.0.1 (DEV 3a)");
		// Player instance
		player = new Player();
		init();

		// Mainloop
		while (!Display.isCloseRequested()) {

			// Call main render method
			setCamera();
			// Draw the Background
			drawBG();
			// update and draw the player
			player.update();
			// Draw Platforms
			for (final Platform platform : level.getPlatforms()) {
				platform.Draw();
			}
			//Draw mobs
			for (Mob mob : level.getMobs()) {
				mob.update();
			}
			// Draw Goal
			level.getGP().Draw();
			// Update screen
			Display.update();
			// 60fps
			Display.sync(60);

			if (player.win) {
				Thread.sleep(1000);
				level = new Level();
				player.win = false;
				player.reset();
				levelNum++;
			}
		}
		Display.destroy();
	}

	public static void init() throws FileNotFoundException, IOException {
		fontTexture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, fontTexture);
		final PNGDecoder decoder = new PNGDecoder(new FileInputStream(
				"res/font.png"));
		final ByteBuffer buffer = BufferUtils.createByteBuffer(4
				* decoder.getWidth() * decoder.getHeight());
		decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.ABGR);
		buffer.flip();
		glTexImage2D(GL_TEXTURE, 0, GL_RGB, decoder.getWidth(),
				decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	// important Opengl shit
	public static void setCamera() {
		glClearColor(1f, 1f, 1f, 1.0f);
		// Clear
		glClear(GL_COLOR_BUFFER_BIT);
		// Modify projection matrix - 2d projection
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 1280, 0, 720, -1, 1);

		// Modify modelview matrix
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

	}

	private static void renderString(String string, int textureObject,
			int gridSize, float x, float y, float characterWidth,
			float characterHeight) {
		glPushAttrib(GL_TEXTURE_BIT | GL_ENABLE_BIT | GL_COLOR_BUFFER_BIT);
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, textureObject);
		// Enable linear texture filtering for smoothed results.
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		// Enable additive blending. This means that the colours will be added
		// to already existing colours in the
		// frame buffer. In practice, this makes the black parts of the texture
		// become invisible.
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		// Store the current model-view matrix.
		glPushMatrix();
		// Offset all subsequent (at least up until 'glPopMatrix') vertex
		// coordinates.
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
		// Iterate over all the characters in the string.
		for (int i = 0; i < string.length(); i++) {
			// Get the ASCII-code of the character by type-casting to integer.
			final int asciiCode = string.charAt(i);
			// There are 16 cells in a texture, and a texture coordinate ranges
			// from 0.0 to 1.0.
			final float cellSize = 1.0f / gridSize;
			// The cell's x-coordinate is the greatest integer smaller than
			// remainder of the ASCII-code divided by the
			// amount of cells on the x-axis, times the cell size.
			final float cellX = asciiCode % gridSize * cellSize;
			// The cell's y-coordinate is the greatest integer smaller than the
			// ASCII-code divided by the amount of
			// cells on the y-axis.
			final float cellY = asciiCode / gridSize * cellSize;
			glTexCoord2f(cellX, cellY + cellSize);
			glVertex2f(i * characterWidth / 3, y);
			glTexCoord2f(cellX + cellSize, cellY + cellSize);
			glVertex2f(i * characterWidth / 3 + characterWidth / 2, y);
			glTexCoord2f(cellX + cellSize, cellY);
			glVertex2f(i * characterWidth / 3 + characterWidth / 2, y
					+ characterHeight);
			glTexCoord2f(cellX, cellY);
			glVertex2f(i * characterWidth / 3, y + characterHeight);
		}
		glEnd();
		glPopMatrix();
		glPopAttrib();
	}

	public static void drawBG() {
		glClear(GL_COLOR_BUFFER_BIT);
		renderString("hello", fontTexture, 16, -0.9f, 0, 0.3f, 0.225f);
		
		// static sky
		{
			glBegin(GL_QUADS);
			glColor3d(0.3, 0.4, 0.6);
			glVertex2d(0, 0);
			glVertex2d(1280, 0);

			glColor3d(0.1, 0.2, 0.5);
			glVertex2d(1280, 720);
			glVertex2d(0, 720);
			glEnd();
		}
		// static ground
		{
			// mud
			{
				glBegin(GL_QUADS);
				glColor3d(0.6, 0.2, 0.1);
				glVertex2d(0, 0);
				glVertex2d(1280, 0);

				glVertex2d(1280, 32);
				glVertex2d(0, 32);
				glEnd();
			}
			// grass
			{
				glBegin(GL_QUADS);
				glColor3d(0.2, 0.75, 0.2);
				glVertex2d(0, 32);
				glVertex2d(1280, 32);

				glVertex2d(1280, 37);
				glVertex2d(0, 37);
				glEnd();
			}
		}
	}

	public static ArrayList<Platform> getPlatforms() {
		return level.getPlatforms();
	}
}