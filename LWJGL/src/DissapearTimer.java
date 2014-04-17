
public class DissapearTimer implements Runnable {

	private DissapearingPlatform plat;
	private boolean done = false;
	
	public DissapearTimer(DissapearingPlatform obj) {
		plat = obj;
	}
	
	@Override
	public void run() {
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		this.timerDone();

	}

	public synchronized void timerDone(){
		done = true;
		plat.timerDone();
	}
	
	public synchronized boolean isTimerDone() {
		return done;
	}
	
}
