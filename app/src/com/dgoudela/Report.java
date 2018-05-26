package com.eap.sdy;

import org.json.JSONException;
import org.json.JSONObject;

public class Report {

	protected int id;
	protected String item;
	protected String date;
	protected int category;
	protected double lat;
	protected double lng;
	protected String description;
	protected int upvotes;
	
	public Report (JSONObject jreport)
	{
		try {
			id=jreport.getInt("id");
			item=jreport.getString("device_id");
			date=jreport.getString("date");
			category=jreport.getInt("problem_type");
			lat=jreport.getDouble("lat");
			lng=jreport.getDouble("lng");
			description=jreport.getString("description");
			upvotes=jreport.getInt("upvote_count");
		} catch (JSONException e) {
			e.printStackTrace();
		}
			
	}
}
