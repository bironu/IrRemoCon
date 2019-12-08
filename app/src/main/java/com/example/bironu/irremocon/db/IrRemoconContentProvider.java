package com.example.bironu.irremocon.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;

/**
 *
 */

public class IrRemoconContentProvider extends ContentProvider {
    // Authority
    public static final String AUTHORITY = IrRemoconContentProvider.class
            .getName()
            .toLowerCase();

    private static final int IR_CODE_LIST = 1;
    private static final int IR_CODE_ID = 2;

    // 利用者がメソッドを呼び出したURIに対応する処理を判定処理に使用します
    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, IrCodeTable.TABLE_NAME, IR_CODE_LIST);
        sUriMatcher.addURI(AUTHORITY, IrCodeTable.TABLE_NAME + "/#", IR_CODE_ID);
    }

    // DBHelperのインスタンス
    private IrRemoconDatabaseHelper mDBHelper;

    // コンテンツプロバイダの作成
    @Override
    public boolean onCreate()
    {
        mDBHelper = new IrRemoconDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
        case IR_CODE_LIST:
            qb.setTables(IrCodeTable.TABLE_NAME);
            break;
        case IR_CODE_ID:
            qb.setTables(IrCodeTable.TABLE_NAME);
            qb.appendWhere(IrCodeTable._ID + "=" + uri.getLastPathSegment());
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(this
                                          .getContext()
                                          .getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values)
    {
        String insertTable;
        Uri contentUri;
        switch (sUriMatcher.match(uri)) {
        case IR_CODE_LIST:
            insertTable = IrCodeTable.TABLE_NAME;
            contentUri = IrCodeTable.CONTENT_URI;
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final long rowId = db.insert(insertTable, null, values);
        if (rowId >= 0) {
            Uri returnUri = ContentUris.withAppendedId(contentUri, rowId);
            getContext()
                    .getContentResolver()
                    .notifyChange(returnUri, null);
            return returnUri;
        }
        else {
            throw new IllegalArgumentException("Failed to insert row into " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        case IR_CODE_LIST:
            count = db.update(IrCodeTable.TABLE_NAME, values, selection, selectionArgs);
            break;
        case IR_CODE_ID:
            String id = uri
                    .getPathSegments()
                    .get(1);
            count = db.update(IrCodeTable.TABLE_NAME, values, IrCodeTable._ID
                    + "="
                    + id
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                    + ')' : ""), selectionArgs);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext()
                .getContentResolver()
                .notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs)
    {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        case IR_CODE_LIST:
            count = db.delete(IrCodeTable.TABLE_NAME, selection, selectionArgs);
            break;
        case IR_CODE_ID:
            String id = uri
                    .getPathSegments()
                    .get(1);
            count = db.delete(IrCodeTable.TABLE_NAME, IrCodeTable._ID
                    + "="
                    + id
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                    + ')' : ""), selectionArgs);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext()
                .getContentResolver()
                .notifyChange(uri, null);
        return count;
    }

    // コンテントタイプ取得
    @Override
    public String getType(@NonNull Uri uri)
    {
        switch (sUriMatcher.match(uri)) {
        case IR_CODE_LIST:
            return IrCodeTable.CONTENT_TYPE;
        case IR_CODE_ID:
            return IrCodeTable.CONTENT_ITEM_TYPE;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }
}
