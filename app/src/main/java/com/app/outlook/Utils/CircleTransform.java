package com.app.outlook.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import com.squareup.picasso.Transformation;

/**
 * Created by New Joinee on 02-04-2015.
 */

public class CircleTransform implements Transformation {

    private int size = 0;

    public CircleTransform(int size) {
        this.size = size;
    }

    public CircleTransform() {
        this.size = 200;
    }

    @Override
    public Bitmap transform(Bitmap source) {
//        int size = Math.min(source.getWidth(), source.getHeight());
//
//        int x = (source.getWidth() - size) / 2;
//        int y = (source.getHeight() - size) / 2;
//
//        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
//        if (squaredBitmap != source) {
//            source.recycle();
//        }
//
//        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
//
//        Canvas canvas = new Canvas(bitmap);
//        Paint paint = new Paint();
//        BitmapShader shader = new BitmapShader(squaredBitmap,
//                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
//        paint.setShader(shader);
//        paint.setAntiAlias(true);
//
//        float r = size / 2f;
//        canvas.drawCircle(r, r, r, paint);
//
//        squaredBitmap.recycle();
//        return bitmap;

        int radius = (int) Math.ceil(size / 2);

        Bitmap output = Bitmap.createBitmap(size,
                size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final Rect rect = new Rect(0, 0, source.getWidth(),
                source.getHeight());
        final Rect target = new Rect(0, 0, size, size);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(radius, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, rect, target, paint);
        source.recycle();

        return output;
    }

    @Override
    public String key() {
        return "circle";
    }
}
