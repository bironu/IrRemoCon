package com.example.bironu.irremocon.activity.main.presentation

import android.app.Activity
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.widget.CursorAdapter
import android.widget.Toast
import com.example.bironu.common.dialog.AlertDialogFragment
import com.example.bironu.irremocon.R
import com.example.bironu.irremocon.activity.main.MainActivity
import com.example.bironu.irremocon.activity.main.MainConstatnt
import kotlinx.android.synthetic.main.main_activity.*

/**
 *
 */
class MainPresenterImpl(activity: MainActivity) : MainPresenter {
    private val mActivity = activity

    private var mProgressDialog: AlertDialogFragment? = null

    override fun setIrCodeListCursor(cursor: Cursor?) {
        val adapter = mActivity.ir_code_list.adapter
        if (adapter is CursorAdapter) {
            adapter.swapCursor(cursor)
        }
    }

    override fun showLearningDialog() {
        mProgressDialog = AlertDialogFragment.Builder(mActivity)
                .setViewId(R.layout.simple_progress)
                .setCancelable(true)
                .setTitle(R.string.dialog_title_learning)
                .setMessage(R.string.dialog_message_learning)
                .setOnCancelListener(mActivity)
                .create()
        mProgressDialog!!.show(mActivity.fragmentManager, MainConstatnt.LEARNING_DIALOG_TAG)
    }

    override fun hideLearningDialog() {
        mProgressDialog?.dismiss()
        mProgressDialog = null
    }

    override fun showRegisterIrCodeDialog(code: String) {
        SaveIrCodeDialog.newInstance(code).show(mActivity.fragmentManager,
                                                MainConstatnt.REGISTER_IR_CODE_DIALOG)
    }

    override fun showDeleteConfirmDialog(id: Long) {
        val param = Bundle()
        param.putLong(MainConstatnt.PARAM_ID_TAG, id)
        AlertDialogFragment.Builder(mActivity)
                .setTitle(R.string.dialog_title_delete_confirm)
                .setMessage(R.string.dialog_message_delete_confirm)
                .setPositiveButton(android.R.string.ok, mActivity)
                .setNegativeButton(android.R.string.cancel, mActivity)
                .setParam(param)
                .create()
                .show(mActivity.fragmentManager, MainConstatnt.DELETE_CONFIRM_DIALOG_TAG)
    }

    override fun showToast(result: String) {
        Toast.makeText(mActivity, result, Toast.LENGTH_SHORT).show()
    }
}