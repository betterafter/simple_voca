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

    private final String INLINE = ",";
    private final String LINER = "%%@@%@@%%@@%";
    private final String SMALL_QUOTATION_MARK = "@@@!!!###%%%";
    private final String BIG_QUOTATION_MARK = "%%%##@#@@#@!!!";
    public String code = "0x002C";

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

    /*

    db 밖에서 오는 작은 따옴표는 모두 코드 형태로 바꿔줘야 하며,
    db에서 앱으로 가는 작은 따옴표 코드는 모두 작은 따옴표 자체로 바꿔줘야 함.

     */

    // 디비로 들어오는 형태로 코드로 바꿔야함.
    public void insert(String word, String mean, String announce, String example, String example_mean,
                       String memo, String image, String sort, String flags){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        word = change_small_quotation_mark_to_code(word);
        mean = change_small_quotation_mark_to_code(mean);
        announce = change_small_quotation_mark_to_code(announce);

        example = change_small_quotation_mark_to_code(example);
        example_mean = change_small_quotation_mark_to_code(example_mean);
        memo = change_small_quotation_mark_to_code(memo);

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


    // 앱에서 문자열을 찾아서 디비에 있는 걸 지워줘야 하므로 앱 -> 디비 형태
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

        word = change_small_quotation_mark_to_code(word);
        mean = change_small_quotation_mark_to_code(mean);
        announce = change_small_quotation_mark_to_code(announce);

        example = change_small_quotation_mark_to_code(example);
        example_mean = change_small_quotation_mark_to_code(example_mean);
        memo = change_small_quotation_mark_to_code(memo);

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

        word = change_small_quotation_mark_to_code(word);
        System.out.println(word);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName
                + " where word = " + "\"" + word + "\"" + " and sort = " + "\"" + category + "\"";

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        return Integer.parseInt(cursor.getString(0));
    }





    // 디비 -> 디비
    public String[] findTableData(int position){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "SELECT * FROM " + tableName
                +" where id = " + "\"" + position + "\"";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();

        // 단어, 단어 뜻, 발음, 예문, 예문 뜻, 메모, 이미지, 그룹, 플래그
        return new String[]{
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),

                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),

                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9)};
    }






    public boolean CheckIfWordInCategory(String word, String category, int position){

        word = change_small_quotation_mark_to_code(word);

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

        word = change_small_quotation_mark_to_code(word);

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
        if(cursor.getCount() <= 0 && SettingFragment.service != null){
            SettingFragment.service.setChecked(false);
        }
    }





    public void makeList(ArrayList<ListItem> vocaList){

        vocaList.clear();
        if(LoadingActivity.sharedPreferences.getString("word_order", "알파벳 순서").equals("무작위로")){
            vocaList.addAll((LoadingActivity.vocaShuffleLists.get(LoadingActivity.SELECTED_CATEGORY_NAME)));
            return;
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
                        change_code_to_small_quotation_mark(cursor.getString(1)),
                        change_code_to_small_quotation_mark(cursor.getString(2)),
                        change_code_to_small_quotation_mark(cursor.getString(3)),
                        change_code_to_small_quotation_mark(cursor.getString(4)),
                        change_code_to_small_quotation_mark(cursor.getString(5)),
                        change_code_to_small_quotation_mark(cursor.getString(6)),

                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9)
                };
                vocaList.add(new ListItem(data, PARENT_VIEW));
            }
            while(cursor.moveToNext());
        }


        if(LoadingActivity.sharedPreferences.getString("word_order", "알파벳 순서").equals("알파벳 순서")){
            Collections.sort(vocaList);
        }

    }

    // 무작위 단어 순서 만들 때
    // 맨 처음에 미리 만들어서 가지고 있다가
    // 단어 추가하면 만든 리스트 바로 뒤에 추가만 하고
    // 설정창에서 무작위로 바꿀 때마다 불러서 순서 다시 셔플함
    public void makeShuffleList(String category_name){

        ArrayList<ListItem> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        String sql;
        if(category_name.equals("전체")){
            sql = "SELECT * FROM " + tableName;
        }
        else{
            sql = "SELECT * FROM " + tableName
                    + " where sort = " + "\"" + category_name + "\"";
        }


        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();


        if(cursor.getCount() > 0){
            do {
                String[] data = new String[]{
                        change_code_to_small_quotation_mark(cursor.getString(1)),
                        change_code_to_small_quotation_mark(cursor.getString(2)),
                        change_code_to_small_quotation_mark(cursor.getString(3)),
                        change_code_to_small_quotation_mark(cursor.getString(4)),
                        change_code_to_small_quotation_mark(cursor.getString(5)),
                        change_code_to_small_quotation_mark(cursor.getString(6)),

                        cursor.getString(7), cursor.getString(8), cursor.getString(9)
                };
                arrayList.add(new ListItem(data, PARENT_VIEW));
            }
            while(cursor.moveToNext());
        }
        Collections.shuffle(arrayList);
        LoadingActivity.vocaShuffleLists.put(category_name, arrayList);
    }


    // 카테고리 공유하기 기능 사용할 때 이걸로 뽑아서 보내줌
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
                        change_code_to_small_quotation_mark(cursor.getString(1)),
                        change_code_to_small_quotation_mark(cursor.getString(2)),
                        change_code_to_small_quotation_mark(cursor.getString(3)),
                        change_code_to_small_quotation_mark(cursor.getString(4)),
                        change_code_to_small_quotation_mark(cursor.getString(5)),
                        change_code_to_small_quotation_mark(cursor.getString(6)),

                        cursor.getString(7), cursor.getString(8), cursor.getString(9)
                };
                vocaList.add(new ListItem(data, PARENT_VIEW));
            }
            while(cursor.moveToNext());
        }
    }

    // 공유를 위해 카테고리에 있는 단어 리스트를 만듬 -> csv로 공유하기 때문에 code 형식으로 보내야 함.
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
                            makeNoneCSVString(cursor.getString(1)),
                            makeNoneCSVString(cursor.getString(2)),
                            makeNoneCSVString(cursor.getString(3)),

                            makeNoneCSVString(cursor.getString(4)),
                            makeNoneCSVString(cursor.getString(5)),
                            makeNoneCSVString(cursor.getString(6)),

                            cursor.getString(7),
                            cursor.getString(8),
                            cursor.getString(9)
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
                    change_code_to_small_quotation_mark(cursor.getString(0)),
                    change_code_to_small_quotation_mark(cursor.getString(1)),
                    change_code_to_small_quotation_mark(cursor.getString(2))
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
                    change_code_to_small_quotation_mark(cursor.getString(0)),
                    change_code_to_small_quotation_mark(cursor.getString(1)),
                    change_code_to_small_quotation_mark(cursor.getString(2))
            };
            res.add(temp);
        }
        while(res.size() < 20);

        return res;
    }

    // ',' 에 대한 코드가 있으면 쉼표로 변환해주는 작업이 필요함
    public void listToDatabase(ArrayList<ListItem> updatedTableColumns){

        for(int i = 0; i < updatedTableColumns.size(); i++){
            if(updatedTableColumns.get(i).getData().length < 2) continue;
            if(CheckIfWordInCategory(updatedTableColumns.get(i).getData()[0], updatedTableColumns.get(i).getData()[7])){
                continue;
            }

            insert(
                    updatedTableColumns.get(i).getData()[0].replaceAll(code, ","),
                    updatedTableColumns.get(i).getData()[1].replaceAll(code, ","),
                    updatedTableColumns.get(i).getData()[2].replaceAll(code, ","),

                    updatedTableColumns.get(i).getData()[3].replaceAll(code, ","),
                    updatedTableColumns.get(i).getData()[4].replaceAll(code, ","),
                    updatedTableColumns.get(i).getData()[5].replaceAll(code, ","),

                    updatedTableColumns.get(i).getData()[6].replaceAll(code, ","),
                    updatedTableColumns.get(i).getData()[7].replaceAll(code, ","),
                    updatedTableColumns.get(i).getData()[8].replaceAll(code, ","));

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






    public int getAllWordSize(String categoryName){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql =
                "select * " +
                        "from " + tableName;

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

        word = change_small_quotation_mark_to_code(word);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql
                = "update " + tableName +
                " set " +
                " flags = " + "\"" + type + "\"" +
                " where word = " + "\"" + word + "\"" +
                " and sort = " + "\"" + category + "\"";
        sqLiteDatabase.execSQL(sql);
    }







    public String makeNoneCSVString(String str){
        str = str.replaceAll(",", code);
        return str;
    }

    private String change_small_quotation_mark_to_code(String str){
        str = str.replaceAll("'", SMALL_QUOTATION_MARK);
        str = str.replaceAll("\"", BIG_QUOTATION_MARK);
        return str;
    }

    private String change_code_to_small_quotation_mark(String str){
        str = str.replaceAll(SMALL_QUOTATION_MARK, "'");
        str = str.replaceAll(BIG_QUOTATION_MARK, "\"");
        return str;
    }



}

