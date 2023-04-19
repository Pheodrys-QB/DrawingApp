package com.example.viewpager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static com.example.viewpager.display.colorList;
import static com.example.viewpager.display.current_color;
import static com.example.viewpager.display.drawPaint;
import static com.example.viewpager.display.drawPathList;
import static com.example.viewpager.display.bitmap;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import yuku.ambilwarna.AmbilWarnaDialog;

public class drawing extends Activity {
    public static Path path = new Path();
    Bundle myBundle;
    Button pencil, eraser, color_button, zoomImg, newPaint, save, dowloadImg, eyedropper;
    int mDefault;
    View imgView, motionLayer;
    float offsetX, offsetY, refX, refY;
    boolean openED = false;
    ScaleGestureDetector scaleDetector;
    float scaleFactor = 1.0f;
    float maxZoom = 5.0f;
    float minZoom = 0.5f;

    boolean isGesture = true;
    boolean isZoom = false;
    String folder;
    Intent intent;

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
        setContentView(R.layout.drawing_view);

        intent = getIntent();
        String tempStr = intent.getStringExtra("folder");
        if (tempStr == null) {
            folder = "Default";
        } else {
            folder = tempStr;
        }
        imgView = (View) findViewById(R.id.drawView);
        pencil = (Button) findViewById(R.id.pencil);
        eraser = (Button) findViewById(R.id.eraser);
        color_button = (Button) findViewById(R.id.color_button);
        eyedropper = (Button) findViewById(R.id.eyedropper);
        zoomImg = (Button) findViewById(R.id.zoomImg);
        newPaint = (Button) findViewById(R.id.newPaint);
        save = (Button) findViewById(R.id.save);
        dowloadImg = (Button) findViewById(R.id.dowloadImg);

        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBrushSize brushsize = new dialogBrushSize(drawing.this);
                brushsize.show();
            }
        });

        eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawPathList.clear();
                colorList.clear();
                path.reset();
                bitmap.eraseColor(Color.WHITE);
            }
        });
        mDefault = ContextCompat.getColor(drawing.this, R.color.black);
        color_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });

        eyedropper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEyeDropper();
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
        current_color = c;
        path = new Path();
    }

    private void new_Paint() {
        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
        newDialog.setTitle("New drawing");
        newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
        newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                drawPathList.clear();
                colorList.clear();
                path.reset();
                bitmap.eraseColor(Color.WHITE);
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

    private void openEyeDropper() {
        openED = true;
        ImageView t = findViewById(R.id.canvasLayer);
        t.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    Bitmap newbitmap = ((BitmapDrawable) t.getDrawable()).getBitmap();
                    int pixel = newbitmap.getPixel((int) motionEvent.getX(), (int) motionEvent.getY());
                    int r = Color.red(pixel);
                    int g = Color.green(pixel);
                    int b = Color.blue(pixel);
                    mDefault = Color.rgb(r, g, b);
                    drawPaint.setColor(mDefault);
                    currentColor(drawPaint.getColor());
                }
                openED = false;
                t.setOnTouchListener(null);
                return true;
            }
        });
    }

    private void dowloadImage() {
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("Save drawing");
        saveDialog.setMessage("Save drawing to " + folder + "?");
        saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                File saveDir = new File(getFilesDir(), folder);
                File saveFile = new File(saveDir, UUID.randomUUID().toString() + ".png");
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(saveFile);
                    // Use the compress method on the BitMap object to write image to the OutputStream
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Drawing saved to " + folder, Toast.LENGTH_SHORT);
                        savedToast.show();
                    } catch (IOException e) {
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                        e.printStackTrace();
                    }
                }

//                imgView.setDrawingCacheEnabled(true);
//                String imgSaved = MediaStore.Images.Media.insertImage(
//                        getContentResolver(), imgView.getDrawingCache(),
//                        UUID.randomUUID().toString() + ".png", "drawing");
//                if (imgSaved != null) {
//                    Toast savedToast = Toast.makeText(getApplicationContext(),
//                            "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
//                    savedToast.show();
//                } else {
//                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
//                            "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
//                    unsavedToast.show();
//                }
//                imgView.destroyDrawingCache();
            }
        });
        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        saveDialog.show();
    }

    private void zoomImgage() {
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

    private void openColorPicker() {
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

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
