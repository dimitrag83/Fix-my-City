package com.eap.sdy;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnTaskCompleted {
	
	//metablhtes pou apo8hkeuoun ta stoixeia
	private int category;
	private String description;
	private String image;
	private String code;
	private String item;
	
	//metablhtes poy antiproswpeuoun ta widgets
	private RadioGroup catView;
	private EditText descView;
	private EditText imageView;
	private TextView itemCodeView;
	private Button nextButton;
	private Button scanButton;
		
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		catView = (RadioGroup) findViewById(R.id.radioGroup1);
		descView = (EditText) findViewById(R.id.editText1);
		imageView = (EditText) findViewById(R.id.editText2);
		itemCodeView = (TextView) findViewById(R.id.itemCodeText);
		nextButton = (Button) findViewById(R.id.button1);
		scanButton = (Button) findViewById(R.id.scanButton);
		
		//orizoume ena neo listener gia to koumpi
		nextButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Log.i("Next button","Click");
				//pairnoyme tis times apo ta pedia
				
				int checkedButtonId = catView.getCheckedRadioButtonId();
				switch (checkedButtonId)
				{
				case R.id.radio0:
					category = 1;
					break;
				case R.id.radio1:
					category=2;
					break;
				case R.id.radio2:
					category=3;
					break;
				default: //den exei tsekaristei kapoio koumpi
					category=-1;
					break;
				
				}
				
				try{
					description = descView.getText().toString();
					image = imageView.getText().toString();
				}
				catch (Exception e)
				{
					//do nothing
				}
				
				//Elegxoume an ola ta pedia exoyn kapoia timh
				//profanws o elegxos edw einai sxetika aploikos!
				if (description.length()==0 || image.length()==0 || category==-1)
				{	
					Toast.makeText(getApplicationContext(), "One or more of the fields is empty! Please try again.", Toast.LENGTH_LONG).show();
				}
				else
				{
					Log.i("Data submitted", "cat:"+category+", desc:"+description+", img:"+image+", item "+code);
					//san Context pairnoyme auto ths synolikhs efarmoghs
					Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
					//pername ta dedomena sto epomeno activity
					intent.putExtra("Category", category);
					intent.putExtra("Description", description);
					intent.putExtra("Image", image);
					intent.putExtra("Item", code);
					startActivity(intent);
				}
			}
			
		});
		
		scanButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
				startActivityForResult(intent, 1);
			}
			
		});
		
		
	}
	
	//process the result from the activity for scanning the QR code
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == 1) {
	        if(resultCode == RESULT_OK){
	            String result=data.getStringExtra("result");
	            
	            //check the result in the remote database
	           	
	        	String checkResult="No object identified";
	        	
	        	List<NameValuePair> values = new ArrayList<NameValuePair>();
				values.add(new BasicNameValuePair("id", result));
	        	HttpCheckAsyncTask ct = new  HttpCheckAsyncTask(this);
	        	ct.execute(values);        	
	        }
	        if (resultCode == RESULT_CANCELED) {
	            //Write your code if there's no result
	        }
	    }
	}//onActivityResult
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	//callback from the asynctask that checks if the scanned code is in the database
	@Override
	public void onCodeCheckCompleted(String result) {
		// TODO Auto-generated method stub
		String[] splitResult = result.split(";");
		item = splitResult[0];
		if (splitResult.length>1)
		{
			code = splitResult[1];
			nextButton.setEnabled(true);
		}
		else
		{
			nextButton.setEnabled(false);
		}
        itemCodeView.setText(item);
		
	}

	@Override
	public void onReportsReceived(ArrayList<Report> reports) {
		//not used in this class, leave blank
		
	}

}





