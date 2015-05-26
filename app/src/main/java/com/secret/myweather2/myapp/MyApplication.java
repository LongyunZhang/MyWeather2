package com.secret.myweather2.myapp;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.secret.myweather2.com.secret.bean.City;
import com.secret.myweather2.com.secret.db.CityDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sekike on 15-3-30.
 */
public class MyApplication extends Application {
    private static final String TAG = "MyAPP";
    private static Application mApplication;
    private static List<City> mCityList;
    private static ArrayList<Map<String, String>> mFavorCity = new ArrayList<Map<String, String>>();
    public static String[] mCityString;
    public static CityDB mCityDB;
    private static ArrayList<Map<String, String>> city_listems = new ArrayList<Map<String, String>>();


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Application oncreate");
        mApplication = this;

        mCityDB = openCityDB();
        initCityList();
        Log.d(TAG,"initCityList");
    }

    public static Application getInstance() {
        return mApplication;
    }

    public CityDB openCityDB() {
        String path = "/data" + Environment.getDataDirectory().getAbsolutePath() + File.separator +
                getPackageName() + File.separator + "databases" + File.separator + CityDB.CITY_DB_NAME;
        File db = new File(path);
        Log.d(TAG, "openCityDB");
        Log.d(TAG, path);
        if (!db.exists()) {
            Log.i(TAG, "db is not exists");
            try {
                InputStream is = getAssets().open("city.db");
                db.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this, path);
    }

    private void initCityList() {
        mCityList = new ArrayList<City>();
        mCityList = mCityDB.getAllCity();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    prepareCityList();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private boolean prepareCityList() {
        mCityString = new String[100];
        Log.d("MyApp mCityList",mCityList.get(2585).getCity());
        Log.d("MyApp mCityList size",String.valueOf(mCityList.size()));
        try {
            for (City city : mCityList) {
                String cityName = city.getCity();
                //String cityCode = city.getNumber();
//                mCityString[mCityList.indexOf(city)] = cityName;//add to String
                //Log.d(TAG,cityName);
                Map<String, String> city_listem = new HashMap<String, String>();
                city_listem.put("city", cityName);
                //city_listem.put("code",cityCode);
                city_listems.add(city_listem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String[] getmCityString() {
        return mCityString;
    }

    public static String getCodeByName(String cityName) {
        for (City city : mCityList) {
            if (city.getCity().equals(cityName)) {
                return city.getNumber();
            }
        }
        return null;
    }

    public static ArrayList<Map<String, String>> getCodeByInformation(String arg) {
        ArrayList<Map<String, String>> city_ByArg = new ArrayList<Map<String, String>>();
        String PY = arg.toUpperCase();
        for (City city : mCityList) {
            if (city.getAllFirstPY().equals(PY) || city.getFirstPY().equals(PY) || city.getCity().equals(arg)) {
                Map<String, String> item = new HashMap<String, String>();
                item.put("city", city.getCity());
                city_ByArg.add(item);
            }
        }
        return city_ByArg;
    }

    public static List getCity_listems() {
        return city_listems;
    }

    public static List<City> getmCityList() {
        return mCityList;
    }

    public static void addFavorCity(String str) {
        Map<String, String> city_item = new HashMap<String, String>();
        city_item.put("city", str);
        city_item.put("code", getCodeByName(str));
        mFavorCity.add(city_item);
    }

    public static List getFavorCity() {
        return mFavorCity;
    }

}
