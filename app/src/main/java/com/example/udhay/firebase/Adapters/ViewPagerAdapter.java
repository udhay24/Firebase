package com.example.udhay.firebase.Adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.udhay.firebase.DocumentFragment;
import com.example.udhay.firebase.ImagesFragment;
import com.example.udhay.firebase.MessageFragment;
import com.example.udhay.firebase.TextRecognitionFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    public static final String[] tabLayoutTitle = new String[]{"Messages" , "Images" , "Documents" , "Text recognition"};

    public ViewPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int i) {

        switch(i){
            case 0:
                return new MessageFragment();
            case 1:
                return new ImagesFragment();
            case 2:
                return new DocumentFragment();
            case 3:
                return new TextRecognitionFragment();

                default:
                    return new MessageFragment();

        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabLayoutTitle[position];
    }
}
