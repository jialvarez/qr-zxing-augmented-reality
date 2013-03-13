package com.example.cameraview;

import java.util.Hashtable;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

public class CameraActivity extends Activity implements Camera.PreviewCallback {

    private Camera mCamera;
    private CameraPreview mPreview;
    
	private Result result;
	private MultiFormatReader reader; 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		reader = new MultiFormatReader();
	    Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
	    hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
	    reader.setHints(hints);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }
    
    public void onPause() {
    	super.onPause();
    	
    	if (mCamera != null) {
	    	mCamera.setPreviewCallback(null);
	    	mPreview.getHolder().removeCallback(mPreview);
	    	mCamera.release();
    	}
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        Size size = mCamera.getParameters().getPreviewSize();
	    PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, size.width, size.height, 0, 0, size.width, size.height, false);
	    HybridBinarizer hybBin = new HybridBinarizer(source);
	    BinaryBitmap bitmap = new BinaryBitmap(hybBin);

        ImageView myImage = (ImageView) findViewById(R.id.foto);
        
	    try {
			result = reader.decode(bitmap);
	        Log.d("Result", "Result found!: " + String.valueOf(result));

	        myImage.setVisibility(View.VISIBLE);
	        
        	if (String.valueOf(result).contentEquals("1"))
	        	myImage.setImageResource(R.drawable.juan);
	        else if (String.valueOf(result).contentEquals("2"))
	        	myImage.setImageResource(R.drawable.antonio);
	        
	    } catch (NotFoundException e1) {
	    	
	    	if (myImage != null)
        	myImage.setVisibility(View.INVISIBLE);
	    	
	        Log.d("NotFoundException", "NotFoundException");
	    } finally {
	        reader.reset();
	    }
	}

 }