package com.eap.sdy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class HttpReportsCheckAsyncTask extends AsyncTask<List, Void, ArrayList>{

	Context c;
	private OnTaskCompleted listener;
	
	public HttpReportsCheckAsyncTask(OnTaskCompleted ac)
	{
		listener=ac;
	}
	
	@Override
	protected ArrayList doInBackground(List... params) {
		List<NameValuePair> data = params[0];
		
		HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(
                "http://150.140.15.50/sdy51/2014/getreports.php");
        
        try {
            // Add your data         
            httppost.setEntity(new UrlEncodedFormEntity(data));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            
            //diabase thn apokrish
            String responseText = EntityUtils.toString(response.getEntity());
            Log.i("Response", responseText);
            
            JSONArray results = new JSONArray(responseText);
            //the first object in the array is the status
            JSONObject status = results.getJSONObject(0);
            int statuscode = status.getInt("status");

            if(statuscode==200 && status.getInt("total_rows")>0)
            {
            	//continue to get reports
            	ArrayList<Report> reports = new ArrayList<Report>();
            	for (int x=1; x<results.length();x++)
            	{
            		/*
            		 * 
            		 * $sql="SELECT issues.id, date, problem_type, lat, lng, address, description, image_path, device_id, upvote_count
		   FROM issues join upvotes on issues.id = upvotes.issue_id
		   WHERE DISTANCE(lat, lng, $lat, $lng) < $range";
		   
            		 */
            		JSONObject jreport = results.getJSONObject(x);
            		Report report = new Report(jreport);
            		reports.add(report);
            	}
            	return reports;
            }
            else
            {
            	Log.i("Get Reports", "Status:400 or no results");
            	return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
              
		return null;
	}
	
	@Override
	protected void onPostExecute(ArrayList result) {
		listener.onReportsReceived(result);
		//Toast.makeText(c, result, Toast.LENGTH_LONG).show();		
    }

}
