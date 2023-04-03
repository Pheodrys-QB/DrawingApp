package com.example.paintapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
//                dialog_size_image imagesize = new dialog_size_image(MainActivity.this);
//                imagesize.show();
                Intent callDrawingView = new Intent(MainActivity.this, drawing.class);
                startActivity(callDrawingView);
            }
        });
    }
}