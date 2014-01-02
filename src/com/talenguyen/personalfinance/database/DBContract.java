package com.talenguyen.personalfinance.database;

import com.talenguyen.simplecontentprovider.dbtable.AbsDBTable;

/**
 * @author: GIANG
 * @date: 1/1/14
 * @time: 8:23 PM
 */
public class DBContract {
    public static final String AUTHORITY = "com.talenguyen.personalfinance.provider";
    public static final String DB_NAME = "PersonalFinance";
    public static final int DB_VERSION = 1;
    public static final AbsDBTable TABLE_FINANCE = new FinanceTable(AUTHORITY);
    public static final AbsDBTable[] TABLES = new AbsDBTable[]{TABLE_FINANCE};
}
