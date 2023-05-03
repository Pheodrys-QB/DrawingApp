package com.example.viewpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class OnlineImageAdaptor extends BaseAdapter {
    private class Image{
        Bitmap bm;
        String id;
        Image(Bitmap b, String i){
            bm = b;
            id = i;
        }
    }
    private ArrayList<Image> pictures;
    private Context mContext;
    public OnlineImageAdaptor(Context mContext) {
        this.mContext = mContext;
        this.pictures = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return pictures.size();
    }

    @Override
    public Bitmap getItem(int position) {
        return this.pictures.get(position).bm;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void add(Bitmap bm, String id){
        Image newItem = new Image(bm, id);
        this.pictures.add(newItem);
    }
    public String getID(int i){
        return this.pictures.get(i).id;
    }
    public void reset(){
        this.pictures.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = (ImageView) convertView;

        if(imageView == null){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(350,450));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }


        imageView.setImageBitmap(pictures.get(position).bm);

        return imageView;
    }
}