package com.swufe.translation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private DBHelper dbHelper;
    private String TBNAME;
    public DBManager(Context context) {
        dbHelper = new DBHelper(context);
        TBNAME = DBHelper.TB_NAME ;
    }
    public void add(RecordItem item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME,"NAME=?",new String[]{item.getName()});
        ContentValues values = new ContentValues();
        values. put("name",item.getName());
        db. insert(TBNAME,null, values);
        db. close();
    }

    public void addAll(List<RecordItem> list) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (RecordItem item : list) {
            ContentValues values = new ContentValues();
            values.put("name", item.getName());
            db.insert(TBNAME, null, values);
        }
        db.close();
    }
    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME, null, null);
        db.close();
    }

    public void delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME, "ID=?", new String[]{String.valueOf(id)});
        db.close();
    }
    public void delete(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(TBNAME,"NAME=?",new String[]{name});
        db.close();
    }
    public void update(RecordItem item){
        SQLiteDatabase db = dbHelper . getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",item. getName());
        db. update(TBNAME, values,"ID=?",new String[]{String.valueOf(item.getId())});
        db.close();
    }


    public List<RecordItem> listAll(){//数据库查询
        List<RecordItem> rateList = null;
        SQLiteDatabase db = dbHelper . getReadableDatabase();
        Cursor cursor = db. query(TBNAME,null, null, null, null, null, null);
        if(cursor!=null){
            rateList = new ArrayList<RecordItem>();
            while( cursor . moveToNext()){
                RecordItem item = new RecordItem();
                item. setId(cursor. getInt(cursor. getColumnIndex("ID")));
                item.setName( cursor . getString(cursor. getColumnIndex( "NAME")));
                rateList. add(item);
            }
            cursor.close();
        }
        db.close();
        return rateList ;
    }

    public RecordItem findById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, "ID=?", new String[]{String.valueOf(id)},
                null, null, null);
        RecordItem rateItem = null;
        if (cursor != null && cursor.moveToFirst()){
            rateItem = new RecordItem();
            rateItem.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            rateItem.setName(cursor.getString(cursor.getColumnIndex("NAME")));
            cursor.close();
        }
        db.close();
        return rateItem;
    }

}

