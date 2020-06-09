package com.swufe.translation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements Runnable {
    private final String TAG = "Translation";
    EditText editText ;
    TextView textView;
    String str;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        editText = findViewById(R.id.second_input);
        textView = findViewById(R.id.second_output);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = findViewById(R.id.viewpager);
        MyPageAdapter pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        TabLayout tablayout = findViewById(R.id.sliding_tabs);
        tablayout.setupWithViewPager(viewPager);

    }//

    @Override
    public void run() {
        Document doc0 = null;
        try {
            doc0 = Jsoup.connect("https://www.iciba.com/word?w=".concat(str)).get();
            Elements tables0 = doc0.getElementsByTag("p");
            int i0 = 0;
            for(Element table0 : tables0){
                Log.i(TAG,"run00000000: table0["+i0+"]=" + table0.text());
                i0++;
            }
            textView = findViewById(R.id.second_output);
            textView.setText(tables0.get(0).text());
            //Log.i(TAG,"run00000000: table0["+i0+"]=" + tables0.get(22));
//            Elements tables1 = tables0.get(22).getElementsByTag("ul");//第二个ul里有翻译
//            for(Element table1 : tables1){
//                Log.i(TAG,"run11111111: table1["+i0+"]=" + table1);
//                i0++;
//            }
//            Elements tables2 = tables1.get(0).getElementsByTag("li");
//            for(Element table2 : tables2){
//                Log.i(TAG,"run11111111: table1["+i0+"]=" + table2);
//                i0++;
//            }
//            Log.i(TAG,"run11111111: " + tables2.get(0).text());//英式发音
//            Log.i(TAG,"run11111111: " + tables2.get(1).text());//美式发音
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onClick(View btn){
        editText = findViewById(R.id.second_input);
        str = editText.getText().toString();
        Thread thread = new Thread(this);
        thread.start();
    }
}
