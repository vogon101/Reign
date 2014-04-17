import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.PNGDecoder;

import static org.lwjgl.opengl.GL11.*;

public class Reign {

	private static Player player;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	private static Level level = new Level();
	public static int levelNum = 1;
	
	private static TrueTypeFont font;
	private static int fontTexture;

	public static void main(String[] args) throws Exception {
		// Set screen size
		Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
		Display.create();
		Display.setTitle("Reign - The Platform Game | Pre Alpha 1.0.1 (DEV 3a)");
		//Fonts??
		Font awtFont = new Font("Times New Roman", Font.BOLD, 24);
		font = new TrueTypeFont(awtFont, false);
		// Player instance
		player = new Player();
		init();

		
		
		// Mainloop
		while (!Display.isCloseRequested()) {
			font.drawString(100, 100, "HELLO");
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
			
			//GUI TOP
			{
				
								/*
				 * For a quad the coords are:
				 * vertex 1 = 0, 0
				 * vertex 2 = width, 0
				 * vertex 3 = width, height
				 * vertex 4 = 0, height
				 */
				glBegin(GL_QUADS);
				glColor3d(0, 0, 0);
				glVertex2d(0, 720);
				glVertex2d(1280, 720);

				glVertex2d(1280, 640);
				glVertex2d(0, 640);
				glEnd();
			}
			
			//RENDER HINTS
			if (levelNum == 1) {
				drawString("Watch out for the spikes, ", 200, 700);
				drawString("they will kill you if you get too close!", 200, 685);
				
			}
			if (levelNum == 2) {
				drawString("Those red mobs will kill you just like spikes", 200, 700);
				drawString(" but you can win points if you jump on their heads!", 200, 685);
				
			}
			if (levelNum == 3) {
				drawString("The pink platforms will dissappear", 200, 700);
				drawString("after a while so watch out! ", 200, 685);
			}
			if (levelNum == 5) {
				drawString("The blue platforms are TRAMPOLINES!!", 200, 700);
			}
		}
	}

	public static ArrayList<Platform> getPlatforms() {
		return level.getPlatforms();
	}
	
	public static ArrayList<Mob> getMobs() {
		return level.getMobs();
	}
	
