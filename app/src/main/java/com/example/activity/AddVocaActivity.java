package com.example.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simple_voca.ImageSerializer;
import com.example.simple_voca.R;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class AddVocaActivity extends AppCompatActivity {

    private TextView add_voca_select_group;
    private TextInputEditText add_voca_word;
    private TextInputEditText add_voca_mean;
    private TextInputEditText add_voca_announce;
    private TextInputEditText add_voca_example;
    private TextInputEditText add_voca_example_mean;
    private TextInputEditText add_voca_memo;
    private ImageView add_voca_select_picture_imageview;
    private TextView add_select_group_textview;

    private Button add_voca_save_button;
    private String SAVE_STATE = "SAVE";
    private int POSITION = -1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voca);

        SAVE_STATE = "SAVE";
        POSITION = -1;

        add_voca_select_group = findViewById(R.id.add_voca_select_group);
        add_voca_word = findViewById(R.id.add_voca_word);
        add_voca_mean = findViewById(R.id.add_voca_mean);
        add_voca_announce = findViewById(R.id.add_voca_announce);
        add_voca_example = findViewById(R.id.add_voca_example);
        add_voca_example_mean = findViewById(R.id.add_voca_example_mean);
        add_voca_memo = findViewById(R.id.add_voca_memo);
        add_voca_select_picture_imageview = findViewById(R.id.add_voca_select_picture_imageview);
        add_select_group_textview = findViewById(R.id.add_voca_select_group);

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
                String group = add_voca_select_group.getText().toString();

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
                    LoadingActivity.vocaDatabase.makeList(LoadingActivity.vocaList);
                }

                SAVE_STATE = "SAVE";
                Intent intent = new Intent(AddVocaActivity.this, MainActivity.class);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 200 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            add_voca_select_picture_imageview.setImageURI(selectedImageUri);
        }

    }
}
