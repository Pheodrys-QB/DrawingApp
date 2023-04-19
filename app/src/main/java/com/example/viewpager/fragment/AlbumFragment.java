package com.example.viewpager.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.viewpager.R;
import com.example.viewpager.drawing;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FrameLayout bar;
    private LinearLayout returnBtn, createBtn;
    String foldername = "Default";
    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        setDetailFragment(foldername);
                    }
                }
            }
    );

    public AlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommunityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumFragment newInstance(String param1, String param2) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View albumFragmentView = inflater.inflate(R.layout.fragment_album, container, false);
        bar = albumFragmentView.findViewById(R.id.returnBar);
        bar.setVisibility(View.GONE);

        returnBtn = albumFragmentView.findViewById(R.id.returnBtn);
        createBtn = albumFragmentView.findViewById(R.id.createDraw);

        getChildFragmentManager().beginTransaction().replace(R.id.albumHolder, new FileListFragment()).commit();


        return albumFragmentView;
    }

    public void setDetailFragment(String folder) {
        foldername = folder;
        bar.setVisibility(View.VISIBLE);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getChildFragmentManager().beginTransaction().replace(R.id.albumHolder, new FileListFragment()).commit();
                returnBtn.setOnClickListener(null);
                createBtn.setOnClickListener(null);
                bar.setVisibility(View.GONE);
            }
        });
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), drawing.class);
                intent.putExtra("folder", folder);
                activityLauncher.launch(intent);
            }
        });
        getChildFragmentManager().beginTransaction().replace(R.id.albumHolder, PictureGridFragment.newInstance(folder)).commit();

    }


}