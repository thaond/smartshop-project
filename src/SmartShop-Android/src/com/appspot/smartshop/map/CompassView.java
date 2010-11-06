package com.appspot.smartshop.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Config;
import android.util.Log;
import android.view.View;

import com.appspot.smartshop.R;
import com.appspot.smartshop.utils.Global;

public class CompassView  extends View {
	public static final String TAG = "[CompassView]";
	
	private Paint   mPaint = new Paint();
    private Path    mPath = new Path();
    private boolean mAnimate;
    private long    mNextTime;
    
    public float x1 = 1.1f;
    public float y1 = 1.1f;
    public float x2 = 100.2f;
    public float y2 = 100f;
    
    private CompassView mView;
    private float[] mValues;
    
    Bitmap bmp = BitmapFactory.decodeResource(
			Global.application.getResources(), R.drawable.compass_arrow);
    
    private SensorListener mListener = new SensorListener() {
    
        public void onSensorChanged(int sensor, float[] values) {
//            if (Config.LOGD) Log.d(TAG, "sensorChanged (" + values[0] + ", " + values[1] + ", " + values[2] + ")");
            mValues = values;
            if (mView != null) {
                mView.invalidate();
            }
        }

        public void onAccuracyChanged(int sensor, int accuracy) {
        }
    };

    public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Global.mSensorManager.registerListener(mListener, 
        		SensorManager.SENSOR_ORIENTATION,
        		SensorManager.SENSOR_DELAY_GAME);
		mView = this;
    }

    boolean first = true;
    public static final int SIZE = 30;
    @Override protected void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        int cx = SIZE / 2;
        int cy = SIZE / 2;

        canvas.translate(cx, cy);
        if (mValues != null) {            
            canvas.rotate(-mValues[0]);
        }
        
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(1f);
//        if (x1 == x2) {
//        	if (y2 > y1) {
//        		canvas.drawLine(0, 0, 0, SIZE / 2, mPaint);
//        	} else {
//        		canvas.drawLine(0, 0, 0, - SIZE / 2, mPaint);
//        	}
//        } else {
//        	double alpha = Math.atan((y2 - y1) / (x2 - x1));
//        	float yB = (float) (SIZE / 2 * Math.cos(alpha));
//        	float xB = (float) (SIZE / 2 * Math.sin(alpha));
//        	canvas.drawLine(0, 0, xB, yB, mPaint);
//        }
        canvas.drawColor(Color.BLACK);
        mPaint.setColor(Color.RED);
        Path path = new Path();
        path.lineTo(-5, -5);
        path.lineTo(0, 15);
        path.lineTo(5, 0);
        path.lineTo(0, 0);
    }

    @Override
    protected void onAttachedToWindow() {
        mAnimate = true;
        super.onAttachedToWindow();
    }
    
    @Override
    protected void onDetachedFromWindow() {
        mAnimate = false;
        super.onDetachedFromWindow();
    }
}
