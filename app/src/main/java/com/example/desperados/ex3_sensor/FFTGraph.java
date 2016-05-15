package com.example.desperados.ex3_sensor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

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



    public FFTGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupPaint();
        fft = new FFT(64);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        float step = canvas.getWidth()/64;
        float height = canvas.getHeight();

        canvas.drawARGB(255, 0, 255, 0);
        /*drawPaint.setColor(Color.RED);
        canvas.drawText("FFT", 10, 100, textPaint);
        canvas.drawLine(300,100,600,100,drawPaint);*/
        //Log.i("Array", ""+MainActivity.mag.size());

        if (MainActivity.mag.size()==64) {

            double re[] = new double [64];
            double im[] = new double[64];
            double res[] = new double [64];
            for (int i = 0; i < 64; i++)
                re[i] = MainActivity.mag.get(i);
            Log.i("Array", ""+re);
            fft.fft(re, im);
            for (int i = 0; i < 64; i++)
                res[i] = Math.sqrt(Math.pow(re[i], 2) + Math.pow(im[i], 2));
            for(int i=1; i < 64; i++)
                canvas.drawLine((i-1)*step,height-(float)res[i-1],i*step,height-(float)res[i],drawPaint);



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
        textPaint.setTextSize(50);

    }


}
