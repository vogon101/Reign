package com.vogon101.game.platform.reign;
import static org.lwjgl.opengl.GL11.*; 


public class Mob {

	protected double x = 500, y = 38, xSpeed = 0, ySpeed = 0, xSave =0, ySave = 0;
	protected double height = 64;
	protected double width = 64;
	private boolean alive = true;
	
	public void update() {
		if (alive) {
			logic();
			draw();
		}
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
	
	public void kill() {
		alive = false;
	}
	
	
	public void draw() {
		glPushMatrix();
		glTranslated(x, y, 0);
		
		

		/*
		 * For a quad the coords are:
		 * vertex 1 = 0, 0
		 * vertex 2 = width, 0
		 * vertex 3 = width, height
		 * vertex 4 = 0, height
		 */
				
		glBegin(GL_QUADS);
		{
			glColor3d(1, 0, 0);
			glVertex2d(0, 0);
			glVertex2d(width, 0);
			glVertex2d(width, height);
			glVertex2d(0, height);
		}
		glEnd();
		glPopMatrix();
		
	}
	
	public double getTopEdge() {
		return y+height;
	}
	
	public double getLeftEdge() {
		return x;
	}
	
	public double getRightEdge() {
		return x+width;
	}
	
	public double getBottomEdge(){
		return y;
	}
	
	public boolean isAlive() {return alive;}
	
}
