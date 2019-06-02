package com.framework.app.component.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.framework.app.component.R;

public class MyTextView extends TextView {
    private float spacing = Spacing.NORMAL;
    private CharSequence originalText = "";


    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public float getSpacing() {
        return this.spacing;
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LetterSpacingTextView);
        originalText = array.getString(R.styleable.LetterSpacingTextView_text);
        setSpacing(array.getFloat(R.styleable.LetterSpacingTextView_textSpacing, 0));
        array.recycle();
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
        applySpacing();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        originalText = text;
        applySpacing();
    }

    @Override
    public CharSequence getText() {
        return originalText;
    }

    private void applySpacing() {
        if (this == null || this.originalText == null) return;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < originalText.length(); i++) {
            builder.append(originalText.charAt(i));
            if (i + 1 < originalText.length()) {
                builder.append("\u00A0");
            }
        }
        SpannableString finalText = new SpannableString(builder.toString());
        if (builder.toString().length() > 1) {
            for (int i = 1; i < builder.toString().length(); i += 2) {
                finalText.setSpan(new ScaleXSpan((spacing + 1) / 10), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        super.setText(finalText, BufferType.SPANNABLE);
    }

    public class Spacing {
        public final static float NORMAL = 10;
    }
}