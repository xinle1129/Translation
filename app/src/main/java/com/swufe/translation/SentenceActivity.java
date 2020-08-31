package com.swufe.translation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class SentenceActivity extends AppCompatActivity{
    private final String TAG = "TranslationSentence";
    EditText editText;
    String str,voice,meaning;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence);
        Intent intent = getIntent();
        String sentence = intent.getStringExtra("sentence");
        String meaning0 = intent.getStringExtra("meaning");

        TextView sentenceText = findViewById(R.id.sentence);
        TextView meaningText = findViewById(R.id.sentence_mean);

        sentenceText.setText(sentence);
        meaningText.setText(meaning0);

    }

    public void onClick(View view){
        TextView meaningText = findViewById(R.id.sentence_mean);
        android.content.ClipboardManager cm = (android.content.ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(meaningText.getText());
        Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
    }
    public void onClick_back(View btn){
        finish();
    }
}