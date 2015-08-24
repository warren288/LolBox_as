package com.warren.lolbox.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 天赋图视图
 * @author:yangsheng
 * @date:2015/8/23
 */
public class GiftView extends View {

    public GiftView(Context context) {
        super(context);
    }

    public GiftView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GiftView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();

    }
}
