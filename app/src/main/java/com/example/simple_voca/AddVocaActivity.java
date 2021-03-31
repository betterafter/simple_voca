package com.example.simple_voca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddVocaActivity extends AppCompatActivity {

    private TextView add_voca_select_group;
    private TextInputEditText add_voca_word;
    private TextInputEditText add_voca_mean;
    private TextInputEditText add_voca_announce;
    private TextInputEditText add_voca_example;
    private TextInputEditText add_voca_example_mean;
    private TextInputEditText add_voca_memo;
    private ImageButton add_voca_select_picture_button;

    private Button add_voca_save_button;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voca);

        add_voca_select_group = findViewById(R.id.add_voca_select_group);
        add_voca_word = findViewById(R.id.add_voca_word);
        add_voca_mean = findViewById(R.id.add_voca_mean);
        add_voca_announce = findViewById(R.id.add_voca_announce);
        add_voca_example = findViewById(R.id.add_voca_example);
        add_voca_example_mean = findViewById(R.id.add_voca_example_mean);
        add_voca_memo = findViewById(R.id.add_voca_memo);
        add_voca_select_picture_button = findViewById(R.id.add_voca_select_picture_button);

        add_voca_save_button = findViewById(R.id.add_voca_save_button);
        add_voca_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = add_voca_word.getText().toString();
                String mean = add_voca_mean.getText().toString();
                String announce = add_voca_announce.getText().toString();
                String example = add_voca_example.getText().toString();
                String example_mean = add_voca_example_mean.getText().toString();
                String memo = add_voca_memo.getText().toString();

                LoadingActivity.vocaDatabase.insert(word, mean, announce, example, example_mean, memo);
                LoadingActivity.vocaDatabase.makeList(LoadingActivity.vocaList);

                Intent intent = new Intent(AddVocaActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
