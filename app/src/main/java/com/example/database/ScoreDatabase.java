package com.example.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.example.Items.ScoreListItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class ScoreDatabase extends SQLiteOpenHelper {

    final static String tableName = "Score";

    public ScoreDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ScoreDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public void forceInit(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE " + tableName);
        String sql = "CREATE TABLE " + tableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "score TEXT," +
                "category TEXT," +
                "date TEXT" + ")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + tableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "score TEXT," +
                "category TEXT," +
                "date TEXT" + ")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS groupTBL");
        onCreate(sqLiteDatabase);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insert(String score, String category){
        LocalDate onlyDate = LocalDate.now();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "INSERT INTO " + tableName + " VALUES(null, '"
                + score +  "', '"
                + category +  "', '"
                + onlyDate.toString()
                + "');";
        sqLiteDatabase.execSQL(sql);
    }

    public void insert(String score, String category, String date){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "INSERT INTO " + tableName + " VALUES(null, '"
                + score +  "', '"
                + category +  "', '"
                + date
                + "');";
        sqLiteDatabase.execSQL(sql);

        System.out.println(sql);
    }

    public void delete(int index){
        SQLiteDatabase db = getWritableDatabase();

        ArrayList<ScoreListItem> updatedTableColumns = new ArrayList<ScoreListItem>();
        makeList(updatedTableColumns);
        if(updatedTableColumns.size() == 0){
            return;
        }


        // Remove the columns we don't want anymore from the table's list of columns
        updatedTableColumns.remove(index);

        db.execSQL("ALTER TABLE " + tableName + " RENAME TO " + tableName + "_old;");

        String sql = "CREATE TABLE " + tableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "score TEXT," +
                "category TEXT," +
                "date TEXT" + ")";
        db.execSQL(sql);

        for(int i = 0; i < updatedTableColumns.size(); i ++){
            insert(updatedTableColumns.get(i).score + "", updatedTableColumns.get(i).category,
                    updatedTableColumns.get(i).getDate());
        }
        db.execSQL("DROP TABLE " + tableName + "_old;");
    }


    public void deleteAll(){

        SQLiteDatabase db = getWritableDatabase();
        String sql = "delete from " + tableName;
        db.execSQL(sql);
    }





    public void makeList(ArrayList<ScoreListItem> scoreList){

        scoreList.clear();

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();


        if(cursor.getCount() > 0){
            do {
                String[] data = new String[]{
                        cursor.getString(1), cursor.getString(2), cursor.getString(3)
                };
                scoreList.add(new ScoreListItem(data[0], data[1], data[2]));
            }
            while(cursor.moveToNext());
        }

        Collections.sort(scoreList);
    }



    public void makeCategoryList(ArrayList<ScoreListItem> scoreList, String category){

        scoreList.clear();

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName + " where category = " + '\"' + category + '\"'
                + " order by date desc limit 10";

        //String sql = "Select * from " + tableName;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();

        System.out.println(cursor.getCount());


        if(cursor.getCount() > 0){
            do {
                String[] data = new String[]{
                        cursor.getString(1), cursor.getString(2), cursor.getString(3)
                };
                scoreList.add(new ScoreListItem(data[0], data[1], data[2]));
            }
            while(cursor.moveToNext());
        }

        Collections.sort(scoreList);
    }




    public int getSize(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();

        return cursor.getCount();
    }




}

