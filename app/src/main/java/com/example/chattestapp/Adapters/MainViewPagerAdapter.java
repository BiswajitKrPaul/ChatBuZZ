package com.example.chattestapp.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chattestapp.Fragments.ChatList;
import com.example.chattestapp.Fragments.ProfileFragment;
import com.example.chattestapp.Fragments.StatusFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    public MainViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return ChatList.newInstance();
            case 1:
                return StatusFragment.newInstance();
            case 2:
                return ProfileFragment.newInstance();
        }
        return null;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return ChatList.PAGE_TITLE;
            case 1:
                return StatusFragment.PAGE_TITLE;
            case 2:
                return ProfileFragment.PAGE_TITLE;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
