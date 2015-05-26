package com.secret.myweather2.com.secret.util;

import android.support.v4.view.ViewPager;
import android.util.Log;

import com.secret.myweather2.MainActivity;

/**
 * Created by sekike on 15-5-16.
 */
public class PageChangeListener extends ViewPager.SimpleOnPageChangeListener {
    private int pagePosition=0;

    @Override
    public void onPageSelected(int position){
        pagePosition=position;
    Log.d("PageChange to :",String.valueOf(position));
    }

    public int getPosition(){
        return pagePosition;
    }
}
