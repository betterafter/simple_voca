package com.danerdaner.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.danerdaner.Items.ListItem;
import com.danerdaner.activity.LoadingActivity;
import com.danerdaner.activity.MainActivity;
import com.danerdaner.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import androidx.annotation.Nullable;

public class VocaDatabase extends SQLiteOpenHelper {

    final static String tableName = "voca";

    private final int PARENT_VIEW = 0;
    private final int CHILD_VIEW = 1;
    private final int LOADING_VIEW = 2;
    public static String remindFlag = "REMIND";
    public static String importantFlag = "IMPORTANT";
    public static String nullFlag = "0";

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

        String[] prev = findTableData(index);
        String word = prev[0];
        String category = prev[7];

        SQLiteDatabase db = getWritableDatabase();
        String sql = "delete from " + tableName +
                " where word = " + "\"" + word + "\"" +
                " and sort = " + "\"" + category + "\"";

        db.execSQL(sql);
    }

    public void deleteAll(String category){

        SQLiteDatabase db = getWritableDatabase();
        String sql = "delete from " + tableName +
                " where sort = " + "\"" + category + "\"";

        db.execSQL(sql);
    }







    public void change(int index, String word, String mean, String announce, String example,
                       String example_mean, String memo, String image, String sort, String flag){

        String[] prev = findTableData(index);

        SQLiteDatabase db = getWritableDatabase();
        String sql
                = "update " + tableName +
                " set word = " + "\"" + word + "\"" + "," +
                " mean = " + "\"" + mean + "\"" + "," +
                " announce = " + "\"" + announce + "\"" + "," +
                " example = " + "\"" + example + "\"" + "," +
                " example_mean = " + "\"" + example_mean + "\"" + "," +
                " memo = " + "\"" + memo + "\"" + "," +
                " image = " + "\"" + image + "\"" + "," +
                " sort = " + "\"" + sort + "\"" + "," +
                " flags = " + "\"" + flag + "\"" +
                " where word = " + "\"" + prev[0] + "\"" +
                " and sort = " + "\"" + prev[7] + "\"";


        db.execSQL(sql);



        MainActivity.vocaRecyclerViewAdapter.notifyDataSetChanged();
    }

    public int findTableIndex(String word, String category){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName
                + " where word = " + "\"" + word + "\"" + " and sort = " + "\"" + category + "\"";

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        return Integer.parseInt(cursor.getString(0));
    }

    public String[] findTableData(int position){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName
                +" where id = " + "\"" + position + "\"";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        return new String[]{cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6),
                cursor.getString(7), cursor.getString(8), cursor.getString(9)};
    }

    public boolean CheckIfWordInCategory(String word, String category, int position){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName
                + " where word = " + "\"" + word + "\"" + " and sort = " + "\"" + category + "\"";

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();

        if(cursor.getCount() == 1 && cursor.getString(0).equals(Integer.toString(position))) return false;
        if(cursor.getCount() <= 0) return false;
        else return true;
    }

    public boolean CheckIfWordInCategory(String word, String category){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName
                + " where word = " + "\"" + word + "\"" + " and sort = " + "\"" + category + "\"";

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();

        if(cursor.getCount() <= 0) return false;
        else return true;
    }

    public void UnCheckedIfNoWordInTable(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if(cursor.getCount() <= 0){
            SettingFragment.service.setChecked(false);
        }
    }


    public void makeList(ArrayList<ListItem> vocaList){

        vocaList.clear();
        if(LoadingActivity.sharedPreferences.getString("word_order", "알파벳 순서").equals("무작위로")){
            if(LoadingActivity.vocaShuffleList.size() > 0) {
                vocaList.addAll(LoadingActivity.vocaShuffleList);
                return;
            }
        }

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql;
        if(LoadingActivity.SELECTED_CATEGORY_NAME.equals("전체")){
            sql = "SELECT * FROM " + tableName;
        }
        else{
            sql = "SELECT * FROM " + tableName
                    + " where sort = " + "\"" + LoadingActivity.SELECTED_CATEGORY_NAME + "\"";
        }

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


        if(LoadingActivity.sharedPreferences.getString("word_order", "알파벳 순서").equals("알파벳 순서")){
            Collections.sort(vocaList);
        }

    }

    public void makeList(ArrayList<ListItem> vocaList, String category_name){

        vocaList.clear();

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName
                + " where sort = " + "\"" + category_name + "\"";

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

        if(LoadingActivity.sharedPreferences.getString("word_order", "알파벳 순서").equals("알파벳 순서")){
            Collections.sort(vocaList);
        }
        else{
            Collections.shuffle(vocaList);
        }
    }

    public void makeCategoryList(ArrayList<ListItem> vocaList, String category){
        int cnt = 0;
        vocaList.clear();

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();


        if(cursor.getCount() > 0){
            do {
                cnt ++;
                if(cursor.getString(8).equals(category)) {
                    String[] data = new String[]{
                            cursor.getString(1), cursor.getString(2), cursor.getString(3),
                            cursor.getString(4), cursor.getString(5), cursor.getString(6),
                            cursor.getString(7), cursor.getString(8), cursor.getString(9),
                    };
                    vocaList.add(new ListItem(data, PARENT_VIEW));

                }
            }
            while(cursor.moveToNext());
        }

    }

    public ArrayList<String[]> makeTestList(String category){

        ArrayList<String[]> res = new ArrayList<>();
        String sql = "select word, mean, announce from " + tableName + " where sort = " + '\"' + category + '\"';

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();

        if(cursor.getCount() < 20) return res;


        do{
            Random random = new Random();
            int idx = random.nextInt(cursor.getCount());
            cursor.moveToPosition(idx);

            String[] temp = new String[]{
                    cursor.getString(0), cursor.getString(1), cursor.getString(2)
            };
            res.add(temp);
        }
        while(res.size() < 20);

        return res;
    }

    public ArrayList<String[]> makeImportantTestList(String category){

        ArrayList<String[]> res = new ArrayList<>();
        String sql = "select word, mean, announce from " + tableName + " where sort = " + '\"' + category + '\"'
                + " and flags = " + '\"' + importantFlag + '\"';

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();

        if(cursor.getCount() < 20) return res;


        do{
            Random random = new Random();
            int idx = random.nextInt(cursor.getCount());
            cursor.moveToPosition(idx);

            String[] temp = new String[]{
                    cursor.getString(0), cursor.getString(1), cursor.getString(2)
            };
            res.add(temp);
        }
        while(res.size() < 20);

        return res;
    }

    public void listToDatabase(ArrayList<ListItem> updatedTableColumns){

        for(int i = 0; i < updatedTableColumns.size(); i++){
            if(CheckIfWordInCategory(updatedTableColumns.get(i).getData()[0], updatedTableColumns.get(i).getData()[7])){
                continue;
            }
            for(int j = 0; j < updatedTableColumns.size(); j ++){
                insert(updatedTableColumns.get(j).getData()[0], updatedTableColumns.get(j).getData()[1],
                        updatedTableColumns.get(j).getData()[2],updatedTableColumns.get(j).getData()[3],
                        updatedTableColumns.get(j).getData()[4],updatedTableColumns.get(j).getData()[5],
                        updatedTableColumns.get(j).getData()[6],updatedTableColumns.get(j).getData()[7],
                        updatedTableColumns.get(j).getData()[8]);
            }
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
        String sql;
        if(categoryName.equals("전체")){
            sql =
                    "select * " +
                            "from " + tableName + " " +
                            "where flags = " + "\"REMIND\"" ;
        }
        else{
            sql =
                    "select * " +
                            "from " + tableName + " " +
                            "where sort = " + "\"" + categoryName + "\" " +
                            "and flags = " + "\"REMIND\"" ;
        }

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        return cursor.getCount();
    }







    public int getCategoryImportantWordSize(String categoryName){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql;
        if(categoryName.equals("전체")){
            sql =
                    "select * " +
                            "from " + tableName + " " +
                            "where flags = " + "\"IMPORTANT\"" ;
        }
        else{
            sql =
                    "select * " +
                            "from " + tableName + " " +
                            "where sort = " + "\"" + categoryName + "\" " +
                            "and flags = " + "\"IMPORTANT\"" ;
        }
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        return cursor.getCount();
    }






    public void changeBookmarkState(String word, String category, String type){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql
                = "update " + tableName +
                " set " +
                " flags = " + "\"" + type + "\"" +
                " where word = " + "\"" + word + "\"" +
                " and sort = " + "\"" + category + "\"";
        sqLiteDatabase.execSQL(sql);
    }


    public void print(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql =
                "select * " +
                        "from " + tableName;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            do {
                System.out.println(cursor.getString(0) + " , "+
                        cursor.getString(1) + " , " + cursor.getString(2) + " , " + " , " + cursor.getString(3)
                        + " , " + cursor.getString(4) + " , "  + cursor.getString(5) + " , " + cursor.getString(6) + " , " +
                        cursor.getString(7) + " , " + cursor.getString(8) + " , " + cursor.getString(9));
            }
            while(cursor.moveToNext());
        }
    }
}

