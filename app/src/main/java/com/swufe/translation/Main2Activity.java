package com.swufe.translation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.List;

public class Main2Activity extends AppCompatActivity{
    private final String TAG = "TranslationWords";
    EditText editText;
    String str, voice, meaning;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        String words = intent.getStringExtra("word");
        String en_voice = intent.getStringExtra("en_voice");
        String am_voice = intent.getStringExtra("am_voice");
        String meaning0 = intent.getStringExtra("meaning");

        TextView wordText = findViewById(R.id.word);
        TextView en_voiceText = findViewById(R.id.word_en_voice);
        TextView am_voiceText = findViewById(R.id.word_am_voice);
        TextView meaningText = findViewById(R.id.word_mean);

        wordText.setText(words);
        en_voiceText.setText(en_voice);
        am_voiceText.setText(am_voice);
        meaningText.setText(meaning0);
    }
    public void onClick(View view){
        TextView meaningText = findViewById(R.id.word_mean);
        android.content.ClipboardManager cm = (android.content.ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(meaningText.getText());
        Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
    }
    public void onClick_back(View btn){
        finish();
    }
}
