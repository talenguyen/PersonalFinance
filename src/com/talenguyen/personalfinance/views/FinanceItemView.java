package com.talenguyen.personalfinance.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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

        void onNewItemSubmit(boolean positive, String name, float amount);

        void onUpdateItemSubmit(boolean positive, String name, float amount);

    }

	private static final boolean POSITIVE_DEFAULT = false;

	protected static final String TAG = FinanceItemView.class.getSimpleName();

	protected boolean mKeySpaceEntered = false;

    private FinanceItemViewCallback mCallback;
    private CheckBox positiveCheckbox;
    private EditText nameEditText;
    private EditText amountEditText;
    private Button removeButton;

	protected boolean isInitialized;

    public FinanceItemView(Context context) {
        this(context, null);
    }

    public FinanceItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
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
                positiveCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						submitUpdateItem();
					}
				});
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
                nameEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        Log.d(TAG, "onFocusChange()");
                    }
                });
            } else if (view.getId() == R.id.amountEditText) {
                amountEditText = (EditText) view;
                amountEditText.setOnEditorActionListener(new OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_ACTION_DONE) {
                            submitNewItem();
                            return true;
                        }
                        return false;
                    }
                });
            }
        }
    }

    private void submitNewItem() {
    	final FinanceItem item = getFinanceInstance();
        if (item == null) {
            return;
        }

        Log.d(TAG, "submitNewItem: " + item.getName());
        mCallback.onNewItemSubmit(item.isPositive(), item.getName(), item.getAmount());
        isInitialized = true;
    }

    private void submitUpdateItem() {
    	final FinanceItem item = getFinanceInstance();
        if (!isInitialized || item == null) {
            return;
        }

        Log.d(TAG, "submitUpdateItem: " + item.getName());
        mCallback.onUpdateItemSubmit(item.isPositive(), item.getName(), item.getAmount());
    }

    private FinanceItem getFinanceInstance() {
    	 if (mCallback == null) {
             return null;
         }

         String name = nameEditText.getText().toString();
         if (TextUtils.isEmpty(name)) {
             name = "Unknown";
         }
         String amount = amountEditText.getText().toString();
         if (TextUtils.isEmpty(amount)) {
             amount = "0";
         }
         return new FinanceItem(0, name, Float.parseFloat(amount), positiveCheckbox.isChecked(), null, null);
    }
}
