package com.example.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Items.ListItem;
import com.example.simple_voca.CSVBuilder;
import com.example.simple_voca.FileIOManager;
import com.example.simple_voca.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

public class Category_MainActivity extends AppCompatActivity {

    LinearLayout category_main_add_button_layout;
    ImageButton category_main_add_button;
    ImageButton category_main_delete_button;
    private static final int PICKFILE_REQUEST_CODE = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_main);




        // 카테고리 추가 버튼
        category_main_add_button_layout = findViewById(R.id.category_main_add_button_layout);
        category_main_add_button_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Category_MainActivity.this, Category_AddActivity.class);
                startActivity(intent);
            }
        });
        category_main_add_button = findViewById(R.id.category_main_add_button);
        category_main_add_button.setClickable(false);


        category_main_delete_button = findViewById(R.id.category_main_delete_button);



        makeCategoryList();
    }

    /////////////////////////////////////// 공유 기능과 테스트 기능 구현하시는 분들은 이쪽으로 //////////////////////////////////////////
    // 1. 각각 share button 과 test button을 이용해서 화면 전환을 해주세요.
    // 2. 각 기능을 사용할 카테고리명은 onClick 안의 categoryName으로 해주시면 됩니다. (필요 없다면 안써도 됨)
    // 3. intent를 이용해서 각각 필요한 화면으로 전환을 하거나 필요한 기능을 불러보세요.
    // 4. 궁금한게 있다면 바로 문의해주세요.



    public void makeCategoryList(){

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_category_main_item, null);

        LinearLayout linearLayout = findViewById(R.id.category_main_category_list);

        for(int i = 0; i < LoadingActivity.categoryList.size(); i++){


            View view = inflater.inflate(R.layout.activity_category_main_item, null);
            CardView cardView = view.findViewById(R.id.category_main_category_cardview);
            TextView category_name = view.findViewById(R.id.category_main_category_name);
            TextView category_subtitle = view.findViewById(R.id.category_main_category_subtitle);
            TextView category_all_word = view.findViewById(R.id.category_main_category_all_word);
            TextView category_remind_word = view.findViewById(R.id.category_main_category_remind_word);
            TextView category_important_word = view.findViewById(R.id.category_main_category_important_word);
            ImageButton select_button = view.findViewById(R.id.category_main_category_select_button);
            ImageButton share_button = view.findViewById(R.id.category_main_category_share_button);
            ImageButton test_button = view.findViewById(R.id.category_main_category_test_button);

            String categoryName = LoadingActivity.categoryDatabase.getCategoryName(i);

            category_name.setText(categoryName);
            if(categoryName.equals(LoadingActivity.SELECTED_CATEGORY_NAME)){
                cardView.setCardBackgroundColor(getResources().getColor(R.color.mainBlue));
                category_name.setTextColor(getResources().getColor(R.color.white));
                select_button.setBackgroundColor(getResources().getColor(R.color.mainBlue));
                category_subtitle.setTextColor(getResources().getColor(R.color.white));
            }
            category_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoadingActivity.SELECTED_CATEGORY_NAME = categoryName;
                    LoadingActivity.SELECTED_CATEGORY_SUBTITLE =
                            LoadingActivity.categoryDatabase.getCategorySubTitle(LoadingActivity.SELECTED_CATEGORY_NAME);
                    LoadingActivity.vocaDatabase.makeList(LoadingActivity.vocaList);

                    Intent intent = new Intent(Category_MainActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            category_subtitle.setText(LoadingActivity.categoryDatabase.getCategorySubTitle(i));
            category_all_word.setText(Integer.toString(LoadingActivity.vocaDatabase.getCategoryAllWordSize(categoryName)) + "개");
            category_remind_word.setText(Integer.toString(LoadingActivity.vocaDatabase.getCategoryRemindedWordSize(categoryName)) + "개");
            category_important_word.setText(Integer.toString(LoadingActivity.vocaDatabase.getCategoryImportantWordSize(categoryName)) + "개");




            final String cn = categoryName;
            // 공유 기능 버튼 클릭
            share_button.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {

                    String categoryName = cn;
                    try {
                        fileShare(categoryName);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }


                }
            });


            // 테스트 기능 버튼 클릭
            test_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    
                    // 푸름이 코드
                    //String categoryName = cn;
                    //fileSelectIntent();

                    // 여기부터 준우가 적은거임..
                    Intent intent = new Intent(Category_MainActivity.this, Test_MainActivity.class);
                    startActivity(intent);

                }
            });





            linearLayout.addView(view);
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fileShare(String category) throws Exception {
        CSVBuilder csvBuilder = new CSVBuilder();
        FileIOManager fileIOManager = new FileIOManager();
        ArrayList<ListItem> list = new ArrayList<ListItem>();
        LoadingActivity.vocaDatabase.makeCategoryList(list, category);


        File xlsFile = fileIOManager.writeCategoryFile(category,csvBuilder.getCSVString(list));;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/*");    // 엑셀파일 공유 시

        Uri contentUri = FileProvider.getUriForFile(getApplicationContext(),
                getApplicationContext().getPackageName() + ".fileprovider", xlsFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        startActivity(Intent.createChooser(shareIntent,"엑셀 공유"));
    }

    private void fileSelectIntent(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        String file;
        CSVBuilder csvBuilder = new CSVBuilder();
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            byte[] buffer = new byte[inputStream.available() + 2];
            inputStream.read(buffer);
            file = new String(buffer);
            Log.i( "PoohReum" , "read" + file );
            csvBuilder.StringToDatabase(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void readFile(){

    }
}
