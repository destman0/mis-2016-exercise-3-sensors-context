package com.example.desperados.ex3_sensor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.apache.commons.collections4.queue.CircularFifoQueue;

/**
 * Created by Dest 0 on 13/05/2016.
 */
public class FFTGraph extends View {
    // setup initial color
    private final int paintColor = Color.RED;
    private final int textColor = Color.WHITE;
    // defines paint and canvas
    private Paint drawPaint, textPaint;
    FFT fft;
    public static CircularFifoQueue<Double> av = new CircularFifoQueue<Double>(100);
    public static CircularFifoQueue<Double> max = new CircularFifoQueue<Double>(100);





    public FFTGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int size = MainActivity.fsize;
        int buffer = MainActivity.mag.size();

        float step = canvas.getWidth() / size;
        float height = canvas.getHeight();
        float center = canvas.getWidth()/2;

        canvas.drawARGB(255, 173, 216, 230);

        //When the buffer for the sensor values is full,...
        if (buffer >= size) {

            canvas.drawText("Window Size: "+size, center, 100, textPaint);
            canvas.drawText(""+size, center, 150, textPaint);
            fft = new FFT(size);
            double re[] = new double[size];
            double im[] = new double[size];
            double res[] = new double[size];
            double maximum = 0;
            double sum =0;
            double average = 0;
            for (int i = 0; i < size; i++)
            {
                re[i] = MainActivity.mag.get(i);
                im[i] = 0;
             }
            //...performing fast fourier transform
            fft.fft(re, im);
            for (int i = 0; i < size; i++) {
                //Getting the magnitude
                res[i] = Math.sqrt(Math.pow(re[i], 2) + Math.pow(im[i], 2));
                sum = sum + res[i];
                if(res[i]>maximum)
                    maximum = res[i];
                if(i > 0)
                canvas.drawLine((i-1)*step,height-(float)res[i-1],i*step,height-(float)res[i],drawPaint);
            }
            //Calculating the average value for this transformation
            average = sum / size;
            //Storing the average value in the buffer
            av.add(average);
            max.add(maximum);


        }
        //When the application is started, or the user changed the size of FFT window, it takes time to fill the buffer
        else {
            canvas.drawText("Filling the buffer for FFT",step, 100, textPaint);
            canvas.drawText("Please wait...",step, 150, textPaint);
        }

        //When the buffer of average values from FFT is full, calculating the averages, and...
        if(av.size()==100){
            double sum1 = 0;
            double max1 = 0;
            double av1;
            for (int i = 1; i<100; i++) {
                sum1 = sum1 + av.get(i);
                if(av.get(i)>max1)
                    max1 = av.get(i);
            }
            av1 = sum1/100;
            av.clear();
            max.clear();
            Log.i("Average of Averages",""+av1);
            Log.i("Max of max",""+max1);

            //... identifying which activity is user currently doing
            if(av1<=15){
                Log.i("Sit",""+1);
            MainActivity.state = 1;
            }
            else if(av1>15&&av1<20){
                Log.i("Walk",""+2);
            MainActivity.state = 2;
            }
            else{
                Log.i("Run",""+3);
            MainActivity.state = 3;
            }

        }


    }


    //Setting up paints for line drawing and texts
    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(40);

    }


}
