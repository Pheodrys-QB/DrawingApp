package com.example.viewpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

public class ImageAdaptor extends BaseAdapter {
    private File[] pictures;
    private Context mContext;
    public ImageAdaptor(File[] mPicture, Context mContext) {
        this.pictures = mPicture;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return pictures.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = (ImageView) convertView;

        if(imageView == null){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(350,450));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        Bitmap myBitmap = BitmapFactory.decodeFile(pictures[position].getAbsolutePath());

        imageView.setImageBitmap(myBitmap);

        return imageView;
    }
}