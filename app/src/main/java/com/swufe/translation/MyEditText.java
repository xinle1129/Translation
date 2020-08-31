package com.swufe.translation;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;


import androidx.core.content.ContextCompat;

public class MyEditText extends EditText {
//    /**
//     * 第二步:
//     * 声明3个变量: 两个图片对象(变量的值是通过实例化对象得到的,
//     * 在JAVA世界里,除了基本数据类型和静态成员不是对象外,
//     * 其他一切都是对象.类也是一个对象,类是Class类的对象,图片是drawable类的对象)
//     * 1.当EditText文本内容为空的时候,右侧清空图标应为灰色,此时点击是没有任何效果的
//     * 2.当EditText文本内容不为空的时候,右侧清空图标应为蓝色,此时点击,清空EditText文本内容
//     * 3.上下文对象
//     */
    //private Drawable imageBlue;
    private Drawable imageGray;
    private  Drawable search;
    private Context myContext;
//
//    /**
//     * 实现EditText父类的三个构造方法
//     * 这三个方法必须调用自定义的初始化函数 init()方法
//     */
    public MyEditText(Context context) {
        super(context);
        init(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

//    /**
//     * 初始化方法:用于初始化声明的三个全局变量 :imageBlue,imageGray,myContext
//     * 并负责监听EditText文本内容的更改
//     */
    private void init(Context context){
        this.myContext = context;
//
//        /**
//         * 得到图片资源:
//         * 第一种方式:(已过时,不推荐使用,
//         * 还应注意R文件导入的包应为自己项目下的包,
//         * 因为图片资源在自己项目目录下):
//         *  imageBlue = myContext.getResources().getDrawable(R.drawable.delete);
//         *
//         *  第二种方式:(网友推荐,项目会报错?)
//         *  调用getDrawable()带两个参数的方法.第二参数置为null
//         *imageBlue = myContext.getResources().getDrawable(R.drawable.delete, null);
//         *
//         * 第三种方式:(谷歌官方推荐使用) ,myContext为自己声明的上下文对象
//         * imageBlue = ContextCompat.getDrawable(myContext, R.drawable.delete);
//         */
        //imageBlue = myContext.getResources().getDrawable(R.mipmap.delete,null);
        imageGray = myContext.getResources().getDrawable(R.mipmap.delete_gray,null);
        imageGray.setBounds(-11,0,43,53);
        search = myContext.getDrawable(R.mipmap.search);
        search.setBounds(11, 2, 71, 60);

//
//        /**
//         * 设置文字监听器(EditText文本内容改变时,会触发对应的回调函数)
//         * onTextChanged() EditText文本内容更改时触发
//         * beforeTextChanged() EditText文本内容更改前触发
//         * afterTextChanged() EditText文本内容更改后触发
//         *
//         * 对于此项目,清空EditText应在EditText文本内容更改后触发
//         *
//         */
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //文字改变后设置清空图片的位置
                setImage();

            }
        });
        //初始的时候也应设置清空图片的位置
        setImage();
    }

//    /**
//     * 设置图片位置方法
//     * 当length()大于0，即 EditText里面有文本内容的时候，图片为蓝色
//     * 当 length()小于0，即 EditText里面没有文本内容的时候，图片为灰色
//     * setCompoundDrawablesWithIntrinsicBounds() 四个参数代表左上右下
//     */
    private void  setImage(){
        if (length()>0) {
            setCompoundDrawables(search,null,imageGray,null);
        }else{
            setCompoundDrawables(search, null, null, null);
        }
    }

    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            //匹配手指离开EditText
            case MotionEvent.ACTION_UP:

                //得到手指离开EditText时的X Y坐标
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();

                //创建一个长方形
                Rect rect = new Rect();

                //让长方形的宽等于edittext的宽，让长方形的高等于edittext的高
                getGlobalVisibleRect(rect);

                //把长方形缩短至右边50个宽度内
                rect.left = rect.right - 80;

                //如果x和y坐标在长方形当中，说明你点击了右边的xx图片,清空输入框
                if(rect.contains(x,y)){
                    setText("");
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
