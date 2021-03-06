package com.moris.tavda.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.moris.tavda.fragment.AbstractTabFragment;
import com.moris.tavda.fragment.HistoryFragment;

import java.util.HashMap;
import java.util.Map;

public class TabsPagerFragmentAdapter extends FragmentPagerAdapter {
    private Map<Integer,AbstractTabFragment> tabs;
    private Context context;

    public TabsPagerFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;

        InitTabsMap(context);

//        tabs= new String[] {
//                .,
//                .getString(R.string.tab_item_ideas),
//                .getString(R.string.tab_item_todo),
//                .getString(R.string.tab_item_birthdays)
//        };
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    private void InitTabsMap(Context context) {
        tabs = new HashMap<>();
        tabs.put(0, HistoryFragment.getInstance(context));
//        tabs.put(1, IdeasFragment.getInstance(context));
//        tabs.put(2, TODOFragment.getInstance(context));
//        tabs.put(3, BirthdaysFragment.getInstance(context));
    }
}
