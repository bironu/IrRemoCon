package com.example.bironu.irremocon.activity.main.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import com.example.bironu.irremocon.R
import com.example.bironu.irremocon.activity.main.MainConstatnt.Companion.ARGS_IR_CODE_TAG

/**
 *
 */
class SaveIrCodeDialog() : DialogFragment() {

    interface OnSaveIrCodeListener {
        fun onSaveIrCode(name: String, code: String)
    }

    companion object {
        val TAG = this.javaClass.simpleName
        fun newInstance(code: String): SaveIrCodeDialog {
            val args = Bundle()
            args.putString(ARGS_IR_CODE_TAG, code)

            val instance = SaveIrCodeDialog()
            instance.arguments = args
            return instance
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = this.activity
        val inflater = LayoutInflater.from(activity)
        val customView = inflater.inflate(R.layout.save_ir_code_dialog, null)
        val builder = AlertDialog.Builder(activity)
        builder
                .setTitle(R.string.dialog_title_learn_success)
                .setMessage(R.string.dialog_message_learn_success)
                .setView(customView)
                .setPositiveButton(R.string.button_save, { dialog: DialogInterface, which: Int ->
                    val code = this.arguments.getString(ARGS_IR_CODE_TAG)
                    val nameEdit = customView.findViewById<EditText>(R.id.edit_ir_code_name)
                    val listener = activity as OnSaveIrCodeListener
                    listener.onSaveIrCode(nameEdit.text.toString(), code)
                })
                .setNegativeButton(android.R.string.cancel, null)

        return builder.create()
    }
}