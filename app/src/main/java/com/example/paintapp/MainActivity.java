package com.example.paintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import android.widget.Toast;
import yuku.ambilwarna.AmbilWarnaDialog;

import static com.example.paintapp.display.colorList;
import static com.example.paintapp.display.current_brush;
import static com.example.paintapp.display.drawPaint;
import static com.example.paintapp.display.drawPathList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public static Path path = new Path();
    public static Paint brush = new Paint();
    Bundle myBundle;
    Button pencil, eraser, color_button, zoomImg, newPaint, save, dowloadImg;
    int mDefault;
    View imgView, motionLayer;
    float offsetX, offsetY, refX, refY;

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
        imgView = (View) findViewById(R.id.drawView);
        myBundle = savedInstanceState;

        pencil = (Button) findViewById(R.id.pencil);
        eraser = (Button) findViewById(R.id.eraser);
        color_button = (Button) findViewById(R.id.color_button);
        zoomImg = (Button) findViewById(R.id.zoomImg);
        newPaint = (Button) findViewById(R.id.newPaint);
        save = (Button) findViewById(R.id.save);
        dowloadImg = (Button) findViewById(R.id.dowloadImg);

        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawPaint.setColor(Color.BLACK);
                currentColor(drawPaint.getColor());
            }
        });

        eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawPathList.clear();
                colorList.clear();
                path.reset();
            }
        });

        mDefault = ContextCompat.getColor(MainActivity.this, R.color.black);
        color_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });

        zoomImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImgage();
            }
        });

        newPaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_Paint();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        dowloadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dowloadImage();
            }
        });
    }
    public void currentColor(int c) {
        current_brush = c;
        path = new Path();
    }

    private void new_Paint() {
        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
        newDialog.setTitle("New drawing");
        newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
        newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//                drawPath.reset();
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

    private void dowloadImage() {
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("Save drawing");
        saveDialog.setMessage("Save drawing to device Gallery?");
        saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){

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

    private void zoomImgage() {
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

    private void openColorPicker(){
        AmbilWarnaDialog color_picker = new AmbilWarnaDialog(this, mDefault, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefault = color;
                drawPaint.setColor(mDefault);
                currentColor(drawPaint.getColor());
            }
        });
        color_picker.show();
    }
}