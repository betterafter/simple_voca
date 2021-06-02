package com.danerdaner.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.danerdaner.Items.ListItem;
import com.danerdaner.simple_voca.ImageSerializer;
import com.danerdaner.simple_voca.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditVocaActivity extends AppCompatActivity {

    private TextView add_voca_select_group;
    private TextView add_voca_title;
    private TextInputEditText add_voca_word;
    private TextInputEditText add_voca_mean;
    private TextInputEditText add_voca_announce;
    private TextInputEditText add_voca_example;
    private TextInputEditText add_voca_example_mean;
    private TextInputEditText add_voca_memo;
    private ImageView add_voca_select_picture_imageview;
    private Spinner add_select_group_spinner;
    private ImageButton add_voca_word_search_button;

    private ArrayList<String> categoryList;



    private Button add_voca_save_button;
    private String SAVE_STATE = "SAVE";
    private int POSITION = -1;
    private int SPINNER_SELETED_POSITION;

    private String prevWord, prevGroup;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voca);

        SAVE_STATE = "SAVE";
        POSITION = -1;

        categoryList = new ArrayList<>();
        for(int i = 0; i < LoadingActivity.categoryList.size(); i++){
            categoryList.add(LoadingActivity.categoryList.get(i).getData()[0]);

            if(LoadingActivity.categoryList.get(i).getData()[0].equals(LoadingActivity.SELECTED_CATEGORY_NAME)){
                SPINNER_SELETED_POSITION = i;
            }
        }


        ArrayAdapter<String>  arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, categoryList);
        add_select_group_spinner = findViewById(R.id.add_voca_select_group_spinner);
        add_select_group_spinner.setAdapter(arrayAdapter);

        add_voca_title = findViewById(R.id.add_voca_title);
        add_voca_word = findViewById(R.id.add_voca_word);
        add_voca_mean = findViewById(R.id.add_voca_mean);
        add_voca_announce = findViewById(R.id.add_voca_announce);
        add_voca_example = findViewById(R.id.add_voca_example);
        add_voca_example_mean = findViewById(R.id.add_voca_example_mean);
        add_voca_memo = findViewById(R.id.add_voca_memo);
        add_voca_select_picture_imageview = findViewById(R.id.add_voca_select_picture_imageview);
        add_voca_word_search_button = findViewById(R.id.add_voca_word_search_button);



        add_voca_select_picture_imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 200);
            }
        });

        // 저장 버튼 클릭
        add_voca_save_button = findViewById(R.id.add_voca_save_button);
        add_voca_save_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                String word = add_voca_word.getText().toString();
                String mean = add_voca_mean.getText().toString();
                String announce = add_voca_announce.getText().toString();
                String example = add_voca_example.getText().toString();
                String example_mean = add_voca_example_mean.getText().toString();
                String memo = add_voca_memo.getText().toString();
                String group = add_select_group_spinner.getSelectedItem().toString();

                System.out.println(example);
                System.out.println(example_mean);
                System.out.println(memo);

                if(!checkString(word, getApplicationContext())) return;
                if(!checkString(mean, getApplicationContext())) return;
                if(!checkString(announce, getApplicationContext())) return;
                if(word.length() <= 0){
                    Toast.makeText(getApplicationContext(),
                            "단어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mean.length() <= 0){
                    Toast.makeText(getApplicationContext(),
                            "단어의 뜻을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }


                example = changeChar(example);
                example_mean = changeChar(example_mean);
                memo = changeChar(memo);

                if(SAVE_STATE.equals("SAVE")) {

                    if(LoadingActivity.vocaDatabase.CheckIfWordInCategory(word, group, POSITION)){
                        Toast.makeText(getApplicationContext(),
                                "해당 카테고리에 이미 같은 단어가 존재합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 데이터베이스에 넣기
                    LoadingActivity.vocaDatabase.insert(
                            word,
                            mean,
                            announce,
                            example,
                            example_mean,
                            memo,
                            ImageSerializer.PackImageToSerialized(add_voca_select_picture_imageview),
                            group,
                            "0");

                    String[] data = new String[]{
                            word, mean, announce, example, example_mean, mean,
                            ImageSerializer.PackImageToSerialized(add_voca_select_picture_imageview),
                            group, "0"
                    };
                    LoadingActivity.vocaShuffleList.add(new ListItem(data, 0));
                    LoadingActivity.vocaDatabase.makeList(LoadingActivity.vocaList);
                }
                else if(SAVE_STATE.equals("EDIT") && POSITION >= 0){

                    if(LoadingActivity.vocaDatabase.CheckIfWordInCategory(word, group, POSITION)){
                        Toast.makeText(getApplicationContext(),
                                "해당 카테고리에 이미 같은 단어가 존재합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    LoadingActivity.vocaDatabase.change(
                            POSITION,
                            word,
                            mean,
                            announce,
                            example,
                            example_mean,
                            memo,
                            ImageSerializer.PackImageToSerialized(add_voca_select_picture_imageview),
                            group,
                            "0");

                    // shuffle에도 수정한 단어 수정하기
                    for(int i = 0; i < LoadingActivity.vocaShuffleList.size(); i++){
                        if(LoadingActivity.vocaShuffleList.get(i).getData()[0].equals(prevWord) &&
                                LoadingActivity.vocaShuffleList.get(i).getData()[7].equals(prevGroup)){
                            String[] data = new String[]{
                                    word, mean, announce, example, example_mean, mean,
                                    ImageSerializer.PackImageToSerialized(add_voca_select_picture_imageview),
                                    group, "0"
                            };
                            LoadingActivity.vocaShuffleList.remove(i);
                            LoadingActivity.vocaShuffleList.add(i, new ListItem(data, 0));
                            break;
                        }
                    }

                    LoadingActivity.vocaDatabase.makeList(LoadingActivity.vocaList);
                    MainActivity.vocaRecyclerViewAdapter.notifyDataSetChanged();
                }

                SAVE_STATE = "SAVE";
                Intent intent = new Intent(AddEditVocaActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        add_voca_word_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://en.dict.naver.com/#/search?range=all&query="+add_voca_word.getText()));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        add_select_group_spinner.setSelection(SPINNER_SELETED_POSITION);
        Intent intent = getIntent();
        if(intent.getStringExtra("STATE") != null) {
            SAVE_STATE = intent.getStringExtra("STATE");
            POSITION = intent.getIntExtra("POSITION", -1);
        }

        if(SAVE_STATE.equals("EDIT")){
            System.out.println();
            String[] data = LoadingActivity.vocaDatabase.findTableData(POSITION);

            add_voca_title.setText("단어 수정");
            add_voca_save_button.setText("단어 수정하기");

            add_voca_word.setText(data[0]);
            add_voca_mean.setText(data[1]);
            add_voca_announce.setText(data[2]);
            add_voca_example.setText(data[3]);
            add_voca_example_mean.setText(data[4]);
            add_voca_memo.setText(data[5]);
            if(data[6] != null && data[6].length() > 10) {
                add_voca_select_picture_imageview.setImageDrawable(new BitmapDrawable(
                        getApplicationContext().getResources(), ImageSerializer.PackSerializedToImage(data[6])));
            }

            prevGroup = add_select_group_spinner.getSelectedItem().toString();
            prevWord = add_voca_word.getText().toString();

            // 전체 카테고리에서 단어 수정을 실행할 때는 각 단어가 포함 되어 있는 카테고리 이름을 불러준다.
            if(LoadingActivity.SELECTED_CATEGORY_NAME.equals("전체")){
                for(int i = 0; i < categoryList.size(); i++){
                    if(categoryList.get(i).equals(data[7])){
                        add_select_group_spinner.setSelection(i); break;
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 200 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            TextView picture_text = findViewById(R.id.picture_text);
            picture_text.setText("");
            int width = ((LinearLayout)add_voca_select_picture_imageview.getParent().getParent()).getWidth();
            int height = ((LinearLayout)add_voca_select_picture_imageview.getParent()).getHeight();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    300, 300
            );
            params.gravity = Gravity.CENTER;
            add_voca_select_picture_imageview.getLayoutParams().width = width / 4;
            add_voca_select_picture_imageview.getLayoutParams().height = width / 4;
            //add_voca_select_picture_imageview.setLayoutParams(params);
            add_voca_select_picture_imageview.setImageURI(selectedImageUri);
        }
    }



    private boolean checkString(String str, Context context){

        Pattern pattern = Pattern.compile("[ \n!@#$%^&*(),.?\"\':{}|<>]");
        Matcher matcher = pattern.matcher(str);

        if(!matcher.find()) {
            return true;
        }
        else{
            Toast.makeText(context, "단어, 단어의 뜻, 단어의 발음 입력에 특수문자 및 공백을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private String changeChar(String str){

        String res = "";
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i) == '\''){
                res = res + "\"";
            }
            else res += str.charAt(i);
        }

        return res;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void add_edit_onBackClick(View view){
        finish();
    }
}
