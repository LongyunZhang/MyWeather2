package com.secret.myweather2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;

import com.secret.myweather2.com.secret.bean.TodayWeather;
import com.secret.myweather2.com.secret.util.MyViewPager;
import com.secret.myweather2.com.secret.util.NetUtil;
import com.secret.myweather2.com.secret.util.PageChangeListener;
import com.secret.myweather2.com.secret.util.PinYin;
import com.secret.myweather2.com.secret.util.ViewPagerAdapter;
import com.secret.myweather2.myapp.MyApplication;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private static ImageView mUpdateBtn, chooseCity, weatherImg, pmImg;
    private static TextView cityTv, timeTv, shiduTv, weekTv, pmTv, qualityTv, tempertureTv, tianqiTv, fengliTv;
    private static Button btn_delete;
    private static Activity mActivity;
    public static final int UPDATE = 0;
    public static final int RESPONSE = 1;
    private ProgressDialog dialog;
    private int currentPageNo=0;
    private PageChangeListener pageChangeListener;

    private List<View> views;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;

    private LayoutInflater layoutInflater;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylayout);
        Log.d("MyAPP", "Main onCreate");
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("xk", "Internet connected!");
            Toast.makeText(MainActivity.this, "网络正常", Toast.LENGTH_LONG).show();
        } else {
            Log.d("xk", "Not connected");
            Toast.makeText(MainActivity.this, "网络连接失败", Toast.LENGTH_LONG).show();
        }

        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);
        chooseCity = (ImageView) findViewById(R.id.title_city_manager);
        chooseCity.setOnClickListener(this);
        MyApplication.addFavorCity("北京");
        initViewPager();

        initView(currentPageNo);
        Log.d("MyAPP", "initView");


    }
    void initView(int no) {
        cityTv = (TextView) views.get(no).findViewById(R.id.content_city_name);
        timeTv = (TextView) views.get(no).findViewById(R.id.content_publish_time);
        shiduTv = (TextView) views.get(no).findViewById(R.id.content_humidity);
        weekTv = (TextView) views.get(no).findViewById(R.id.content_date);
        pmTv = (TextView) views.get(no).findViewById(R.id.content_pm_value);
        qualityTv = (TextView) views.get(no).findViewById(R.id.content_pm_describe);
        tempertureTv = (TextView) views.get(no).findViewById(R.id.content_temperature);
        tianqiTv = (TextView) views.get(no).findViewById(R.id.content_weather_describe1);
        fengliTv = (TextView) views.get(no).findViewById(R.id.content_weather_describe2);
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        shiduTv.setText("N/A");
        weekTv.setText("N/A");
        pmTv.setText("N/A");
        qualityTv.setText("N/A");
        tempertureTv.setText("N/A");
        tianqiTv.setText("N/A");
        fengliTv.setText("N/A");
        pmImg = (ImageView) views.get(no).findViewById(R.id.content_pm_pic);
        weatherImg = (ImageView) views.get(no).findViewById(R.id.content_weather_pic);
        List<Map<String,String>> mFavorCity = new ArrayList<Map<String, String>>();
        mFavorCity=MyApplication.getFavorCity();
        String cityname=mFavorCity.get(no).get("city");
        Log.d("MyApp citycode","before");

        String citycode=MyApplication.getCodeByName(cityname);
        Log.d("MyApp citycode after", citycode);
        queryWeatherCode(citycode);
    }

    private void initViewPager() {
        layoutInflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(layoutInflater.inflate(R.layout.innerlayout1, null));
        viewPagerAdapter = new ViewPagerAdapter(views, this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                try {
                    initView(position);
                    currentPageNo=position;
                    btn_delete=(Button)views.get(currentPageNo).findViewById(R.id.btn_delete);
                    btn_delete.setOnClickListener(MainActivity.this);
                    Log.d("PageChange to :", String.valueOf(position));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int a) {
            }

            public void onPageScrolled(int a, float b, int c) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onClick(View view) {
        if (view.getId() == R.id.title_update_btn) {
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code", "101010100");
            Log.d("myWeather", cityCode);
            queryWeatherCode(cityCode);

        }
        if (view.getId() == R.id.title_city_manager) {
            Intent intent = new Intent(MainActivity.this, ChooseCity.class);
            //startActivityForResult(intent, 1);
            startActivityForResult(intent, 2);
        }
        if (view.getId() == R.id.btn_delete){

            views.remove(currentPageNo);
            viewPagerAdapter.notifyDataSetChanged();
        }
        Log.d("current button:",String.valueOf(view.getId()));
        Log.d("delete button:",String.valueOf(R.id.btn_delete));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String str = intent.getStringExtra("city");
            String number = MyApplication.getCodeByName(str);
            Toast.makeText(this, "您选中了：" + str, Toast.LENGTH_SHORT).show();
            queryWeatherCode(number);
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            String str = intent.getStringExtra("city");
            MyApplication.addFavorCity(str);
            views.add(layoutInflater.inflate(R.layout.innerlayout1, null));
            viewPagerAdapter.notifyDataSetChanged();

            Log.d("MyFavoriteCity", MyApplication.getFavorCity().toString());
        }
    }


    public Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE:
                    updateUI((TodayWeather) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    public void updateUI(TodayWeather todayWeather) {
        Log.d("json", todayWeather.toString());
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime() + "发布");
        shiduTv.setText("湿度：" + todayWeather.getHumidity());
        pmTv.setText(todayWeather.getPm25());
        qualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        tempertureTv.setText(todayWeather.getTemperature() + "℃" + "(" + todayWeather.getLow() + "~" + todayWeather.getHigh() + ")");
        tianqiTv.setText(todayWeather.getClimate());
        fengliTv.setText(todayWeather.getWind());

        String pmImgStr = "0_50";
        if (null != todayWeather.getPm25()) {
            int pmValue = Integer.parseInt(todayWeather.getPm25().trim());
            if (pmValue > 50 && pmValue < 201) {
                int startV = (pmValue - 1) / 50 * 50 + 1;
                int endV = ((pmValue - 1) / 50 + 1) * 50;
                pmImgStr = Integer.toString(startV) + "_" + endV;
            } else if (pmValue >= 201 && pmValue < 301) {
                pmImgStr = "201_300";
            } else if (pmValue >= 301) {
                pmImgStr = "greater_300";
            }
        }
        String typeImg =
                "biz_plugin_weather_" + PinYin.converterToSpell(todayWeather.getClimate());
        Class aClass = R.drawable.class;
        int typeId = -1;
        int pmImgId = -1;
        try {
            //一般尽量采用这种形式
            Field pmField = aClass.getField("biz_plugin_weather_" + pmImgStr);
            Object pmImgO = pmField.get(new Integer(0));
            pmImgId = (Integer) pmImgO;

            Field field = aClass.getField(typeImg);
            Object value = field.get(
                    new Integer(0));
            typeId = (Integer) value;

        } catch (Exception e) { //e.printStackTrace(); if ( -1 == typeId)
            e.printStackTrace();
            if (-1 == typeId)
                typeId = R.drawable.biz_plugin_weather_qing;
            if (-1 == pmImgId)
                pmImgId = R.drawable.biz_plugin_weather_0_50;
        } finally {
            Drawable drawable = getResources().getDrawable(pmImgId);
            pmImg.setImageDrawable(drawable);
            drawable = getResources().getDrawable(typeId);
            weatherImg.setImageDrawable(drawable);
            Toast.makeText(MainActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
            dialog.cancel();
        }
    }
    public void queryWeatherCode(String cityCode) {
        dialog=ProgressDialog.show(this,null,"更新天气中……",false,true);
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        //final MainActivity mainActivity= (MainActivity)context;
        Log.d("myWeather", address);
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("xk", "Internet connected!");
            //Toast.makeText(MainActivity.this, "网OK!", Toast.LENGTH_LONG).show();
        } else {
            Log.d("xk", "Not connected");
            Toast.makeText(this, "网络连接失败!", Toast.LENGTH_LONG).show();
        }

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HttpClient httpClient = new DefaultHttpClient();
                            HttpGet httpGet = new HttpGet(address);
                            HttpResponse httpResponse = httpClient.execute(httpGet);
                            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                HttpEntity entity = httpResponse.getEntity();

                                InputStream responseStream = entity.getContent();
                                responseStream = new GZIPInputStream(responseStream);

                                BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
                                StringBuilder response = new StringBuilder();
                                String str;
                                while ((str = reader.readLine()) != null) {
                                    response.append(str);
                                }
                                Log.d("printJSON", response.toString());
                                TodayWeather todayWeather;
                                todayWeather=parseXML(response.toString());//parseXML
                                if (todayWeather != null) {
                                    Message msg = new Message();
                                    msg.what = UPDATE;
                                    msg.obj = todayWeather;
                                    uiHandler.sendMessage(msg);//sendMessage
                                    Log.d("ALL", todayWeather.toString());
                                }

                                //parseJSON(response.toString());
                            }
                        } catch (Exception e)

                        {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();
    }

    public TodayWeather parseXML(String xmldata) {

        TodayWeather todayWeather = new TodayWeather();
        try {
            int fengxiangCount = 0;
            int windCount = 0;
            int temCount = 0;
            int dateCount = 0;
            int highCount = 0;
            int lowCount = 0;
            int typeCount = 0;
            int humidityCount = 0;
            int updatetimeCount = 0;
            int pm25Count = 0;
            int qualityCount = 0;
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myapp", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("city")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setCity(xmlPullParser.getText());
                            Log.d("city:", xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("updatetime") && updatetimeCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setUpdatetime(xmlPullParser.getText());
                            Log.d("updatetime", xmlPullParser.getText());
                            updatetimeCount++;
                        } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setHigh(xmlPullParser.getText().substring(3));
                            Log.d("high", xmlPullParser.getText().substring(3));
                            highCount++;
                        } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setLow(xmlPullParser.getText().substring(3));
                            Log.d("low", xmlPullParser.getText().substring(3));
                            lowCount++;
                        } else if (xmlPullParser.getName().equals("wendu") && temCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setTemperature(xmlPullParser.getText());
                            Log.d("tem", xmlPullParser.getText());
                            temCount++;
                        } else if (xmlPullParser.getName().equals("fengli") && windCount == 0) {
                            eventType = xmlPullParser.next();
                            Log.d("wind", xmlPullParser.getText());
                            todayWeather.setWind(xmlPullParser.getText());
                            windCount++;
                        } else if (xmlPullParser.getName().equals("pm25") && pm25Count == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setPm25(xmlPullParser.getText());
                            Log.d("pm2.5", xmlPullParser.getText());
                            pm25Count++;
                        } else if (xmlPullParser.getName().equals("quality") && qualityCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setQuality(xmlPullParser.getText());
                            Log.d("quality", xmlPullParser.getText());
                            qualityCount++;
                        } else if (xmlPullParser.getName().equals("shidu") && humidityCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setHumidity(xmlPullParser.getText());
                            Log.d("humidity", xmlPullParser.getText());
                            humidityCount++;
                        } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setClimate(xmlPullParser.getText());
                            Log.d("type", xmlPullParser.getText());
                            typeCount++;
                        } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
/*
                            Calendar cal = Calendar.getInstance();
                            int date = cal.get(Calendar.DATE);
*/
                            eventType = xmlPullParser.next();
                            todayWeather.setDate(xmlPullParser.getText());
/*
                            Log.d("date:", String.valueOf(date));
*/
                            dateCount++;
/*
                            Pattern pattern = Pattern.compile("(^\\d+)");
                            Matcher matcher = pattern.matcher(xmlPullParser.nextText());
                            if (matcher.find()) {
                                Log.d("find:", String.valueOf(matcher.find()));
                                if (Integer.parseInt(matcher.group(0)) == date) {
                                    Pattern p2 = Pattern.compile("(ÐÇÆÚ.)");
                                    Matcher m2 = p2.matcher(xmlPullParser.nextText());
                                    if (m2.find()) {
                                        String week = m2.group(0);
                                        Log.d("week:", week);
                                    }
                                }
                            }
*/
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;

                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return todayWeather;
    }

}



