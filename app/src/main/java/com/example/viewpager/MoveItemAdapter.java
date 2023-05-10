package com.example.viewpager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MoveItemAdapter extends BaseAdapter {

    final ArrayList<String> folderList;
    final Context parent;

    public MoveItemAdapter(ArrayList<String> folderList, Context parent) {
        this.folderList = folderList;
        this.parent = parent;
    }

    @Override
    public int getCount() {
        return folderList.size();
    }

    @Override
    public String getItem(int i) {
        return folderList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public String getFolder(int i){
        return  folderList.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View viewProduct;
        if (convertView == null) {
            viewProduct = View.inflate(parent, R.layout.list_item, null);
        } else viewProduct = convertView;

        ((TextView) viewProduct.findViewById(R.id.label)).setText(folderList.get(position));

        return viewProduct;
    }
}
