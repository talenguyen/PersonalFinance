package com.talenguyen.personalfinance.database;

import android.net.Uri;

import com.talenguyen.simplecontentprovider.AbsContentProvider;
import com.talenguyen.simplecontentprovider.dbtable.AbsDBTable;

/**
 * @author: GIANG
 * @date: 1/1/14
 * @time: 1:18 PM
 */
public class PFContentProvider extends AbsContentProvider {

    @Override
    protected String getDBName() {
        return DBContract.DB_NAME;
    }

    @Override
    protected int getDBVersion() {
        return DBContract.DB_VERSION;
    }

    @Override
    protected AbsDBTable[] getTables() {
        return DBContract.TABLES;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
