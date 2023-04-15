package com.example.paintapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dialogSize = new Dialog(MainActivity.this);
//                dialogSize.setContentView(R.layout.dialog_canvas_size);
//                dialogSize.show();

                Intent callDrawingView = new Intent(MainActivity.this, drawing.class);
                startActivity(callDrawingView);
            }
        });
    }
//    public void okSize(View v){
////        String width = String.valueOf(width_edit.getText());
////        String height = String.valueOf(height_edit.getText());
//        bitmap = Bitmap.createBitmap(Integer.parseInt(width_edit.getText().toString()), Integer.parseInt(height_edit.getText().toString()), Bitmap.Config.ARGB_8888);
//        Intent callDrawingView = new Intent(MainActivity.this, drawing.class);
//        startActivity(callDrawingView);
//        dialogSize.dismiss();
//    }


//    @Override
//    public void onDialogToActivity(Bundle bundle) {
//        Intent callDrawingView = new Intent(MainActivity.this, drawing.class);
//        callDrawingView.putExtras(bundle);
//        startActivity(callDrawingView);
//    }
}