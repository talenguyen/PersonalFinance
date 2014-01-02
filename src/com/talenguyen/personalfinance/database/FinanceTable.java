package com.talenguyen.personalfinance.database;

import java.util.ArrayList;
import java.util.List;

import com.talenguyen.simplecontentprovider.dbtable.AbsDBTable;
import com.talenguyen.simplecontentprovider.dbtable.DBColumn;
import com.talenguyen.simplecontentprovider.dbtable.DataType;

/**
 * @author: GIANG
 * @date: 1/1/14
 * @time: 1:12 PM
 */
public class FinanceTable extends AbsDBTable {

    private static final String TAG = FinanceTable.class.getSimpleName();
    public static final String NAME = "name";
    public static final String AMOUNT = "amount";
    public static final String POSITIVE = "positive";
    public static final String DATE = "date";
    public static final String TIME = "time";


    private static final List<DBColumn> COLUMNS;
    static {
        COLUMNS = new ArrayList<DBColumn>();
        COLUMNS.add(new DBColumn(NAME, DataType.TEXT, null));
        COLUMNS.add(new DBColumn(AMOUNT, DataType.REAL, null));
        COLUMNS.add(new DBColumn(POSITIVE, DataType.BOOL, null));
        COLUMNS.add(new DBColumn(DATE, DataType.TEXT, null));
        COLUMNS.add(new DBColumn(TIME, DataType.TEXT, null));
    }

    /**
     * Constructor of {@link com.thechange.libs.dbtable.AbsDBTable}
     *
     * @param authority the <b>authority</b> of {@link android.content.ContentProvider}
     */
    public FinanceTable(String authority) {
        super(authority);
    }

    @Override
    public String getTableName() {
        return TAG;
    }

    @Override
    public List<DBColumn> getColumns() {
        return COLUMNS;
    }
}
