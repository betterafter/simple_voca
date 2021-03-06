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
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

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
    private TextInputLayout err1, err2;

    private ArrayList<String> categoryList;

    private final String INLINE = ",";
    private final String LINER = "%%@@%@@%%@@%";
    private final String SMALL_QUOTATION_MARK = "@@@!!!###%%%";
    private final String BIG_QUOTATION_MARK = "%%%##@#@@#@!!!";



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

        err1 = findViewById(R.id.add_voca_word_layout);
        err2 = findViewById(R.id.add_voca_mean_layout);



        add_voca_select_picture_imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 200);
            }
        });

        // ?????? ?????? ??????
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


                if(word.length() <= 0){
                    Toast.makeText(getApplicationContext(),
                            "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mean.length() <= 0){
                    Toast.makeText(getApplicationContext(),
                            "????????? ?????? ??????????????????.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!checkString(word, getApplicationContext())) return;
                if(!checkString(mean, getApplicationContext())) return;
                if(!checkString(announce, getApplicationContext())) return;
                if(!checkString(example, getApplicationContext())) return;
                if(!checkString(example_mean, getApplicationContext())) return;
                if(!checkString(memo, getApplicationContext())) return;
                if(!checkString(group, getApplicationContext())) return;

                if(SAVE_STATE.equals("SAVE")) {

                    if(LoadingActivity.vocaDatabase.CheckIfWordInCategory(word, group, POSITION)){
                        Toast.makeText(getApplicationContext(),
                                "?????? ??????????????? ?????? ?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // ????????????????????? ??????
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

                    if(!LoadingActivity.vocaShuffleLists.containsKey(group)){
                        LoadingActivity.vocaDatabase.makeShuffleList(group);
                    }
                    // ????????? ?????? ??? ?????? ?????? ????????????
                    LoadingActivity.vocaShuffleLists.get(group).add(new ListItem(data, 0));

                    // ?????? ?????? ??? ????????? ?????? ????????????
                    LoadingActivity.vocaShuffleLists.get("??????").add(new ListItem(data, 0));
                    LoadingActivity.vocaDatabase.makeList(LoadingActivity.vocaList);

                }
                else if(SAVE_STATE.equals("EDIT") && POSITION >= 0){

                    if(LoadingActivity.vocaDatabase.CheckIfWordInCategory(word, group, POSITION)){
                        Toast.makeText(getApplicationContext(),
                                "?????? ??????????????? ?????? ?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
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


                    // ?????? shuffle?????? ????????? ?????? ????????????
                    ArrayList<ListItem> allArrayList = new ArrayList<>();
                    allArrayList.addAll(LoadingActivity.vocaShuffleLists.get("??????"));
                    for(int i = 0; i < allArrayList.size(); i++){
                        if(allArrayList.get(i).getData()[0].equals(prevWord) &&
                                allArrayList.get(i).getData()[7].equals(prevGroup)){
                            String[] data = new String[]{
                                    word, mean, announce, example, example_mean, mean,
                                    ImageSerializer.PackImageToSerialized(add_voca_select_picture_imageview),
                                    group, "0"
                            };
                            allArrayList.remove(i);
                            allArrayList.add(i, new ListItem(data, 0));

                            LoadingActivity.vocaShuffleLists.put("??????", allArrayList);
                            break;
                        }
                    }

                    // shuffle?????? ????????? ?????? ????????????
                    ArrayList<ListItem> specArrayList = new ArrayList<>();
                    specArrayList.addAll(LoadingActivity.vocaShuffleLists.get(group));
                    for(int i = 0; i < specArrayList.size(); i++){
                        if(specArrayList.get(i).getData()[0].equals(prevWord) &&
                                specArrayList.get(i).getData()[7].equals(prevGroup)){
                            String[] data = new String[]{
                                    word, mean, announce, example, example_mean, mean,
                                    ImageSerializer.PackImageToSerialized(add_voca_select_picture_imageview),
                                    group, "0"
                            };
                            specArrayList.remove(i);
                            specArrayList.add(i, new ListItem(data, 0));
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
            String[] data = LoadingActivity.vocaDatabase.findTableData(POSITION);


            add_voca_title.setText("?????? ??????");
            add_voca_save_button.setText("?????? ????????????");

            data[0] = change_code_to_small_quotation_mark(data[0]);
            data[1] = change_code_to_small_quotation_mark(data[1]);
            data[2] = change_code_to_small_quotation_mark(data[2]);

            data[3] = change_code_to_small_quotation_mark(data[3]);
            data[4] = change_code_to_small_quotation_mark(data[4]);
            data[5] = change_code_to_small_quotation_mark(data[5]);


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

            // ?????? ?????????????????? ?????? ????????? ????????? ?????? ??? ????????? ?????? ?????? ?????? ???????????? ????????? ????????????.
            if(LoadingActivity.SELECTED_CATEGORY_NAME.equals("??????")){
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


        if(!str.contains(LINER)) {
            return true;
        }
        else{
            Toast.makeText(context, "???????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private String change_code_to_small_quotation_mark(String str){
        str = str.replaceAll(SMALL_QUOTATION_MARK, "'");
        str = str.replaceAll(BIG_QUOTATION_MARK, "\"");
        return str;
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
