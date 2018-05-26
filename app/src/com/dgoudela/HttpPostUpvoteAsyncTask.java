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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class HttpPostUpvoteAsyncTask extends AsyncTask<List, Void, Integer>{

	Context c;
	
	public HttpPostUpvoteAsyncTask(Context ac)
	{
		c=ac;
	}
	
	@Override
	protected Integer doInBackground(List... params) {
		List<NameValuePair> data = params[0];
		
		
        
        try {
            // dhmiourgia enos HTTP client kai ru8mish parametrwn syndeshs
        	
        	HttpParams httpParameters = new BasicHttpParams();
        	HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8); //gia na uposthrizetai to encoding ths bashs
        	HttpProtocolParams.setHttpElementCharset(httpParameters, HTTP.UTF_8);
                       
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            httpclient.getParams().setParameter("http.socket.timeout", new Integer(2000)); //timeout meta apo 2000ms
            httpclient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8); 
            
            HttpPost httppost = new HttpPost(
                    "http://150.140.15.50/sdy51/2014/upvote.php"); //se poio service 8a steiloume ta dedomena me POST
            
            httppost.setEntity(new UrlEncodedFormEntity(data, HTTP.UTF_8)); //dedomena pou 8a apostaloun kai encoding dedomenwn

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            
            //diabase thn apokrish
            String responseText = EntityUtils.toString(response.getEntity());
            Log.i("Response", responseText);
            JSONObject message = new JSONObject(responseText);
            int statuscode = message.getInt("status");
            return statuscode;
            

        } catch (Exception e) {
            e.printStackTrace();
        }
              
		return -1;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
       
		if(result==200)
			Toast.makeText(c, "Your upvote was registered Successfully!", Toast.LENGTH_LONG).show();
		else
			Toast.makeText(c, "The upvote failed! Code was "+result, Toast.LENGTH_LONG).show();
    }

}
