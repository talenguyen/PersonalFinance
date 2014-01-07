package com.talenguyen.personalfinance.database;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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

    public static void insertFinanceItemAsync(final Context context, final boolean positive, final String name, final float amount) {
        new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				insertFinanceItem(context.getApplicationContext(), positive, name, amount);
				return null;
			}
		}.execute();
    }
    
    public static void deleteFinanceItemByIdAsync(final Context context, final long id) {
    	new AsyncTask<Void, Void, Void>() {
    		
    		@Override
    		protected Void doInBackground(Void... params) {
    			deleteFinanceItemById(context.getApplicationContext(), id);
    			return null;
    		}
    	}.execute();    	
    }
    
    public static void updateFinanceItemAsync(final Context context, final long id, final boolean positive, final String name, final float amount) {
        new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				updateFinanceItem(context.getApplicationContext(), id, positive, name, amount);
				return null;
			}
		}.execute();    	
    }
    
    public static Uri insertFinanceItem(Context context, boolean positive, String name, float amount) {
        final ContentValues values = newContentValues(positive, name, amount);
        return context.getContentResolver().insert(DBContract.TABLE_FINANCE.getContentUri(), values);
    }

	private static ContentValues newContentValues(boolean positive,
			String name, float amount) {
		final ContentValues values = new ContentValues();
		values.put(FinanceTable.POSITIVE, positive);
        values.put(FinanceTable.NAME, name);
        values.put(FinanceTable.AMOUNT, amount);
        values.put(FinanceTable.DATE, DateTimeUtils.formatDate(System.currentTimeMillis()));
        values.put(FinanceTable.TIME, DateTimeUtils.formatTime(System.currentTimeMillis()));
		return values;
	}

	public static void deleteFinanceItemById(Context context, long id) {
		final Uri deleteUri = ContentUris.withAppendedId(DBContract.TABLE_FINANCE.getContentUri(), id);
		context.getContentResolver().delete(deleteUri, null, null);
	}
	
	public static void updateFinanceItem(Context context, long id, boolean positive, String name, float amount) {
		final Uri updateUri = ContentUris.withAppendedId(DBContract.TABLE_FINANCE.getContentUri(), id);
		final ContentValues values = newContentValues(positive, name, amount);
		context.getContentResolver().update(updateUri, values, null, null);
	}
}
