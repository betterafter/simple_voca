package com.example.Items;

public class ScoreListItem implements Comparable {
    public int score;
    public String category;
    private String date;
    private int[] params;

    public ScoreListItem(String score, String category, String date){
        setData(date);
        this.score = Integer.parseInt(score);
        this.category = category;
        this.date = date;
    }

    public String getDate(){
        return date;
    }

    public void setData(String date){
        String[] paramsString = date.split("-");
        params = new int[paramsString.length];
        for(int i = 0; i < paramsString.length; i ++){
            params[i] = Integer.parseInt(paramsString[i]);
        }
    }

    public int getScore() {
        return score;
    }

    public int[] getParams(){
        return params;
    }

    @Override
    public int compareTo(Object o) {
        ScoreListItem target = (ScoreListItem)o;
        int[] targetParams = target.getParams();
        for(int i = 0; i < targetParams.length; i ++){
            if(targetParams[i] != params[i]){
                return params[i] - targetParams[i];
            }
        }
        return 0;
    }
}
