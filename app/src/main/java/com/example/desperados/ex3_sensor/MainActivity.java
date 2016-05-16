//The following sources were used as a reference:
//https://developer.android.com/guide/topics/sensors/sensors_motion.html
//https://examples.javacodegeeks.com/android/core/widget/seekbar/android-seekbar-example/
//http://developer.android.com/training/custom-views/create-view.html
//https://github.com/codepath/android_guides/wiki/Basic-Painting-with-Views
//http://code.tutsplus.com/tutorials/android-sdk-creating-custom-views--mobile-14548
//http://www.cs.dartmouth.edu/~campbell/cs65/lecture22/lecture22.html
//http://stackoverflow.com/questions/1963806/is-there-a-fixed-sized-queue-which-removes-excessive-elements
//https://developer.android.com/guide/topics/ui/notifiers/notifications.html
//http://stackoverflow.com/questions/5693997/android-how-to-create-an-ongoing-notification
//http://stackoverflow.com/questions/14885368/update-text-of-notification-not-entire-notification


//The icons are taken from IconFinder.com under Creative Commons licence (Non-commercial)

package com.example.desperados.ex3_sensor;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;


import org.apache.commons.collections4.queue.CircularFifoQueue;


public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private SeekBar sb_sensor, sb_fft;
    private SensorGraph sgraph;
    private FFTGraph fftgraph;
    public static int fsize=32;
    //Creating an initial buffer for the magnitude values
    public static CircularFifoQueue<Double> mag = new CircularFifoQueue<Double>(32);
    //States of user activities
    public static int state = 0, oldstate = 0;
    NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;
    int mId = 001;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVariables();

        //Checking if we have an accelerometer in our phone and if yes, initializing it.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        } else {
            Toast.makeText(getApplicationContext(), "No accelerometer found", Toast.LENGTH_SHORT).show();
        }

        //Creating an ongoing notification, with initial values, which will be later updated
         mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.sand)
                        .setContentTitle("Current Activity")
                        .setOngoing(true)
                        .setContentText("Analyzing your Activity");


        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(mId, mBuilder.build());





        //User touched the seekbar for live accelerometer data visualization
        sb_sensor.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 50;


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSensorManager.unregisterListener(MainActivity.this);
                mSensorManager.registerListener(MainActivity.this, mSensor, (100-progress)*2000 );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

        });

        //User touched the seekbar for the FFT transformation visualization
        sb_fft.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 5;


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                fsize = (int)Math.pow(2,progress);
                //Updating the size of magnitude values buffer
                mag = new CircularFifoQueue<Double>(fsize);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

        });

    }




    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {



        //accelerometer visualization
        //Updating the color bars on the custom view with live accelerometer visualization
        sgraph.x1=event.values[0];
        sgraph.x2=event.values[1];
        sgraph.x3=event.values[2];
        double magnitude = Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2));
        sgraph.x4 = (float)magnitude;
        sgraph.invalidate();

        //fft
        //Adding magnitude values to the buffer
        mag.add(magnitude);
        fftgraph.invalidate();

        //Updating the text and the icon of the notification if the user's activity is changed
        if(state!=oldstate) {
            if (state == 1) {
                mBuilder.setContentText("You are sitting");
                mBuilder.setSmallIcon(R.drawable.tvseat);
                mNotificationManager.notify(mId, mBuilder.build());
                oldstate = state;
            }

            if (state == 2) {
                mBuilder.setContentText("You are walking");
                mBuilder.setSmallIcon(R.drawable.walking);
                mNotificationManager.notify(mId, mBuilder.build());
                oldstate = state;
            }

            if (state == 3) {
                mBuilder.setContentText("You are running");
                mBuilder.setSmallIcon(R.drawable.runningman);
                mNotificationManager.notify(mId, mBuilder.build());
                oldstate = state;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }



    private void initializeVariables() {
        sb_sensor = (SeekBar) findViewById(R.id.sb_sensor);
        sb_fft = (SeekBar) findViewById(R.id.sb_fft );
        sgraph = (SensorGraph) findViewById(R.id.SensorGraph);
        fftgraph = (FFTGraph) findViewById(R.id.FFTGraph);


    }


}
