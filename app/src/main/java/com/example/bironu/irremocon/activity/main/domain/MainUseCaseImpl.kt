package com.example.bironu.irremocon.activity.main.domain

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import com.example.bironu.irremocon.activity.main.MainConstatnt
import com.example.bironu.irremocon.activity.main.data.MainRepository
import com.example.bironu.irremocon.activity.main.data.SerialPort
import com.example.bironu.irremocon.activity.main.presentation.MainPresenter
import com.example.bironu.irremocon.db.IrCodeTable
import io.reactivex.Maybe
import io.reactivex.MaybeOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 *
 */
class MainUseCaseImpl(repository: MainRepository, presenter: MainPresenter, port: SerialPort) : MainUseCase {
    companion object {
        val TAG: String = this.javaClass.simpleName
        val TIMEOUT_READ_MILLISECONDS = 30000
        val TIMEOUT_WRITE_MILLISECONDS = 300000
        val READ_BUFFER_SIZE = 4096
    }

    private val mPort: SerialPort = port
    private var mDisposable: Disposable? = null

    private val mRepository: MainRepository = repository
    private val mPresenter: MainPresenter = presenter

    override fun initialize(savedInstanceState: Bundle?) {
        // Find all available drivers from attached devices.

        mRepository.requestCursorLoad(IrCodeTable.LOADER_ID, { id: Int, cursor: Cursor? ->
            when (id) {
                IrCodeTable.LOADER_ID -> {
                    Log.d("usecase", "cursorloadcallback " + cursor)
                    mPresenter.setIrCodeListCursor(cursor)
                }
            }
        })
    }

    override fun savedInstance(outState: Bundle?) {

    }

    override fun restoreInstance(savedInstanceState: Bundle?) {

    }

    override fun dispose() {
    }

    override fun prepareLearnIrCode() {
        mPresenter.showLearningDialog()
        mDisposable = Maybe
                .create(MaybeOnSubscribe<String> { emitter ->
                    try {
                        var readString: String? = null
                        val sendBuffer = "r".toByteArray()
                        val numBytesWrite = mPort.write(sendBuffer, TIMEOUT_WRITE_MILLISECONDS)
                        if (numBytesWrite != null) {
                            val readBuffer = ByteArray(READ_BUFFER_SIZE)
                            val numBytesRead = mPort.read(readBuffer, TIMEOUT_READ_MILLISECONDS)
                            Log.d(TAG, "Read $numBytesRead bytes.")
                            if (numBytesRead != null) {
                                readString = String(readBuffer, 0, numBytesRead)
                            }
                        }
                        if (readString != null) {
                            emitter.onSuccess(readString)
                        }
                        else {
                            emitter.onComplete()
                        }
                    }
                    catch (ex: Exception) {
                        emitter.onError(ex)
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ value ->
                    mPresenter.hideLearningDialog()
                    mPresenter.showRegisterIrCodeDialog(value)
                    mPresenter.showToast("success")
                }, { e ->
                    mPresenter.hideLearningDialog()
                    mPresenter.showToast("error")
                    e.printStackTrace()
                }, {
                    mPresenter.hideLearningDialog()
                    mPresenter.showToast("timeout")
                })
    }

    override fun saveIrCode(name: String, code: String) {
        mRepository.saveIrCode(name, code)
    }

    override fun cancelLearnIrCode() {
        //mPresenter.hideLearningDialog()
        mDisposable?.dispose()
        mDisposable = null
    }

    override fun deleteIrCode(param: Bundle) {
        val id = param.getLong(MainConstatnt.PARAM_ID_TAG)
        Log.d("usecase", "deleteIrCode " + id)
        mRepository.deleteIrCode(id)
    }

    override fun shortTapIrCode(id: Long, name: String, code: String) {
        mDisposable = Maybe
                .create(MaybeOnSubscribe<String> { emitter ->
                    try {
                        var readString: String? = null
                        val sendBuffer = code.toByteArray()
                        val numBytesWrite = mPort.write(sendBuffer, TIMEOUT_WRITE_MILLISECONDS)
                        if (numBytesWrite != null) {
                            val readBuffer = ByteArray(READ_BUFFER_SIZE)
                            val numBytesRead = mPort.read(readBuffer, TIMEOUT_READ_MILLISECONDS)
                            Log.d(TAG, "Read $numBytesRead bytes.")
                            if (numBytesRead != null) {
                                readString = String(readBuffer, 0, numBytesRead)
                            }
                        }
                        if (readString != null) {
                            emitter.onSuccess(readString)
                        }
                        else {
                            emitter.onComplete()
                        }
                    }
                    catch (ex: Exception) {
                        emitter.onError(ex)
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ value ->
                    mPresenter.showToast(value)
                }, { e ->
                    mPresenter.showToast("error")
                    e.printStackTrace()
                }, {
                    mPresenter.showToast("timeout")
                })
    }

    override fun longTapIrCode(id: Long, name: String, code: String) {
        mPresenter.showDeleteConfirmDialog(id)
    }
}