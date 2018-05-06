package com.cqf.hn.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by cqf on 2017/11/17 21:50
 */
public class PasswordView extends FrameLayout implements View.OnClickListener {
    EditText catchInput;
    LinearLayout content;
    private View root;
    private CharSequence text = "";
    private CharSequence lastText = "";
    private int index = 0;
    private TextImageView[] views;
    private InputCodeEndListener listener;
    private int passwordLength;
    private float itemWidth;
    private float itemHeight;
    private int inputBg;
    private int unInputBg;
    private int textColor;
    private float textSize;

    public PasswordView(@NonNull Context context) {
        this(context, null);
    }

    public PasswordView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PasswordView);
            text = typedArray.getString(R.styleable.PasswordView_password_viewText);
            passwordLength = typedArray.getInteger(R.styleable.PasswordView_password_length, 6);
            itemWidth = typedArray.getDimension(R.styleable.PasswordView_password_itemWidth, dpToPixel(32));
            itemHeight = typedArray.getDimension(R.styleable.PasswordView_password_itemHeight, dpToPixel(35));
            inputBg = typedArray.getResourceId(R.styleable.PasswordView_password_inputBg, 0);
            unInputBg = typedArray.getResourceId(R.styleable.PasswordView_password_unInputBg, 0);
            textColor = typedArray.getColor(R.styleable.PasswordView_password_textColor, 0);
            textSize = typedArray.getDimension(R.styleable.PasswordView_password_textSize, pixelsToDp(23));
            typedArray.recycle();
        }
        root = LayoutInflater.from(context).inflate(R.layout.view_password, this);
        catchInput = findViewById(R.id.catchInput);
        content = findViewById(R.id.content);
        content.setOnClickListener(this);
        catchInput.setCursorVisible(false);
        views = new TextImageView[passwordLength];
        for (int i = 0; i < passwordLength; i++) {
            TextImageView view = new TextImageView(context);
            view.setTextSize(textSize);
            view.setTextColor(textColor);
            content.addView(view);
            if (unInputBg != 0) {
                view.setBackgroundResource(unInputBg);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) itemWidth, (int) itemHeight);
            if (i == 0) {
                params.setMargins((int) dpToPixel(1), 0, 0, 0);
            }
            if (i == passwordLength - 1) {
                params.setMargins(0, 0, (int) dpToPixel(1), 0);
            }
            view.setLayoutParams(params);
            views[i] = view;
            if (text != null && i < text.length()) {
                setItemText(text.subSequence(i, i + 1));
            }
        }
        catchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (index > s.length()) {
                        removeItemText();
                    } else {
                        setText(s);
                        if (s.length() == passwordLength) {
                            if (listener != null) {
                                listener.onInputCodeEnd(s);
                            }
                        }
                    }
                } else if (s.length() == 0 && index > 0) {
                    removeItemText();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setText(CharSequence text) {
        index = 0;
        for (int i = 0; i < passwordLength; i++) {
            if (text != null) {
                if (i < text.length()) {
                    setItemText(text.subSequence(i, i + 1));
                }
            }
        }
    }

    public void removeText() {
        catchInput.setText("");
        for (int i = 0; i < passwordLength; i++) {
            if (index != 0) {
                removeItemText();
            } else {
                break;
            }
        }
    }

    public void setItemText(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            if (index < passwordLength) {
                if (inputBg != 0) {
                    views[index].setText(text, inputBg);
                } else {
                    views[index].setText(text);
                }
                index++;
            }
        }
    }

    public void removeItemText() {
        if (index > 0) {
            index--;
            if (unInputBg != 0) {
                views[index].removeText(unInputBg);
            } else {
                views[index].setText("");
            }
        }
    }

    public CharSequence getText() {
        StringBuilder builder = new StringBuilder();
        for (TextImageView view : views) {
            builder.append(view.getText());
        }
        return builder.toString();
    }

    public int getItemCount() {
        return passwordLength;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.content) {
            requestEtFocus();
        }
    }

    public void requestEtFocus() {
        catchInput.setFocusable(true);
        catchInput.setFocusableInTouchMode(true);
        catchInput.setClickable(true);
        catchInput.requestFocus();
        showSoftKeyboard(catchInput);
        catchInput.setCursorVisible(false);
        catchInput.setSelection(catchInput.length());
    }

    public interface InputCodeEndListener {
        void onInputCodeEnd(CharSequence s);
    }

    public InputCodeEndListener getListener() {
        return listener;
    }

    public void setListener(InputCodeEndListener listener) {
        this.listener = listener;
    }

    public void showSoftKeyboard(View view) {
        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, 0);
    }

    public float dpToPixel(float dp) {
        return dp * (getDisplayMetrics().densityDpi / 160F);
    }

    public float pixelsToDp(float f) {
        return f / (getDisplayMetrics().densityDpi / 160F);
    }

    public DisplayMetrics getDisplayMetrics() {
        return getContext().getResources().getDisplayMetrics();
    }
}
