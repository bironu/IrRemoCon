package com.example.bironu.irremocon.activity.main.domain

import android.os.Bundle
import io.reactivex.disposables.Disposable

/**
 *
 */
interface MainUseCase {
    fun initialize(savedInstanceState: Bundle?)
    fun savedInstance(outState: Bundle?)
    fun restoreInstance(savedInstanceState: Bundle?)
    fun dispose()

    fun prepareLearnIrCode()
    fun cancelLearnIrCode()
    fun saveIrCode(name: String, code: String)
    fun deleteIrCode(param: Bundle)

    fun shortTapIrCode(id: Long, name: String, code: String)
    fun longTapIrCode(id: Long, name: String, code: String)
}