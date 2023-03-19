package com.example.paintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.graphics.Path;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static com.example.paintapp.display.bitmap;
import static com.example.paintapp.display.current_brush;
import static com.example.paintapp.display.drawPaint;
import static com.example.paintapp.display.drawPath;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public static Path path = new Path();
    public static Paint brush = new Paint();
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
    }

    public void redColor(View v) {

    }

    public void pencil(View v) {
        drawPaint.setColor(Color.BLACK);
        currentColor(drawPaint.getColor());
    }

    public void eraser(View v) {
       drawPath.reset();
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
                drawPath.reset();
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

    public void dowloadPaint(View v) {
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("Save drawing");
        saveDialog.setMessage("Save drawing to device Gallery?");
        saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                imgView = (View) findViewById(R.id.drawView);
                imgView.setDrawingCacheEnabled(true);
                String imgSaved = MediaStore.Images.Media.insertImage(
                        getContentResolver(), imgView.getDrawingCache(),
                        UUID.randomUUID().toString()+".png", "drawing");
                if(imgSaved!=null){
                    Toast savedToast = Toast.makeText(getApplicationContext(),
                            "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                    savedToast.show();
                }
                else{
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
                imgView.destroyDrawingCache();
            }
        });
        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        saveDialog.show();
    }

    public void zoomImg(View v) {
        imgView = (View) findViewById(R.id.drawView);
        motionLayer = (View) findViewById(R.id.motionLayer);
        scaleDetector = new ScaleGestureDetector(motionLayer.getContext(), new ScaleListener());
        if (isGesture) {
            isGesture = false;
            motionLayer.setOnTouchListener(null);
            motionLayer.setClickable(false);

        } else {
            isGesture = true;
            motionLayer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    scaleDetector.onTouchEvent(motionEvent);
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            refX = motionEvent.getX();
                            refY = motionEvent.getY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float nX = motionEvent.getX();
                            float nY = motionEvent.getY();

                            offsetX += nX - refX;
                            offsetY += nY - refY;

                            refX = nX;
                            refY = nY;

                            imgView.setTranslationX(offsetX);
                            imgView.setTranslationY(offsetY);
                            imgView.invalidate();
                    }
                    return true;
                }
            });
        }
    }

    public void openColorPicker(View v){
        setContentView(R.layout.color_picker);
        ImageView imgcolorpicker = (ImageView) findViewById(R.id.imgcolorpicker);
        imgcolorpicker.setDrawingCacheEnabled(true);
        imgcolorpicker.buildDrawingCache(true);
        Button btn_ok = (Button)findViewById(R.id.btn_ok);
        Button btn_close = (Button)findViewById(R.id.btn_close);
        View viewColor = (View)findViewById(R.id.view_color);

        imgcolorpicker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    bitmap = imgcolorpicker.getDrawingCache();
                    int pixel = bitmap.getPixel((int)motionEvent.getX(), (int)motionEvent.getY());
                    int r = Color.red(pixel);
                    int g = Color.green(pixel);
                    int b = Color.blue(pixel);
                    viewColor.setBackgroundColor(Color.rgb(r, g, b));
                    drawPaint.setColor(Color.rgb(r, g, b));
                }
                return true;
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}