package com.example.viewpager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public class OnlineFullView extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private long like = 0;
    private Bitmap bitmap;
    private DocumentReference docref;
    private long toLike = 1;
    private ImageView imageView;
    private TextView username;
    private ImageView favoriteBtn;
    private ImageView downloadBtn;
    private Map<String, Object> data;

    private class getImgThread extends Thread {
        private String imgID;

        getImgThread(String id) {
            this.imgID = id;
        }

        public void run() {
            StorageReference islandRef = storage.getReference().child("images/" + imgID + ".png");

            final long ONE_MEGABYTE = 1024 * 1024;
            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Data for "images/island.jpg" is returns, use this as needed
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                            downloadBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    imageView.setDrawingCacheEnabled(true);
                                    String imgSaved = MediaStore.Images.Media.insertImage(
                                            getContentResolver(), imageView.getDrawingCache(),
                                            UUID.randomUUID().toString() + ".png", "drawing");
                                    if (imgSaved != null) {
                                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                                "Drawing downloaded to Gallery!", Toast.LENGTH_SHORT);
                                        savedToast.show();
                                    } else {
                                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                                "Oops! Image could not be downloaded.", Toast.LENGTH_SHORT);
                                        unsavedToast.show();
                                    }
                                    imageView.destroyDrawingCache();
                                }
                            });
                        }
                    });

                }
            });

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_fullview);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        imageView = findViewById(R.id.img_full);
        username = findViewById(R.id.online_username);
        favoriteBtn = findViewById(R.id.favorite);
        downloadBtn = findViewById(R.id.online_download);


        String imgID = getIntent().getStringExtra("imageID");
        if (imgID == null) return;

        db.collection("posts").document(imgID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        docref = document.getReference();
                        data = document.getData();
                        String artistID = document.get("user").toString();
                        username.setText("");
                        db.collection("users").document(artistID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                if (task2.isSuccessful()) {
                                    DocumentSnapshot document2 = task2.getResult();
                                    if (document2.exists()) {
                                        username.setText(document2.get("username").toString());
                                    }
                                }
                            }
                        });
                        like = (long) document.get("like");
                    }
                }

            }
        });
        if (user != null) {
            db.collection("users").document(user.getUid()).collection("liked").document(imgID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            favoriteBtn.setColorFilter(Color.argb(255, 255, 0, 0));
                            toLike = -1;
                        }
                    }

                }
            });
            favoriteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    docref.update("like", like + toLike);
                    toLike = -1 * toLike;
                    if (toLike > 0) {
                        favoriteBtn.setColorFilter(Color.argb(255, 255, 255, 255));
                        // remove from liked
                        db.collection("users").document(user.getUid()).collection("liked").document(imgID).delete();
                    } else {
                        favoriteBtn.setColorFilter(Color.argb(255, 255, 0, 0));
                        // add to liked
                        db.collection("users").document(user.getUid()).collection("liked").document(imgID).set(data);

                    }
                }
            });
        }


        getImgThread nt = new getImgThread(imgID);
        nt.start();

    }
}
