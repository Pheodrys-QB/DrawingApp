package com.example.viewpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class OnlineImageAdaptor extends BaseAdapter {
    private class Image {
        Bitmap bm;
        String id;
        String artist;

        Image(Bitmap b, String i, String a) {
            bm = b;
            id = i;
            artist = a;
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

    public void add(Bitmap bm, String id, String artist) {
        Image newItem = new Image(bm, id, artist);
        this.pictures.add(newItem);
    }

    public String getID(int i) {
        return this.pictures.get(i).id;
    }

    public String getArtist(int i) {
        return this.pictures.get(i).artist;
    }

    public void reset() {
        this.pictures.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RoundedImageView imageView = (RoundedImageView) convertView;

        if (imageView == null) {
            imageView = new RoundedImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(340, 340));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setCornerRadius(40.0f);
        }


        imageView.setImageBitmap(pictures.get(position).bm);

        return imageView;
    }
}