package com.danerdaner.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.danerdaner.simple_voca.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Setting_Developer_Info_Activity extends AppCompatActivity {

    ImageButton developer_info_back_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.developer_information);

        developer_info_back_button = findViewById(R.id.developer_info_back_button);
        developer_info_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
