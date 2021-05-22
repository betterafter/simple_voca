package com.danerdaner.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.danerdaner.Items.ScoreListItem;
import com.danerdaner.simple_voca.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class Test_MainActivity extends AppCompatActivity {

    private LinearLayout important_word_test_button;
    private LinearLayout all_word_test_button;
    private String Category_Name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);

        Intent intent = getIntent();
        Category_Name = intent.getStringExtra("category_name");
        ArrayList<String[]> testList = LoadingActivity.vocaDatabase.makeTestList(Category_Name);
        ArrayList<String[]> importantTestList = LoadingActivity.vocaDatabase.makeImportantTestList(Category_Name);

        TextView category_name_text = findViewById(R.id.test_main_category_name);
        category_name_text.setText(Category_Name);

        all_word_test_button = findViewById(R.id.all_word_test_button);
        all_word_test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(testList.size() < 20){
                    Toast.makeText(getApplicationContext(),
                            "전체 단어의 개수가 최소 20개일 때만 테스트를 실행할 수 있습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(Test_MainActivity.this, Test_TestActivity.class);
                    intent.putExtra("list", testList);
                    intent.putExtra("category_name", Category_Name);
                    intent.putExtra("test_type", "ALL");
                    startActivity(intent);
                }
            }
        });

        makeChart();

        important_word_test_button = findViewById(R.id.important_word_test_button);
        important_word_test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(importantTestList.size() < 20){
                    Toast.makeText(getApplicationContext(),
                            "중요 단어의 개수가 최소 20개일 때만 테스트를 실행할 수 있습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(Test_MainActivity.this, Test_TestActivity.class);
                    intent.putExtra("list", importantTestList);
                    intent.putExtra("category_name", Category_Name);
                    intent.putExtra("test_type", "IMPORTANT");
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        makeChart();
    }

    private void makeChart(){

        LoadingActivity.ScoreDatabase.makeCategoryList(LoadingActivity.categoryTestResultList, Category_Name);
        ArrayList<ScoreListItem> scoreList = LoadingActivity.categoryTestResultList;
        //String[] dateList = new String[10];
        ArrayList<String> YScoreList = new ArrayList<>();
        ArrayList<String> countList = new ArrayList<>();

        LineChart lineChart = (LineChart)findViewById(R.id.test_main_chart);

        List<Entry> entries = new ArrayList<>();
        for(int i = 0; i < scoreList.size(); i++){
            entries.add(new Entry(i , scoreList.get(i).getScore()));
            //dateList[i] = scoreList.get(i).getDate();
            //System.out.println("date : " + dateList[i] + " ");
        }

        for(int i = 1; i <= 10; i++){
            YScoreList.add(Integer.toString((i + 1) * 10));
            countList.add(i + "회");
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "점수");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(ResourcesCompat.getColor(getResources(), R.color.mainBlue, null));
        lineDataSet.setCircleHoleColor(Color.WHITE);
        lineDataSet.setColor(ResourcesCompat.getColor(getResources(), R.color.mainBlue, null));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(countList));
        //xAxis.setLabelCount(10);


        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setAxisMinimum(0);
        yLAxis.setAxisMaximum(100);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");
        lineChart.setVisibleXRangeMaximum(10);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(1000, Easing.EaseInCubic);
        lineChart.invalidate();
    }

    public void test_main_onBackClick(View view){
        Intent intent = new Intent(Test_MainActivity.this, Category_MainActivity.class);
        startActivity(intent);
        finish();
    }


}
