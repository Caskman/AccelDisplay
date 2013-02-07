package caskman.acceldisplay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ViewFileActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_file_layout);
		loadFile();
	}
	
	private void loadFile() {
		Intent intent = getIntent();
		String url = intent.getStringExtra("FILE_URL");
		TextView t = (TextView)findViewById(R.id.viewBox);
		t.setText(url);
	}
	
	
	
}
