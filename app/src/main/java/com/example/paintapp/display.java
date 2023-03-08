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
import static com.example.paintapp.MainActivity.brush;
import static com.example.paintapp.MainActivity.path;

import java.util.ArrayList;

public class display extends androidx.appcompat.widget.AppCompatImageView {
    public static ArrayList<Path> pathlist = new ArrayList<>();
    public static ArrayList<Integer> colorlist = new ArrayList<>();
    public ViewGroup.LayoutParams params;

    Bitmap bitmap;
    Canvas mcanvas;
    public static int current_brush = Color.RED;

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
        brush.setAntiAlias(true);
        brush.setColor(Color.RED);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeCap(Paint.Cap.ROUND);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeWidth(10f);
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        mcanvas = new Canvas(bitmap);
        setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x,y);
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x,y);
                pathlist.add(path);
                colorlist.add(current_brush);
                invalidate();
                return true;
            case  MotionEvent.ACTION_UP:
                pathlist.clear();
                colorlist.clear();
                path.reset();
                return  true;
            default:
                return false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(int i = 0; i < pathlist.size(); i++){
            brush.setColor(colorlist.get(i));
            mcanvas.drawPath(pathlist.get(i), brush);
            invalidate();
        }
    }
}
