package com.example.desperados.ex3_sensor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
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



    public FFTGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupPaint();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawARGB(255, 0, 255, 0);
        drawPaint.setColor(Color.RED);
        canvas.drawText("FFT", 10, 100, textPaint);
        canvas.drawLine(300,100,600,100,drawPaint);


    }



    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(15);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(50);

    }


}
