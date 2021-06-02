package com.danerdaner.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.danerdaner.simple_voca.CSVBuilder;
import com.danerdaner.simple_voca.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void settings_onBackClick(View view){
        finish();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            Uri uri = data.getData();
            String file;
            CSVBuilder csvBuilder = new CSVBuilder();

            InputStream inputStream = getContentResolver().openInputStream(uri);
            byte[] buffer = new byte[inputStream.available() + 2];
            inputStream.read(buffer);
            file = new String(buffer);
            System.out.println(file);
            csvBuilder.StringToDatabase(file);

            Toast.makeText(getApplicationContext(),
                    "성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //finish();
    }
}
