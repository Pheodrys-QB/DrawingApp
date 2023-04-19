package com.example.viewpager;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import static com.example.viewpager.drawing.path;
import static com.example.viewpager.display.current_size;
import static com.example.viewpager.display.drawPaint;

import androidx.annotation.NonNull;

public class dialogBrushSize extends Dialog {
    Button ok_brush_size, cancel_brush_size;
    SeekBar brush_size;
    TextView show_brush_size;
    public dialogBrushSize(@NonNull Context context) {
        super(context);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_brush_size);

        brush_size = (SeekBar) findViewById(R.id.brush_size);
        show_brush_size = (TextView) findViewById(R.id.show_brush_size);
        ok_brush_size = (Button) findViewById(R.id.ok_brush_size);
        cancel_brush_size = (Button) findViewById(R.id.cancel_brush_size);

        brush_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //Log.d("AA","Value: " + i);
                show_brush_size.setText("Selected brush size: " + Integer.toString(i));
                ok_brush_size.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        drawPaint.setStrokeWidth(i);
                        currentSize(i);
                        dismiss();
                    }
                });

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        cancel_brush_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }
    public void currentSize(int c) {
        current_size = c;
        path = new Path();
    }
}