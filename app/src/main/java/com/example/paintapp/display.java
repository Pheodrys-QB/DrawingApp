package com.example.paintapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class display extends androidx.appcompat.widget.AppCompatImageView {
    public ViewGroup.LayoutParams params;
    public static Bitmap bitmap;
    public static Canvas mcanvas;

    public static Path drawPath;
    public static Paint drawPaint, canvasPaint;
    public static int current_brush = Color.BLACK;

    public display(Context context){
        super(context);
        init(context);
    }

    public display(Context context, @Nullable AttributeSet atts){
        super(context, atts);
        init(context);
    }

    public display(Context context, @Nullable AttributeSet atts, int defStyleAttr){
        super(context, atts, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        drawPath = new Path();
        drawPaint = new Paint();

        drawPaint.setColor(current_brush);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        mcanvas = new Canvas(bitmap);
        setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }
}
