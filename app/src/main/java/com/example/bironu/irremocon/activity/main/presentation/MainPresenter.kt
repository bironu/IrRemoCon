package com.example.bironu.irremocon.activity.main.presentation

import android.database.Cursor
import android.widget.ListView

/**
 *
 */
interface MainPresenter {
    fun setIrCodeListCursor(cursor: Cursor?)
    fun showLearningDialog()
    fun hideLearningDialog()
    fun showRegisterIrCodeDialog(code: String)
    fun showDeleteConfirmDialog(id: Long)
    fun showToast(msg: String)
}
