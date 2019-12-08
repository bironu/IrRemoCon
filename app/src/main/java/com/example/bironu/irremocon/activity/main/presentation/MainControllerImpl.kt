package com.example.bironu.irremocon.activity.main.presentation

import android.content.DialogInterface
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.CursorAdapter
import com.example.bironu.irremocon.R
import com.example.bironu.irremocon.activity.main.MainConstatnt
import com.example.bironu.irremocon.activity.main.domain.MainUseCase
import com.example.bironu.irremocon.db.IrCodeTable

/**
 *
 */
class MainControllerImpl(useCase: MainUseCase) : MainController {
    private val mUseCase: MainUseCase = useCase

    override fun onCreate(savedInstanceState: Bundle?) {
        mUseCase.initialize(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        mUseCase.savedInstance(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        mUseCase.restoreInstance(savedInstanceState)
    }

    override fun onDestroy() {
        mUseCase.dispose()
    }

    override fun onDialogItemClick(tag: String?, which: Int, param: Bundle?) {
        Log.d("controller", "onDialogItemClick $tag, $which, $param")
        when (tag) {
            MainConstatnt.DELETE_CONFIRM_DIALOG_TAG -> {
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        if (param != null) {
                            mUseCase.deleteIrCode(param)
                        }
                    }
                }
            }
            MainConstatnt.LEARNING_DIALOG_TAG -> {
            }
            MainConstatnt.REGISTER_IR_CODE_DIALOG -> {
            }
        }
    }

    override fun onDialogCancel(tag: String?) {
        when (tag) {
            MainConstatnt.LEARNING_DIALOG_TAG -> mUseCase.cancelLearnIrCode()
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            val adapter = parent.adapter
            if (adapter is CursorAdapter) {
                val cursor = adapter.getItem(position)
                if (cursor is Cursor) {
                    val name = cursor.getString(cursor.getColumnIndex(IrCodeTable.COLUMN_NAME))
                    val code = cursor.getString(cursor.getColumnIndex(IrCodeTable.COLUMN_CODE))
                    mUseCase.shortTapIrCode(id, name, code)
                }
            }
        }
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        var result: Boolean = false
        if (parent != null) {
            val adapter = parent.adapter
            if (adapter is CursorAdapter) {
                val cursor = adapter.getItem(position)
                if (cursor is Cursor) {
                    val name = cursor.getString(cursor.getColumnIndex(IrCodeTable.COLUMN_NAME))
                    val code = cursor.getString(cursor.getColumnIndex(IrCodeTable.COLUMN_CODE))
                    mUseCase.longTapIrCode(id, name, code)
                    result = true
                }
            }
        }
        return result
    }

    override fun onOptionsItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_learn -> {
                mUseCase.prepareLearnIrCode()
                return true
            }
            else -> return false
        }
    }

    override fun onSaveIrCode(name: String, code: String) {
        mUseCase.saveIrCode(name, code)
    }

}
