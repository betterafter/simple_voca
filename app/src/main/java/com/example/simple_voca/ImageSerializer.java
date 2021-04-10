package com.example.simple_voca;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;


public class ImageSerializer {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static String PackImageToSerialized(ImageView image){
        if(image.getDrawable() == null || image.getDrawable() instanceof VectorDrawable){
            return " ";
        }

        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        return BitmapConverter.BitmapToString(bitmap);
    }

    public static Bitmap PackSerializedToImage(String data){
        return BitmapConverter.StringToBitmap(data);
    }
}
