package com.example.mylocation.vrwalk;

import static android.graphics.Matrix.ScaleToFit.*;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    // define the display assembly compass picture
    private ImageView image;

    // device sensor manager
    private SensorManager mSensorManager;

    String filename;
    boolean sizeSet;
    int height;
    int width;

    float[] M = {0,0,0,0,0,0,0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // our compass image
        image = (ImageView) findViewById(R.id.imageViewCompass);


        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        // mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {



        if(event.sensor.getType()==Sensor.TYPE_ROTATION_VECTOR)SensorManager.getRotationMatrixFromVector(M,event.values);

        float[] values={M[0],-M[3],0,-M[1],M[4],0,0,0,1};
        Matrix mat = new Matrix();
        mat.setValues(values);
        mat.preTranslate(-image.getWidth()/2, -image.getHeight()/2);
        mat.postTranslate(image.getWidth()/2, image.getHeight()/2);
        if (M[8]>=0) filename = "@drawable/img_compass_1";
        else filename = "@drawable/img_compass_2";


        int imageResource = getResources().getIdentifier(filename, null, getPackageName());
        if (sizeSet == false) {
            height = image.getLayoutParams().height;
            width = image.getLayoutParams().width;
            sizeSet = true;
        }
        Drawable res = getResources().getDrawable(imageResource);
        image.setImageDrawable(res);
        image.setImageMatrix(mat);




    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
}