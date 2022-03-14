package com.example.mylocation.vrwalk;

import android.app.Activity;
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

    // record the compass picture angle turned
    private float currentDegree = 0f;

    // device sensor manager
    private SensorManager mSensorManager;

    TextView tvHeading1;
    TextView tvHeading2;
    TextView tvHeading3;
    TextView tvHeading4;
    String filename;
    double d2X = 0;
    double d2Y = 0;
    double d2Z = 0;
    double dX = 0;
    double dY = 0;
    double dZ = 0;
    double Mx;
    double My;
    double Mz;
    double X = 0;
    double Y = 0;
    double Z = 0;
    double dt=0;
    double tlast=0;
    double T;
    double Tbeg=System.currentTimeMillis()/1000.0;;

    float[] M = {0,0,0,0,0,0,0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // our compass image
        image = (ImageView) findViewById(R.id.imageViewCompass);

        // TextView that will tell the user what degree is he heading
        tvHeading1 = (TextView) findViewById(R.id.tvHeading1);
        tvHeading2 = (TextView) findViewById(R.id.tvHeading2);
        tvHeading3 = (TextView) findViewById(R.id.tvHeading3);
        tvHeading4 = (TextView) findViewById(R.id.tvHeading4);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_GAME);

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated


        if(event.sensor.getType()==Sensor.TYPE_ROTATION_VECTOR)SensorManager.getRotationMatrixFromVector(M,event.values);
        if(event.sensor.getType()==Sensor.TYPE_LINEAR_ACCELERATION){
            d2X = 0;
            d2Y = 0;
            d2Z = 0;
            d2X = M[0]*event.values[0]+M[1]*event.values[1]+M[2]*event.values[2];
            d2Y = M[3]*event.values[0]+M[4]*event.values[1]+M[5]*event.values[2];
            d2Z = M[6]*event.values[0]+M[7]*event.values[1]+M[8]*event.values[2];


            T = System.currentTimeMillis()/1000.0-Tbeg;
            if(tlast!=0){
            }
            tlast = T;

            double x_,y_,z_,x__,y__,z__;
            x_=M[6];
            y_=M[7];
            z_=M[8];
            x__=M[0];
            y__=M[1];
            z__=M[2];

            Complex k;
            double pitch,azimuth,roll;
            Complex factor;

            k = new Complex(z_,y_);
            pitch = Complex.phase(k);
            factor = Complex.exp(new Complex(0,-pitch));
            k=Complex.mult(k,factor);
            z_=k.real;
            y_=k.imaginary;

            k = new Complex(z__,y__);
            k=Complex.mult(k,factor);
            z__=k.real;
            y__=k.imaginary;

            k = new Complex(z_,x_);
            roll = Complex.phase(k);
            factor = Complex.exp(new Complex(0,-roll));
            k=Complex.mult(k,factor);
            z_=k.real;
            x_=k.imaginary;

            k = new Complex(z__,x__);
            k=Complex.mult(k,factor);
            z__=k.real;
            x__=k.imaginary;

            k = new Complex(x__,y__);
            azimuth = Complex.phase(k);
            factor = Complex.exp(new Complex(0,-azimuth));
            k=Complex.mult(k,factor);
            x__=k.real;
            y__=k.imaginary;

            azimuth=-(azimuth*180/Math.PI);
            pitch=(pitch*180/Math.PI);
            roll=(roll*180/Math.PI);

            azimuth=((azimuth%360)+360)%360;
            pitch=(((pitch+180)%360)+360)%360-180;
            roll=(((roll+180)%360)+360)%360-180;

            String st1;
            String st2;
            String st3;
            if(30*(int)(0.5+12*azimuth/360)>0){
                st1="p"+Integer.toString(30*(int)(0.5+12*azimuth/360));
            }
            else{
                st1="m"+Integer.toString(30*(int)(0.5+12*Math.abs(azimuth)/360));
            }

            if(30*(int)(0.5+12*pitch/360)>0){
                st2="p"+Integer.toString(30*(int)(0.5+12*pitch/360));
            }
            else{
                st2="m"+Integer.toString(30*(int)(0.5+12*Math.abs(pitch)/360));
            }

            if(30*(int)(0.5+12*roll/360)>0){
                st3="p"+Integer.toString(30*(int)(0.5+12*roll/360));
            }
            else{
                st3="m"+Integer.toString(30*(int)(0.5+12*Math.abs(roll)/360));
            }

            filename="@drawable/rotated_compass_"+st1+"_"+st2+"_"+st3;

            int imageResource = getResources().getIdentifier(filename, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            image.setImageDrawable(res);




            tvHeading1.setText(Double.toString(azimuth));
            tvHeading2.setText(Double.toString(pitch));
            tvHeading3.setText(Double.toString(roll));
            tvHeading4.setText("");

        }

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -0,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -0;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
}