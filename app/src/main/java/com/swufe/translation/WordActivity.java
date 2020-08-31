package com.swufe.translation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class WordActivity extends AppCompatActivity implements  Runnable{
    private final String TAG = "TranslationWord";
    EditText editText;
    String str,voice,meaning;
    Handler handler;
    TextView wordText;
    TextView en_voiceText;
    TextView am_voiceText;
    TextView meaningText;
    TextView textView;
    Button word_copy;
    ScrollView scroll;
    InputMethodManager imm;
    int a=0,b=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////111        Intent intent = getIntent();
////        String word = intent.getStringExtra("word");
////        String en_voice = intent.getStringExtra("en_voice");
////        String am_voice = intent.getStringExtra("am_voice");
////        String meaning0 = intent.getStringExtra("meaning");
        wordText = findViewById(R.id.word);
        en_voiceText = findViewById(R.id.word_en_voice);
        am_voiceText = findViewById(R.id.word_am_voice);
        meaningText = findViewById(R.id.word_mean);
        textView = findViewById(R.id.textView);
        word_copy = findViewById(R.id.word_copy);
        scroll = findViewById(R.id.scroll);

//111
//        wordText.setText(word);
//        en_voiceText.setText(en_voice);
//        am_voiceText.setText(am_voice);
//        meaningText.setText(meaning0);

        editText = findViewById(R.id.myeditText);
        //内容为空时清空页面
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editText.getText().length()==0){
                    wordText.setVisibility(View.INVISIBLE);
                    en_voiceText.setVisibility(View.INVISIBLE);
                    am_voiceText.setVisibility(View.INVISIBLE);
                    meaningText.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                    word_copy.setVisibility(View.INVISIBLE);
                    scroll.setVisibility(View.INVISIBLE);
                }
            }
        });
        //对键盘进行相关控制
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“搜索”键*/
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    str = Trim(v.getText().toString());
                    Thread thread = new Thread(WordActivity.this);
                    thread.start();
                    //输入后收起键盘
                    //InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm.isActive()){

                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );

                    }
                    return true;
                }
                return false;
            }
        });


        handler = new Handler() {
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 3){
                    wordText.setText(str);
                    en_voiceText.setText(voice.substring(0, a));
                    am_voiceText.setText(voice.substring(b));
                    meaningText.setText(meaning);
                    wordText.setVisibility(View.VISIBLE);
                    en_voiceText.setVisibility(View.VISIBLE);
                    am_voiceText.setVisibility(View.VISIBLE);
                    meaningText.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    word_copy.setVisibility(View.VISIBLE);
                    scroll.setVisibility(View.INVISIBLE);
                }else if(msg.what == 4){
                    wordText.setText(str);
                    en_voiceText.setText(voice.substring(0, a));
                    meaningText.setText(meaning);
                    wordText.setVisibility(View.VISIBLE);
                    en_voiceText.setVisibility(View.VISIBLE);
                    am_voiceText.setVisibility(View.INVISIBLE);
                    scroll.setVisibility(View.INVISIBLE);
                    meaningText.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    word_copy.setVisibility(View.VISIBLE);
                }else if(msg.what == 6){
                    TextView sentenceText = findViewById(R.id.sentence);
                    TextView meaningText1 = findViewById(R.id.sentence_mean1);
                    sentenceText.setText(str);
                    meaningText1.setText(meaning);
                    scroll.setVisibility(View.VISIBLE);
                    wordText.setVisibility(View.INVISIBLE);
                    en_voiceText.setVisibility(View.INVISIBLE);
                    am_voiceText.setVisibility(View.INVISIBLE);
                    meaningText.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                    word_copy.setVisibility(View.INVISIBLE);
                }else if(msg.what == 5) {
                        Toast.makeText(WordActivity.this, "请输入有效内容！", Toast.LENGTH_LONG).show();
                }else if(msg.what == 7){
                        Toast.makeText(WordActivity.this, "搜不到您要的结果，换成其他内容试试呗~", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public void run() {
        if(str.equals("")){
            Message msg = handler.obtainMessage(5);
            handler.sendMessage(msg);
            return;
        }
        RecordItem recordItem = new RecordItem(str);
        DBManager dbManager = new DBManager(this);

//        List<RecordItem> recordItems = dbManager.listAll();
//        for(RecordItem i : recordItems){
//            Log. i(TAG, "onOptionsItenSelected: 取出数据[id="+i. getId()+"]Name=" + i.getName());
//        }
        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.iciba.com/word?w=".concat(str)).get();
            int i0 = 0,i00=0;
            Elements ps = doc.getElementsByTag("p");
            for(Element p : ps){
                i00++;
            }
            if(i00>=2&&ps.get(1).text().equals("以上结果来自机器翻译。")){//i00很重要，因为ps可能为数量不够
//111                Intent intent = new Intent(this,SentenceActivity.class);
//                intent.putExtra("sentence",str);
//                intent.putExtra("meaning",ps.get(0).text());
//                startActivity(intent);
                dbManager.add(recordItem);
                meaning = ps.get(0).text();
                Message msg = handler.obtainMessage(6);
                handler.sendMessage(msg);
            }else{
                Elements uls = doc.getElementsByTag("ul");
                Boolean flag1 = false,flag2 = false;
                for(Element ul : uls){
                    if(ul.attr("class").equals("Mean_symbols__5dQX7")){
                        voice = ul.text();
                        a = voice.indexOf("]")+1;
                        Log. i(TAG, "onOptionsItenSelected: 取出数据a="+a);
                        b = voice.indexOf("美");
                        flag1 = true;
                    }else if(ul.attr("class").equals("Mean_part__1RA2V")){
                        meaning = ul.text();
                        flag2 = true;
                        break;
                    }
                    i0++;
                }
                if(flag1&&flag2){
                    //Log.i(TAG,"run00000000:都有啊");
                    if(a<2&&b==-1){//一个音也没有，却还有标签
                        dbManager.add(recordItem);
                        Message msg = handler.obtainMessage(6);
                        handler.sendMessage(msg);
//111                        Intent intent = new Intent(this,WordsActivity.class);
//                        intent.putExtra("word",str);
//                        intent.putExtra("en_voice",voice.substring(0, a));
//                        intent.putExtra("meaning",meaning);
//                        startActivity(intent);
                    }else if(b==-1){
                        dbManager.add(recordItem);
                        Message msg = handler.obtainMessage(4);
                        handler.sendMessage(msg);
                    }else{
                        dbManager.add(recordItem);
                        Message msg = handler.obtainMessage(3);
                        handler.sendMessage(msg);
                    }

                }else if(flag2){
                    dbManager.add(recordItem);
                    Intent intent = new Intent(this,SentenceActivity.class);
                    intent.putExtra("sentence",str);
                    intent.putExtra("meaning",meaning);
                    startActivity(intent);
                }else{
                    Message msg = handler.obtainMessage(7);
                    handler.sendMessage(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void onClick(View view){
        TextView meaningText = findViewById(R.id.word_mean);
        android.content.ClipboardManager cm = (android.content.ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(meaningText.getText());
        Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
    }
    public void onClick_scroll(View view){
        TextView meaningText = findViewById(R.id.sentence_mean1);
        android.content.ClipboardManager cm = (android.content.ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(meaningText.getText());
        Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
    }
    public static String Trim(String str){
        StringBuilder sb = new StringBuilder();
        boolean isFirstSpace = false;//标记是否是第一个空格
        str = str.trim();//考虑开头和结尾有空格的情形
        char c;
        for(int i = 0; i < str.length(); i++){
            c = str.charAt(i);
            if(c == ' ' || c == '\t')//遇到空格字符时,先判断是不是第一个空格字符
            {
                if(!isFirstSpace)
                {
                    sb.append(c);
                    isFirstSpace = true;
                }
            }
            else{//遇到非空格字符时
                sb.append(c);
                isFirstSpace = false;
            }
        }
        return sb.toString();
    }
    public void onClick_record(View btn){
        Intent intent = new Intent(this,MyListActivity.class);//每个种类里面都要有该方法//可以哟，居然有getcontext，太方便了
        startActivity(intent);
    }
    //禁用返回按钮
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
//            //do something.
//            return true;
//        } else {
//            return super.dispatchKeyEvent(event);
//        }
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (WordActivity.this.getCurrentFocus() != null) {
                if (WordActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(WordActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    editText.clearFocus();
                }
            }
        }
        return super.onTouchEvent(event);
    }

}
