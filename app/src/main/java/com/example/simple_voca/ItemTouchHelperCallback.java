package com.example.simple_voca;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    enum ButtonsState {
        GONE,
        LEFT_VISIBLE,
        RIGHT_VISIBLE
    }

    private boolean swipeBack = false;

    private ButtonsState buttonShowedState = ButtonsState.GONE;

    private boolean isDrew = false;

    private RectF leftButton, rightButton;

    private RectF buttonInstance = null;

    private RecyclerView.ViewHolder currentItemViewHolder = null;

    private static final float buttonWidth = 300;


    public ItemTouchHelperCallback(){

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
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onChildDraw(Canvas c,
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {


        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


            if(dX > 0){
                System.out.println(dX);
                drawButtons(c, viewHolder, ButtonsState.LEFT_VISIBLE);
                rightButton = null;
            }

            if(dX < 0){
                System.out.println(dX);
                drawButtons(c, viewHolder, ButtonsState.RIGHT_VISIBLE);
                leftButton = null;
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    private void setTouchListener(final Canvas c,
                                  final RecyclerView recyclerView,
                                  final RecyclerView.ViewHolder viewHolder,
                                  final float dX, final float dY,
                                  final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if (swipeBack) {
                    if (dX < -buttonWidth) buttonShowedState = ButtonsState.RIGHT_VISIBLE;
                    else if (dX > buttonWidth) buttonShowedState  = ButtonsState.LEFT_VISIBLE;
                }
                return false;
            }
        });
    }


    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder, ButtonsState buttonShowedState) {

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        if(buttonShowedState == ButtonsState.LEFT_VISIBLE){
            leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom());
            p.setColor(Color.BLUE);
            c.drawRect(leftButton, p);
            drawText("EDIT", c, leftButton, p);
        }

        else if(buttonShowedState == ButtonsState.RIGHT_VISIBLE){
            rightButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom());
            p.setColor(Color.RED);
            c.drawRect(rightButton, p);
            drawText("DELETE", c, rightButton, p);

        }
    }

    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 60;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p);
    }

//
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }
}
