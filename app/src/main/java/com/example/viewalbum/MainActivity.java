package com.example.viewalbum;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ArrayList<Integer> imgList = new ArrayList<>(Arrays.asList(R.drawable.pic1, R.drawable.pic2, R.drawable.pic1, R.drawable.pic1,
            R.drawable.pic1, R.drawable.pic1, R.drawable.pic1, R.drawable.pic1, R.drawable.pic1, R.drawable.pic1, R.drawable.pic1,
            R.drawable.pic1, R.drawable.pic1, R.drawable.pic1, R.drawable.pic1));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridView = findViewById(R.id.myGrid);
        gridView.setAdapter(new ImageAdaptor(imgList, this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int item_pos = imgList.get(position);
                ShowDialog(item_pos);
            }
        });
    }

    public void ShowDialog(int item_pos){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);

        TextView Image_name = dialog.findViewById(R.id.txt_Image_name);
        ImageView Image = dialog.findViewById(R.id.img);
        Button btn_Full = dialog.findViewById(R.id.btn_full);
        Button btn_Close = dialog.findViewById(R.id.btn_close);

        String title = getResources().getResourceName(item_pos);

        int index = title.indexOf("/");
        String name = title.substring(index+1,title.length());
        Image_name.setText(name);
        Image.setImageResource(item_pos);

        btn_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_Full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, FullView.class);
                i.putExtra("img_id", item_pos);
                startActivity(i);
            }
        });
        dialog.show();
    }
}