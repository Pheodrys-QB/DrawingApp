package com.example.viewpager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class FullView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_view);

        ImageView imageView = findViewById(R.id.img_full);
        String imgPath = getIntent().getExtras().getString("filepath");
        File file = new File(imgPath);
        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        imageView.setImageBitmap(myBitmap);
    }
}
