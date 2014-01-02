package com.talenguyen.personalfinance.database;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import com.talenguyen.framework.utils.DateTimeUtils;

/**
 * @author: GIANG
 * @date: 1/1/14
 * @time: 8:25 PM
 */
public class DBController {

    public static CursorLoader getTodayFinanceLoader(Context context) {
        return new CursorLoader(context, DBContract.TABLE_FINANCE.getContentUri(), null, FinanceTable.DATE + "=?", new String[]{DateTimeUtils.formatDate(System.currentTimeMillis())}, null);
    }

    public static Uri insertFinanceItem(Context context, boolean positive, String name, float amount) {
        final ContentValues values = new ContentValues();
        values.put(FinanceTable.POSITIVE, positive);
        values.put(FinanceTable.NAME, name);
        values.put(FinanceTable.AMOUNT, amount);
        values.put(FinanceTable.DATE, DateTimeUtils.formatDate(System.currentTimeMillis()));
        values.put(FinanceTable.TIME, DateTimeUtils.formatTime(System.currentTimeMillis()));
        return context.getContentResolver().insert(DBContract.TABLE_FINANCE.getContentUri(), values);
    }

	public static void deleteFinanceItemById(Context context, long id) {
		final Uri deleteUri = ContentUris.withAppendedId(DBContract.TABLE_FINANCE.getContentUri(), id);
		context.getContentResolver().delete(deleteUri, null, null);
	}
}
