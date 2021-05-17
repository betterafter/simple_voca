package com.danerdaner.simple_voca;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.danerdaner.Items.ListItem;
import com.danerdaner.activity.MainActivity;

import androidx.annotation.Nullable;

public class GridItemView extends LinearLayout {
    TextView word;
    TextView announce;
    TextView mean;
    Context context;

    String url = "https://blog.kakaocdn.net/dn/bo3KaK/btquRMFtTMq/aqjWG83rTIrEXWKpCEjFgK/%EB%9D%A0%EB%A7%81.mp3?attach=1&knm=tfile.mp3";

    public GridItemView(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public GridItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
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
//                    MainActivity.player = MediaPlayer.create(context, Uri.parse(url));
//                    MainActivity.player.start();

                    MainActivity.tts.speak(word.getText().toString(),TextToSpeech.QUEUE_FLUSH, null);
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
