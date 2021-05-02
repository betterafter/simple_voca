package com.example.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.Items.ListItem;
import com.example.activity.LoadingActivity;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class VocaDatabase extends SQLiteOpenHelper {

    final static String tableName = "voca";

    private final int PARENT_VIEW = 0;
    private final int CHILD_VIEW = 1;
    private final int LOADING_VIEW = 2;

    public VocaDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public VocaDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    // flag = NULL / REMIND / IMPORTANT
    public void forceInit(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE " + tableName);
        String sql = "CREATE TABLE " + tableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "word TEXT," +
                "mean TEXT," +
                "announce TEXT," +
                "example TEXT," +
                "example_mean TEXT," +
                "memo TEXT," +
                "image TEXT," +
                "sort TEXT," +
                "flags TEXT" + ")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "CREATE TABLE " + tableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "word TEXT," +
                "mean TEXT," +
                "announce TEXT," +
                "example TEXT," +
                "example_mean TEXT," +
                "memo TEXT," +
                "image TEXT," +
                "sort TEXT," +
                "flags TEXT" + ")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS groupTBL");
        onCreate(sqLiteDatabase);
    }

    public void insert(String word, String mean, String announce, String example, String example_mean,
                       String memo, String image, String sort, String flags){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "INSERT INTO " + tableName + " VALUES(null, '"
                + word +  "', '"
                + mean +  "', '"
                + announce + "', '"
                + example + "', '"
                + example_mean + "', '"
                + memo + "', '"
                + image + "', '"
                + sort + "', '"
                + flags
                + "');";


        sqLiteDatabase.execSQL(sql);
    }


    public void delete(int index){
        SQLiteDatabase db = getWritableDatabase();

        ArrayList<ListItem> updatedTableColumns = new ArrayList<ListItem>();
        makeList(updatedTableColumns);
        if(updatedTableColumns.size() == 0){
            return;
        }


        // Remove the columns we don't want anymore from the table's list of columns
        updatedTableColumns.remove(index);

        db.execSQL("ALTER TABLE " + tableName + " RENAME TO " + tableName + "_old;");

        String sql = "CREATE TABLE " + tableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "word TEXT," +
                "mean TEXT," +
                "announce TEXT," +
                "example TEXT," +
                "example_mean TEXT," +
                "memo TEXT," +
                "image TEXT," +
                "sort TEXT," +
                "flags TEXT" + ")";
        db.execSQL(sql);

        for(int i = 0; i < updatedTableColumns.size(); i ++){
            insert(updatedTableColumns.get(i).getData()[0], updatedTableColumns.get(i).getData()[1],
                    updatedTableColumns.get(i).getData()[2],updatedTableColumns.get(i).getData()[3],
                    updatedTableColumns.get(i).getData()[4],updatedTableColumns.get(i).getData()[5],
                    updatedTableColumns.get(i).getData()[6],updatedTableColumns.get(i).getData()[7],
                    updatedTableColumns.get(i).getData()[8]);
        }
        db.execSQL("DROP TABLE " + tableName + "_old;");

    }

    public void change(int index, String word, String mean, String announce, String example,
                       String example_mean, String memo, String image, String sort, String flag){
        SQLiteDatabase db = getWritableDatabase();
        String[] colmn = new String[9];
        ArrayList<ListItem> updatedTableColumns = new ArrayList<ListItem>();
        makeList(updatedTableColumns);
        if(updatedTableColumns.size() == 0){
            return;
        }

        // Remove the columns we don't want anymore from the table's list of columns
        updatedTableColumns.remove(index);
        colmn[0] = word;
        colmn[1] = mean;
        colmn[2] = announce;
        colmn[3] = example;
        colmn[4] = example_mean;
        colmn[5] = memo;
        colmn[6] = image;
        colmn[7] = sort;
        colmn[8] = flag + "";
        updatedTableColumns.add(index, new ListItem(colmn, PARENT_VIEW));

        db.execSQL("ALTER TABLE " + tableName + " RENAME TO " + tableName + "_old;");

        String sql = "CREATE TABLE " + tableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "word TEXT," +
                "mean TEXT," +
                "announce TEXT," +
                "example TEXT," +
                "example_mean TEXT," +
                "memo TEXT," +
                "image TEXT," +
                "sort TEXT," +
                "flags TEXT" + ")";
        db.execSQL(sql);

        for(int i = 0; i < updatedTableColumns.size(); i ++){
            insert(updatedTableColumns.get(i).getData()[0], updatedTableColumns.get(i).getData()[1],
                    updatedTableColumns.get(i).getData()[2],updatedTableColumns.get(i).getData()[3],
                    updatedTableColumns.get(i).getData()[4],updatedTableColumns.get(i).getData()[5],
                    updatedTableColumns.get(i).getData()[6],updatedTableColumns.get(i).getData()[7],
                    updatedTableColumns.get(i).getData()[8]);
        }

        db.execSQL("DROP TABLE " + tableName + "_old;");
    }




    public void makeList(ArrayList<ListItem> vocaList){

        vocaList.clear();

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName
                + " where sort = " + "\"" + LoadingActivity.SELECTED_CATEGORY_NAME + "\"";

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();


        if(cursor.getCount() > 0){
            do {
                String[] data = new String[]{
                        cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5), cursor.getString(6),
                        cursor.getString(7), cursor.getString(8), cursor.getString(9),
                };
                vocaList.add(new ListItem(data, PARENT_VIEW));
            }
            while(cursor.moveToNext());
        }
    }

    public void makeEmptyMeanList(ArrayList<ListItem> vocaList){

        vocaList.clear();

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName
                + " where sort = " + "\"" + LoadingActivity.SELECTED_CATEGORY_NAME + "\"";

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();


        if(cursor.getCount() > 0){
            do {
                String[] data = new String[]{
                        cursor.getString(1), "", cursor.getString(3),
                        cursor.getString(4), cursor.getString(5), cursor.getString(6),
                        cursor.getString(7), cursor.getString(8), cursor.getString(9),
                };
                vocaList.add(new ListItem(data, PARENT_VIEW));
            }
            while(cursor.moveToNext());
        }
    }


    public String[] getWordChangerString(int i, ArrayList<ListItem> vocaList){
        return vocaList.get(i).getData();
    }

    public int getSize(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();

        return cursor.getCount();
    }

    public int getCategoryAllWordSize(String categoryName){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql =
                "select * " +
                "from " + tableName + " " +
                "where sort = " + "\"" + categoryName + "\"";

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        return cursor.getCount();
    }

    public int getCategoryRemindedWordSize(String categoryName){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql =
                "select * " +
                        "from " + tableName + " " +
                        "where sort = " + "\"" + categoryName + "\" " +
                        "and flags = " + "\"REMIND\"" ;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        return cursor.getCount();
    }

    public int getCategoryImportantWordSize(String categoryName){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql =
                "select * " +
                        "from " + tableName + " " +
                        "where sort = " + "\"" + categoryName + "\" " +
                        "and flags = " + "\"IMPORTANT\"" ;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        return cursor.getCount();
    }
}
