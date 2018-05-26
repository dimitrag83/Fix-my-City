package com.eap.sdy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MapActivity extends Activity implements OnMapReadyCallback, OnTaskCompleted {
	
	private GoogleMap theMap;
	private Location userLocation;
	private boolean focusOnUser = true;
	private ArrayList<Report> reportList;
	private HashMap<String, Integer> markerMap; //a map for relating markerIDs to report IDs
	private final double range = 2.5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		markerMap = new HashMap<String, Integer>();
		
		//enable the user's location tracking
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			//un-necessary methods
			public void onStatusChanged(String provider, int status, Bundle extras) {}
		    public void onProviderEnabled(String provider) {}
		    public void onProviderDisabled(String provider) {}

		    @Override
			public void onLocationChanged(Location location) {
				//every time there is a location update
				userLocation = location;	
				if (focusOnUser)
				{
					//animate the map to keep the user's coords in view.
					theMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(userLocation.getLatitude(), userLocation.getLongitude())), 3000, null);
				}
				//now that we have the user's coordinates, get the relevant reports
				//do it only one time, when we get the user coords!
				if(reportList==null)
				{
					getReports();
				}
				
			}
		  };

		// Register the listener with the Location Manager to receive location updates
		Criteria c = new Criteria();
		c.setAccuracy(Criteria.ACCURACY_COARSE);
		String lp = locationManager.getBestProvider(c, true);
		if (lp!=null)
		{
			locationManager.getLastKnownLocation(lp);
			locationManager.requestLocationUpdates(lp, 1000, 0, locationListener);
		}
		else
		{
			//latView.setText("No location providers available");
		}
		
		//prepare the map and run the callback method when the map has loaded
		MapFragment mapFragment = (MapFragment) getFragmentManager()
			    .findFragmentById(R.id.the_map);
		mapFragment.getMapAsync(this);	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
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

	@Override
	public void onMapReady(GoogleMap map) {
		theMap = map;
		if (userLocation!=null)
		{
			//move the map to the user's location
			theMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(userLocation.getLatitude(), userLocation.getLongitude())), 3000, null);
			Log.i("MapView", "Getting report data");
			//ask to retrieve the report data from the server
			getReports();
			
		}
		//create a listener so that we can know which infowindow the user has clicked on, so we can 
		//then show the full report.
		
		theMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker marker) {
				// TODO Auto-generated method stub
				Log.i("Map View", "Clicked"+ marker.getId()+" for item "+reportList.get(markerMap.get(marker.getId())).item);
				Intent intent = new Intent(getApplicationContext(), ReportDetailsActivity.class);
				
				//pass the data to the next activity
				Report r = reportList.get(markerMap.get(marker.getId()));
				intent.putExtra("details", "Item Code: "+ r.item+"\n"+
										"Problem Category: " + r.category+"\n"+
										"Problem Description: " +r.description+"\n"+
										"Upvotes: " +r.upvotes);
				intent.putExtra("reportid", r.id);
				startActivity(intent);
			}
		});
		
	}

	@Override
	public void onCodeCheckCompleted(String result) {
		// useless here, keep it empty
		
	}

	public void getReports()
	{
		//ask to retrieve the report data from the server
		List<NameValuePair> values = new ArrayList<NameValuePair>();
		values.add(new BasicNameValuePair("lat", Double.toString(userLocation.getLatitude())));
		values.add(new BasicNameValuePair("lng", Double.toString(userLocation.getLongitude())));
		values.add(new BasicNameValuePair("range", Double.toString(range)));
		HttpReportsCheckAsyncTask ct = new  HttpReportsCheckAsyncTask(this);
    	ct.execute(values);
	}
	
	@Override
	public void onReportsReceived(ArrayList<Report> reports) {
		// we have just received the report data, now make the markers on the map!
		reportList = reports;
		for (int x=0;x<reportList.size();x++)
		{
			//add the marker to the map;
			int markerid = 1;
			switch (reportList.get(x).category)
			{
			case 1:
				markerid = R.drawable.marker1;
				break;
			case 2:
				markerid = R.drawable.marker2;
				break;
			case 3:
				markerid = R.drawable.marker3;
				break;
			case 4:
				markerid = R.drawable.marker4;
				break;
			case 5:
				markerid = R.drawable.marker5;
				break;
			default:
				break;
			}
			
			Marker m = theMap.addMarker(new MarkerOptions()
	        .position(new LatLng(reportList.get(x).lat,reportList.get(x).lng))
	        .icon(BitmapDescriptorFactory.fromResource(markerid))
	        .title(reportList.get(x).description)
	        .snippet("Date:"+reportList.get(x).date));
			
			//add the marker to the marker-report map. This map correlates the Google-assigned markerIDs
			//with the position of the report on the list, so we can know which report to show later
			//when a user clicks on the marker info window
			markerMap.put(m.getId(), x);
			
		}
	}
}
