package caskman.acceldisplay;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class AccelVisualActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		String data = loadFile();
		setContentView(new DisplayPanel(this,data));
		Model.getModel().initializeData(data);
	}

	private String loadFile() {
		Intent intent = getIntent();
		String data = intent.getStringExtra("DATA");
		return data;
//		TextView t = (TextView) findViewById(R.id.viewBox);

//		DefaultHttpClient httpclient = new DefaultHttpClient();
//		HttpGet httppost = new HttpGet(url);
//		try {
//			HttpResponse response = httpclient.execute(httppost);
//			HttpEntity ht = response.getEntity();
//
//			BufferedHttpEntity buf = new BufferedHttpEntity(ht);
//
//			InputStream is = buf.getContent();
//
//			BufferedReader r = new BufferedReader(new InputStreamReader(is));
//
//			StringBuilder total = new StringBuilder();
//			String line;
//			while ((line = r.readLine()) != null) {
//				total.append(line + "\n");
//			}
//
//			return total.toString();
////			t.setText(total.toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "Error";
////			t.setText(e.toString());
//		}
	}

}
