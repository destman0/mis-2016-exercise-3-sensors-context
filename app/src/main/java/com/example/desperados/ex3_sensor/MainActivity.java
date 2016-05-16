//The following sources were used as a reference:
//https://developer.android.com/guide/topics/sensors/sensors_motion.html
//https://examples.javacodegeeks.com/android/core/widget/seekbar/android-seekbar-example/
//http://developer.android.com/training/custom-views/create-view.html
//https://github.com/codepath/android_guides/wiki/Basic-Painting-with-Views
//http://code.tutsplus.com/tutorials/android-sdk-creating-custom-views--mobile-14548
//http://www.cs.dartmouth.edu/~campbell/cs65/lecture22/lecture22.html
//http://stackoverflow.com/questions/1963806/is-there-a-fixed-sized-queue-which-removes-excessive-elements
//https://developer.android.com/guide/topics/ui/notifiers/notifications.html


package com.example.desperados.ex3_sensor;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Display;
import android.util.DisplayMetrics;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


import org.apache.commons.collections4.queue.CircularFifoQueue;


public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private SeekBar sb_sensor, sb_fft;
    private SensorGraph sgraph;
    private FFTGraph fftgraph;
    public static int fsize=64;
    public static CircularFifoQueue<Double> mag = new CircularFifoQueue<Double>(64);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVariables();


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // Success! There's a accelerometer.
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        } else {
            // Failure! No accelerometer.
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.tvseat)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        int mId = 001;
        mNotificationManager.notify(mId, mBuilder.build());





        sb_sensor.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;


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

        sb_fft.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 6;


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                fsize = (int)Math.pow(2,progress);
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
        sgraph.x1=event.values[0];
        sgraph.x2=event.values[1];
        sgraph.x3=event.values[2];
        double magnitude = Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2));
        sgraph.x4 = (float)magnitude;
        sgraph.invalidate();

        //fft
        mag.add(magnitude);
        fftgraph.invalidate();

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
