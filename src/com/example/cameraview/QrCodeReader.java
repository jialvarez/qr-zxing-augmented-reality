package com.example.cameraview;

import java.util.Hashtable;

import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

public class QrCodeReader implements PreviewCallback {
	private Result result;
	private MultiFormatReader reader; 
	private boolean init = false;

	public QrCodeReader(){
		reader = new MultiFormatReader();
	    Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
	    hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
	    reader.setHints(hints);
	}

	public void onPreviewFrame(byte[] data, Camera camera) {
        Camera.Size size = camera.getParameters().getPreviewSize();
	    PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, size.width, size.height, 0, 0, size.width, size.height, false);
	    HybridBinarizer hybBin = new HybridBinarizer(source);
	    BinaryBitmap bitmap = new BinaryBitmap(hybBin);

	        try {
				result = reader.decode(bitmap);
		        Log.d("Result", "Result found!: " + String.valueOf(result));
	    } catch (NotFoundException e) {
	        Log.d("NotFoundException", "NotFoundException");
	    } catch (ReaderException e) {
	        Log.d("ReaderException", "ReaderException");
	    } finally {
	        reader.reset();
	    }
	}
}