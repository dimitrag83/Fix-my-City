package com.eap.sdy;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ReportDetailsActivity extends Activity {

	Button upButton;
	int reportid; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_details);
		Bundle b = getIntent().getExtras();
		
		reportid = b.getInt("reportid");
		TextView detailsText = (TextView)findViewById(R.id.reportDetailsText);
		detailsText.setText("Report ID: "+reportid+"\n"+b.getString("details"));
		
		upButton = (Button) findViewById(R.id.upVoteButton);
		upButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				List<NameValuePair> data = new ArrayList<NameValuePair>();
				data.add(new BasicNameValuePair("id", Integer.toString(reportid)));
				
				HttpPostUpvoteAsyncTask t = new HttpPostUpvoteAsyncTask(getApplicationContext());
				t.execute(data);
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.report_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
