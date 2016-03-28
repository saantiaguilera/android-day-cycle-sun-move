package com.santiago.suncycleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * Created by santi on 14/01/16.
 */
public class SunCycleView extends View {

    private static final float RADIUS_PERCENTAGE_OF_TOTAL_WIDTH = 0.5f;

    private Paint paint;
    private RectF oval;
    private Bitmap sunBitmap;

    private int todayInMinutes = 0;

    private boolean shouldMove = false;

    public SunCycleView(Context context) {
        this(context, null);
    }

    public SunCycleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SunCycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //load styled attributes.
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SunCycleView,
                defStyleAttr, 0);

        //Paint for the arc
        paint = new Paint();
        paint.setColor(attributes.getColor(R.styleable.SunCycleView_line_color, getResources().getColor(R.color.white)));
        paint.setStrokeWidth(attributes.getColor(R.styleable.SunCycleView_stroke_width, 2));
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{5, 10}, 0));

        shouldMove = attributes.getBoolean(R.styleable.SunCycleView_move, false);

        //Oval that will represent the arc
        oval = new RectF();

        //Image of the sunBitmap to draw
        sunBitmap =  BitmapFactory.decodeResource(context.getResources(), R.drawable.sun_image);
        sunBitmap = Bitmap.createScaledBitmap(sunBitmap, sunBitmap.getWidth()*2, sunBitmap.getHeight()*2, false);

        attributes.recycle();
    }

    public void setLinesColors(int color) {
        paint.setColor(color);
    }

    public void setStrokeWidth(int width) {
        paint.setStrokeWidth(width);
    }

    public void move(boolean move) {
        shouldMove = move;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //We will be using them later so keep to vars for easier calcs
        float viewWidth = (float) getWidth();
        float viewHeight = (float) getHeight();

        //Get the radius
        final float radius = viewWidth * RADIUS_PERCENTAGE_OF_TOTAL_WIDTH;

        //Set oval dimensions
        oval.set(viewWidth / 2 - radius,
                viewHeight / 4,
                viewWidth / 2 + radius,
                viewHeight / 4 + radius * 2);

        //Get angles between the intersection with the oval and the canvas
        float angleStart = (float) (Math.asin((viewHeight / 4 + radius - viewHeight) / radius) * 180 / Math.PI);
        float angleEnd = 180 - angleStart;

        //Draw it with the paint and angles
        canvas.drawArc(oval, -angleStart, -(angleEnd - angleStart), false, paint);

        if (shouldMove) {
            todayInMinutes += 60;

            if(todayInMinutes >= 1440)
                todayInMinutes = 0;
        } else todayInMinutes = Calendar.getInstance().get(Calendar.MINUTE) + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60);

        double minuteRatio = 1 - (double) todayInMinutes / 1440.0;
        double angle = (angleEnd - angleStart) * minuteRatio + angleStart;
        angle = angle * Math.PI / 180.0;

        //Draw the bitmap where it corresponds
        canvas.drawBitmap(sunBitmap, ((float) Math.cos(angle)) * radius + viewWidth / 2 - sunBitmap.getWidth() / 2,
                viewHeight / 4 + radius - ((float)Math.sin(angle))*radius - sunBitmap.getHeight()/2, null);

        if (shouldMove)
            postInvalidateDelayed(1000);
    }
}
