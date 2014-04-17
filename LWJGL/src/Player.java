import static  org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Player {
	
	private double x, y, xSpeed, ySpeed, floor, runtime_fwd, runtime_bk;
	private int jumptimer, score = 0, mobkills = 0, deaths = 0;
	private boolean jump,newFloor, alive = true;
	public boolean win;
	
	private static Texture texture;
	
	public Player() {
		x= 100;
		y= 38;
		floor = 37;
		newFloor = false;
		win = false;
		runtime_fwd = 1000;
		runtime_bk = -1000;
	}
	
	public void logic() {

		
		//Move Player
		x += xSpeed;
		y += ySpeed;
		
		//Get all the platforms in the level
		ArrayList<Platform> plats = Reign.getPlatforms();
		//Colision Code for platforms
		for (Platform plat : plats) {
			//if inside horizontal area of platform
			if (x > plat.getLeftEdge() && x < plat.getRightEdge()){
				//if above top of platform
				if (y > plat.getTopEdge()-4 && y < plat.getTopEdge()+4) {
					
					//if level done
					if (plat.getClass() == GoalPlatform.class) {
						win = true;
						score++;
					}
					
					//if you're on a disappearing platform
					else if (plat.getClass() == DissapearingPlatform.class) {
						DissapearingPlatform a = (DissapearingPlatform) plat;
						if (a.isThere) {
							if (!a.isCounting) {
								a.startCountdown(true);
								floor = a.getTopEdge();
								plat.isFloor = true;
								newFloor = true;
							}
							else {
								floor = a.getTopEdge();
								plat.isFloor = true;
								newFloor = true;
							}
						}
						else {
							floor = 37;
							jump = false;
							jumptimer = 0;
							plat.isFloor = false;
							newFloor = false;
							
						}
					}
					else if (plat.getClass() == BouncePlatform.class) {
						//set the floor level for gravity
						floor = plat.getTopEdge();
						//tracking booleans
						plat.isFloor = true;
						newFloor = true;
						jump = true;
						jumptimer = 0;
					}
					else {
						//set the floor level for gravity
						floor = plat.getTopEdge();
						//tracking booleans
						plat.isFloor = true;
						newFloor = true;
					}
				}
			}
			else if ((x < plat.getLeftEdge() || x > plat.getRightEdge()) && plat.isFloor){
				floor = 37;
				plat.isFloor = false;
				newFloor = false;
			}
		}
		
		//get all mobs in level
		ArrayList<Mob> mobs = Reign.getMobs();
		//collision code for mobs
		for (Mob mob : mobs) {
			if (mob.isAlive()) {
				if (mob.getClass() != Spikes.class) {
					if (x>mob.getLeftEdge() && x < mob.getRightEdge()) {
						if (y<mob.getTopEdge()/2 && y> mob.getBottomEdge()-5) {
							//die
							alive = false;
							if (score > 0)
								score--;
							deaths ++;
						}
						else if (y<mob.getTopEdge()+5&&y>mob.getTopEdge()-5) {
							//kill mob
							mob.kill();
							score++;
							mobkills++;
						}
					}
				}
				else {
					Spikes mob2 = (Spikes) mob;
					if (x>mob.getLeftEdge() && x < mob.getRightEdge()) {
						if (y<mob.getTopEdge()+5 && y> mob.getBottomEdge()-5) {
							
							//die
							alive = false;
							deaths++;
							if (score > 0)
								score--;
						}
					}
					
				}
			}
		}
		
		
		Reign.drawString("SCORE   " + String.valueOf(score), 50, 700);
		Reign.drawString("LEVEL   " + String.valueOf(Reign.levelNum), 50, 685);
		Reign.drawString("DEATHS  " + String.valueOf(deaths), 50, 670);
		Reign.drawString("KILLS   " + String.valueOf(mobkills), 50, 655);
		
		
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
			if (xSpeed < 3) {
				xSpeed +=runtime_fwd/1000;
			}
			runtime_bk = -1000;
			runtime_fwd += 2;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			if (xSpeed > -3) {
				xSpeed +=runtime_bk/1000;
			}
			runtime_bk +=2;
			runtime_fwd =1000 ;
		}
		else {
			xSpeed = 0;
			runtime_fwd = 1000;
			runtime_bk  = -1000;
		}
		
		if (!alive) {
			x = 100; y= 38;
			alive = true;
			jump = true;
;			jumptimer = 0;
			
		}
		
	}
		
		

	
	public void update() {
		
		logic();
		draw();
		
	}
	
	public void draw () {
		
		glPushMatrix();
		glTranslated(x, y, 0);
		
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND); glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
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
			
		}
		else {
			System.out.println("FATAL EXCEPTION: TEXTURE FAILED TO LOAD");
			System.exit(0);
		}
		glDisable(GL_TEXTURE_2D);
		
		glPopMatrix();
	}
	
	public void reset () {
		x = 100;
		y= 100;
		floor = 37;
		jump = false;
	}
	
	
	
	
	//TODO: enemies
	//TODO: spikes
	//TODO: death
	//TODO: score
}

