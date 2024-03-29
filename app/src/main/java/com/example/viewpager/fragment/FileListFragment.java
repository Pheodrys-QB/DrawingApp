package com.example.viewpager.fragment;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.viewpager.FileListAdapter;
import com.example.viewpager.R;

import java.io.File;
import java.io.FileFilter;

public class FileListFragment extends Fragment implements FileListAdapter.RecyclerToFolderlistFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FileListFragment() {
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
    public static FileListFragment newInstance(String param1, String param2) {
        FileListFragment fragment = new FileListFragment();
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
        View fragmentView = inflater.inflate(R.layout.filelist_fragment, container, false);


        RecyclerView recyclerView = fragmentView.findViewById(R.id.recycler_view);
        TextView noFilesText = fragmentView.findViewById(R.id.nofiles_textview);


        File root = getActivity().getFilesDir();
        File[] folders = root.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isDirectory());
            }
        });

        if(folders==null || folders.length ==0){
            noFilesText.setVisibility(View.VISIBLE);
            return fragmentView;
        }

        noFilesText.setVisibility(View.INVISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(new FileListAdapter(this.getContext(), folders, this));




        return fragmentView;
    }

    @Override
    public void onRecyclerToFolderlistFragment(String folder) {
            AlbumFragment mParent = (AlbumFragment) getParentFragment();
            mParent.setDetailFragment(folder);

    }
}