package com.wiseking.ray.beatboss.util;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author yangshuai
 * @version 创建时间：2015-8-25 下午5:58:48
 * 类说明 :有文字的imageView
 */
public class DrawTextImageView extends ImageView {

    public DrawTextImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawTextImageView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawTextImageView (Context context) {
        super(context);
        paint = new Paint();
    }

    /**
     * 文字
     */
    private String textString = "";

    /**
     * 文字大小
     */
    private float textSize = 30;

    /**
     * 文字位置x
     */
    private float x = -1000;

    /**
     * 文字位置y
     */
    private float y = -1000;

    /**
     * 文字颜色
     */
    private int textColor = R.color.holo_red_light;

    /**
     * 文字线条粗细
     */
    private int textDrawStrokeWidth = 3;

    Paint paint;

    /**
     *
     */
    public void setDrawText(String string){
        textString = string;
        drawableStateChanged();
    }

    /**
     *  设置文字
     */
    public void setDrawTextSize(float textSize){
        this.textSize = textSize;
        drawableStateChanged();
    }

    /**
     * 设置文字位置x，y
     */
    public void setDrawLocalXY(float x, float y){
        this.x = x;
        this.y = y;
        drawableStateChanged();
    }

    /**
     * 设置文字颜色
     */
    public void setDrawTextColorResourse(int textColor){
        this.textColor = textColor;
        drawableStateChanged();
    }

    /**
     * 设置文字粗细
     */
    public void setDrawTextStrokeWidth(int textDrawStrokeWidth){
        this.textDrawStrokeWidth = textDrawStrokeWidth;
        drawableStateChanged();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!textString.equals("")) {
//            Paint paint = new Paint();
            paint.setColor(getResources().getColor(textColor));
            paint.setStrokeWidth(textDrawStrokeWidth);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.LEFT);
            Rect bounds= new Rect();
            paint.getTextBounds(textString,0,textString.length(),bounds);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            canvas.drawText(textString, getMeasuredWidth()/2-bounds.width()/2,baseline, paint);
//            canvas.drawText(textString, x==-1000 ? ( canvas.getWidth() - ( textSize * textString.length() ) ) : x, y == -1000 ? ( canvas.getHeight() - 50 ) : y, paint);
        }


    }

}
