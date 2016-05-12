//The following sources were used as a reference:
//https://developer.android.com/guide/topics/sensors/sensors_motion.html
//https://examples.javacodegeeks.com/android/core/widget/seekbar/android-seekbar-example/
//http://developer.android.com/training/custom-views/create-view.html

package com.example.desperados.ex3_sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;


public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private static TextView vt_x, vt_y, vt_z, vt_m;
    private SeekBar sb_sensor;








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

        vt_m.setText("Covered: " + sb_sensor.getProgress() + "/" + sb_sensor.getMax());

        sb_sensor.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                vt_m.setText("Covered: " + progress + "/" + seekBar.getMax());
                Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
                mSensorManager.unregisterListener(MainActivity.this);
                mSensorManager.registerListener(MainActivity.this, mSensor, (100-progress)*2000 );
                Log.i("Progress on stop", ""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
                //mSensorManager.unregisterListener(MainActivity.this);
                //mSensorManager.registerListener(MainActivity.this, mSensor, 200000);
                Log.i("Progress on start", ""+progress);

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
               // Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
                //Log.i("i am triggered", ""+mSensor.getMinDelay());
                //mSensorManager.unregisterListener(MainActivity.this);
                //mSensorManager.registerListener(MainActivity.this, mSensor,progress*200 );
                Log.i("Progress on change", ""+progress);

            }





        });
    }








    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.

        //final float alpha = 0.8;

        // Isolate the force of gravity with the low-pass filter.
       // gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        //gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        //gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
       // linear_acceleration[0] = event.values[0] - gravity[0];
        //linear_acceleration[1] = event.values[1] - gravity[1];
        //linear_acceleration[2] = event.values[2] - gravity[2];

        vt_x.setText(Float.toString(event.values[0]));
        vt_y.setText(Float.toString(event.values[1]));
        vt_z.setText(Float.toString(event.values[2]));
        //vt_m.setText(Double.toString(Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2))));

        //Log.i("i am triggered", ""+event.values[0]);

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
        vt_x = (TextView)findViewById(R.id.vt_x);
        vt_y = (TextView)findViewById(R.id.vt_y);
        vt_z = (TextView)findViewById(R.id.vt_z);
        vt_m = (TextView)findViewById(R.id.vt_m);
        sb_sensor = (SeekBar) findViewById(R.id.sb_sensor);

    }


}
