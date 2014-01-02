package com.talenguyen.personalfinance.data;

import android.database.Cursor;

import com.talenguyen.personalfinance.database.FinanceTable;

/**
 * @author: GIANG
 * @date: 1/1/14
 * @time: 8:53 PM
 */
public class FinanceItem {
    long id;
    String name;
    float amount;
    boolean positive;
    String date;
    String time;

    public FinanceItem(long id, String name, float amount, boolean positive, String date, String time) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.positive = positive;
        this.date = date;
        this.time = time;
    }

    public FinanceItem(Cursor cursor) {
        if (cursor != null) {
            this.id = cursor.getLong(cursor.getColumnIndex(FinanceTable._ID));
            this.name = cursor.getString(cursor.getColumnIndex(FinanceTable.NAME));
            this.amount = cursor.getFloat(cursor.getColumnIndex(FinanceTable.AMOUNT));
            this.positive = cursor.getInt(cursor.getColumnIndex(FinanceTable.POSITIVE)) == 1;
            this.date = cursor.getString(cursor.getColumnIndex(FinanceTable.DATE));
            this.time = cursor.getString(cursor.getColumnIndex(FinanceTable.TIME));
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getAmount() {
        return amount;
    }

    public boolean isPositive() {
        return positive;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
