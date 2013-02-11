package caskman.acceldisplay;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainRunnable implements Runnable {
	
	private static int FPS = 20;
	private static int PERIOD = 1000/FPS;
	private static int MISSED_FRAME_LIMIT = 5;
	private boolean flag;
	private long nextUpdateTick;
	private int missedFrame;
	private SurfaceHolder holder;
	
	public MainRunnable(SurfaceHolder sHolder) {
		holder = sHolder;
	}
	
	public void setRunning(boolean b) {
		flag = b;
	}
	
	@Override
	public void run() {
		
		float interpol;
		nextUpdateTick = System.currentTimeMillis();
		while(flag) {
			missedFrame = 0;
			while (System.currentTimeMillis() >= nextUpdateTick && missedFrame <= MISSED_FRAME_LIMIT) {
				update();
				nextUpdateTick += PERIOD;
				missedFrame++;
			}
			
			interpol = (float)(System.currentTimeMillis() + PERIOD - nextUpdateTick) / (float)(PERIOD);
			render(interpol);
		}
	}

	private void update() {
		Model.getModel().update();
	}
	
	private void render(float interpol) {
		Canvas c = null;
		try {
			c = holder.lockCanvas();
			if (c != null)
				Model.getModel().draw(c,interpol);
		} finally {
			if (c != null)
				holder.unlockCanvasAndPost(c);
		}
	}
	
}
