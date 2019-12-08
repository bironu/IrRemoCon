package com.example.bironu.irremocon.activity.main.presentation

import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView

/**
 *
 */
interface MainController : AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    fun onCreate(savedInstanceState: Bundle?)
    fun onSaveInstanceState(outState: Bundle?)
    fun onRestoreInstanceState(savedInstanceState: Bundle?)
    fun onDestroy()
    fun onDialogItemClick(tag: String?, which: Int, param: Bundle?)
    fun onDialogCancel(tag: String?)
    fun onOptionsItemClick(item: MenuItem?): Boolean
    fun onSaveIrCode(name: String, code: String)
}