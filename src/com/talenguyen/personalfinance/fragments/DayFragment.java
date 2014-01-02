package com.talenguyen.personalfinance.fragments;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.talenguyen.framework.adapter.CustomAdapter;
import com.talenguyen.personalfinance.R;
import com.talenguyen.personalfinance.data.FinanceItem;
import com.talenguyen.personalfinance.database.DBController;
import com.talenguyen.personalfinance.database.FinanceTable;
import com.talenguyen.personalfinance.views.FinanceItemView;
import com.talenguyen.personalfinance.views.FinanceItemView.FinanceItemViewCallback;

/**
 * @author: GIANG
 * @date: 1/1/14
 * @time: 8:34 PM
 */
public class DayFragment extends Fragment implements LoaderCallbacks<Cursor> {

	private FinanceAdapter mAdapter;
	private CustomAdapter<FinanceItem> mCustomAdapter;
	private TextView mTotlalAmountView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new FinanceAdapter(this);
		mCustomAdapter = new CustomAdapter<FinanceItem>(getActivity(), layoutId, viewHolderFactory)
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View localView = inflater.inflate(R.layout.fragment_day,
				container, false);
		mTotlalAmountView = (TextView) localView
				.findViewById(R.id.totalAmountTextView);
		final ListView listView = (ListView) localView
				.findViewById(R.id.list_view);
		final FinanceItemView footer = new FinanceItemView(getActivity());
		footer.setFinanceItemViewCallback(new FinanceItemViewCallback() {
			@Override
			public void onRemoveButtonClick() {

			}

			@Override
			public void onSubmit(final boolean positive, final String name,
					final float amount) {
				footer.reset();
				new AsyncTask<Void, Void, Uri>() {
					@Override
					protected Uri doInBackground(Void... voids) {
						return DBController.insertFinanceItem(getActivity()
								.getApplicationContext(), positive, name,
								amount);
					}

					@Override
					protected void onPostExecute(Uri result) {
						super.onPostExecute(result);
						if (result != null) {
							refreshList();
						}
					}
				}.execute();
			}
		});
		listView.addFooterView(footer);
		listView.setAdapter(mAdapter);
		return localView;
	}

	@Override
	public void onResume() {
		super.onResume();
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public android.support.v4.content.Loader<Cursor> onCreateLoader(int i,
			Bundle bundle) {
		return DBController.getTodayFinanceLoader(getActivity());
	}

	@Override
	public void onLoadFinished(
			android.support.v4.content.Loader<Cursor> cursorLoader,
			Cursor cursor) {
		final float totalAmount = getTotalAmount(cursor);
		mTotlalAmountView.setText(String.valueOf(totalAmount));
		mAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(
			android.support.v4.content.Loader<Cursor> cursorLoader) {
		mAdapter.swapCursor(null);
	}

	private float getTotalAmount(Cursor cursor) {
		if (cursor == null || !cursor.moveToFirst()) {
			return 0.f;
		}
		float totalAmount = 0.f;
		do {
			final float amount = cursor.getFloat(cursor
					.getColumnIndex(FinanceTable.AMOUNT));
			totalAmount += amount;
		} while (cursor.moveToNext());
		return totalAmount;
	}

	private void refreshList() {
		getLoaderManager().restartLoader(0, null, this);
	}

	private static class FinanceAdapter extends CursorAdapter {

		protected static final String TAG = FinanceAdapter.class.getSimpleName();
		final WeakReference<DayFragment> dayfragmentRef;
		public FinanceAdapter(DayFragment dayFragment) {
			super(dayFragment.getActivity(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
			dayfragmentRef = new WeakReference<DayFragment>(dayFragment);
		}

		@Override
		public View newView(final Context context, Cursor cursor, ViewGroup viewGroup) {
			final FinanceItemView view = new FinanceItemView(context);
			final long id = cursor.getLong(cursor.getColumnIndex(FinanceTable._ID));
			view.setFinanceItemViewCallback(new FinanceItemViewCallback() {
				
				@Override
				public void onSubmit(boolean positive, String name, float amount) {
					
				}
				
				@Override
				public void onRemoveButtonClick() {
					Log.d(TAG, "onRemoveButtonClick");
					DBController.deleteFinanceItemById(context, id);
					final DayFragment fragment = dayfragmentRef.get();
					if (fragment != null) {
						fragment.refreshList();
					}
				}
			});
			return view;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			final FinanceItem item = new FinanceItem(cursor);
			((FinanceItemView) view).bindView(item);
		}
	}
}
