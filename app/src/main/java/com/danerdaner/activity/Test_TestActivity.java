package com.danerdaner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.danerdaner.simple_voca.R;
import com.danerdaner.simple_voca.TestAnswer;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Test_TestActivity extends AppCompatActivity {

    private TextView problem_number;
    private TextView problem_word;
    private TextView problem_announce;
    private TextView category_name;
    private String Category_name;

    private String test_type;

    private Button[] problem_answer = new Button[4];

    private Random r;

    // Pair 대용으로 사용한 SimpleEntry Class (Key, Value Type을 이용해서 pair 저장)
    private Stack<AbstractMap.SimpleEntry<Integer,String[]>> answers = new Stack<>();
    private Boolean[] corrects = new Boolean[20];

    private int currentNum=0;

    // 정답 답변 정보
    private ArrayList<TestAnswer> myAnswers = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_test);

        Intent intent = getIntent();
        ArrayList<String[]> TestList = (ArrayList<String[]>) intent.getSerializableExtra("list");
        test_type = intent.getStringExtra("test_type");
        Category_name = intent.getStringExtra("category_name");

        r = new Random(System.currentTimeMillis()); // Seed 설정

        category_name = findViewById(R.id.test_test_category_name);
        category_name.setText(Category_name);

        problem_number = findViewById(R.id.test_test_category_number);
        problem_word = findViewById(R.id.test_test_category_word);
        problem_announce = findViewById(R.id.test_test_category_announce);


        problem_answer[0] = findViewById(R.id.test_test_number_1);
        problem_answer[1] = findViewById(R.id.test_test_number_2);
        problem_answer[2] = findViewById(R.id.test_test_number_3);
        problem_answer[3] = findViewById(R.id.test_test_number_4);

        makeProblem(TestList,currentNum);

        // 테스트를 다 봤으면 Intent를 이용해 Test_ResultActivity로 넘어갈 것.
    }

    private void makeProblem(ArrayList<String[]> TestList, int problemNum){
        int answerNum = r.nextInt(4); // 정답 번호
        TestAnswer problemAnswer = new TestAnswer();
        String[] selections = new String[4];

        problem_number.setText((problemNum+1)+" / "+TestList.size());
        problem_word.setText(TestList.get(problemNum)[0]);
        problem_announce.setText("["  + TestList.get(problemNum)[2] +  "]");

        problemAnswer.setProblem(new String[]{TestList.get(problemNum)[0], TestList.get(problemNum)[2]});
        problemAnswer.setAnswer(answerNum);

        problem_answer[answerNum].setText(TestList.get(problemNum)[1]); // Answer Setting
        selections[answerNum] = TestList.get(problemNum)[1];

        // 정답 Button OnClick 설정
        if(problemNum < 19) {
            problem_answer[answerNum].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myAnswers.get(problemNum).setCorrect(true);
                    makeProblem(TestList, problemNum+1);
                }
            });
        }else{
            problem_answer[answerNum].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myAnswers.get(problemNum).setCorrect(true);

                    // 화면전환
                    Intent intent = new Intent(Test_TestActivity.this, Test_ResultActivity.class);

                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);

                    intent.putExtra("answers", myAnswers);
                    intent.putExtra("category_name", Category_name);
                    intent.putExtra("test_type", test_type);
                    startActivity(intent);
                }
            });
        }

        // 중복제거용 remove와 복원용 Stack 활용
        answers.push(new AbstractMap.SimpleEntry<>(problemNum, TestList.remove(problemNum)));

        for(int i=0;i<4; i++){ // 정답인 버튼만 빼고 설정
            if(i == answerNum)
                continue;
            // n = 선택지로 쓰일 단어 인덱스
            int n = r.nextInt(TestList.size());
            problem_answer[i].setText(TestList.get(n)[1]);
            selections[i] = TestList.get(n)[1];

            // 오답 Button OnClick 설정
            if(problemNum < 19) {
                problem_answer[i].setOnClickListener(new WrongAnswer(problemNum,i,TestList));
            }else{
                problem_answer[i].setOnClickListener(new WrongAnswerLast(problemNum,i,TestList));
            }

            // 중복제거용 remove와 복원용 Stack 활용
            answers.push(new AbstractMap.SimpleEntry<>(n,TestList.remove(n)));
        }

        while(!answers.isEmpty()){
            AbstractMap.SimpleEntry<Integer,String[]> toPut = answers.pop();
            TestList.add(toPut.getKey(),toPut.getValue());
        }

        problemAnswer.setSelects(selections);
        myAnswers.add(problemAnswer);
    }

    private class WrongAnswer implements View.OnClickListener {
        private int problemNum;
        private int selectionNum;
        private ArrayList<String[]> TestList;

        public WrongAnswer(int problemNum, int selectionNum, ArrayList<String[]> TestList){
            this.problemNum = problemNum;
            this.selectionNum = selectionNum;
            this.TestList = TestList;
        }

        @Override
        public void onClick(View view) {
            myAnswers.get(problemNum).setCorrect(false);
            myAnswers.get(problemNum).setWrongAnswer(selectionNum);
            makeProblem(TestList, problemNum + 1);
        }
    }

    private class WrongAnswerLast implements View.OnClickListener {
        private int problemNum;
        private int selectionNum;
        private ArrayList<String[]> TestList;

        public WrongAnswerLast(int problemNum, int selectionNum, ArrayList<String[]> TestList){
            this.problemNum = problemNum;
            this.selectionNum = selectionNum;
            this.TestList = TestList;
        }

        @Override
        public void onClick(View view) {
            myAnswers.get(problemNum).setCorrect(false);
            myAnswers.get(problemNum).setWrongAnswer(selectionNum);

            // 화면전환
            Intent intent = new Intent(Test_TestActivity.this, Test_ResultActivity.class);

            intent.putExtra("answers", myAnswers);
            intent.putExtra("category_name", Category_name);
            intent.putExtra("test_type", test_type);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
