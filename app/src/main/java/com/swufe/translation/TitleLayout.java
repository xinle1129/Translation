package com.swufe.translation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class TitleLayout extends LinearLayout {
    public TitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title,this);

//        //控制登录用户名图标大小
//        EditText editText = (EditText) findViewById(R.id.myeditText);
//        Drawable drawable = getResources().getDrawable(R.mipmap.search);
//        drawable.setBounds(0, 0, 80, 80);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
//        editText.setCompoundDrawables(drawable, null, null, null);//只放左边
    }
}
