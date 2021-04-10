package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.simple_voca.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VocaViewPagerAdapter extends RecyclerView.Adapter<VocaViewPagerAdapter.viewHolder> {

    Context context;
    ArrayList<Button> ButtonList;

    public VocaViewPagerAdapter(Context context, ArrayList<Button> ButtonList){
        this.context = context;
        this.ButtonList = ButtonList;
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        Button button;

        public viewHolder(@NonNull View itemView){
            super(itemView);

            button = itemView.findViewById(R.id.viewpager_button);
        }
    }

    @NonNull
    @Override
    public VocaViewPagerAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.activity_main_viewpager_item, parent, false) ;
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VocaViewPagerAdapter.viewHolder holder, int position) {

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return ButtonList.size();
    }


}
