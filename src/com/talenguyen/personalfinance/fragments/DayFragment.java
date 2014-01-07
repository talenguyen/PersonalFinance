package com.talenguyen.personalfinance.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.talenguyen.framework.adapter.CustomAdapter;
import com.talenguyen.framework.adapter.CustomAdapter.AdapterViewFactory;
import com.talenguyen.framework.adapter.CustomAdapter.ViewHolder;
import com.talenguyen.framework.utils.DateTimeUtils;
import com.talenguyen.personalfinance.R;
import com.talenguyen.personalfinance.data.FinanceItem;
import com.talenguyen.personalfinance.database.DBController;
import com.talenguyen.personalfinance.database.FinanceTable;
import com.talenguyen.personalfinance.views.FinanceItemView;
import com.talenguyen.personalfinance.views.FinanceItemView.FinanceItemViewCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: GIANG
 * @date: 1/1/14
 * @time: 8:34 PM
 */
public class DayFragment extends Fragment implements LoaderCallbacks<Cursor> {

	private static final String TAG = DayFragment.class.getSimpleName();
	private CustomAdapter<FinanceItem> mAdapter;
	private TextView mTotalAmountView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new CustomAdapter<FinanceItem>(new AdapterViewFactory<FinanceItem>() {

			@Override
			public ViewHolder<FinanceItem> newViewHolder() {
				return new ViewHolder<FinanceItem>() {

					FinanceItemView view;

					@Override
					public void findView(View view) {
						this.view = (FinanceItemView) view;
					}

					@Override
					public void bindView(final FinanceItem item) {
						view.bindView(item);
						view.setFinanceItemViewCallback(new FinanceItemViewCallback() {

							@Override
							public void onNewItemSubmit(boolean positive, String name, float amount) {

							}

							@Override
							public void onRemoveButtonClick() {
								// final FinanceItem item = mAdapter.getItem(position);
								DBController.deleteFinanceItemByIdAsync(getActivity(), item.getId());
								mAdapter.remove(item);
								mAdapter.notifyDataSetChanged();

							}

							@Override
							public void onUpdateItemSubmit(boolean positive,
									String name, float amount) {
								DBController.updateFinanceItemAsync(getActivity(), item.getId(), positive, name, amount);
							}
						});
					}
				};
			}

			@Override
			public View newView(ViewGroup parent) {
				return new FinanceItemView(getActivity());
			}


		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View localView = inflater.inflate(R.layout.fragment_day,
				container, false);
		mTotalAmountView = (TextView) localView
				.findViewById(R.id.totalAmountTextView);
		final ListView listView = (ListView) localView
				.findViewById(R.id.list_view);
		final FinanceItemView footer = new FinanceItemView(getActivity());
		footer.setFinanceItemViewCallback(new FinanceItemViewCallback() {
			@Override
			public void onRemoveButtonClick() {

			}

			@Override
			public void onNewItemSubmit(final boolean positive, final String name,
					final float amount) {
				DBController.insertFinanceItemAsync(getActivity(), positive, name, amount);
				final FinanceItem item = new FinanceItem(0, name, amount, positive, DateTimeUtils.formatDate(System.currentTimeMillis()), DateTimeUtils.formatTime(System.currentTimeMillis()));
				mAdapter.add(item);
				mAdapter.notifyDataSetChanged();
				footer.reset();
				listView.setSelection(mAdapter.getCount() - 1);
			}

			@Override
			public void onUpdateItemSubmit(boolean positive, String name,
					float amount) {
				// TODO Auto-generated method stub

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
		Log.d(TAG, "onLoadFinished");
		final float totalAmount = getTotalAmount(cursor);
		mTotalAmountView.setText(String.valueOf(totalAmount));
		mAdapter.swapData(getItems(cursor));
	}

	@Override
	public void onLoaderReset(
			android.support.v4.content.Loader<Cursor> cursorLoader) {
		mAdapter.swapData(null);
	}

	private List<FinanceItem> getItems(Cursor cursor) {
		if (cursor == null || !cursor.moveToFirst()) {
			return null;
		}

		final List<FinanceItem> result = new ArrayList<FinanceItem>();
		do {
			final FinanceItem item = new FinanceItem(cursor);
			result.add(item);
		} while (cursor.moveToNext());

		return result;
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

}
