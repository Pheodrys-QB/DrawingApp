package com.example.viewpager.fragment;

import static com.example.viewpager.display.bitmap;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viewpager.FullView;
import com.example.viewpager.ImageAdaptor;
import com.example.viewpager.MoveItemAdapter;
import com.example.viewpager.R;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;


public class PictureGridFragment extends Fragment {

    private String folder;
    private String movetoFolder;
    private ActivityResultLauncher<Intent> laucher;

    public PictureGridFragment() {
        // Required empty public constructor
    }


    public static PictureGridFragment newInstance(String folder, ActivityResultLauncher<Intent> activityLauncher) {
        PictureGridFragment fragment = new PictureGridFragment();
        Bundle args = new Bundle();
        fragment.laucher = activityLauncher;
        args.putString("folder", folder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            folder = getArguments().getString("folder");
            movetoFolder = folder;
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
        AlbumFragment mParent = (AlbumFragment) getParentFragment();

        final Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.custom_dialog);

        ImageView Image = dialog.findViewById(R.id.img);
        Button btn_Full = dialog.findViewById(R.id.btn_full);
        Button btn_copy = dialog.findViewById(R.id.btn_copy);
        Button btn_move = dialog.findViewById(R.id.btn_move);
        Button btn_Close = dialog.findViewById(R.id.btn_close);


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
                dialog.dismiss();
                laucher.launch(i);
            }
        });
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                File saveDir = new File(getActivity().getFilesDir(), folder);
                File saveFile = new File(saveDir, UUID.randomUUID().toString() + ".png");
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(saveFile);
                    // Use the compress method on the BitMap object to write image to the OutputStream
                    myBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                        Toast savedToast = Toast.makeText(getContext(),
                                "Drawing saved to " + folder, Toast.LENGTH_SHORT);
                        savedToast.show();
                    } catch (IOException e) {
                        Toast unsavedToast = Toast.makeText(getContext(),
                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                        e.printStackTrace();
                    }
                    mParent.setDetailFragment(folder);
                }
            }
        });
        btn_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File mItem = item;

                dialog.dismiss();
                final Dialog mDialog = new Dialog(getContext());
                mDialog.setContentView(R.layout.move_dialog);

                ArrayList<String> arr = new ArrayList<>();

                File root = getActivity().getFilesDir();
                File[] folders = root.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        if (file.isDirectory() && !file.getName().equals(folder)) {
                            arr.add(file.getName());
                            return true;
                        }
                        ;
                        return false;
                    }
                });

                ListView listView = mDialog.findViewById(R.id.move_list);
                Button cancelBtn = mDialog.findViewById(R.id.move_cancel);
                Button moveBtn = mDialog.findViewById(R.id.move_move);

                MoveItemAdapter adapter = new MoveItemAdapter(arr, getContext());
                listView.setAdapter(adapter);
                Log.d("set Adapter", "set");
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        movetoFolder = adapter.getFolder(i);
                    }
                });

                moveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File folderfile = new File(getActivity().getFilesDir(), movetoFolder);
                        File to = new File(folderfile, mItem.getName());
                        mItem.renameTo(to);
                        mDialog.dismiss();
                        mParent.setDetailFragment(folder);
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();
                    }
                });
                mDialog.show();
            }
        });
        dialog.show();
    }
}