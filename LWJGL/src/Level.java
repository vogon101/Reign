import java.util.ArrayList;
import java.util.Random;


public class Level {

	private ArrayList<Platform> platforms = new ArrayList<Platform>();
	private ArrayList<Mob> mobs = new ArrayList<Mob>();
	private GoalPlatform gp;
	
	public Level () {
		Random rand = new Random();
		Platform plat3;
		Platform plat = new Platform(sRi(240, 300, rand), sRi(65, 75, rand), 100, 16);
		Platform plat2 = new Platform(sRi(430, 450, rand), sRi(160, 170, rand), 100, 16);
		if (Reign.levelNum < 2) {
			plat3 = new Platform(sRi(580, 620, rand), sRi(245, 255, rand), 100, 16);
		}
		else if (Reign.levelNum < 4){
			plat3 = new DissapearingPlatform(sRi(580, 620, rand), sRi(245, 255, rand), 100, 16);
			((DissapearingPlatform) plat3).isCounting = false;
		}
		else {
			plat3 = new BouncePlatform(sRi(580, 620, rand), sRi(245, 255, rand), 100, 16);
			plat3.setColor(0d, 0d, 1d);
		}
		Platform plat4 = new Platform(sRi(730, 770, rand), sRi(345, 355, rand), 100, 16);
		platforms.add(plat);platforms.add(plat2);platforms.add(plat3);platforms.add(plat4);
		
		double r=1, g=1, b=0;
		gp = new GoalPlatform(sRi(890, 910, rand), sRi(445, 455, rand), 150, 16);
		gp.setColor(r,g,b);
		
		platforms.add(gp);
		
		for (int i = 0; i< Reign.levelNum; i++) {
			double a=  1.0 + (6.5-1.0) * rand.nextDouble();
			System.out.println(a);
			mobs.add(new Mob(sRi(150,500, rand), 38, a,  0));
		}
	}
	
	public ArrayList<Platform> getPlatforms() {
		return platforms;
	}
	
	public ArrayList<Mob> getMobs() {
		return mobs;
	}
	
	public GoalPlatform getGP() {
		return gp;
	}
	
	private int sRi(int aStart, int aEnd, Random aRandom){
	    if (aStart > aEnd) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    //get the range, casting to long to avoid overflow problems
	    long range = (long)aEnd - (long)aStart + 1;
	    // compute a fraction of the range, 0 <= frac < range
	    long fraction = (long)(range * aRandom.nextDouble());
	    int randomNumber =  (int)(fraction + aStart);    
	    return randomNumber;
	  }
}

//TODO: LEVEL GEN