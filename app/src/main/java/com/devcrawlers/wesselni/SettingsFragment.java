package com.devcrawlers.wesselni;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.PagerTitleStrip;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.location.SettingsClient;


public class SettingsFragment extends Fragment {
    ViewPager pager;
    PagerTitleStrip pagerTitleStrip;
    private FragmentManager manager;

    public SettingsFragment(FragmentManager manager) {

        this.manager = manager;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        pager = view.findViewById(R.id.settings_pager);
          pagerTitleStrip = view.findViewById(R.id.settings_pager_title);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext(), manager);

        pager.setAdapter(viewPagerAdapter);

        return view;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private Context context;

        Fragment[] frags = new Fragment[]{
                new GeneralSettingsFragment(),
                new ProfileSettingsFragment(),
                new SecuritySettingsFragment()
        };
        String[] titles = new String[]{
                "Générale", "Profile", "Sécurité"
        };

        public ViewPagerAdapter(Context context, @NonNull FragmentManager fm) {
            super(fm);
            this.context = context;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return frags.length;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return frags[position];
        }
    }
}



