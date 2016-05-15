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
public class SensorGraph extends View{

    // setup initial color
    private final int paintColor = Color.RED;
    // defines paint and canvas
    private Paint drawPaint;
    public float x1, x2, x3, x4;


    public SensorGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupPaint();




    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(200,50,x1*10,50,drawPaint);
        drawPaint.setColor(Color.GREEN);
        canvas.drawLine(200,100,x2*10,100,drawPaint);
        drawPaint.setColor(Color.BLUE);
        canvas.drawLine(200,150,x3*10,150,drawPaint);
        drawPaint.setColor(Color.BLACK);
        canvas.drawLine(200,200,x4*10,200,drawPaint);

    }


    // Setup paint with color and stroke styles
    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

}
