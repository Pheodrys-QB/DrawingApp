//package com.example.viewpager;
//
//
//import android.os.Bundle;
//import android.view.WindowManager;
//
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//
//
//import com.example.viewpager.fragment.AlbumFragment;
//import com.example.viewpager.fragment.CommunityFragment;
//import com.example.viewpager.fragment.ProfileFragment;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.ismaeldivita.chipnavigation.ChipNavigationBar;
////import com.taimoorsikander.cityguide.R;
//
//
//public class chipNavigation extends AppCompatActivity {
//
//
//    ChipNavigationBar chipNavigationBar;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_main);
//
//
//        chipNavigationBar = findViewById(R.id.bottom_navigation);
//        chipNavigationBar.setItemSelected(R.id.bottom_navigation,true);
//        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout,new CommunityFragment()).commit();
//
//
//        bottomMenu();
//    }
//
//
//    private void bottomMenu() {
//
//
//        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(int i) {
//                Fragment fragment = null;
//                switch (i) {
//                    case  R.id.menu_community:
//                        fragment = new CommunityFragment();
//                        break;
//                    case R.id.menu_album:
//                        fragment = new AlbumFragment();
//                        break;
//                    case R.id.menu_profile:
//                        fragment = new ProfileFragment();
//                        break;
//                }
//                getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout,fragment).commit();
//            }
//        });
//
//
//    }
//
//
//}