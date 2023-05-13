package com.example.viewpager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FullView extends AppCompatActivity {

    private String filepath;
    private String imgID;
    private boolean isMine = false;
    FirebaseFirestore db;
    FirebaseUser user;
    FirebaseStorage storage;
    Context context;

    ImageView imageView;
    ImageView editBtn;
    ImageView removeBtn;
    ImageView postBtn;
    ImageView downloadBtn;

    Bitmap myBitmap;


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
                    myBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(myBitmap);
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
        setContentView(R.layout.activity_full_view);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        context = this;
        Intent intent = getIntent();

        imageView = findViewById(R.id.img_full);
        editBtn = findViewById(R.id.edit);
        removeBtn = findViewById(R.id.trashcan);
        postBtn = findViewById(R.id.upload);


        isMine = intent.getBooleanExtra("yourImage", false);
        if (!isMine) {
            filepath = intent.getStringExtra("filepath");
            File file = new File(filepath);
            myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            imageView.setImageBitmap(myBitmap);

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });
            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    file.delete();
                    intent.putExtra("filepath", "");
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });

            postBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (user == null) return;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();


                    Map<String, Object> docData = new HashMap<>();
                    docData.put("user", user.getUid());
                    docData.put("like", (long) 0);
                    docData.put("timestamp", FieldValue.serverTimestamp());
                    db.collection("posts").add(docData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String docid = documentReference.getId();
                            StorageReference imgRef = storage.getReference().child("images/" + docid + ".png");

                            UploadTask uploadTask = imgRef.putBytes(byteArray);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to upload", Toast.LENGTH_LONG).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(context, "Upload succeeded", Toast.LENGTH_LONG).show();
                                    db.collection("users").document(user.getUid()).collection("posted").document(docid).set(docData);
                                }
                            });
                        }
                    });
                }
            });
        } else {
            imgID = intent.getStringExtra("imageID");
            if (imgID == null) return;
            getImgThread nt = new getImgThread(imgID);
            nt.start();

            editBtn.setVisibility(View.GONE);
            postBtn.setVisibility(View.GONE);


            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.collection("posts").document(imgID).delete();
                    db.collection("users").document(user.getUid()).collection("posted").document(imgID).delete();
                    finish();
                }
            });
        }


        downloadBtn = findViewById(R.id.download);
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
}
