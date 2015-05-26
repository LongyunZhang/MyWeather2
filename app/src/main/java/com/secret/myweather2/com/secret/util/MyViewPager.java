package com.secret.myweather2.com.secret.util;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by sekike on 15-5-13.
 */
public class MyViewPager implements ViewPager.OnPageChangeListener{


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("选择了第" , String.valueOf(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
