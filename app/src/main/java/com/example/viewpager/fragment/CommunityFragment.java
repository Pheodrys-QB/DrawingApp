package com.example.viewpager.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.viewpager.FullView;
import com.example.viewpager.OnlineFullView;
import com.example.viewpager.OnlineImageAdaptor;
import com.example.viewpager.R;
import com.example.viewpager.drawing;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    private DocumentSnapshot beginDoc;
    private SwipeRefreshLayout refreshLayout;
    private int totalAmount = 0;
    private int addLimit = 30;
    private int totalLimit = 0;
    private boolean isLoading;
    private int THRESHOLD = 3;
    private int mode = 0;
    private int count;
    private LinearLayout popularTab;
    private LinearLayout recentTab;

    private class downloadThread extends Thread {
        private int mode;
        private int limit;
        private DocumentSnapshot startAfter;
        private boolean isFirst;

        downloadThread(int mode, int limit, DocumentSnapshot startAfter, boolean isFirst) {
            this.mode = mode;
            this.limit = limit;
            this.startAfter = startAfter;
            this.isFirst = isFirst;
        }

        public void run() {
            Log.d("Download iamge", "Begin");
            if (this.isFirst) {
                Query first = db.collection("posts");
                if (mode == 1) {
                    first = first.orderBy("like", Query.Direction.DESCENDING);
                }

                first = first.limit(this.limit);

                first.get().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot documentSnapshots) {


                                int snapshotCount = documentSnapshots.size() - 1;
                                if (snapshotCount == -1) return;
                                beginDoc = documentSnapshots.getDocuments()
                                        .get(snapshotCount);
                                totalAmount += snapshotCount;
                                totalLimit += addLimit;
                                count = 0;
                                for (DocumentSnapshot doc : documentSnapshots.getDocuments()) {
                                    Log.d("Download iamge", doc.getId());

                                    StorageReference islandRef = storage.getReference().child("images/" + doc.getId() + ".png");

                                    final long ONE_MEGABYTE = 1024 * 1024;
                                    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            // Data for "images/island.jpg" is returns, use this as needed
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            adaptor.add(bitmap, doc.getId());

                                            if (count == snapshotCount) {
                                                getActivity().runOnUiThread(new Runnable() {

                                                    @Override
                                                    public void run() {
                                                        Log.d("Download iamge", "notify");

                                                        adaptor.notifyDataSetChanged();
                                                        Log.d("Download iamge", "end");

                                                        isLoading = false;
                                                        refreshLayout.setRefreshing(false);

                                                    }
                                                });
                                            }
                                            count += 1;

                                        }
                                    });
                                }


                            }

                        });
            } else {
                Query next = db.collection("posts");
                if (mode == 1) {
                    next = next.orderBy("like", Query.Direction.DESCENDING);
                }
                next = next.startAfter(beginDoc)
                        .limit(this.limit);
                next.get().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot documentSnapshots) {
                                int snapshotCount = documentSnapshots.size() - 1;
                                if (snapshotCount == -1) return;
                                beginDoc = documentSnapshots.getDocuments()
                                        .get(snapshotCount);
                                totalAmount += snapshotCount;
                                totalLimit += addLimit;
                                count = 0;
                                for (DocumentSnapshot doc : documentSnapshots.getDocuments()) {
                                    StorageReference islandRef = storage.getReference().child("images/" + doc.getId() + ".png");

                                    final long ONE_MEGABYTE = 1024 * 1024;
                                    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            // Data for "images/island.jpg" is returns, use this as needed
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            adaptor.add(bitmap, doc.getId());
                                            if (count == snapshotCount) {
                                                getActivity().runOnUiThread(new Runnable() {

                                                    @Override
                                                    public void run() {
                                                        Log.d("Download iamge", "notify");

                                                        adaptor.notifyDataSetChanged();
                                                        Log.d("Download iamge", "end");

                                                        isLoading = false;
                                                        refreshLayout.setRefreshing(false);

                                                    }
                                                });
                                            }
                                            count += 1;
                                        }
                                    });
                                }

                            }

                        });

            }


        }
    }

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

        popularTab = curView.findViewById(R.id.popular);
        recentTab = curView.findViewById(R.id.recent);


        refreshLayout = curView.findViewById(R.id.refresh_layer);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isLoading) {
                    isLoading = true;
                    totalAmount = 0;
                    adaptor.reset();

                    downloadThread dt = new downloadThread(mode, addLimit, beginDoc, true);
                    dt.start();

                }
            }
        });


        popularTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recentTab.setBackgroundColor(Color.TRANSPARENT);
                popularTab.setBackgroundColor(Color.WHITE);
                mode = 1;
                isLoading = true;
                refreshLayout.setRefreshing(true);
                adaptor.reset();
                downloadThread nt = new downloadThread(mode, addLimit, beginDoc, true);
                nt.start();
            }
        });
        recentTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recentTab.setBackgroundColor(Color.WHITE);
                popularTab.setBackgroundColor(Color.TRANSPARENT);
                mode = 0;
                isLoading = true;
                refreshLayout.setRefreshing(true);
                adaptor.reset();
                downloadThread nt = new downloadThread(mode, addLimit, beginDoc, true);
                nt.start();
            }
        });
        adaptor = new OnlineImageAdaptor(this.getContext());
        GridView gridView = curView.findViewById(R.id.imageGrid);

        isLoading = true;
        downloadThread dt = new downloadThread(mode, addLimit, beginDoc, true);
        dt.start();
        // update beginDoc

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
                if (totalLimit - lastItem <= THRESHOLD) {
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