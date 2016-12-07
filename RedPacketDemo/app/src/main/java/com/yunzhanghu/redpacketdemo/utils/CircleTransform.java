package com.yunzhanghu.redpacketdemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;


/**
 * Created by max on 16/3/6.
 */
public class CircleTransform extends BitmapTransformation {


    public CircleTransform(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int outWidth, int outHeight) {
        return circleCrop(bitmapPool, bitmap);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }
        float r = size / 2f;
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#F9F9F9"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(r, r, r, paint);
        paint.reset();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        canvas.drawCircle(r, r, r, paint);
        return result;
    }


    @Override
    public String getId() {
        return getClass().getName();
    }
}
