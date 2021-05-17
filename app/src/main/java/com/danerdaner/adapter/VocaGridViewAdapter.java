package com.danerdaner.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.danerdaner.Items.ListItem;
import com.danerdaner.simple_voca.GridItemView;

import java.util.ArrayList;

public class VocaGridViewAdapter extends BaseAdapter {

    // getApplicationContext로 특정 부분의 context를 가져오기 위해서 생성자에서 받아오게 함.
    Context context;
    private ArrayList<ListItem> wordDataList;


    public VocaGridViewAdapter(ArrayList<ListItem> wordDataList, Context context){
        this.wordDataList = wordDataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return wordDataList.size();
    }

//    public void addItem(GridItem item){
//        wordDataList.add(item);
//    }

    @Override
    public Object getItem(int position) {
        return wordDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridItemView view = null;
        if(convertView == null) {
            view = new GridItemView(context);

        }else{
            view = (GridItemView) convertView;
        }

        ListItem item = wordDataList.get(position);
        view.setData(item);

        return view;
    }
}
