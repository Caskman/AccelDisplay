package caskman.acceldisplay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Spinner s = (Spinner)findViewById(R.id.spinner);
		s.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				EditText e = (EditText)findViewById(R.id.urlBox);
				int pos = arg0.getSelectedItemPosition();
				if (pos >= 1 && pos <= 3)
					e.setText(getResources().getStringArray(R.array.sampleUrls)[pos-1]);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void openFile(View view) {
		Intent intent = new Intent(this,ViewFileActivity.class);
		EditText e = (EditText)findViewById(R.id.urlBox);
		intent.putExtra("FILE_URL", e.getText().toString());
		startActivity(intent);
	}
	
	public void openDisplay(View view) {
		Intent intent = new Intent(this,AccelVisualActivity.class);
		EditText e = (EditText)findViewById(R.id.urlBox);
		intent.putExtra("FILE_URL", e.getText().toString());
		startActivity(intent);
	}

	public void spinnerSelected(View view) {
		Spinner s = (Spinner) findViewById(R.id.spinner);
		EditText e = (EditText)findViewById(R.id.urlBox);
		int pos = s.getSelectedItemPosition();
		switch (pos) {
		case 0:
			break;
		case 1:
		case 2:
		case 3:
			e.setText(getResources().getStringArray(R.array.sampleUrls)[pos]);
			break;
		}
	}
	
}
