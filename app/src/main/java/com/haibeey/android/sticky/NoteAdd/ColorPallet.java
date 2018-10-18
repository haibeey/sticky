package com.haibeey.android.sticky.NoteAdd;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.HashMap;

/**
 * Created by haibeey on 6/15/2018.
 */

public final class ColorPallet extends View {
    private Paint paint = new Paint();
    private RectF CurrentRectf;
    private Mova mova;
    private HashMap TopLengthMapping=new HashMap<Integer,Long>();
    private int marginInParent;

    public ColorPallet(Context context) {
        super(context);
        init();
    }

    public ColorPallet(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setMova(Mova mova) {
        this.mova = mova;
    }

    public ColorPallet(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private RectF getRectF(int length, int height) {
        float ratioOfLength = ((float) length / 256);
        float top = ratioOfLength * height;
        float space = height / 256f;
        return new RectF(15, top, getWidth(), top + space);
    }

    private void setMarginInParent(){
        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) getLayoutParams();
        marginInParent=layoutParams.topMargin;

    }

    private void init(){

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:{

                if(event.getY()>getHeight()+marginInParent || event.getY()<marginInParent)break;

                if(mova!=null){
                    int y=(int)event.getY();
                    Object color=TopLengthMapping.get(y);
                    Log.e("The color",TopLengthMapping.get((int)event.getY())+" "+(int)color);
                    mova.setSelectedColor(color==null?Color.RED:(int)color);
                }

                CurrentRectf=mova.getRectF();
                CurrentRectf.top=event.getY();
                mova.moves(CurrentRectf);
                break;
            }
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setMarginInParent();
        super.onDraw(canvas);

        int Height = getHeight();

        int length = 0;

        for (float i = 0; i < 256; i += 256 / 42) {
            length++;
            paint.setColor(Color.rgb(255, 0, (int) i));
            RectF colorRect=getRectF(length, Height);

            for(int startTop =(int)colorRect.top;startTop<=(colorRect.bottom);startTop++){
                TopLengthMapping.put(startTop,Color.rgb(255, 0, (int) i));
            }
            canvas.drawRect(colorRect, paint);
        }
        for (float i = 0; i < 256; i += 256 / 42) {
            length++;
            paint.setColor(Color.rgb(255 - (int) i, 0, 255));
            RectF colorRect=getRectF(length, Height);
            for(int startTop =(int)colorRect.top;startTop<=(colorRect.bottom);startTop++){
                TopLengthMapping.put(startTop, Color.rgb(255 - (int) i, 0, 255));
            }
            canvas.drawRect(colorRect, paint);
        }
        for (float i = 0; i < 256; i += 256 / 42) {
            length++;
            paint.setColor(Color.rgb(0, (int) i, 255));
            RectF colorRect=getRectF(length, Height);
            for(int startTop =(int)colorRect.top;startTop<=(colorRect.bottom);startTop++){
                TopLengthMapping.put(startTop,  Color.rgb(0, (int) i, 255));
            }
            canvas.drawRect(colorRect, paint);
        }
        for (float i = 0; i < 256; i += 256 / 42) {
            length++;
            paint.setColor(Color.rgb(0, 255, 255 - (int) i));

            RectF colorRect=getRectF(length, Height);
            for(int startTop =(int)colorRect.top;startTop<=(colorRect.bottom);startTop++){
                TopLengthMapping.put(startTop, Color.rgb(0, 255, 255 - (int) i));
            }
            canvas.drawRect(colorRect, paint);
        }
        for (float i = 0; i < 256; i += 256 / 42) {
            length++;
            paint.setColor(Color.rgb((int) i, 255, 0));
            RectF colorRect=getRectF(length, Height);
            for(int startTop =(int)colorRect.top;startTop<=(colorRect.bottom);startTop++){
                TopLengthMapping.put(startTop, Color.rgb((int) i, 255, 0));
            }
            canvas.drawRect(colorRect, paint);
        }
        for (float i = 0; i < 256; i += 256 / 42) {
            length++;
            paint.setColor(Color.rgb(255, 255 - (int) i, 0));
            RectF colorRect=getRectF(length, Height);
            for(int startTop =(int)colorRect.top;startTop<=(colorRect.bottom);startTop++){
                TopLengthMapping.put(startTop, Color.rgb(255, 255 - (int) i, 0));
            }
            canvas.drawRect(colorRect, paint);
        }

    }

}