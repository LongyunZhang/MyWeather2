package com.secret.myweather2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.secret.myweather2.myapp.MyApplication;

import java.util.List;
import java.util.Map;


public class ChooseCity extends ActionBarActivity {

    private ListView mlistView;
    //private String[] data = {"第1组", "第2组", "第3组", "第4组", "第5组", "第6组", "第7组", "第8组", "第9组", "第10组", "第11组", "第12组", "第13组", "第14组", "第15组", "第16组", "第17组", "第18组", "第19组", "第20组","第21组", "第22组"};
//    private String[] data;
    private List<Map<String, String>> listems;
    private List<Map<String, String>> city_ByPY;
    private EditText mEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city);

        mEditText = (EditText) findViewById(R.id.search_edit);
        mEditText.addTextChangedListener(mTextWatcher);

        mlistView = (ListView) findViewById(R.id.list_view);

        listems = MyApplication.getCity_listems();
//        data=MyApplication.getmCityString();
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this,android.R.layout.simple_list_item_1,data);
        SimpleAdapter simplead = new SimpleAdapter(this, listems,
                R.layout.list_content, new String[]{"city"}, new int[]{R.id.city});
        mlistView.setAdapter(simplead);
        //mlistView.setAdapter(adapter);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(ChooseCity.this, "你单击了:"+data[i],Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("city", listems.get(i).get("city"));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;

        private int editStart;
        private int editEnd;
        private String content;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            temp = charSequence;
            Log.d("myapp", "beforeTextChanged:" + temp);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            Toast.makeText(ChooseCity.this, charSequence, Toast.LENGTH_SHORT).show();
            Log.d("myapp", "onTextChanged:" + charSequence);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            editStart = mEditText.getSelectionStart();
            editEnd = mEditText.getSelectionEnd();
            if (temp.length() > 10) {
                Toast.makeText(ChooseCity.this, "你输⼊的字数已经超过了限制!", Toast.LENGTH_SHORT).show();
                editable.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                mEditText.setText(editable);
                mEditText.setSelection(tempSelection);
            }
            content = editable.toString();
            if (content.length() == 0) {
                city_ByPY = listems;
            } else {
                city_ByPY = MyApplication.getCodeByInformation(content);
            }
//            for (int i = 0; i <= 100; i++)
//                Log.d("mCityList:", MyApplication.getmCityList().get(i).getCity() + MyApplication.getmCityList().get(i).getAllFirstPY());
            Log.d("content:", content);

            SimpleAdapter simplead = new SimpleAdapter(ChooseCity.this, city_ByPY,
                    R.layout.list_content, new String[]{"city"},
                    new int[]{R.id.city});
            mlistView.setAdapter(simplead);


            mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(ChooseCity.this, "你单击了:"+data[i],Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("city", city_ByPY.get(i).get("city"));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });

            Log.d("myapp", "afterTextChanged:size=" + city_ByPY.size() + city_ByPY.toString());
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_city, menu);
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
}
