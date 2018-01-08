package com.example.bironu.irremocon.activity.main.data

/**
 *
 */
interface SerialPort : AutoCloseable {
    fun write(buf: ByteArray, timeout: Int): Int?
    fun read(buf: ByteArray, timeout: Int): Int?
}