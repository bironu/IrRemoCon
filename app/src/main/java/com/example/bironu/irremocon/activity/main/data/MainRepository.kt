package com.example.bironu.irremocon.activity.main.data

import android.database.Cursor
import android.hardware.usb.UsbManager

/**
 *
 */
interface MainRepository {
    fun requestCursorLoad(id: Int, listener: (Int, Cursor?) -> Unit)
    fun saveIrCode(name: String, code: String)
    fun deleteIrCode(id: Long)
    fun getString(id: Int): String
}
