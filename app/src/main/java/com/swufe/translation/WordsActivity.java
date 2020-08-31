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

public class WordsActivity extends AppCompatActivity{
    private final String TAG = "TranslationWords";
    EditText editText;
    String str, voice, meaning;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);
        Intent intent = getIntent();
        String words = intent.getStringExtra("word");
        String en_voice = intent.getStringExtra("en_voice");
        String meaning0 = intent.getStringExtra("meaning");

        TextView wordText = findViewById(R.id.words);
        TextView en_voiceText = findViewById(R.id.words_en_voice);
        TextView meaningText = findViewById(R.id.words_mean);

        wordText.setText(words);
        en_voiceText.setText(en_voice);
        meaningText.setText(meaning0);

    }


    public void onClick(View view){
        TextView meaningText = findViewById(R.id.words_mean);
        android.content.ClipboardManager cm = (android.content.ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(meaningText.getText());
        Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
    }
    public void onClick_back(View btn){
        finish();
    }
}