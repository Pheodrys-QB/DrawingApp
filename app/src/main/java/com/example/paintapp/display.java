package com.example.paintapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import static com.example.paintapp.drawing.path;
import static com.example.paintapp.dialog_size_image.width;
import static com.example.paintapp.dialog_size_image.height;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class display extends androidx.appcompat.widget.AppCompatImageView implements DrawingToDisplay {
    public ViewGroup.LayoutParams params;
    public static Bitmap bitmap;
    public static Canvas mcanvas;

    public static ArrayList<Path> drawPathList = new ArrayList<>();
    public static ArrayList<Integer> colorList = new ArrayList<>();
    public static ArrayList<Integer> sizeBrushList = new ArrayList<>();
    public static Paint drawPaint, canvasPaint;
    public static int current_color = Color.BLACK;
    public static int current_size = 20;

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
        drawPaint = new Paint();

        drawPaint.setColor(current_color);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(current_size);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

        bitmap = Bitmap.createBitmap(1080, 1080, Bitmap.Config.ARGB_8888);
        mcanvas = new Canvas(bitmap);
        setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(touchX, touchY);
                drawPathList.add(path);
                colorList.add(current_color);
                sizeBrushList.add(current_size);
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(int i = 0; i < drawPathList.size(); i++){
            drawPaint.setColor(colorList.get(i));
            drawPaint.setStrokeWidth(sizeBrushList.get(i));
            mcanvas.drawPath(drawPathList.get(i), drawPaint);
        }
        canvas.drawBitmap(bitmap, 0, 0, canvasPaint);

    }

    @Override
    public void onDrawingToDisplay(int width, int height) {
    }

    private void newCanvas(int width, int height){
        bitmap = Bitmap.createBitmap(width , height, Bitmap.Config.ARGB_8888);
        mcanvas = new Canvas(bitmap);
        setImageBitmap(bitmap);
    }
}