	public static void drawString(String s, int x, int y) {
	      int startX = x;
	      GL11.glBegin(GL11.GL_POINTS);
	      glColor3d(0, 1, 0);
	      for (char c : s.toLowerCase().toCharArray()) {
	         if (c == 'a') {
	            for (int i = 0; i < 8; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	               GL11.glVertex2f(x + 7, y + i);
	            }
	            for (int i = 2; i <= 6; i++) {
	               GL11.glVertex2f(x + i, y + 8);
	               GL11.glVertex2f(x + i, y + 4);
	            }
	            x += 8;
	         } else if (c == 'b') {
	            for (int i = 0; i < 8; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	            }
	            for (int i = 1; i <= 6; i++) {
	               GL11.glVertex2f(x + i, y);
	               GL11.glVertex2f(x + i, y + 4);
	               GL11.glVertex2f(x + i, y + 8);
	            }
	            GL11.glVertex2f(x + 7, y + 5);
	            GL11.glVertex2f(x + 7, y + 7);
	            GL11.glVertex2f(x + 7, y + 6);
	            GL11.glVertex2f(x + 7, y + 1);
	            GL11.glVertex2f(x + 7, y + 2);
	            GL11.glVertex2f(x + 7, y + 3);
	            x += 8;
	         } else if (c == 'c') {
	            for (int i = 1; i <= 7; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	            }
	            for (int i = 2; i <= 5; i++) {
	               GL11.glVertex2f(x + i, y);
	               GL11.glVertex2f(x + i, y + 8);
	            }
	            GL11.glVertex2f(x + 6, y + 1);
	            GL11.glVertex2f(x + 6, y + 2);

	            GL11.glVertex2f(x + 6, y + 6);
	            GL11.glVertex2f(x + 6, y + 7);

	            x += 8;
	         } else if (c == 'd') {
	            for (int i = 0; i <= 8; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	            }
	            for (int i = 2; i <= 5; i++) {
	               GL11.glVertex2f(x + i, y);
	               GL11.glVertex2f(x + i, y + 8);
	            }
	            GL11.glVertex2f(x + 6, y + 1);
	            GL11.glVertex2f(x + 6, y + 2);
	            GL11.glVertex2f(x + 6, y + 3);
	            GL11.glVertex2f(x + 6, y + 4);
	            GL11.glVertex2f(x + 6, y + 5);
	            GL11.glVertex2f(x + 6, y + 6);
	            GL11.glVertex2f(x + 6, y + 7);

	            x += 8;
	         } else if (c == 'e') {
	            for (int i = 0; i <= 8; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	            }
	            for (int i = 1; i <= 6; i++) {
	               GL11.glVertex2f(x + i, y + 0);
	               GL11.glVertex2f(x + i, y + 8);
	            }
	            for (int i = 2; i <= 5; i++) {
	               GL11.glVertex2f(x + i, y + 4);
	            }
	            x += 8;
	         } else if (c == 'f') {
	            for (int i = 0; i <= 8; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	            }
	            for (int i = 1; i <= 6; i++) {
	               GL11.glVertex2f(x + i, y + 8);
	            }
	            for (int i = 2; i <= 5; i++) {
	               GL11.glVertex2f(x + i, y + 4);
	            }
	            x += 8;
	         } else if (c == 'g') {
	            for (int i = 1; i <= 7; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	            }
	            for (int i = 2; i <= 5; i++) {
	               GL11.glVertex2f(x + i, y);
	               GL11.glVertex2f(x + i, y + 8);
	            }
	            GL11.glVertex2f(x + 6, y + 1);
	            GL11.glVertex2f(x + 6, y + 2);
	            GL11.glVertex2f(x + 6, y + 3);
	            GL11.glVertex2f(x + 5, y + 3);
	            GL11.glVertex2f(x + 7, y + 3);

	            GL11.glVertex2f(x + 6, y + 6);
	            GL11.glVertex2f(x + 6, y + 7);

	            x += 8;
	         } else if (c == 'h') {
	            for (int i = 0; i <= 8; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	               GL11.glVertex2f(x + 7, y + i);
	            }
	            for (int i = 2; i <= 6; i++) {
	               GL11.glVertex2f(x + i, y + 4);
	            }
	            x += 8;
	         } else if (c == 'i') {
	            for (int i = 0; i <= 8; i++) {
	               GL11.glVertex2f(x + 3, y + i);
	            }
	            for (int i = 1; i <= 5; i++) {
	               GL11.glVertex2f(x + i, y + 0);
	               GL11.glVertex2f(x + i, y + 8);
	            }
	            x += 7;
	         } else if (c == 'j') {
	            for (int i = 1; i <= 8; i++) {
	               GL11.glVertex2f(x + 6, y + i);
	            }
	            for (int i = 2; i <= 5; i++) {
	               GL11.glVertex2f(x + i, y + 0);
	            }
	            GL11.glVertex2f(x + 1, y + 3);
	            GL11.glVertex2f(x + 1, y + 2);
	            GL11.glVertex2f(x + 1, y + 1);
	            x += 8;
	         } else if (c == 'k') {
	            for (int i = 0; i <= 8; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	            }
	            GL11.glVertex2f(x + 6, y + 8);
	            GL11.glVertex2f(x + 5, y + 7);
	            GL11.glVertex2f(x + 4, y + 6);
	            GL11.glVertex2f(x + 3, y + 5);
	            GL11.glVertex2f(x + 2, y + 4);
	            GL11.glVertex2f(x + 2, y + 3);
	            GL11.glVertex2f(x + 3, y + 4);
	            GL11.glVertex2f(x + 4, y + 3);
	            GL11.glVertex2f(x + 5, y + 2);
	            GL11.glVertex2f(x + 6, y + 1);
	            GL11.glVertex2f(x + 7, y);
	            x += 8;
	         } else if (c == 'l') {
	            for (int i = 0; i <= 8; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	            }
	            for (int i = 1; i <= 6; i++) {
	               GL11.glVertex2f(x + i, y);
	            }
	            x += 7;
	         } else if (c == 'm') {
	            for (int i = 0; i <= 8; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	               GL11.glVertex2f(x + 7, y + i);
	            }
	            GL11.glVertex2f(x + 3, y + 6);
	            GL11.glVertex2f(x + 2, y + 7);
	            GL11.glVertex2f(x + 4, y + 5);

	            GL11.glVertex2f(x + 5, y + 6);
	            GL11.glVertex2f(x + 6, y + 7);
	            GL11.glVertex2f(x + 4, y + 5);
	            x += 8;
	         } else if (c == 'n') {
	            for (int i = 0; i <= 8; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	               GL11.glVertex2f(x + 7, y + i);
	            }
	            GL11.glVertex2f(x + 2, y + 7);
	            GL11.glVertex2f(x + 2, y + 6);
	            GL11.glVertex2f(x + 3, y + 5);
	            GL11.glVertex2f(x + 4, y + 4);
	            GL11.glVertex2f(x + 5, y + 3);
	            GL11.glVertex2f(x + 6, y + 2);
	            GL11.glVertex2f(x + 6, y + 1);
	            x += 8;
	         } else if (c == 'o' || c == '0') {
	            for (int i = 1; i <= 7; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	               GL11.glVertex2f(x + 7, y + i);
	            }
	            for (int i = 2; i <= 6; i++) {
	               GL11.glVertex2f(x + i, y + 8);
	               GL11.glVertex2f(x + i, y + 0);
	            }
	            x += 8;
	         } else if (c == 'p') {
	            for (int i = 0; i <= 8; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	            }
	            for (int i = 2; i <= 5; i++) {
	               GL11.glVertex2f(x + i, y + 8);
	               GL11.glVertex2f(x + i, y + 4);
	            }
	            GL11.glVertex2f(x + 6, y + 7);
	            GL11.glVertex2f(x + 6, y + 5);
	            GL11.glVertex2f(x + 6, y + 6);
	            x += 8;
	         } else if (c == 'q') {
	            for (int i = 1; i <= 7; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	               if (i != 1)
	                  GL11.glVertex2f(x + 7, y + i);
	            }
	            for (int i = 2; i <= 6; i++) {
	               GL11.glVertex2f(x + i, y + 8);
	               if (i != 6)
	                  GL11.glVertex2f(x + i, y + 0);
	            }
	            GL11.glVertex2f(x + 4, y + 3);
	            GL11.glVertex2f(x + 5, y + 2);
	            GL11.glVertex2f(x + 6, y + 1);
	            GL11.glVertex2f(x + 7, y);
	            x += 8;
	         } else if (c == 'r') {
	            for (int i = 0; i <= 8; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	            }
	            for (int i = 2; i <= 5; i++) {
	               GL11.glVertex2f(x + i, y + 8);
	               GL11.glVertex2f(x + i, y + 4);
	            }
	            GL11.glVertex2f(x + 6, y + 7);
	            GL11.glVertex2f(x + 6, y + 5);
	            GL11.glVertex2f(x + 6, y + 6);

	            GL11.glVertex2f(x + 4, y + 3);
	            GL11.glVertex2f(x + 5, y + 2);
	            GL11.glVertex2f(x + 6, y + 1);
	            GL11.glVertex2f(x + 7, y);
	            x += 8;
	         } else if (c == 's') {
	            for (int i = 2; i <= 7; i++) {
	               GL11.glVertex2f(x + i, y + 8);
	            }
	            GL11.glVertex2f(x + 1, y + 7);
	            GL11.glVertex2f(x + 1, y + 6);
	            GL11.glVertex2f(x + 1, y + 5);
	            for (int i = 2; i <= 6; i++) {
	               GL11.glVertex2f(x + i, y + 4);
	               GL11.glVertex2f(x + i, y);
	            }
	            GL11.glVertex2f(x + 7, y + 3);
	            GL11.glVertex2f(x + 7, y + 2);
	            GL11.glVertex2f(x + 7, y + 1);
	            GL11.glVertex2f(x + 1, y + 1);
	            GL11.glVertex2f(x + 1, y + 2);
	            x += 8;
	         } else if (c == 't') {
	            for (int i = 0; i <= 8; i++) {
	               GL11.glVertex2f(x + 4, y + i);
	            }
	            for (int i = 1; i <= 7; i++) {
	               GL11.glVertex2f(x + i, y + 8);
	            }
	            x += 7;
	         } else if (c == 'u') {
	            for (int i = 1; i <= 8; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	               GL11.glVertex2f(x + 7, y + i);
	            }
	            for (int i = 2; i <= 6; i++) {
	               GL11.glVertex2f(x + i, y + 0);
	            }
	            x += 8;
	         } else if (c == 'v') {
	            for (int i = 2; i <= 8; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	               GL11.glVertex2f(x + 6, y + i);
	            }
	            GL11.glVertex2f(x + 2, y + 1);
	            GL11.glVertex2f(x + 5, y + 1);
	            GL11.glVertex2f(x + 3, y);
	            GL11.glVertex2f(x + 4, y);
	            x += 7;
	         } else if (c == 'w') {
	            for (int i = 1; i <= 8; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	               GL11.glVertex2f(x + 7, y + i);
	            }
	            GL11.glVertex2f(x + 2, y);
	            GL11.glVertex2f(x + 3, y);
	            GL11.glVertex2f(x + 5, y);
	            GL11.glVertex2f(x + 6, y);
	            for (int i = 1; i <= 6; i++) {
	               GL11.glVertex2f(x + 4, y + i);
	            }
	            x += 8;
	         } else if (c == 'x') {
	            for (int i = 1; i <= 7; i++)
	               GL11.glVertex2f(x + i, y + i);
	            for (int i = 7; i >= 1; i--)
	               GL11.glVertex2f(x + i, y + 8 - i);
	            x += 8;
	         } else if (c == 'y') {
	            GL11.glVertex2f(x + 4, y);
	            GL11.glVertex2f(x + 4, y + 1);
	            GL11.glVertex2f(x + 4, y + 2);
	            GL11.glVertex2f(x + 4, y + 3);
	            GL11.glVertex2f(x + 4, y + 4);

	            GL11.glVertex2f(x + 3, y + 5);
	            GL11.glVertex2f(x + 2, y + 6);
	            GL11.glVertex2f(x + 1, y + 7);
	            GL11.glVertex2f(x + 1, y + 8);

	            GL11.glVertex2f(x + 5, y + 5);
	            GL11.glVertex2f(x + 6, y + 6);
	            GL11.glVertex2f(x + 7, y + 7);
	            GL11.glVertex2f(x + 7, y + 8);
	            x += 8;
	         } else if (c == 'z') {
	            for (int i = 1; i <= 6; i++) {
	               GL11.glVertex2f(x + i, y);
	               GL11.glVertex2f(x + i, y + 8);
	               GL11.glVertex2f(x + i, y + i);
	            }
	            GL11.glVertex2f(x + 6, y + 7);
	            x += 8;
	         } else if (c == '1') {
	            for (int i = 2; i <= 6; i++) {
	               GL11.glVertex2f(x + i, y);
	            }
	            for (int i = 1; i <= 8; i++) {
	               GL11.glVertex2f(x + 4, y + i);
	            }
	            GL11.glVertex2f(x + 3, y + 7);
	            x += 8;
	         } else if (c == '2') {
	            for (int i = 1; i <= 6; i++) {
	               GL11.glVertex2f(x + i, y);
	            }
	            for (int i = 2; i <= 5; i++) {
	               GL11.glVertex2f(x + i, y + 8);
	            }
	            GL11.glVertex2f(x + 1, y + 7);
	            GL11.glVertex2f(x + 1, y + 6);

	            GL11.glVertex2f(x + 6, y + 7);
	            GL11.glVertex2f(x + 6, y + 6);
	            GL11.glVertex2f(x + 6, y + 5);
	            GL11.glVertex2f(x + 5, y + 4);
	            GL11.glVertex2f(x + 4, y + 3);
	            GL11.glVertex2f(x + 3, y + 2);
	            GL11.glVertex2f(x + 2, y + 1);
	            x += 8;
	         } else if (c == '3') {
	            for (int i = 1; i <= 5; i++) {
	               GL11.glVertex2f(x + i, y + 8);
	               GL11.glVertex2f(x + i, y);
	            }
	            for (int i = 1; i <= 7; i++) {
	               GL11.glVertex2f(x + 6, y + i);
	            }
	            for (int i = 2; i <= 5; i++) {
	               GL11.glVertex2f(x + i, y + 4);
	            }
	            x += 8;
	         } else if (c == '4') {
	            for (int i = 2; i <= 8; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	            }
	            for (int i = 2; i <= 7; i++) {
	               GL11.glVertex2f(x + i, y + 1);
	            }
	            for (int i = 0; i <= 4; i++) {
	               GL11.glVertex2f(x + 4, y + i);
	            }
	            x += 8;
	         } else if (c == '5') {
	            for (int i = 1; i <= 7; i++) {
	               GL11.glVertex2f(x + i, y + 8);
	            }
	            for (int i = 4; i <= 7; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	            }
	            GL11.glVertex2f(x + 1, y + 1);
	            GL11.glVertex2f(x + 2, y);
	            GL11.glVertex2f(x + 3, y);
	            GL11.glVertex2f(x + 4, y);
	            GL11.glVertex2f(x + 5, y);
	            GL11.glVertex2f(x + 6, y);

	            GL11.glVertex2f(x + 7, y + 1);
	            GL11.glVertex2f(x + 7, y + 2);
	            GL11.glVertex2f(x + 7, y + 3);

	            GL11.glVertex2f(x + 6, y + 4);
	            GL11.glVertex2f(x + 5, y + 4);
	            GL11.glVertex2f(x + 4, y + 4);
	            GL11.glVertex2f(x + 3, y + 4);
	            GL11.glVertex2f(x + 2, y + 4);
	            x += 8;
	         } else if (c == '6') {
	            for (int i = 1; i <= 7; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	            }
	            for (int i = 2; i <= 6; i++) {
	               GL11.glVertex2f(x + i, y);
	            }
	            for (int i = 2; i <= 5; i++) {
	               GL11.glVertex2f(x + i, y + 4);
	               GL11.glVertex2f(x + i, y + 8);
	            }
	            GL11.glVertex2f(x + 7, y + 1);
	            GL11.glVertex2f(x + 7, y + 2);
	            GL11.glVertex2f(x + 7, y + 3);
	            GL11.glVertex2f(x + 6, y + 4);
	            x += 8;
	         } else if (c == '7') {
	            for (int i = 0; i <= 7; i++)
	               GL11.glVertex2f(x + i, y + 8);
	            GL11.glVertex2f(x + 7, y + 7);
	            GL11.glVertex2f(x + 7, y + 6);

	            GL11.glVertex2f(x + 6, y + 5);
	            GL11.glVertex2f(x + 5, y + 4);
	            GL11.glVertex2f(x + 4, y + 3);
	            GL11.glVertex2f(x + 3, y + 2);
	            GL11.glVertex2f(x + 2, y + 1);
	            GL11.glVertex2f(x + 1, y);
	            x += 8;
	         } else if (c == '8') {
	            for (int i = 1; i <= 7; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	               GL11.glVertex2f(x + 7, y + i);
	            }
	            for (int i = 2; i <= 6; i++) {
	               GL11.glVertex2f(x + i, y + 8);
	               GL11.glVertex2f(x + i, y + 0);
	            }
	            for (int i = 2; i <= 6; i++) {
	               GL11.glVertex2f(x + i, y + 4);
	            }
	            x += 8;
	         } else if (c == '9') {
	            for (int i = 1; i <= 7; i++) {
	               GL11.glVertex2f(x + 7, y + i);
	            }
	            for (int i = 5; i <= 7; i++) {
	               GL11.glVertex2f(x + 1, y + i);
	            }
	            for (int i = 2; i <= 6; i++) {
	               GL11.glVertex2f(x + i, y + 8);
	               GL11.glVertex2f(x + i, y + 0);
	            }
	            for (int i = 2; i <= 6; i++) {
	               GL11.glVertex2f(x + i, y + 4);
	            }
	            GL11.glVertex2f(x + 1, y + 0);
	            x += 8;
	         } else if (c == '.') {
	            GL11.glVertex2f(x + 1, y);
	            x += 2;
	         } else if (c == ',') {
	            GL11.glVertex2f(x + 1, y);
	            GL11.glVertex2f(x + 1, y + 1);
	            x += 2;
	         } else if (c == '\n') {
	            y -= 10;
	            x = startX;
	         } else if (c == ' ') {
	            x += 8;
	         }
	      }
	      GL11.glEnd();
	   }

}