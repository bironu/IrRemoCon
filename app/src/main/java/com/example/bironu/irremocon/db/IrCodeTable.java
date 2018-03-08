package com.example.bironu.irremocon.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 *
 */
public class IrCodeTable implements BaseColumns {
    public static final String TABLE_NAME = "ircode";
    public static final String CONTENT_URI_STRING = "content://" +
                                                    IrRemoconContentProvider.AUTHORITY + "/" +
                                                    TABLE_NAME;
    public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.bironu." + TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.bironu." +
                                                   TABLE_NAME;
    public static final int LOADER_ID = 1;

    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_CODE = "CODE";

    public static final String CREATE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                                              _ID + " integer PRIMARY KEY" +
                                              ", " + COLUMN_NAME + " text" +
                                              ", " + COLUMN_CODE + " text" +
                                              ");";
    public static final String DROP_QUERY = "drop table if exists " + TABLE_NAME + ";";
}
