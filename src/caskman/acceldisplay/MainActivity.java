package caskman.acceldisplay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Spinner s = (Spinner)findViewById(R.id.sampleLinkSpinner);
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
		
		s = (Spinner)findViewById(R.id.originSpinner);
		s.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int pos = arg0.getSelectedItemPosition();
				if (pos == 0) {
					((Spinner)findViewById(R.id.sampleLinkSpinner)).setEnabled(false);
					((EditText)findViewById(R.id.urlBox)).setEnabled(false);
					((Spinner)findViewById(R.id.localFileSpinner)).setEnabled(false);
					((Button)findViewById(R.id.viewButton)).setEnabled(false);
					((Button)findViewById(R.id.animateButton)).setEnabled(false);
				} else if (pos == 1) {
					((Spinner)findViewById(R.id.sampleLinkSpinner)).setEnabled(true);
					((EditText)findViewById(R.id.urlBox)).setEnabled(true);
					((Spinner)findViewById(R.id.localFileSpinner)).setEnabled(false);
					((Button)findViewById(R.id.viewButton)).setEnabled(true);
					((Button)findViewById(R.id.animateButton)).setEnabled(true);
				} else if (pos == 2) {
					((Spinner)findViewById(R.id.sampleLinkSpinner)).setEnabled(false);
					((EditText)findViewById(R.id.urlBox)).setEnabled(false);
					((Spinner)findViewById(R.id.localFileSpinner)).setEnabled(true);
					((Button)findViewById(R.id.viewButton)).setEnabled(true);
					((Button)findViewById(R.id.animateButton)).setEnabled(true);
				}
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
		intent.putExtra("DATA", getData());
		startActivity(intent);
	}
	
	public void openDisplay(View view) {
		Intent intent = new Intent(this,AccelVisualActivity.class);
		intent.putExtra("DATA", getData());
		startActivity(intent);
	}

	private String getData() {
		int type = ((Spinner)findViewById(R.id.originSpinner)).getSelectedItemPosition();
		String data = "Error";
		if (type == 1) {
			data = requestFile(((EditText)findViewById(R.id.urlBox)).getText().toString());
		} else if (type == 2) {
			data = getLocalFile(getResources().getStringArray(R.array.localFilenames)[((Spinner)findViewById(R.id.localFileSpinner)).getSelectedItemPosition() - 1]);
		}
		return data;
	}

	private String getLocalFile(String filename) {
		AssetManager am = getAssets();
		String data = "Error";
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(am.open(filename)));
			data = "";
			if (in.ready())
				data += in.readLine();
			while (in.ready()) 
				data += "\n"+in.readLine();
			
		} catch (IOException e) {
			data = e.toString();
		}
		return data;
	}
	
	private String requestFile(String url) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httppost = new HttpGet(url);
		BufferedReader r;
		try {
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity ht = response.getEntity();

			BufferedHttpEntity buf = new BufferedHttpEntity(ht);

			InputStream is = buf.getContent();

			r = new BufferedReader(new InputStreamReader(is));

			StringBuilder total = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				total.append(line + "\n");
			}
			r.close();
			return total.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		} 

	}
	
	
	
}
