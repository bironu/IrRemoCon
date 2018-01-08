package com.example.bironu.irremocon.activity.main

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.hardware.usb.UsbManager
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.bironu.common.dialog.AlertDialogFragment
import com.example.bironu.irremocon.R
import com.example.bironu.irremocon.activity.main.data.MainRepositoryImpl
import com.example.bironu.irremocon.activity.main.data.SerialPort
import com.example.bironu.irremocon.activity.main.data.SerialPortImpl
import com.example.bironu.irremocon.activity.main.domain.MainUseCaseImpl
import com.example.bironu.irremocon.activity.main.presentation.MainController
import com.example.bironu.irremocon.activity.main.presentation.MainControllerImpl
import com.example.bironu.irremocon.activity.main.presentation.MainPresenterImpl
import com.example.bironu.irremocon.activity.main.presentation.SaveIrCodeDialog
import kotlinx.android.synthetic.main.main_activity.*

/**
 *
 */
class MainActivity : Activity(), AlertDialogFragment.OnItemClickListener, AlertDialogFragment.OnCancelListener, SaveIrCodeDialog.OnSaveIrCodeListener {
    private var mPort: SerialPort? = null
    private var mController: MainController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

//        val binding = DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity)
//        val viewModel = MainViewModel()
//        binding.setMainViewModel(viewModel)

        this.ir_code_list.adapter = IrCodeListAdapter(this)

        val repository = MainRepositoryImpl(this)
        mPort = SerialPortImpl(this.getSystemService(Context.USB_SERVICE) as UsbManager)
        val presenter = MainPresenterImpl(this)
        val useCase = MainUseCaseImpl(repository, presenter, mPort!!)
        mController = MainControllerImpl(useCase)
        mController?.onCreate(savedInstanceState)

//        this.toolbar.setNavigationOnClickListener { v -> finish() }
//        this.toolbar.inflateMenu(R.menu.main_menu)
//        this.toolbar.setOnMenuItemClickListener(mController)

        this.ir_code_list.setOnItemClickListener(mController)
        this.ir_code_list.setOnItemLongClickListener(mController)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mController?.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        mController?.onRestoreInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        mController?.onDestroy()
        mPort?.close()
        super.onDestroy()
    }

    override fun onItemClick(dialog: AlertDialogFragment, which: Int, param: Bundle?) {
        Log.d("activity", "onItemClick")
        mController?.onDialogItemClick(dialog.tag, which, param)
    }

    override fun onCancel(dialog: AlertDialogFragment, param: Bundle?) {
        mController?.onDialogCancel(dialog.tag)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = MenuInflater(this)
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return mController?.onOptionsItemClick(item) ?: false
    }

    override fun onSaveIrCode(name: String, code: String) {
        mController?.onSaveIrCode(name, code)
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
//            System.loadLibrary("native-lib")
        }
    }
}
