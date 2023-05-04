package com.example.viewpager.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.viewpager.MainActivity;
import com.example.viewpager.OnlineFullView;
import com.example.viewpager.OnlineImageAdaptor;
import com.example.viewpager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private DocumentSnapshot beginDoc;
    private int totalAmount = 0;
    private int totalLimit = 0;
    private int addLimit = 30;
    private int count;
    private OnlineImageAdaptor adaptor;
    private boolean isLoading;
    private SwipeRefreshLayout refreshLayout;
    private String username = "";
    private TextView mText;
    private LinearLayout postTab, likeTab;
    private int THRESHOLD = 3;
    private int mode = 0;

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
                DocumentReference ref = db.collection("users").document(mUser.getUid());
                Query first = null;
                if (mode == 0) {
                    first = ref.collection("posted").limit(this.limit);
                }
                if(mode == 1){
                    first = ref.collection("liked").limit(this.limit);
                }

                if(first == null) return;
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
                DocumentReference ref = db.collection("users").document(mUser.getUid());
                Query next = null;
                if (mode == 0) {
                    next = ref.collection("posted").startAfter(this.startAfter).limit(this.limit);
                }
                if(mode == 1){
                    next = ref.collection("liked").startAfter(this.startAfter).limit(this.limit);
                }

                if(next == null) return;


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


    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        if (mUser != null) {
            DocumentReference docRef = db.collection("users").document(mUser.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            username = document.get("username").toString();
                            mText.setText(username);
                        }
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View curView = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView textView = curView.findViewById(R.id.profileUsername);
        textView.setText(username);
        mText = textView;
        curView.findViewById(R.id.logoutBtn).setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        mAuth.signOut();
                                                                        startActivity(new Intent(getContext(), MainActivity.class));
                                                                        getActivity().finish();
                                                                    }
                                                                }
        );

        if(mUser == null) return curView;

        postTab = curView.findViewById(R.id.post);
        likeTab = curView.findViewById(R.id.like);


        refreshLayout = curView.findViewById(R.id.refresh_layer_profile);
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


        postTab.setOnClickListener(new View.OnClickListener() {
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
        likeTab.setOnClickListener(new View.OnClickListener() {
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
                        downloadThread nt = new downloadThread(mode, addLimit, beginDoc,false);
                        nt.start();

                    }
                }
            }
        });


        return curView;
    }

}