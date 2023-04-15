package com.example.paintapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import static com.example.paintapp.display.bitmap;

public class dialog_size_image extends Dialog {
    Bundle myBundle;
    public static EditText width_edit, height_edit;
    Button ok_size, cancel_size;
    LinearLayout canvasLayer;
    public static String width, height;

    public dialog_size_image(@NonNull Context context) {
        super(context);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_canvas_size);

        width_edit = (EditText) findViewById(R.id.width_edit);
        height_edit = (EditText) findViewById(R.id.height_edit);
        ok_size = (Button) findViewById(R.id.ok_size);
        cancel_size = (Button) findViewById(R.id.cancel_size);

        ok_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                width = String.valueOf(width_edit.getText());
                height = String.valueOf(height_edit.getText());
                Bundle bundle = new Bundle();
                bundle.putString("width", width);
                bundle.putString("height", height);

              MainActivity main =  (MainActivity) getOwnerActivity();
              //main.onDialogToActivity(bundle);
                //bitmap = Bitmap.createBitmap(Integer.parseInt(width), Integer.parseInt(height_edit.getText().toString()), Bitmap.Config.ARGB_8888);
                dismiss();
            }
        });
        cancel_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }
}
