package com.example.viewpager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FolderListAdapter extends ArrayAdapter<String> {
    public FolderListAdapter(@NonNull Context context, ArrayList<String> arrList) {
        super(context, 0, arrList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;

        if(currentItemView == null){
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        String currItem = getItem(position);

        TextView name = currentItemView.findViewById(R.id.itemName);

        name.setText(currItem);

        return currentItemView;
    }
}
