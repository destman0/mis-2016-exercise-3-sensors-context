package com.example.desperados.ex3_sensor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.content.Context;

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

        if (buffer >= size) {

            canvas.drawText("Window Size: "+size, center, 100, textPaint);
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

            fft.fft(re, im);
            for (int i = 0; i < size; i++) {
                res[i] = Math.sqrt(Math.pow(re[i], 2) + Math.pow(im[i], 2));
                sum = sum + res[i];
                if(res[i]>maximum)
                    maximum = res[i];
                if(i > 0)
                canvas.drawLine((i-1)*step,height-(float)res[i-1],i*step,height-(float)res[i],drawPaint);
            }
            average = sum / size;
            //Log.i("average",""+average);
           // Log.i("maximum",""+maximum);
            av.add(average);
            max.add(maximum);


        }
        else {
            canvas.drawText("Filling the buffer for FFT",step, 100, textPaint);
        }

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

            if(av1<=12){
                Log.i("Sit",""+1);
            MainActivity.state = 1;
            }
            else if(av1>12&&av1<20){
                Log.i("Walk",""+2);
            MainActivity.state = 2;
            }
            else{
                Log.i("Run",""+3);
            MainActivity.state = 3;
            }

        }


    }



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
