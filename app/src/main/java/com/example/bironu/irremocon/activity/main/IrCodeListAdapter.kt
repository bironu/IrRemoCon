package com.example.bironu.irremocon.activity.main

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import com.example.bironu.irremocon.db.IrCodeTable

/**
 *
 */
class IrCodeListAdapter : CursorAdapter {

    constructor(context: Context) : this(context, null) {
    }

    constructor(context: Context, c: Cursor?) : super(context, c, false) {

    }

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        return inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        if (view is TextView && cursor != null) {
            view.setText(cursor.getString(cursor.getColumnIndex(IrCodeTable.COLUMN_NAME)))
        }
    }

}
