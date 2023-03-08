package com.example.paintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.graphics.Path;
import android.view.ViewGroup;
import android.widget.Button;

import static com.example.paintapp.display.colorlist;
import static com.example.paintapp.display.current_brush;
import static com.example.paintapp.display.pathlist;

public class MainActivity extends AppCompatActivity {
    public static Path path = new Path();
    public static Paint brush = new Paint();

    Button newPaint;

    View imgView;
    View motionLayer;
    float offsetX, offsetY;
    float refX, refY;

    ScaleGestureDetector scaleDetector;
    float scaleFactor = 1.0f;
    float maxZoom = 5.0f;
    float minZoom = 0.5f;

    boolean isGesture = true;
    boolean isZoom = false;

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(minZoom, Math.min(scaleFactor, maxZoom));

            imgView.setScaleX(scaleFactor);
            imgView.setScaleY(scaleFactor);
            imgView.invalidate();
            return true;
        }

        @Override
        public boolean onScaleBegin(@NonNull ScaleGestureDetector detector) {
            isZoom = true;
            return true;
        }

        @Override
        public void onScaleEnd(@NonNull ScaleGestureDetector detector) {
            isZoom = false;
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        imgView = (View) findViewById(R.id.canvasLayer);
//        motionLayer = (View) findViewById(R.id.motionLayer);
//        scaleDetector = new ScaleGestureDetector(motionLayer.getContext(), new ScaleListener());
//        newPaint = (Button) findViewById(R.id.newPaint);
//
//        newPaint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isGesture) {
//                    isGesture = false;
//                    motionLayer.setOnTouchListener(null);
//                    motionLayer.setClickable(false);
//
//                } else {
//                    isGesture = true;
//                    motionLayer.setOnTouchListener(new View.OnTouchListener() {
//                        @Override
//                        public boolean onTouch(View view, MotionEvent motionEvent) {
//                            scaleDetector.onTouchEvent(motionEvent);
//
//                                switch (motionEvent.getAction()) {
//                                    case MotionEvent.ACTION_DOWN:
//                                        refX = motionEvent.getX();
//                                        refY = motionEvent.getY();
//                                        break;
//                                    case MotionEvent.ACTION_MOVE:
//                                        float nX = motionEvent.getX();
//                                        float nY = motionEvent.getY();
//
//                                        offsetX += nX - refX;
//                                        offsetY += nY - refY;
//
//                                        refX = nX;
//                                        refY = nY;
//
//                                        imgView.setTranslationX(offsetX);
//                                        imgView.setTranslationY(offsetY);
//                                        imgView.invalidate();
//                                }
//
//
//                            return true;
//                        }
//
//
//                    });
//                }
//            }
//        });
    }

    public void redColor(View v) {
        brush.setColor(Color.RED);
        currentColor(brush.getColor());
    }

    public void yellowColor(View v) {
        brush.setColor(Color.YELLOW);
        currentColor(brush.getColor());
    }

    public void blueColor(View v) {
        brush.setColor(Color.BLUE);
        currentColor(brush.getColor());
    }

    public void teal_200Color(View v) {
        brush.setColor(Color.MAGENTA);
        currentColor(brush.getColor());
    }

    public void pencil(View v) {
        brush.setColor(Color.BLACK);
        currentColor(brush.getColor());
    }

    public void eraser(View v) {
        pathlist.clear();
        colorlist.clear();
        path.reset();
    }

    public void currentColor(int c) {
        current_brush = c;
        path = new Path();
    }

    public void newPaint(View v) {
        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
        newDialog.setTitle("New drawing");
        newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
        newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                pathlist.clear();
                colorlist.clear();
                path.reset();
                dialog.dismiss();
            }
        });
        newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        newDialog.show();
    }


}