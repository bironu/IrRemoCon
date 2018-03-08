package com.example.bironu.irremocon.activity.main.domain

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import com.example.bironu.irremocon.R
import com.example.bironu.irremocon.activity.main.MainConstatnt
import com.example.bironu.irremocon.activity.main.data.MainRepository
import com.example.bironu.irremocon.activity.main.data.SerialPort
import com.example.bironu.irremocon.activity.main.presentation.MainPresenter
import com.example.bironu.irremocon.db.IrCodeTable
import io.reactivex.Maybe
import io.reactivex.MaybeOnSubscribe
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 *
 */
class MainUseCaseImpl(repository: MainRepository, presenter: MainPresenter, port: SerialPort) :
        MainUseCase {
    companion object {
        val TAG: String = this.javaClass.simpleName
        val TIMEOUT_READ_MILLISECONDS = 30000
        val TIMEOUT_WRITE_MILLISECONDS = 300000
        val READ_BUFFER_SIZE = 4096
    }

    private val mPort: SerialPort = port
    private var mWriteDisposable: Disposable? = null
    private var mReadDisposable: Disposable? = null
    private var mSerialRead: Maybe<String> = Maybe
            .create(MaybeOnSubscribe<String> { emitter ->
                try {
                    val readBuffer = ByteArray(READ_BUFFER_SIZE)
                    val numBytesRead = mPort.read(readBuffer, TIMEOUT_READ_MILLISECONDS) ?: 0
                    Log.d(TAG, "Read $numBytesRead bytes.")
                    if (numBytesRead != 0) {
                        val readString = String(readBuffer, 0, numBytesRead)
                        emitter.onSuccess(readString)
                    } else {
                        emitter.onComplete()
                    }
                }
                catch (ex: Throwable) {
                    emitter.onError(ex)
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val mRepository: MainRepository = repository
    private val mPresenter: MainPresenter = presenter

    override fun initialize(savedInstanceState: Bundle?) {
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
        mWriteDisposable = Single
                .create(SingleOnSubscribe<Int> { emitter ->
                    try {
                        val sendBuffer = "r".toByteArray()
                        val numBytesWrite = mPort.write(sendBuffer, TIMEOUT_WRITE_MILLISECONDS) ?: 0
                        emitter.onSuccess(numBytesWrite)
                    }
                    catch (ex: Exception) {
                        emitter.onError(ex)
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ value ->
                               if (value > 0) {
                                   mReadDisposable = mSerialRead
                                           .subscribe({ value2 ->
                                                          Log.d(TAG, value2)
                                                          mPresenter.hideLearningDialog()
                                                          if (!"NG".equals(value2.trim())) {
                                                              mPresenter.showRegisterIrCodeDialog(
                                                                      value2)
                                                          } else {
                                                              mPresenter.showToast(
                                                                      mRepository.getString(
                                                                              R.string.message_error))
                                                          }
                                                      }, { e ->
                                                          mPresenter.hideLearningDialog()
                                                          mPresenter.showToast(
                                                                  mRepository.getString(
                                                                          R.string.message_error))
                                                          e.printStackTrace()
                                                      }, {
                                                          mPresenter.hideLearningDialog()
                                                          mPresenter.showToast(
                                                                  mRepository.getString(
                                                                          R.string.message_timeout))
                                                      })
                               } else {
                                   mPresenter.showToast(
                                           mRepository.getString(R.string.message_timeout))
                               }
                           }, { e ->
                               mPresenter.hideLearningDialog()
                               mPresenter.showToast(mRepository.getString(R.string.message_error))
                               e.printStackTrace()
                           })
    }

    override fun saveIrCode(name: String, code: String) {
        mRepository.saveIrCode(name, code)
    }

    override fun cancelLearnIrCode() {
        //mPresenter.hideLearningDialog()
        Log.d(TAG, "cancelLearnIrCode")
        mWriteDisposable?.dispose()
        mWriteDisposable = null
        mReadDisposable?.dispose()
        mReadDisposable = null
    }

    override fun deleteIrCode(param: Bundle) {
        val id = param.getLong(MainConstatnt.PARAM_ID_TAG)
        Log.d("usecase", "deleteIrCode " + id)
        mRepository.deleteIrCode(id)
    }

    override fun shortTapIrCode(id: Long, name: String, code: String) {
        mWriteDisposable = Single
                .create(SingleOnSubscribe<Int> { emitter ->
                    try {
                        val sendBuffer = code.toByteArray()
                        val numBytesWrite = mPort.write(sendBuffer, TIMEOUT_WRITE_MILLISECONDS) ?: 0
                        emitter.onSuccess(numBytesWrite)
                    }
                    catch (ex: Exception) {
                        emitter.onError(ex)
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ value ->
                               if (value > 0) {
                                   mReadDisposable = mSerialRead
                                           .subscribe({ value2 ->
                                                          mPresenter.showToast(value2.trim())
                                                      }, { e ->
                                                          mPresenter.showToast(
                                                                  mRepository.getString(
                                                                          R.string.message_error))
                                                          e.printStackTrace()
                                                      }, {
                                                          mPresenter.showToast(
                                                                  mRepository.getString(
                                                                          R.string.message_timeout))
                                                      })
                               } else {
                                   mPresenter.showToast(
                                           mRepository.getString(R.string.message_timeout))
                               }
                           }, { e ->
                               mPresenter.showToast(mRepository.getString(R.string.message_error))
                               e.printStackTrace()
                           })
    }

    override fun longTapIrCode(id: Long, name: String, code: String) {
        mPresenter.showDeleteConfirmDialog(id)
    }
}