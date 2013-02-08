package caskman.acceldisplay;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class Model {

	private static final int DURATION_LIMIT = 1;
	private static Model instance = new Model();
	private Queue<MotionEvent> inputQueue;
	private String[] data;
	private Dimension displayD;
	private DisplayState state;
	private boolean error;
	private boolean isInit;
	private String errorInfo;
	private int duration;
	private int line;
	private float x;
	private float y;
	private float z;
	private float nX;
	private float nY;
	private float nZ;

	private Model() {
		duration = 0;
		line = 0;
		inputQueue = new LinkedBlockingQueue<MotionEvent>();
		state = DisplayState.STOPPED;
		isInit = false;
		errorInfo = "";
	}

	public static Model getModel() {
		return instance;
	}

	public void initializeData(String data) {
		this.data = data.split("\n");
		duration = 0;
		line = 0;
		nX = 0;
		nY = 0;
		nZ = 0;
		error = false;
		state = DisplayState.STOPPED;
		stepValues();
		isInit = true;
	}

	public void setDisplayDimension(Dimension d) {
		displayD = d;
	}

	private void processInput() {
		MotionEvent e = null;
		while (!inputQueue.isEmpty()) {
			e = inputQueue.poll();
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				switch (state) {
				case PLAYING:
					state = DisplayState.PAUSED;
					break;
				case PAUSED:
				case STOPPED:
					state = DisplayState.PLAYING;
					break;
				}
			}
		}
	}

	private void stepValues() {
		while (data[line].trim().charAt(0) == '%')
			line++;
		String[] vals = data[line].split("\t");
		float[] fVals = new float[3];
		try {
			for (int i = 0; i < 3; i++)
				fVals[i] = Float.parseFloat(vals[i]);
		} catch (NumberFormatException e) {
			error = true;
			errorInfo = e.toString();
			return;
		}
		x = nX;
		y = nY;
		z = nZ;
		nX = fVals[0];
		nY = fVals[1];
		nZ = fVals[2];
		line++;
	}
	
	public void update() {
		if (!isInit)
			return;
		
		processInput();

		if (state == DisplayState.PLAYING) {
//			duration = (duration + 1) % DURATION_LIMIT;
			if (duration >= DURATION_LIMIT) {
				duration = 0;
				stepValues();
				if (line >= data.length) {
					state = DisplayState.STOPPED;
					line = 0;
					x = 0;
					y = 0;
					z = 0;
				}
			} else {
				duration++;
		 }
		}
	}

	public void draw(Canvas c, float interpol) {
		Paint p = new Paint();
		p.setColor(0xffffffff);
//		p.setColor((duration >= (DURATION_LIMIT / 2)) ? 0xffff0000 : 0xff0000ff);
		c.drawRect(0, 0, displayD.width, displayD.height, p);
		if (!isInit)
			return;
		drawValuesColor(c,interpol);
		drawValuesText(c,interpol);
		if (state == DisplayState.PAUSED || state == DisplayState.STOPPED) {
			drawStateText(c,interpol);
		}
	}
	
	private void drawValuesColor(Canvas c,float interpol) {
		float[] drawVals = getDrawValues(interpol);
		float midHeight = displayD.height/2;
		float widthInc = displayD.width/3;
		Paint p = new Paint();;
		Rect r = new Rect();
		
		for (int i = 0; i < 3; i++) {
			r.left = (int) (i*widthInc);
			r.right = (int) ((i+1)*widthInc);
			r.top = (int) midHeight;
			r.bottom = (int) midHeight;
			if (drawVals[i] < 0) {
				r.bottom += (int) ((0-drawVals[i])*midHeight);
			} else {
				r.top -= (int) (drawVals[i]*midHeight);
			}
			p.setColor(0xff000000 + (0xff<<(8*i)));
			c.drawRect(r, p);
		}
	}

	private float[] getDrawValues(float interpol) {
		float[] curVals = new float[3];
		float[] nextVals = new float[3];
		float[] drawVals = new float[3];
		float currentFraction = (float)duration/(float)DURATION_LIMIT;
		float nextFraction = (float)(duration + 1)/(float)DURATION_LIMIT;
		curVals[0] = (nX-x)*currentFraction + x;
		curVals[1] = (nY-y)*currentFraction + y;
		curVals[2] = (nZ-z)*currentFraction + z;
		nextVals[0] = (nX-x)*nextFraction + x;
		nextVals[1] = (nY-y)*nextFraction + y;
		nextVals[2] = (nZ-z)*nextFraction + z;
		
		
//		drawVals[0] = (nextVals[0]-curVals[0])*interpol + curVals[0];
//		drawVals[1] = (nextVals[1]-curVals[1])*interpol + curVals[1];
//		drawVals[2] = (nextVals[2]-curVals[2])*interpol + curVals[2];
		
//		drawVals[0] = (nextVals[0] - x)*interpol + x;
//		drawVals[1] = (nextVals[1] - y)*interpol + y;
//		drawVals[2] = (nextVals[2] - z)*interpol + z;
		
		return curVals;
	}
	
	private void drawValuesText(Canvas c,float interpol) {
		float[] drawVals = getDrawValues(interpol);
		
		float heightInc = displayD.height/4;
		Paint p = new Paint();
		p.setColor(0xff000000);
		Rect bounds = new Rect();
		String text;
		for (int i = 0; i < 3; i++) {
			text = ""+drawVals[i];
			p.getTextBounds(text, 0, text.length(), bounds);
			c.drawText(text, (displayD.width - bounds.width())/2, heightInc*(i+1), p);
		}
		c.drawText(data[line], 0, 50, p);
		c.drawText(duration + "", 0, 70, p);
		c.drawText("Error: "+error,0,90,p);
		c.drawText(errorInfo, 0, 110, p);
	}
	
	private void drawStateText(Canvas c,float interpol) {
		Rect bounds = new Rect();
		Paint p = new Paint();
		String text = (state == DisplayState.STOPPED) ? "Stopped"
				: "Paused";
		p.setColor(0xff000000);
		p.setAntiAlias(true);
		p.setTextSize(50F);
		p.getTextBounds(text, 0, text.length(), bounds);
		c.drawText(text, (displayD.width - bounds.width()) / 2,
				(displayD.height - bounds.height()) / 2, p);
	}

	public void manageInput(MotionEvent e) {
		inputQueue.offer(e);
	}

	private enum DisplayState {
		PLAYING, PAUSED, STOPPED
	}
}
