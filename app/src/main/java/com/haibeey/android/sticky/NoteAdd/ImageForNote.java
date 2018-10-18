package com.haibeey.android.sticky.NoteAdd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;


import java.util.ArrayList;

public class ImageForNote extends AppCompatImageView {
    private ArrayList<MyPath> paths=new ArrayList<>();
    private Paint paint=new Paint();
    private MyPath CurrentPath=new MyPath();
    private Mova mova;
    private int StrokeWidth=10;
    private Bitmap bitmap;
    private Canvas canvasThatHasBitmap;

    public ImageForNote(Context context) {
        super(context);
        init();
    }

    public ImageForNote(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageForNote(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setStrokeWidth(int strokeWidth) {
        StrokeWidth = strokeWidth;
    }

    public int getStrokeWidth() {
        return StrokeWidth;
    }

    public void setMova(Mova mova) {
        this.mova = mova;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.draw(canvasThatHasBitmap);
        for(MyPath path:paths){
            drawapath(path,canvas, (int) mova.getSelectedColor());
        }

    }

    private void drawapath(MyPath path,Canvas canvas,int color) {
        path.setColor(color);
        paint.setStrokeWidth(StrokeWidth);
        paint.setColor(color);
        canvas.drawPath(path,paint);
        canvasThatHasBitmap.drawPath(path,paint);
    }

    private void init(){
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        bitmap=Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ARGB_8888);
        canvasThatHasBitmap=new Canvas(bitmap);

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //move to touch location
                CurrentPath=new MyPath();
                CurrentPath.moveTo(event.getX(),event.getY());
                paths.add(CurrentPath);
                return true;
            case MotionEvent.ACTION_UP:
                CurrentPath.moveTo(event.getX(),event.getY());
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                CurrentPath.lineTo(event.getX(),event.getY());
                invalidate();
                return true;
        }
        return false;
    }

    public void Erase(){
        paths.remove(paths.size()-1);
        invalidate();
    }

}
