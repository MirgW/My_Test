package com.moris.tavda.fragment;

import android.content.Context;
import androidx.fragment.app.Fragment;
import android.view.View;

public class AbstractTabFragment extends Fragment {
    private String title;
    protected Context context;
    protected View view;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {

        return title;
    }
}
