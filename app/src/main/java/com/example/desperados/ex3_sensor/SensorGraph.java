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
    private final int textColor = Color.WHITE;
    // defines paint and canvas
    private Paint drawPaint, textPaint;
    public float x1, x2, x3, x4, xcent, ycent;



    public SensorGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupPaint();




    }


    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawARGB(255, 0, 0, 0);
        drawPaint.setColor(Color.RED);
        canvas.drawText("X: "+x1, 10, 100, textPaint);
        canvas.drawLine(xcent,100,xcent+x1*10,100,drawPaint);
        drawPaint.setColor(Color.GREEN);
        canvas.drawText("Y: "+x2, 10, 200, textPaint);
        canvas.drawLine(xcent,200,xcent+x2*10,200,drawPaint);
        drawPaint.setColor(Color.BLUE);
        canvas.drawText("Z: "+x3, 10, 300, textPaint);
        canvas.drawLine(xcent,300,xcent+x3*10,300,drawPaint);
        drawPaint.setColor(Color.WHITE);
        canvas.drawText("Magnitude:", 10, 400, textPaint);
        canvas.drawLine(xcent,400,xcent+x4*10,400,drawPaint);

    }


    // Setup paint with color and stroke styles
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
