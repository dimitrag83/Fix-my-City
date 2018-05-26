package com.eap.sdy;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddressActivity extends Activity {
	
	//metablhtes pou apo8hkeuoun ta stoixeia
	private int category;
	private String description;
	private String image;
	private double lat;
	private double lon;
	private String address;
	private String code;
	
	//metablhtes poy antiproswpeuoun ta widgets
	
	private EditText latView;
	private EditText lonView;
	private EditText addressView;
	private Button submitButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		
		//lambanw tis plhrofories apo thn prohgoymenh o8onh
		Bundle b = getIntent().getExtras();
		category = b.getInt("Category");
		description = b.getString("Description");
		image = b.getString("Image");
		code = b.getString("Item");
		
		Log.i("Received data","cat:"+category+", desc:"+description+", img:"+image+", item: "+code);
		
		
		//arxikopoihsh metablhtwn gia ta views
		latView = (EditText) findViewById(R.id.editText3);
		lonView = (EditText) findViewById(R.id.editText4);
		addressView = (EditText) findViewById(R.id.editText5);
		submitButton = (Button) findViewById(R.id.button2);
		
		submitButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//anakthsh timwn apo ta pedia
				try{
					//prospa8oume na metatrepsoyme oti ebale o xrhsths se dekadiko
					lat = Double.parseDouble(latView.getText().toString());
					lon = Double.parseDouble(lonView.getText().toString());
					//elegxos gia swstes syntetagmenes
					if (lat>90 && lat<-90)
						throw new Exception();
					if (lon>180 && lat<-180)
						throw new Exception();
					address = addressView.getText().toString();
				}
				catch(Exception e)
				{
					//an apotuxei, dwse akures times
					lat=-999;
					lon=-999;
				}
				
				
				//elegxos timwn
				if(lat==-999 || lon==-999 || address==null)
				{
					Toast.makeText(getApplicationContext(), "One or more of the fields is empty or has invalid data! Please try again.", Toast.LENGTH_LONG).show();

				}
				else
				{
					//ypobolh stoixeiwn
					List<NameValuePair> data = new ArrayList<NameValuePair>();
					data.add(new BasicNameValuePair("problem_type", Integer.toString(category)));
					data.add(new BasicNameValuePair("lat", Double.toString(lat)));
					data.add(new BasicNameValuePair("lng", Double.toString(lon)));
					data.add(new BasicNameValuePair("address", address));
					data.add(new BasicNameValuePair("image_path", image));
					data.add(new BasicNameValuePair("description", description));
					data.add(new BasicNameValuePair("deviceid", code));
					
					HttpPostAsyncTask pt = new HttpPostAsyncTask(getApplicationContext());
					pt.execute(data);
				}
			}
			
		});
		
		//elegxos topo8esias
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
		 
		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				latView.setText(Double.toString(location.getLatitude()));
				lonView.setText(Double.toString(location.getLongitude()));
				submitButton.setEnabled(true);
				
			}
		  };

		// Register the listener with the Location Manager to receive location updates
		Criteria c = new Criteria();
		c.setAccuracy(Criteria.ACCURACY_FINE);
		String lp = locationManager.getBestProvider(c, true);
		if (lp!=null)
		{
			locationManager.getLastKnownLocation(lp);
			locationManager.requestLocationUpdates(lp, 1000, 0, locationListener);
		}
		else
		{
			latView.setText("No location providers available");
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.address, menu);
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
