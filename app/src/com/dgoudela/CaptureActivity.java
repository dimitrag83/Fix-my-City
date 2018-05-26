package com.eap.sdy;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.eap.sdy.CameraManager;
import com.eap.sdy.CaptureHandler;
import com.eap.sdy.PreviewCallback;
import com.eap.sdy.BoundingView;
import com.eap.sdy.CameraPreviewView;

/**
 * Capture activity (camera barcode activity)
 */
public class CaptureActivity extends Activity {
    /**
     * Camera preview view
     */
    private CameraPreviewView cameraPreview;
    /**
     * Camera manager
     */
    private CameraManager cameraManager;
    /**
     * Capture handler
     */
    private Handler captureHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        // Create an instance of Camera
        cameraManager = new CameraManager();
        captureHandler = new CaptureHandler(cameraManager, this, new OnDecoded());
        //requesting next frame for decoding
        cameraManager.requestNextFrame(new PreviewCallback(captureHandler, cameraManager));

        // Create our Preview view and set it as the content of our activity.
        cameraPreview = (CameraPreviewView) findViewById(R.id.camera_preview);
        cameraPreview.setCameraManager(cameraManager);
        ((BoundingView) findViewById(R.id.bounding_view)).setCameraManager(cameraManager);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraManager.release();
    }

    private class OnDecoded implements CaptureHandler.OnDecodedCallback {
        @Override
        public void onDecoded(String decodedData) {
        	
        	//return the result to the activity, then the activity will check if the result is valid in the database
        	
        	Intent returnIntent = new Intent();
        	returnIntent.putExtra("result",decodedData);
        	setResult(RESULT_OK,returnIntent);
        	finish();
            //oast.makeText(CaptureActivity.this, "hahaha "+decodedData, Toast.LENGTH_SHORT);
        }
    }
}
