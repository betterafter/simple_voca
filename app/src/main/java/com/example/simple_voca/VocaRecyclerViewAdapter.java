package com.example.simple_voca;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Items.ListItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VocaRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int PARENT_VIEW = 0;
    private final int CHILD_VIEW = 1;


    private ArrayList<ListItem> wordDataList;
    private SparseBooleanArray selectedId = new SparseBooleanArray();

    public VocaRecyclerViewAdapter(ArrayList<ListItem> wordDataList){
        this.wordDataList = wordDataList;
    }


    // 아이템 뷰 저장
    public class ParentViewHolder extends RecyclerView.ViewHolder{

        private TextView add_voca_word;
        private TextView add_voca_mean;
        private TextView add_voca_announce;
        private LinearLayout expandable_layout;

        public boolean isExpanded = false;

        View itemView;

        public ParentViewHolder(@NonNull View itemView) {
            super(itemView);

            add_voca_word = itemView.findViewById(R.id.item_example);
            add_voca_mean = itemView.findViewById(R.id.item_memo);
            add_voca_announce = itemView.findViewById(R.id.item_example_mean);

            this.itemView = itemView;
        }
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {

        private TextView add_voca_example;
        private TextView add_voca_example_mean;
        private TextView add_voca_memo;

        View itemView;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);

            add_voca_example = itemView.findViewById(R.id.item_example);
            add_voca_example_mean = itemView.findViewById(R.id.item_example_mean);
            add_voca_memo = itemView.findViewById(R.id.item_memo);

            this.itemView = itemView;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return wordDataList.get(position).getType();
    }



    // 뷰 홀더 객체 생성
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RecyclerView.ViewHolder viewHolder = null;

        switch(viewType){
            case PARENT_VIEW:
                View view = inflater.inflate(R.layout.voca_recyclerview_adapter_item, parent, false);
                viewHolder = new ParentViewHolder(view);
                break;
            case CHILD_VIEW:
                View subview = inflater.inflate(R.layout.voca_recyclerview_adapter_child_item, parent, false);
                viewHolder = new ChildViewHolder(subview);
                break;
        }

        return viewHolder;
    }

    // 데이터를 뷰 홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        holder.itemView.setLayoutParams(params);

        if(holder instanceof ParentViewHolder){
            ParentViewHolder viewHolder = (ParentViewHolder) holder;
            String[] data = ((ListItem)wordDataList.get(position)).getData();
            viewHolder.add_voca_word.setText(data[0]);
            viewHolder.add_voca_mean.setText(data[1]);
            viewHolder.add_voca_announce.setText(data[2]);



            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    System.out.println(getParentPosition(position) + " ---> "  + position);
                    System.out.println(wordDataList.toString());
                    System.out.println(viewHolder.getAdapterPosition());

                    int currentPosition = viewHolder.getAdapterPosition();

                    if(viewHolder.isExpanded && viewHolder.getItemViewType() == PARENT_VIEW){

                        viewHolder.isExpanded = false;
                        wordDataList.remove(currentPosition + 1);
                        notifyItemRemoved(currentPosition + 1);
                    }

                    else if(!viewHolder.isExpanded && viewHolder.getItemViewType() == PARENT_VIEW){

                        viewHolder.isExpanded = true;
                        wordDataList.add(currentPosition + 1, new ListItem(new String[]{"","","","","",""}, CHILD_VIEW));
                        notifyItemInserted(currentPosition + 1);
                    }
                }
            });
        }
        else if(holder instanceof ChildViewHolder){
            ChildViewHolder viewHolder = (ChildViewHolder) holder;
            String[] data = ((ListItem)wordDataList.get(position)).getData();

            viewHolder.add_voca_example.setText(data[3]);
            viewHolder.add_voca_example_mean.setText(data[4]);
            viewHolder.add_voca_memo.setText(data[5]);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    System.out.println(getParentPosition(position) + " ---> "  + position);
                    System.out.println(wordDataList.toString());
                }
            });

        }


    }

    private int getParentPosition(int position){
        while(true){
            if(wordDataList.get(position).getType() == PARENT_VIEW){
                break;
            }
            else position--;
        }
        return position;
    }

    // 전체 데이터 개수 리턴. 수치 변경 안하면 리사이클러뷰에 안나타날 수 있음
    @Override
    public int getItemCount() {
        return wordDataList.size();
    }
}
