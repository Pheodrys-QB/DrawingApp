package com.example.viewpager.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.viewpager.FullView;
import com.example.viewpager.OnlineFullView;
import com.example.viewpager.OnlineImageAdaptor;
import com.example.viewpager.R;
import com.example.viewpager.drawing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnlineImageAdaptor adaptor;
    private FirebaseStorage storage;
    private FirebaseFirestore db;
    private DocumentReference beginDoc;
    private int totalAmount;
    private int addLimit = 30;
    private boolean isLoading;
    private int THRESHOLD = 3;
    private int mode = 0;

    public CommunityFragment() {
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
    public static CommunityFragment newInstance(String param1, String param2) {
        CommunityFragment fragment = new CommunityFragment();
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
        View curView = inflater.inflate(R.layout.fragment_community, container, false);
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        SwipeRefreshLayout refreshLayout = curView.findViewById(R.id.refresh_layer);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isLoading) {
                    isLoading = true;
                    adaptor.reset();

                    // fetch data
                    // update beginDoc
                    // update totalAmount
                    // update isLoading
                }
            }
        });


        adaptor = new OnlineImageAdaptor(this.getContext());
        GridView gridView = curView.findViewById(R.id.imageGrid);

        isLoading = true;
        //get image from db
        // update beginDoc
        // update totalAmount
        // update isLoading

        gridView.setAdapter(adaptor);
        adaptor.notifyDataSetChanged();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent i = new Intent(getContext(), OnlineFullView.class);

                String imgId = adaptor.getID(position);

                i.putExtra("imageID", imgId);

                startActivity(i);
            }
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;
                if (totalAmount - lastItem >= THRESHOLD) {
                    if (!isLoading) {
                        isLoading = true;
                        // here you have reached end of list, load more data into adaptor
                        // update beginDoc
                        // update totalAmount
                        // update isLoading
                    }
                }
            }
        });


        // Inflate the layout for this fragment
        return curView;
    }
}