package com.example.bironu.irremocon.activity.main.data

import android.app.LoaderManager
import android.content.*
import android.database.Cursor
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import com.example.bironu.irremocon.activity.main.MainActivity
import com.example.bironu.irremocon.db.IrCodeTable
import org.jetbrains.annotations.Contract

/**
 *
 */
class MainRepositoryImpl(activity: MainActivity) : MainRepository, LoaderManager.LoaderCallbacks<Cursor> {
    private val mActivity: MainActivity = activity
    private var mListener: ((Int, Cursor?) -> Unit)? = null

    override fun requestCursorLoad(id: Int, listener: (Int, Cursor?) -> Unit) {
        mListener = listener
        mActivity.loaderManager.initLoader(id, null, this)
    }

    override fun saveIrCode(name: String, code: String) {
        val values = ContentValues(2)
        values.put(IrCodeTable.COLUMN_NAME, name)
        values.put(IrCodeTable.COLUMN_CODE, code)
        mActivity.contentResolver.insert(IrCodeTable.CONTENT_URI, values)
    }

    override fun deleteIrCode(id: Long) {
        Log.d("repository", "deleteIrCode " + ContentUris.withAppendedId(IrCodeTable.CONTENT_URI, id).toString())
        mActivity.contentResolver.delete(ContentUris.withAppendedId(IrCodeTable.CONTENT_URI, id), null, null)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        if (loader != null) {
            mListener?.invoke(loader.id, null)
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        if (loader != null) {
            mListener?.invoke(loader.id, data)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        when (id) {
            IrCodeTable.LOADER_ID -> return CursorLoader(mActivity, IrCodeTable.CONTENT_URI, null, null, null, null)
            else -> throw IllegalArgumentException("unknown Loader ID : " + id)
        }
    }

}