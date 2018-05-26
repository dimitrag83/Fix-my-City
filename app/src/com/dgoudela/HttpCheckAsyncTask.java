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

public class HttpCheckAsyncTask extends AsyncTask<List, Void, String>{

	Context c;
	private OnTaskCompleted listener;
	
	public HttpCheckAsyncTask(OnTaskCompleted ac)
	{
		listener=ac;
	}
	
	@Override
	protected String doInBackground(List... params) {
		List<NameValuePair> data = params[0];
		
		HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(
                "http://150.140.15.50/sdy51/2014/checkcode.php");
        
        try {
            // Add your data         
            httppost.setEntity(new UrlEncodedFormEntity(data));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            
            //diabase thn apokrish
            String responseText = EntityUtils.toString(response.getEntity());
            Log.i("Response", responseText);
            JSONObject message = new JSONObject(responseText);
            int statuscode = message.getInt("status");
            if(statuscode==200)
            	return message.getString("message")+";"+data.get(0).getValue(); //eg."Street Lamp;100001"
            else
            	return "Object not found";
            

        } catch (Exception e) {
            e.printStackTrace();
        }
              
		return "An error occured while checking";
	}
	
	@Override
	protected void onPostExecute(String result) {
		listener.onCodeCheckCompleted(result);
		//Toast.makeText(c, result, Toast.LENGTH_LONG).show();		
    }

}
