package com.example.viewpager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.viewpager.databinding.ActivityMainBinding;
import com.example.viewpager.fragment.MyViewPager2Adapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 mViewPager2;
    private BottomNavigationView mBottomNavigationView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private MainActivity main;
    Boolean isAlreadyExecuted = false;
//    Splash screen animation
    Animation topAnim, bottomAnim;
    ImageView paintImage;
    TextView logo, slogan;
//    View splashLayer;
    private static int SPLASH_SCREEN = 2000;
//    End of splash screen animation
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Splash screen animation
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
//        Hooks
       if(!isAlreadyExecuted) {
           paintImage = findViewById(R.id.paintImage);
           logo = findViewById(R.id.logo);
           slogan = findViewById(R.id.slogan);

           paintImage.setAnimation(topAnim);
           logo.setAnimation(bottomAnim);
           slogan.setAnimation(bottomAnim);
           isAlreadyExecuted = true;
       }

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                startActivity(intent);
//                View myView = findViewById(R.id.splashLayer);
//                    ViewGroup parent = (ViewGroup) myView.getParent();
//                    parent.removeView(myView);
//                finish();
//            }
//        },SPLASH_SCREEN);
//        End of splash screen animation
        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    public void run() {
                        View myView = findViewById(R.id.splashLayer);
                        ViewGroup parent = (ViewGroup) myView.getParent();
                        parent.removeView(myView);
                        isAlreadyExecuted = true;
                    }
                },
                2000);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        main = this;


        File defaultFolder = new File(getFilesDir(), "Default");
        if(!defaultFolder.exists()){
            defaultFolder.mkdir();
        }else if(!defaultFolder.isDirectory()){
            defaultFolder.mkdir();
        }
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                        PackageManager.PERMISSION_GRANTED);

        mViewPager2 = findViewById(R.id.view_pager_2);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        MyViewPager2Adapter adapter = new MyViewPager2Adapter(this);
        mViewPager2.setAdapter(adapter);

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_community).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_album).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_profile).setChecked(true);
                        break;
                }
            }
        });
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case  R.id.menu_community:
                        mViewPager2.setCurrentItem(0);
                        break;
                    case  R.id.menu_album:
                        mViewPager2.setCurrentItem(1);
                        break;
                    case  R.id.menu_profile:
                        if(mUser != null){
                            mViewPager2.setCurrentItem(2);
                            break;
                        }
                        startActivity(new Intent(main, SignIn.class));
                        finish();

                }
                return true;
            }
        });
    }

}