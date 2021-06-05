package com.danerdaner.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.danerdaner.Items.ListItem;
import com.danerdaner.activity.AddEditVocaActivity;
import com.danerdaner.activity.LoadingActivity;
import com.danerdaner.activity.MainActivity;
import com.danerdaner.database.VocaDatabase;
import com.danerdaner.simple_voca.ImageSerializer;
import com.danerdaner.simple_voca.ItemTouchHelperCallback;
import com.danerdaner.simple_voca.R;
import com.danerdaner.simple_voca.VocaForegroundService;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VocaRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int PARENT_VIEW = 0;
    private final int CHILD_VIEW = 1;

    private Context context;
    private Activity activity;

    private Drawable drawable;


    public ArrayList<ListItem> wordDataList;

    public VocaRecyclerViewAdapter(ArrayList<ListItem> wordDataList, Context context, Activity activity){
        this.wordDataList = wordDataList;
        this.context = context;
        this.activity = activity;
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
        private ImageButton add_voca_anounce_button;
        private ImageView add_voca_bookmark;
        private LinearLayout add_voca_category_name;
        private LinearLayout add_item_editor;

        private boolean isExpanded = false;
        private int childPosition = -1;

        public View itemView;

        public String[] getData(){
            return new String[]{add_voca_word.getText().toString(), add_voca_mean.getText().toString()};
        }


        public ParentViewHolder(@NonNull View itemView) {
            super(itemView);

            add_voca = itemView.findViewById(R.id.item);
            add_voca_word = itemView.findViewById(R.id.item_word);
            add_voca_mean = itemView.findViewById(R.id.item_mean);
            add_voca_announce = itemView.findViewById(R.id.item_announce);
            add_voca_bookmark = itemView.findViewById(R.id.item_bookmark);
            add_voca_category_name = itemView.findViewById(R.id.item_category_name);
            add_item_editor = itemView.findViewById(R.id.item_word_editor);

            if(LoadingActivity.SELECTED_CATEGORY_NAME.equals("전체"))
                add_voca_category_name.setVisibility(View.VISIBLE);
            else add_voca_category_name.setVisibility(View.GONE);

            add_voca_editor = itemView.findViewById(R.id.item_word_editor);
            add_voca_editor_delete_button = itemView.findViewById(R.id.word_editor_delete);
            add_voca_editor_edit_button = itemView.findViewById(R.id.word_editor_edit);
            add_voca_editor_close_button = itemView.findViewById(R.id.word_editor_close);
            add_voca_anounce_button = itemView.findViewById(R.id.item_word_announce_button);

            this.itemView = itemView;
            final LinearLayout layout = itemView.findViewById(R.id.item);
            drawable = layout.getBackground();

            add_voca_anounce_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.tts.speak(add_voca_word.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(ItemTouchHelperCallback.viewHolders.size() > 0) {
                        notifyItemChanged(ItemTouchHelperCallback.viewHolders.get(0).getViewHolder().getAdapterPosition());
                        ItemTouchHelperCallback.viewHolders.remove(0);
                    }

                    else {
                        // 뷰 축소
                        if (getAdapterPosition() + 1 < wordDataList.size()
                                && wordDataList.get(getAdapterPosition() + 1).getType() == CHILD_VIEW
                                && getItemViewType() == PARENT_VIEW) {

                            wordDataList.remove(getAdapterPosition() + 1);
                            notifyItemChanged(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition() + 1);
                        }

                        // 뷰 확장
                        else if ((getAdapterPosition() + 1 >= wordDataList.size()
                                || wordDataList.get(getAdapterPosition() + 1).getType() != CHILD_VIEW)
                                && getItemViewType() == PARENT_VIEW) {

                            String[] data = ((ListItem) wordDataList.get(getAdapterPosition())).getData();
                            wordDataList.add(getAdapterPosition() + 1, new ListItem(data, CHILD_VIEW));
                            notifyItemChanged(getAdapterPosition());
                            notifyItemInserted(getAdapterPosition() + 1);
                        }

                        final int pos = getAdapterPosition();
                        System.out.println(pos);
                        //MainActivity.main_recyclerView.scrollToPosition(pos);

                        // 터치했을때 뷰 확장되면서 해당 포지션으로 스무스하게 이동함
                        MainActivity.smoothScroller.setTargetPosition(pos);
                        ((LinearLayoutManager) MainActivity.main_recyclerView.
                                getLayoutManager()).startSmoothScroll(MainActivity.smoothScroller);
                    }
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
            if(!MainActivity.WORD_MEAN_VISIBLE) viewHolder.add_voca_mean.setVisibility(View.INVISIBLE);
            else viewHolder.add_voca_mean.setVisibility(View.VISIBLE);
            viewHolder.add_voca_announce.setText("[" + data[2] + "]");
            ((TextView)viewHolder.add_voca_category_name.getChildAt(0)).setText(data[7]);

            int color=viewHolder.add_voca_mean.getCurrentTextColor();
            String hexColor = String.format("#%06X", (0xFFFFFF & color));

            // 다크모드 아닐 경우
            if(hexColor.equals("#000000")){
                viewHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
                viewHolder.add_item_editor.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
            else{
                viewHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.black));
                viewHolder.add_item_editor.setBackgroundColor(context.getResources().getColor(R.color.black));
            }

            int font_size = Integer.parseInt(LoadingActivity.sharedPreferences.getString("font_size", "24"));
            viewHolder.add_voca_word.setTextSize(font_size);

            if(wordDataList.size() > position + 1 && wordDataList.get(position + 1).getType() == CHILD_VIEW) {
                viewHolder.itemView.findViewById(R.id.item).setBackground(null);
            }
            else{
                viewHolder.itemView.findViewById(R.id.item).setBackground(drawable);
            }

            // 북마크 표시
            if(data[8].equals(VocaDatabase.importantFlag)){
                viewHolder.add_voca_bookmark.setVisibility(View.VISIBLE);
            }
            else{
                viewHolder.add_voca_bookmark.setVisibility(View.GONE);
            }

            // 외운단어 표시
            if(data[8].equals(VocaDatabase.remindFlag)){
                viewHolder.add_voca_word.setPaintFlags(viewHolder.add_voca_word.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else{
                viewHolder.add_voca_word.setPaintFlags(0);
            }

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    removeAllChildView();
                    viewHolder.add_voca_editor.setVisibility(View.VISIBLE);
                    // 여기서 delete button과 edit button에 대해 onClickListener 구현

                    // 닫기 버튼
                    viewHolder.add_voca_editor_close_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            notifyItemChanged(position);

                            viewHolder.add_voca_editor.setVisibility(View.GONE);
                        }
                    });

                    // 단어 삭제 버튼
                    viewHolder.add_voca_editor_delete_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String word = viewHolder.add_voca_word.getText().toString();
                            String category = data[7];

                            AlertDialog.Builder dlg
                                    = new AlertDialog.Builder(activity);
                            dlg.setTitle("단어 삭제"); //제목
                            dlg.setMessage(word + "를 삭제하시겠습니까?"); // 메시지

                            dlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                            dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int index = LoadingActivity.vocaDatabase.findTableIndex(word, category);
                                    LoadingActivity.vocaDatabase.delete(index);
                                    for(int ii = 0; ii < LoadingActivity.vocaShuffleList.size(); ii++){
                                        if(LoadingActivity.vocaShuffleList.get(ii).getData()[0].equals(word) &&
                                                LoadingActivity.vocaShuffleList.get(ii).getData()[7].equals(category)){
                                            LoadingActivity.vocaShuffleList.remove(ii);
                                            break;
                                        }
                                    }
                                    LoadingActivity.vocaDatabase.makeList(wordDataList);
                                    notifyDataSetChanged();

                                    // 단어를 삭제했을 때 단어의 개수가 0개라면 서비스도 종료하고 서비스활성화도 체크 해제해야 한다.
                                    Intent intent = new Intent(context, VocaForegroundService.class);
                                    LoadingActivity.vocaDatabase.UnCheckedIfNoWordInTable();
                                    context.getApplicationContext().stopService(intent);

                                    MainActivity.MakeListPager();
                                    MainActivity.onRecyclerViewScrollListener(MainActivity.main_recyclerView);
                                    viewHolder.add_voca_editor.setVisibility(View.GONE);
                                }
                            });

                            AlertDialog alertDialog = dlg.create();
                            alertDialog.show();
                        }
                    });

                    // 단어 편집 버튼
                    viewHolder.add_voca_editor_edit_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            removeAllChildView();

                            Intent intent = new Intent(context, AddEditVocaActivity.class);
                            String word = viewHolder.add_voca_word.getText().toString();
                            String category = data[7];
                            int index = LoadingActivity.vocaDatabase.findTableIndex(word, category);
                            intent.putExtra("STATE", "EDIT");
                            intent.putExtra("POSITION", index);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                            viewHolder.add_voca_editor.setVisibility(View.GONE);
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


    public void removeAllChildView(){
        for(int i = 0; i < wordDataList.size(); i++){
            if (wordDataList.get(i).getType() == CHILD_VIEW) {
                wordDataList.remove(i);
                notifyItemRemoved(i);
            }
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