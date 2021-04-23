package com.example.database;


import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.Items.categoryListItem;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class categoryDatabase extends SQLiteOpenHelper {

    final static String tableName = "category";

    private final int PARENT_VIEW = 0;
    private final int CHILD_VIEW = 1;
    private final int LOADING_VIEW = 2;

    public categoryDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public categoryDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public void forceInit(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE " + tableName);
        String sql = "CREATE TABLE " + tableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT," +
                "content TEXT" + ")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "CREATE TABLE " + tableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT," +
                "content TEXT" + ")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS groupTBL");
        onCreate(sqLiteDatabase);
    }

    public void insert(String name, String content){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "INSERT INTO " + tableName + " VALUES(null, '"
                + name +  "', '"
                + content
                + "');";


        sqLiteDatabase.execSQL(sql);
    }


    public void delete(int index){
        SQLiteDatabase db = getWritableDatabase();

        ArrayList<categoryListItem> updatedTableColumns = new ArrayList<>();
        makeList(updatedTableColumns);
        if(updatedTableColumns.size() == 0){
            return;
        }


        // Remove the columns we don't want anymore from the table's list of columns
        updatedTableColumns.remove(index);

        db.execSQL("ALTER TABLE " + tableName + " RENAME TO " + tableName + "_old;");

        String sql = "CREATE TABLE " + tableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT," +
                "content TEXT" + ")";
        db.execSQL(sql);

        for(int i = 0; i < updatedTableColumns.size(); i ++){
            insert(updatedTableColumns.get(i).getData()[0], updatedTableColumns.get(i).getData()[1]);
        }
        db.execSQL("DROP TABLE " + tableName + "_old;");

    }

    public void change(int index, String name, String content){
        SQLiteDatabase db = getWritableDatabase();
        String[] colmn = new String[2];
        ArrayList<categoryListItem> updatedTableColumns = new ArrayList<categoryListItem>();
        makeList(updatedTableColumns);
        if(updatedTableColumns.size() == 0){
            return;
        }

        // Remove the columns we don't want anymore from the table's list of columns
        updatedTableColumns.remove(index);
        colmn[0] = name;
        colmn[1] = content;
        updatedTableColumns.add(index, new categoryListItem(colmn));

        db.execSQL("ALTER TABLE " + tableName + " RENAME TO " + tableName + "_old;");

        String sql = "CREATE TABLE " + tableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT," +
                "content TEXT" + ")";
        db.execSQL(sql);

        for(int i = 0; i < updatedTableColumns.size(); i ++){
            insert(updatedTableColumns.get(i).getData()[0], updatedTableColumns.get(i).getData()[1]);
        }

        db.execSQL("DROP TABLE " + tableName + "_old;");
    }




    public void makeList(ArrayList<categoryListItem> vocaList){

        vocaList.clear();

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();


        if(cursor.getCount() > 0){
            do {
                String[] data = new String[]{
                        cursor.getString(1), cursor.getString(2)
                };
                vocaList.add(new categoryListItem(data));
            }
            while(cursor.moveToNext());
        }
    }



    public int getSize(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();

        return cursor.getCount();
    }
}
