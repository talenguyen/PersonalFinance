package com.talenguyen.personalfinance.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.talenguyen.personalfinance.R;
import com.talenguyen.personalfinance.data.FinanceItem;

/**
 * @author: GIANG
 * @date: 1/1/14
 * @time: 8:47 PM
 */
public class FinanceItemView extends LinearLayout {

    public static interface FinanceItemViewCallback {

        void onRemoveButtonClick();
        void onSubmit(boolean positive, String name, float amount);

    }

	private static final boolean POSITIVE_DEFAULT = false;

    private FinanceItemViewCallback mCallback;
    private CheckBox positiveCheckbox;
    private EditText nameEditText;
    private EditText amountEditText;
    private Button removeButton;

    public FinanceItemView(Context context) {
        this(context, null, 0);
    }

    public FinanceItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(VERSION_CODES.HONEYCOMB)
    public FinanceItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public void setFinanceItemViewCallback(FinanceItemViewCallback callback) {
        mCallback = callback;
    }

    public void bindView(FinanceItem financeItem) {
        if (financeItem != null) {
            positiveCheckbox.setChecked(financeItem.isPositive());
            nameEditText.setText(financeItem.getName());
            amountEditText.setText(String.valueOf(financeItem.getAmount()));
        }
    }
    
    public void reset() {
    	positiveCheckbox.setChecked(POSITIVE_DEFAULT);
    	nameEditText.setText("");
    	amountEditText.setText("");
    	nameEditText.requestFocus();
    }

    private void initView(Context context) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.view_finance_item, null);
        while (layout.getChildCount() > 0) {
            final View view = layout.getChildAt(0);
            layout.removeView(view);
            addView(view);
            if (view.getId() == R.id.positiveCheckbox) {
                positiveCheckbox = (CheckBox) view;
            } else if (view.getId() == R.id.removeButton) {
                removeButton = (Button) view;
                removeButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mCallback != null) {
                            mCallback.onRemoveButtonClick();
                        }
                    }
                });
            } else if (view.getId() == R.id.nameEditText) {
                nameEditText = (EditText) view;
            } else if (view.getId() == R.id.amountEditText) {
                amountEditText = (EditText) view;
                amountEditText.setOnEditorActionListener(new OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_ACTION_NEXT) {
                            submitItem();
                            return true;
                        }
                        return false;
                    }
                });
            }
        }
    }

    private void submitItem() {
        if (mCallback == null) {
            return;
        }

        String name = nameEditText.getText().toString();
        if (TextUtils.isEmpty(name)) {
            name = "Unknown";
        }
        String amount = amountEditText.getText().toString();
        if (TextUtils.isEmpty(amount)) {
            amount = "0";
        }

        mCallback.onSubmit(positiveCheckbox.isChecked(), name, Float.parseFloat(amount));

    }
}
