package com.example.viewpager.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.viewpager.FullView;
import com.example.viewpager.ImageAdaptor;
import com.example.viewpager.R;

import java.io.File;


public class PictureGridFragment extends Fragment {

    private String folder;

    public PictureGridFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PictureGridFragment newInstance(String folder) {
        PictureGridFragment fragment = new PictureGridFragment();
        Bundle args = new Bundle();
        args.putString("folder", folder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            folder = getArguments().getString("folder");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.picture_grid_fragment, container, false);
        File dir = new File(getActivity().getFilesDir(), folder);
        File[] fileList = dir.listFiles();

        GridView gridView = fragmentView.findViewById(R.id.myGrid);
        gridView.setAdapter(new ImageAdaptor(fileList, this.getContext()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ShowDialog(fileList[position]);
            }
        });


        return fragmentView;
    }

    public void ShowDialog(File item) {
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.custom_dialog);

        ImageView Image = dialog.findViewById(R.id.img);
        Button btn_Full = dialog.findViewById(R.id.btn_full);
        Button btn_Close = dialog.findViewById(R.id.btn_close);

        String title = item.getName();

        Bitmap myBitmap = BitmapFactory.decodeFile(item.getAbsolutePath());
        Image.setImageBitmap(myBitmap);

        btn_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_Full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), FullView.class);
                i.putExtra("filepath", item.getAbsolutePath());
                startActivity(i);
            }
        });
        dialog.show();
    }
}