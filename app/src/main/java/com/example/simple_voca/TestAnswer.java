package com.example.simple_voca;

import java.io.Serializable;

public class TestAnswer implements Serializable {
    private String[] problem; // 0:단어, 1:발음
    private boolean isCorrect;
    private String[] selects; // 선택지 1~4
    private int answer;
    private int wrongAnswer; // 정답이면 안넣음

    public String[] getProblem() {
        return problem;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public String[] getSelects() {
        return selects;
    }

    public int getAnswer() {
        return answer;
    }

    public int getWrongAnswer() {
        return wrongAnswer;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public void setWrongAnswer(int wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    public void setProblem(String[] problem) {
        this.problem = problem;
    }

    public void setSelects(String[] selects) {
        this.selects = selects;
    }
}
