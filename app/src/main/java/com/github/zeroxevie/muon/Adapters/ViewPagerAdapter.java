package com.github.zeroxevie.muon.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.zeroxevie.muon.Fragments.Application_Record_List_Fragment;
import com.github.zeroxevie.muon.Fragments.Favourite_Record_List_Fragment;
import com.github.zeroxevie.muon.Fragments.Website_Record_List_Fragment;

public class ViewPagerAdapter extends FragmentPagerAdapter
{

    String titles[] = {
            "Favourites",
            "Websites",
            "Applications"
    };

    String fragmentTypes[] =
            {
                    "Favourite",
                    "Website",
                    "Applications"
            };

    public ViewPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new Favourite_Record_List_Fragment();
            case 1: return new Website_Record_List_Fragment();
            case 2: return new Application_Record_List_Fragment();
        }
        return null;
    }

        @Override
        public int getCount()
        {
            // Show 3 total pages.
            return 3;
        }

    public CharSequence getPageTitle(int position) {
        return  titles[position];
    }

}
