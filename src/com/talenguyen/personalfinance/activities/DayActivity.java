package com.talenguyen.personalfinance.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.talenguyen.personalfinance.R;
import com.talenguyen.personalfinance.fragments.DayFragment;

public class DayActivity extends FragmentActivity {
    private static final String TAG_DAY_FRAGMENT = "TAG_DAY_FRAGMENT";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        if (savedInstanceState == null) {
            final FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().add(R.id.fragment_container, new DayFragment(), TAG_DAY_FRAGMENT).commit();
        }
    }
}
