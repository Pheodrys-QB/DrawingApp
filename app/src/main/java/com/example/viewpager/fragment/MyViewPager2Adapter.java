package com.example.viewpager.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyViewPager2Adapter extends FragmentStateAdapter {
    public MyViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new CommunityFragment();
            case 1:
                return new AlbumFragment();
            case 2:
                return new ProfileFragment();
            default:
                return new CommunityFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
