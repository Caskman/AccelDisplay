package caskman.acceldisplay;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Model {

	private static final int DURATION_LIMIT = 10;
	private static Model instance = new Model();
	private String[] data;
	private Dimension displayD;
	private int duration;
	private int line;
	private float x;
	private float y;
	private float z;
	private float pX;
	private float pY;
	private float pZ;

	private Model() {
		duration = 0;
		line = 0;
	}

	public static Model getModel() {
		return instance;
	}

	public void initializeData(String data) {
		this.data = data.split("\n");
	}

	public void setDisplayDimension(Dimension d) {
		displayD = d;
	}

	public void update() {

		if (duration >= DURATION_LIMIT) {
			duration = 0;
			while (data[line].charAt(0) == '%')
				line++;
			String[] vals = data[line].split(" ");
			
		} else {
			duration++;
		}
	}
	
	public void draw(Canvas c,float interpol) {
		Paint p = new Paint();
		p.setColor((duration >= (DURATION_LIMIT/2))?0xffff0000:0xff00ff00);
		c.drawRect(00, 0, displayD.width, displayD.height, p);
	}
}
