package com.appspot.smartshop.utils.capture;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.appspot.smartshop.R;

public class ImageCapture extends Activity implements SurfaceHolder.Callback
{
    private Camera camera;
    private boolean isPreviewRunning = false;
    private SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
    
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Uri target = Media.EXTERNAL_CONTENT_URI;

    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        Log.e(getClass().getSimpleName(), "onCreate");
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.capure_picture);
        surfaceView = (SurfaceView)findViewById(R.id.surface);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuItem item = menu.add(0, 0, 0, "goto gallery");
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(Intent.ACTION_VIEW, target);
                startActivity(intent);
                return true;
            }
        });
        return true;
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
    }

    Camera.PictureCallback mPictureCallbackRaw = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera c) {
            Log.e(getClass().getSimpleName(), "PICTURE CALLBACK RAW: " + data);
            camera.startPreview();
        }
    };
    
    Camera.PictureCallback mPictureCallbackJpeg= new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera c) {
            Log.e(getClass().getSimpleName(), "PICTURE CALLBACK JPEG: data.length = " + data);
        }
    };
    
    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
    	public void onShutter() {
    		Log.e(getClass().getSimpleName(), "SHUTTER CALLBACK");
    	}
    };
    

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	ImageCaptureCallback iccb = null;
    	if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
    	try {
    		String filename = timeStampFormat.format(new Date());
    		ContentValues values = new ContentValues();
    		values.put(Media.TITLE, filename);
    		values.put(Media.DESCRIPTION, "Image capture by camera");
    		Uri uri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
    		//String filename = timeStampFormat.format(new Date());
    		iccb = new ImageCaptureCallback( getContentResolver().openOutputStream(uri));
    	} catch(Exception ex ){
    		ex.printStackTrace();
    		Log.e(getClass().getSimpleName(), ex.getMessage(), ex);
    	}
    	}
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        }
 
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            camera.takePicture(mShutterCallback, mPictureCallbackRaw, iccb);
            return true;
        }

        return false;
    }

    protected void onResume()
    {
        Log.e(getClass().getSimpleName(), "onResume");
        super.onResume();
    }

    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    protected void onStop()
    {
        Log.e(getClass().getSimpleName(), "onStop");
        super.onStop();
    }

    public void surfaceCreated(SurfaceHolder holder)
    {
        Log.e(getClass().getSimpleName(), "surfaceCreated");
        camera = Camera.open();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
    {
        Log.e(getClass().getSimpleName(), "surfaceChanged");
        if (isPreviewRunning) {
            camera.stopPreview();
        }
        Camera.Parameters p = camera.getParameters();
        p.setPreviewSize(w, h);
        camera.setParameters(p);
        try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
		}
        camera.startPreview();
        isPreviewRunning = true;
    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {
        Log.e(getClass().getSimpleName(), "surfaceDestroyed");
        camera.stopPreview();
        isPreviewRunning = false;
        camera.release();
    }
}