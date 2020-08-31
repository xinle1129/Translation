package com.swufe.translation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements Runnable,AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener
{
    private List<HashMap<String,String>> recordItems;
    private SimpleAdapter adapter0;
    private ListView listView;
    private  String str,voice,meaning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        listView = findViewById(R.id.record_list);//必须自己输入才行
        List<HashMap<String,String>> recordList = new ArrayList<HashMap<String,String>>();
        List<HashMap<String,String>> recordList0 = new ArrayList<HashMap<String,String>>();
        //从数据库中获取数据
        DBManager dbManager = new DBManager(this);
        List<RecordItem> recordItems0 = dbManager.listAll();
        for(RecordItem i : recordItems0){
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("Record",i.getName());
            recordList.add(map1);//得先存到recordList中，不然会报错
        }
        for(int j =recordList.size()-1;j>=0;j--){//实现按搜索顺序显示
            recordList0.add(recordList.get(j));
        }
        recordItems = recordList0;
        adapter0 = new SimpleAdapter(MyListActivity.this,recordItems,
                R.layout.activity_record_item,
                new String[]{"Record"},
                new int[]{R.id.item}
                );//也得自己输入

        listView.setAdapter(adapter0);
        if(recordItems.size()==0){
            Log.i("this","onItemClick: titleStr=kong" );
//            Button button = findViewById(R.id.button3);
            ImageView imageView = findViewById(R.id.nothing);
            imageView.setVisibility(View.VISIBLE);
//            button.setVisibility(View.VISIBLE);
            Button button0 = findViewById(R.id.record_clean);
            button0.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
            ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout2);
            constraintLayout.setVisibility(View.INVISIBLE);
        }
        //listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder . setTitle("提示")
                . setMessage("删除这条记录")
                . setPositiveButton("确定", new DialogInterface. OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final ListView listView = findViewById(R.id.record_list);
                        HashMap<String,String> map = (HashMap<String, String>) listView.getItemAtPosition(position);//获取数据项
                        String str = map.get("Record");
                        DBManager dbManager = new DBManager(MyListActivity.this);
                        dbManager.delete(str);
                        recordItems.remove(position);
                        if(recordItems.size()==0){
                            Button btn = findViewById(R.id.record_clean);
                            btn.setVisibility(View.INVISIBLE);
                            ImageView imageView = findViewById(R.id.nothing);
                            imageView.setVisibility(View.VISIBLE);
//                            Button button = findViewById(R.id.button3);
//                            button.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.INVISIBLE);
                            ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout2);
                            constraintLayout.setVisibility(View.INVISIBLE);
                        }
                        adapter0.notifyDataSetChanged();
                    }
                }) . setNegativeButton("取消", null);
        builder . create(). show();

        return true;//只触发长按事件
    }
    //        Log.i("this","onItemClick: titleStr=" + position);
//        Log.i("this","onItemClick: titleStr=" + recordItems.get(position));
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final ListView listView = findViewById(R.id.record_list);
        HashMap<String,String> map = (HashMap<String, String>) listView.getItemAtPosition(position);//获取数据项
        str = map.get("Record");
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        RecordItem recordItem = new RecordItem(str);
        DBManager dbManager = new DBManager(this);
        dbManager.add(recordItem);
        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.iciba.com/word?w=".concat(str)).get();
            int i0 = 0,i00=0;
            Elements ps = doc.getElementsByTag("p");
            for(Element p : ps){
                i00++;
            }
            if(i00>=2&&ps.get(1).text().equals("以上结果来自机器翻译。")){
                Intent intent = new Intent(this,SentenceActivity.class);
                intent.putExtra("sentence",str);
                intent.putExtra("meaning",ps.get(0).text());
                startActivity(intent);
            }else{

                Elements uls = doc.getElementsByTag("ul");
                int a=0,b=0;
                Boolean flag1 = false,flag2 = false;
                for(Element ul : uls){
                    if(ul.attr("class").equals("Mean_symbols__5dQX7")){
                        //Log.i(TAG,"run00000000: table0["+i0+"]=" + ul.text());
                        voice = ul.text();
                        a = voice.indexOf("]")+1;
                        b = voice.indexOf("美");
                        flag1 = true;
                    }else if(ul.attr("class").equals("Mean_part__1RA2V")){
                        //Log.i(TAG,"run00000000: table0["+i0+"]=" + ul.text());
                        meaning = ul.text();
                        flag2 = true;
                        //Log.i(TAG,"run00000000:这里2");
                        break;
                    }
                    i0++;
                }
                if(flag1&&flag2){

                    Intent intent = new Intent(this,Main2Activity.class);
                    if(a<2&&b==-1){
                        intent = new Intent(this,SentenceActivity.class);
                        intent.putExtra("sentence",str);
                        intent.putExtra("meaning",meaning);
                        startActivity(intent);
                    }else if(b==-1){
                        intent = new Intent(this,WordsActivity.class);
                        intent.putExtra("word",str);
                        intent.putExtra("en_voice",voice.substring(0, a));
                        intent.putExtra("meaning",meaning);
                        startActivity(intent);
                    }else{
                        intent.putExtra("am_voice",voice.substring(b));
                        intent.putExtra("word",str);
                        intent.putExtra("en_voice",voice.substring(0, a));
                        intent.putExtra("meaning",meaning);
                        startActivity(intent);
                    }

                }else if(flag2){
                    Intent intent = new Intent(this,SentenceActivity.class);
                    intent.putExtra("sentence",str);
                    intent.putExtra("meaning",meaning);
                    startActivity(intent);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onClick(final View btn){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder . setTitle("提示")
                . setMessage("清空所有查询历史记录")
                . setPositiveButton("确定", new DialogInterface. OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final ListView listView = findViewById(R.id.record_list);
                        DBManager dbManager = new DBManager(MyListActivity.this);
                        dbManager.deleteAll();
                        recordItems.clear();
                        adapter0.notifyDataSetChanged();
                        btn.setVisibility(View.INVISIBLE);
                        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout2);
                        constraintLayout.setVisibility(View.INVISIBLE);
                        ImageView imageView = findViewById(R.id.nothing);
                        imageView.setVisibility(View.VISIBLE);
//                        Button button = findViewById(R.id.button3);
//                        button.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.INVISIBLE);
                    }
                }) . setNegativeButton("取消", null);
        builder . create(). show();


    }

    public void onClick_back(View btn){
        finish();
    }

}
