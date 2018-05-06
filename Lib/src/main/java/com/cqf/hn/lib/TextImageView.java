package com.cqf.hn.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;


/**
 * Created by cqf on 2017/11/17 21:52
 */
public class TextImageView extends AppCompatImageView {
    CharSequence text = "";
    private int textColor;
    private float textSize;
    private Paint textPaint;
    private Paint.FontMetrics fontMetrics;
    private float dy;
    private float textWidth;
    private boolean isDrawSrc;

    public TextImageView(Context context) {
        this(context, null);
    }

    public TextImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TextImageView);
            textSize = typedArray.getDimension(R.styleable.TextImageView_text_size, 0);
            textColor = typedArray.getColor(R.styleable.TextImageView_text_color, 0);
            text = typedArray.getString(R.styleable.TextImageView_text);
            isDrawSrc = typedArray.getBoolean(R.styleable.TextImageView_isDrawSrc, false);
            typedArray.recycle();
        }
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        fontMetrics = textPaint.getFontMetrics();
        dy = Math.abs(Math.abs(fontMetrics.ascent) - Math.abs(fontMetrics.descent));
        getTextWidth(text);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (text.length() > 0) {
            if (isDrawSrc) {
                super.onDraw(canvas);
            }
            canvas.drawText(text, 0, text.length(), (getMeasuredWidth() - textWidth) / 2, (getMeasuredHeight() + dy) / 2, textPaint);
        } else {
            super.onDraw(canvas);
        }
    }

    public void setText(CharSequence text) {
        this.text = text;
        if (text.length() > 0) {
            getTextWidth(text);
            invalidate();
        }
    }

    public void setText(CharSequence text, int resId) {
        this.text = text;
        if (text.length() > 0) {
            getTextWidth(text);
            setBackgroundResource(resId);
        }
    }

    private void getTextWidth(CharSequence text) {
        textWidth = textPaint.measureText(text, 0, text.length());
    }

    public CharSequence getText() {
        return text;
    }

    public void removeText(int src) {
        text = "";
        setBackgroundResource(src);
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        textPaint.setColor(textColor);
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        textPaint.setTextSize(textSize);
        fontMetrics = textPaint.getFontMetrics();
        dy = Math.abs(Math.abs(fontMetrics.ascent) - Math.abs(fontMetrics.descent));
        getTextWidth(text);
    }
}
