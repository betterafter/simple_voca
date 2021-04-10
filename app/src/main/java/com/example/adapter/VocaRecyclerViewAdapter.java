package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Items.ListItem;
import com.example.activity.AddVocaActivity;
import com.example.activity.LoadingActivity;
import com.example.activity.MainActivity;
import com.example.simple_voca.ImageSerializer;
import com.example.simple_voca.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VocaRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int PARENT_VIEW = 0;
    private final int CHILD_VIEW = 1;

    private Context context;


    public ArrayList<ListItem> wordDataList;

    public VocaRecyclerViewAdapter(ArrayList<ListItem> wordDataList,  Context context){
        this.wordDataList = wordDataList;
        this.context = context;
    }


    // 아이템 뷰 저장
    public class ParentViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout add_voca;
        private TextView add_voca_word;
        private TextView add_voca_mean;
        private TextView add_voca_announce;

        private LinearLayout add_voca_editor;
        private ImageButton add_voca_editor_delete_button;
        private ImageButton add_voca_editor_edit_button;
        private ImageButton add_voca_editor_close_button;

        private boolean isExpanded = false;
        private int childPosition = -1;

        View itemView;

        public ParentViewHolder(@NonNull View itemView) {
            super(itemView);

            add_voca = itemView.findViewById(R.id.item);
            add_voca_word = itemView.findViewById(R.id.item_word);
            add_voca_mean = itemView.findViewById(R.id.item_mean);
            add_voca_announce = itemView.findViewById(R.id.item_announce);

            add_voca_editor = itemView.findViewById(R.id.item_word_editor);
            add_voca_editor_delete_button = itemView.findViewById(R.id.word_editor_delete);
            add_voca_editor_edit_button = itemView.findViewById(R.id.word_editor_edit);
            add_voca_editor_close_button = itemView.findViewById(R.id.word_editor_close);


            this.itemView = itemView;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (getAdapterPosition() + 1 < wordDataList.size()
                            && wordDataList.get(getAdapterPosition() + 1).getType() == CHILD_VIEW
                            && getItemViewType() == PARENT_VIEW) {
                        wordDataList.remove(getAdapterPosition() + 1);
                        notifyItemRemoved(getAdapterPosition() + 1);
                    }
                    else if ((getAdapterPosition() + 1 >= wordDataList.size()
                             || wordDataList.get(getAdapterPosition() + 1).getType() != CHILD_VIEW)
                             && getItemViewType() == PARENT_VIEW) {
                        String[] data = ((ListItem)wordDataList.get(getAdapterPosition())).getData();
                        wordDataList.add(getAdapterPosition() + 1, new ListItem(data, CHILD_VIEW));
                        notifyItemInserted(getAdapterPosition() + 1);
                    }

                    final int pos = getAdapterPosition();
                    System.out.println(pos);
                    MainActivity.main_recyclerView.scrollToPosition(pos);
                }
            });
        }
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {

        private TextView add_voca_example;
        private TextView add_voca_example_mean;
        private TextView add_voca_memo;
        private ImageView add_voca_image;

        View itemView;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);

            add_voca_image = itemView.findViewById(R.id.item_image);
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

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    viewHolder.add_voca_editor.setVisibility(View.VISIBLE);
                    // 여기서 delete button과 edit button에 대해 onClickListener 구현


                    viewHolder.add_voca_editor_close_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewHolder.add_voca_editor.setVisibility(View.GONE);
                        }
                    });

                    viewHolder.add_voca_editor_delete_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LoadingActivity.vocaDatabase.delete(position);
                            LoadingActivity.vocaDatabase.makeList(wordDataList);
                            notifyDataSetChanged();
                        }
                    });

                    viewHolder.add_voca_editor_edit_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, AddVocaActivity.class);
                            intent.putExtra("STATE", "EDIT");
                            intent.putExtra("POSITION", viewHolder.getAdapterPosition());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    });

                    return true;
                }
            });
        }
        else if(holder instanceof ChildViewHolder){
            ChildViewHolder viewHolder = (ChildViewHolder) holder;
            String[] data = ((ListItem)wordDataList.get(position)).getData();

            viewHolder.add_voca_example.setText(data[3]);
            viewHolder.add_voca_example_mean.setText(data[4]);
            viewHolder.add_voca_memo.setText(data[5]);
            viewHolder.add_voca_image.setImageDrawable(new BitmapDrawable(
                    context.getResources(), ImageSerializer.PackSerializedToImage(data[6])));

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });
        }
    }


    // 전체 데이터 개수 리턴. 수치 변경 안하면 리사이클러뷰에 안나타날 수 있음
    @Override
    public int getItemCount() {
        return wordDataList.size();
    }

    // 진짜 위치 구하는 방법. 나중에 써먹자.
    //public int getRealPosition(int position) { return position % mCount; }
}
