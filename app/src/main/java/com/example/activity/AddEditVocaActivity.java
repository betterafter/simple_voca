package com.example.activity;

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

import com.example.simple_voca.ImageSerializer;
import com.example.simple_voca.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditVocaActivity extends AppCompatActivity {

    private TextView add_voca_select_group;
    private TextInputEditText add_voca_word;
    private TextInputEditText add_voca_mean;
    private TextInputEditText add_voca_announce;
    private TextInputEditText add_voca_example;
    private TextInputEditText add_voca_example_mean;
    private TextInputEditText add_voca_memo;
    private ImageView add_voca_select_picture_imageview;
    private Spinner add_select_group_spinner;
    private ImageButton add_voca_word_search_button;



    private Button add_voca_save_button;
    private String SAVE_STATE = "SAVE";
    private int POSITION = -1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voca);

        SAVE_STATE = "SAVE";
        POSITION = -1;

        ArrayList<String> categoryList = new ArrayList<>();
        for(int i = 0; i < LoadingActivity.categoryList.size(); i++){
            categoryList.add(LoadingActivity.categoryList.get(i).getData()[0]);
        }


        ArrayAdapter<String>  arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, categoryList);
        add_select_group_spinner = findViewById(R.id.add_voca_select_group_spinner);
        add_select_group_spinner.setAdapter(arrayAdapter);

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

                if(SAVE_STATE.equals("SAVE")) {
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
                    // "전체" 카테고리에도 넣기
                    if(!group.equals("전체")) {
                        LoadingActivity.vocaDatabase.insert(
                                word,
                                mean,
                                announce,
                                example,
                                example_mean,
                                memo,
                                ImageSerializer.PackImageToSerialized(add_voca_select_picture_imageview),
                                "전체",
                                "0");
                    }

                    LoadingActivity.vocaDatabase.makeList(LoadingActivity.vocaList);
                }
                else if(SAVE_STATE.equals("EDIT") && POSITION >= 0){
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
                    // 전체 카테고리도 업데이트
                    if(!group.equals("전체")) {
                        LoadingActivity.vocaDatabase.change(
                                POSITION,
                                word,
                                mean,
                                announce,
                                example,
                                example_mean,
                                memo,
                                ImageSerializer.PackImageToSerialized(add_voca_select_picture_imageview),
                                "전체",
                                "0");
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
        Intent intent = getIntent();
        if(intent.getStringExtra("STATE") != null) {
            SAVE_STATE = intent.getStringExtra("STATE");
            POSITION = intent.getIntExtra("POSITION", -1);
        }

        if(SAVE_STATE.equals("EDIT")){
            String[] data = LoadingActivity.vocaList.get(POSITION).getData();

            add_voca_word.setText(data[0]);
            add_voca_mean.setText(data[1]);
            add_voca_announce.setText(data[2]);
            add_voca_example.setText(data[3]);
            add_voca_example_mean.setText(data[4]);
            add_voca_memo.setText(data[5]);
            add_voca_select_picture_imageview.setImageDrawable(new BitmapDrawable(
                    getApplicationContext().getResources(), ImageSerializer.PackSerializedToImage(data[6])));

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 200 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            TextView picture_text = findViewById(R.id.picture_text);
            picture_text.setText("");
            int width = ((LinearLayout)add_voca_select_picture_imageview.getParent()).getWidth();
            int height = ((LinearLayout)add_voca_select_picture_imageview.getParent()).getHeight();
            System.out.println(width);
            System.out.println(height);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    300, 300
            );
            params.gravity = Gravity.CENTER;
            add_voca_select_picture_imageview.getLayoutParams().width = width / 2;
            add_voca_select_picture_imageview.getLayoutParams().height = width / 2;
            //add_voca_select_picture_imageview.setLayoutParams(params);
            add_voca_select_picture_imageview.setImageURI(selectedImageUri);

            System.out.println(add_voca_select_picture_imageview.getWidth());
            System.out.println(add_voca_select_picture_imageview.getHeight());
        }

    }
}
