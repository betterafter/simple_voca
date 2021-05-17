package com.danerdaner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.danerdaner.activity.LoadingActivity;
import com.danerdaner.simple_voca.R;
import com.danerdaner.simple_voca.TestAnswer;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class TestResultViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<TestAnswer> answerList;
    private Context context;
    private String test_type;
    private String category_name;

    public TestResultViewAdapter(ArrayList<TestAnswer> answerList, Context context, String category_name, String test_type){
        this.answerList = answerList;
        this.context = context;
        this.category_name = category_name;
        this.test_type = test_type;

        for(int i=answerList.size()-1;i>=0;i--){
            TestAnswer t = answerList.get(i);
            if(t.isCorrect())
                answerList.remove(i);
        }
        notifyDataSetChanged();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        private TextView test_result_word;
        private TextView test_result_announce;
        private Button[] test_result_item;
        private ImageButton test_result_bookmark;

        private String bookmark_state = "0";

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            test_result_item = new Button[4];

            test_result_word = itemView.findViewById(R.id.test_result_word);
            test_result_announce = itemView.findViewById(R.id.test_result_announce);
            test_result_item[0] = itemView.findViewById(R.id.test_result_item1);
            test_result_item[1] = itemView.findViewById(R.id.test_result_item2);
            test_result_item[2] = itemView.findViewById(R.id.test_result_item3);
            test_result_item[3] = itemView.findViewById(R.id.test_result_item4);

            test_result_bookmark = itemView.findViewById(R.id.item_result_bookmark);
            if(test_type.equals("IMPORTANT")) test_result_bookmark.setVisibility(View.INVISIBLE);
            else
                test_result_bookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(bookmark_state.equals("0")) {
                            LoadingActivity.vocaDatabase.changeBookmarkState(
                                    test_result_word.getText().toString(),
                                    category_name,
                                    "IMPORTANT"
                                    );
                            bookmark_state = "IMPORTANT";
                            test_result_bookmark.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                                    R.drawable.baseline_bookmark_24, null));
                        }
                        else{
                            LoadingActivity.vocaDatabase.changeBookmarkState(
                                    test_result_word.getText().toString(),
                                    category_name,
                                    "0"
                            );
                            bookmark_state = "0";
                            test_result_bookmark.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                                    R.drawable.baseline_bookmark_border_24, null));
                        }
                    }
                });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RecyclerView.ViewHolder viewHolder = null;
        View view = inflater.inflate(R.layout.test_result_item, parent, false);
        viewHolder = new TestResultViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TestAnswer ans = answerList.get(position);
        TestResultViewAdapter.ViewHolder viewHolder = (TestResultViewAdapter.ViewHolder)holder;

        viewHolder.test_result_word.setText(ans.getProblem()[0]);
        viewHolder.test_result_announce.setText("[" + ans.getProblem()[1] + "]");
        for(int i=0;i<4;i++)
            viewHolder.test_result_item[i].setText(ans.getSelects()[i]);
        viewHolder.test_result_item[ans.getAnswer()].setBackgroundColor(context.getResources().getColor(R.color.mainBlue));
        viewHolder.test_result_item[ans.getAnswer()].setTextColor(context.getResources().getColor(R.color.white));
        viewHolder.test_result_item[ans.getWrongAnswer()].setBackgroundColor(context.getResources().getColor(R.color.mainRed));
        viewHolder.test_result_item[ans.getWrongAnswer()].setTextColor(context.getResources().getColor(R.color.white));
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }
}
