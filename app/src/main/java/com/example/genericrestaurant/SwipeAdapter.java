package com.example.genericrestaurant;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class SwipeAdapter extends FragmentStatePagerAdapter {

    int frag_count;

    public SwipeAdapter(FragmentManager fm, int frag_count) {
        super(fm);
        this.frag_count = frag_count;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                fragment_menu fragmentMenu = new fragment_menu();
                return fragmentMenu;

            case 1:
                fragment_speak fragmentSpeak = new fragment_speak();
                return fragmentSpeak;

            case 2:
                fragment_cart fragmentCart = new fragment_cart();
                return fragmentCart;

                default: return null;
        }

    }

    @Override
    public int getCount() {
        return frag_count;
    }
}
