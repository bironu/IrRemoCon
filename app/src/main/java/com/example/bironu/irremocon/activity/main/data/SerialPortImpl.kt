package com.example.bironu.irremocon.activity.main.data

import android.content.Context
import android.hardware.usb.UsbManager
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import java.io.IOException

/**
 *
 */
class SerialPortImpl : SerialPort {
    private var mPort: UsbSerialPort? = null

    constructor(manager: UsbManager) {
        // Find all available drivers from attached devices.
        val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager)
        if (availableDrivers.isEmpty()) {
            return
        }

        // Open a connection to the first available driver.
        val driver = availableDrivers[0]
        val connection = manager.openDevice(
                driver.device) ?: // You probably need to call UsbManager.requestPermission(driver.getDevice(), ..)
                         return

        // Read some data! Most have just one port (port 0).
        mPort = driver.ports[0]
        try {
            mPort?.open(connection)
            mPort?.setParameters(38400, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)
        }
        catch (e: IOException) {
            e.printStackTrace();
        }
    }

    override fun write(buf: ByteArray, timeout: Int): Int? {
        return mPort?.write(buf, timeout)
    }

    override fun read(buf: ByteArray, timeout: Int): Int? {
        return mPort?.read(buf, timeout)
    }

    override fun close() {
        try {
            mPort?.close()
        }
        catch (e: IOException) {
            e.printStackTrace();
        }
    }

}