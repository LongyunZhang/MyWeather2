package com.secret.myweather2.com.secret.util;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.secret.myweather2.MainActivity;

import java.util.List;

/**
 * Created by sekike on 15-5-5.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private List<View> views;
    private Context context;

    public ViewPagerAdapter(List<View> views, Context context) {
        this.views = views;
        this.context = context;
    }

    @Override
    public int getCount() {
        Log.d("getCount:", String.valueOf(views.size()));
        return views.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        Log.d("instantiateItem:", String.valueOf(position));
        return views.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        Log.d("isViewFromObject:", String.valueOf(view == object));
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
        Log.d("destroyItem:", String.valueOf(position));
    }
}

