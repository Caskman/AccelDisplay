package caskman.acceldisplay;

import android.content.Context;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class DisplayPanel extends SurfaceView implements SurfaceHolder.Callback {

	private MainRunnable runnable;
	private Thread thread;
	
	public DisplayPanel(Context context,String data) {
		super(context);
		getHolder().addCallback(this);
		
		
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		
		Model.getModel().setDisplayDimension(new Dimension(display.getWidth(),display.getHeight()));
//		Model model = new Model(context, new Dimension(display.getWidth(),display.getHeight()),data);
		
		runnable = new MainRunnable(getHolder());
		
		setFocusable(true);
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		runnable.setRunning(true);
		thread = new Thread(runnable);
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		Model.getModel().manageInput(e);
		return true;
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		runnable.setRunning(false);
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				
			}
		}
	}

}
