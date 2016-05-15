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
            for (int i = 0; i < size; i++)
            {
                re[i] = MainActivity.mag.get(i);
                im[i] = 0;
             }

            fft.fft(re, im);
            for (int i = 0; i < size; i++) {
                res[i] = Math.sqrt(Math.pow(re[i], 2) + Math.pow(im[i], 2));
                if(i > 0)
                canvas.drawLine((i-1)*step,height-(float)res[i-1],i*step,height-(float)res[i],drawPaint);
            }

        }
        else {
            canvas.drawText("Filling the buffer for FFT",step, 100, textPaint);
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
