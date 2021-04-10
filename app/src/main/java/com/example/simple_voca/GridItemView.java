package com.example.simple_voca;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Items.ListItem;
import com.example.activity.MainActivity;

import androidx.annotation.Nullable;

public class GridItemView extends LinearLayout {
    TextView word;
    TextView announce;
    TextView mean;
    String url = "https://blog.kakaocdn.net/dn/bo3KaK/btquRMFtTMq/aqjWG83rTIrEXWKpCEjFgK/%EB%9D%A0%EB%A7%81.mp3?attach=1&knm=tfile.mp3";

    public GridItemView(Context context) {
        super(context);
        init(context);
    }

    public GridItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.voca_gridview_adapter_item, this, true);

        word = (TextView) findViewById(R.id.textView);
        announce = (TextView) findViewById(R.id.textView2);
        mean = (TextView) findViewById(R.id.textView3);

        ImageButton btn = (ImageButton)findViewById(R.id.imageButton);
        btn.setOnClickListener(new ImageButton.OnClickListener(){

            @Override
            public void onClick(View v) {
                try {
                    MainActivity.player = MediaPlayer.create(context, Uri.parse(url));
                    MainActivity.player.start();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void setData(ListItem listItem){

        String[] data = listItem.getData();
        word.setText(data[0]);
        announce.setText(data[1]);
        mean.setText(data[2]);
    }

}
