package com.example.simple_voca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VocaRecyclerViewAdapter extends RecyclerView.Adapter<VocaRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String[]> wordDataList;

    public VocaRecyclerViewAdapter(ArrayList<String[]> wordDataList){
        this.wordDataList = wordDataList;
    }

    // 아이템 모션 구현
//    @Override
//    public boolean onItemMove(int form_position, int to_position) {
//
//        return true;
//    }
//
//    @Override
//    public void onItemSwipe(int position) {
//
//    }

    // 아이템 뷰 저장
    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView add_voca_word;
        private TextView add_voca_mean;
        private TextView add_voca_announce;
        //private TextView add_voca_example;
        //private TextView add_voca_example_mean;
        //private TextView add_voca_memo;

        View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            add_voca_word = itemView.findViewById(R.id.item_word);
            add_voca_mean = itemView.findViewById(R.id.item_mean);
            add_voca_announce = itemView.findViewById(R.id.item_announce);
            //add_voca_example = itemView.findViewById(R.id.item_example);
            //add_voca_example_mean = itemView.findViewById(R.id.item_example_mean);
            //add_voca_memo = itemView.findViewById(R.id.item_memo);

            this.itemView = itemView;
        }
    }

    // 뷰 홀더 객체 생성
    @NonNull
    @Override
    public VocaRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.voca_recyclerview_adapter_item, null);
        VocaRecyclerViewAdapter.ViewHolder viewHolder = new VocaRecyclerViewAdapter.ViewHolder(view);

        return viewHolder;
    }

    // 데이터를 뷰 홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull VocaRecyclerViewAdapter.ViewHolder holder, int position) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        holder.itemView.setLayoutParams(params);

        String[] data = wordDataList.get(position);
        holder.add_voca_word.setText(data[0]);
        holder.add_voca_mean.setText(data[1]);
        holder.add_voca_announce.setText(data[2]);
        //holder.add_voca_example.setText(data[3]);
        //holder.add_voca_example_mean.setText(data[4]);
        //holder.add_voca_memo.setText(data[5]);
    }

    // 전체 데이터 개수 리턴. 수치 변경 안하면 리사이클러뷰에 안나타날 수 있음
    @Override
    public int getItemCount() {
        return wordDataList.size();
    }
}
