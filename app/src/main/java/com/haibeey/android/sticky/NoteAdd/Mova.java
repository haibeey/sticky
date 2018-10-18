package com.haibeey.android.sticky.NoteAdd;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Created by haibeey on 7/2/2018.
 */

public final class Mova extends View{
    private RectF rectF;
    private int SelectedColor=-65536 ;
    public Mova(Context context) {
        super(context);
        init();
    }

    public Mova(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Mova(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(21)
    public Mova(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init(){
        rectF=new RectF(0f,0f,getWidth(),getHeight());
    }

    public RectF getRectF() {
        return rectF;
    }

    public void moves(RectF rectF) {
        this.rectF=rectF;
        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) getLayoutParams();
        layoutParams.topMargin= (int) rectF.top;
        setLayoutParams(layoutParams);
        invalidate();
        requestLayout();
    }

    public void setSelectedColor(int selectedColor) {
        SelectedColor = selectedColor;
    }

    public long getSelectedColor() {
        return SelectedColor;
    }
}
