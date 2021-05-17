package com.danerdaner.simple_voca;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.danerdaner.activity.LoadingActivity;
import com.danerdaner.adapter.VocaRecyclerViewAdapter;
import com.danerdaner.database.VocaDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final int PARENT_VIEW = 0;
    private final int CHILD_VIEW = 1;

    enum ButtonsState {
        GONE,
        LEFT_VISIBLE,
        RIGHT_VISIBLE
    }


    private boolean swipeBack = false;
    private ButtonsState buttonShowedState = ButtonsState.GONE;

    private Context context;

    public static ArrayList<swapMenuOptionItem> viewHolders = new ArrayList<>();
    private int DELETE_AND_EDIT = 101;
    private final int REMIND_AND_IMPORTANT = 102;
    private int direction = 0;



    public ItemTouchHelperCallback(Context context){
        this.context = context;
    }

    public class swapMenuOptionItem{
        private RecyclerView.ViewHolder viewHolder;
        private int type;

        public RecyclerView.ViewHolder getViewHolder() {
            return viewHolder;
        }

        public void setViewHolder(RecyclerView.ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public swapMenuOptionItem(RecyclerView.ViewHolder viewHolder, Integer type){
            this.viewHolder = viewHolder;
            this.type = type;
        }


    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int drag_flags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
        int swipe_flags = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
        return makeMovementFlags(drag_flags,swipe_flags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }


    int swipeState = 0;
    @Override
    public void onChildDraw(Canvas c,
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {

        if (viewHolder.getItemViewType() == PARENT_VIEW) {
            // 스와이프 중이라면
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // 외운 단어
                if (dX < 0) {
                    drawButtons(c, viewHolder, ButtonsState.RIGHT_VISIBLE);
                    if (Math.abs(dX) > viewHolder.itemView.findViewById(R.id.main_item).getWidth() / 2) {
                        swipeState = 1;
                    }
                }
                // 중요 단어
                else if (dX > 0) {
                    drawButtons(c, viewHolder, ButtonsState.LEFT_VISIBLE);
                    if (Math.abs(dX) > viewHolder.itemView.findViewById(R.id.main_item).getWidth() / 2) {
                        swipeState = 2;
                    }
                }
                if(dX == 0){
                    // 외운단어 표시일 때
                    if(swipeState == 1){
                        ((VocaRecyclerViewAdapter)recyclerView.getAdapter()).removeAllChildView();

                        if(viewHolder.getAdapterPosition() != RecyclerView.NO_POSITION){
                            String data[] = LoadingActivity.vocaList.get(viewHolder.getAdapterPosition()).getData();

                            if(!data[8].equals(VocaDatabase.remindFlag)) {
                                LoadingActivity.vocaDatabase.change(
                                        viewHolder.getAdapterPosition(),
                                        data[0], data[1], data[2], data[3], data[4], data[5],
                                        data[6], data[7], VocaDatabase.remindFlag);


                                LoadingActivity.vocaDatabase.makeList(LoadingActivity.vocaList);
                            }
                            else {
                                LoadingActivity.vocaDatabase.change(
                                        viewHolder.getAdapterPosition(),
                                        data[0], data[1], data[2], data[3], data[4], data[5],
                                        data[6], data[7], VocaDatabase.nullFlag);

                                LoadingActivity.vocaDatabase.makeList(LoadingActivity.vocaList);
                            }
                        }
                        recyclerView.getAdapter().notifyDataSetChanged();
                        swipeState = 0;
                    }
                    // 북마크 표시일 때
                    else if(swipeState == 2){
                        ((VocaRecyclerViewAdapter)recyclerView.getAdapter()).removeAllChildView();

                        if(viewHolder.getAdapterPosition() != RecyclerView.NO_POSITION){
                            String data[] = LoadingActivity.vocaList.get(viewHolder.getAdapterPosition()).getData();

                            if(!data[8].equals(VocaDatabase.importantFlag)) {
                                LoadingActivity.vocaDatabase.change(
                                        viewHolder.getAdapterPosition(),
                                        data[0], data[1], data[2], data[3], data[4], data[5],
                                        data[6], data[7], VocaDatabase.importantFlag);


                                LoadingActivity.vocaDatabase.makeList(LoadingActivity.vocaList);
                            }
                            else {
                                LoadingActivity.vocaDatabase.change(
                                        viewHolder.getAdapterPosition(),
                                        data[0], data[1], data[2], data[3], data[4], data[5],
                                        data[6], data[7], VocaDatabase.nullFlag);

                                LoadingActivity.vocaDatabase.makeList(LoadingActivity.vocaList);
                            }
                        }
                        recyclerView.getAdapter().notifyDataSetChanged();
                        swipeState = 0;
                    }

                    swipeState = 0;
                }
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }







    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        // Action finished
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return defaultValue * 100;//10 -> almost insensitive
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 2f;
    }


    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder, ButtonsState buttonShowedState) {

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        RectF leftButton1, rightButton1;

        if(buttonShowedState == ButtonsState.LEFT_VISIBLE){
            leftButton1 = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom());
            p.setColor(context.getResources().getColor(R.color.mainYellow));
            c.drawRect(leftButton1, p);
        }

        else if(buttonShowedState == ButtonsState.RIGHT_VISIBLE){
            rightButton1 = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom());
            p.setColor(context.getResources().getColor(R.color.mainBlack));
            c.drawRect(rightButton1, p);
        }

        drawIcon(c, viewHolder, buttonShowedState);
    }


    private void drawIcon(Canvas c, RecyclerView.ViewHolder viewHolder, ButtonsState buttonShowedState){

        View itemView = viewHolder.itemView;
        int height = (itemView.getTop() + itemView.getBottom()) / 2;
        int size = 80;
        int bound = 100;

        if(buttonShowedState == ButtonsState.LEFT_VISIBLE){

            Drawable drawable1 = ContextCompat.getDrawable(context, R.drawable.baseline_bookmark_24);
            drawable1.setBounds(itemView.getLeft() + bound, height - size / 2 + 10,
                    itemView.getLeft() + bound + size, height + size / 2 + 10);
            drawable1.draw(c);
        }
        else if(buttonShowedState == ButtonsState.RIGHT_VISIBLE){

            Drawable drawable2 = ContextCompat.getDrawable(context, R.drawable.outline_done_24);
            drawable2.setBounds(itemView.getRight() - bound - size, height - size / 2 + 10,
                    itemView.getRight() - bound, height + size / 2 + 10);

            drawable2.draw(c);
        }







    }

//
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }
}
