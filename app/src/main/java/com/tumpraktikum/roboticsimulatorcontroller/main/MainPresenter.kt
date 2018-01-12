package com.tumpraktikum.roboticsimulatorcontroller.main

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.tumpraktikum.roboticsimulatorcontroller.R
import com.tumpraktikum.roboticsimulatorcontroller.helper.ConnectThread
import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothManager
import com.tumpraktikum.roboticsimulatorcontroller.helper.interfaces.MessageConstants
import javax.inject.Inject


class MainPresenter @Inject constructor(private val myBluetoothManager: MyBluetoothManager, private val mContext:Context) :
        MainContract.Presenter {

    private var mView: MainContract.View? = null
    private var mAdapter: BluetoothListAdapter? = null
    private var mPairedAdapter: BluetoothListAdapter? = null

    private var mConnectThread: ConnectThread? = null

    private var mItems: ArrayList<BluetoothDevice> = ArrayList()
    private var mPairedItems: ArrayList<BluetoothDevice> = ArrayList()


    override fun takeView(view: MainContract.View) {
        this.mView = view
        if (myBluetoothManager.isBluetoothSupported()) {
            checkIfBluetoothOn()
        } else {
            mView?.showNotSupported()
        }
    }

    override fun dropView() {
        this.mView = null
    }

    override fun checkIfBluetoothOn() {
        if (myBluetoothManager.isBluetoothEnabled()) {
            mView?.showBluetoothDevices()
            mAdapter = mView?.setAdapter()
            mAdapter?.setItems(mItems)
            mAdapter?.notifyDataSetChanged()
            mView?.setOtherListHeight()

            mPairedAdapter = mView?.setPairedAdapter()
            addPairedDevices()
        } else {
            mView?.showEmptyView()
        }
    }

    override fun startDiscovery() {
        mItems.clear()
        mAdapter?.setItems(mItems)
        mAdapter?.notifyDataSetChanged()
        mView?.setOtherListHeight()
        myBluetoothManager.startDiscovery()
    }

    override fun cancelDiscovery() {
        myBluetoothManager.cancelDiscovery();
    }

    override fun turnBluetoothOn(context: AppCompatActivity) {
        myBluetoothManager.enableBluetooth(context)
    }

    override fun connectToDevice() {
    }

    override fun bluetoothDeviceFound(context: Context, intent: Intent) {
        val action = intent.action
        if (BluetoothDevice.ACTION_FOUND == action) {
            // Discovery has found a device. Get the BluetoothDevice
            // object and its info from the Intent.
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            if (!mItems.contains(device)) {
                mItems.add(device)
                mAdapter?.setItems(mItems)
                mAdapter?.notifyDataSetChanged()
                mView?.setOtherListHeight()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MyBluetoothManager.REQUEST_ENABLE_BT && resultCode == -1) {
            // bluetooth turned on successfully
            startDiscovery()
            mView?.showBluetoothDevices()
            checkIfBluetoothOn()
        } else {
            //something wen wrong with bluetooth intent
            mView?.showToast(mContext.getString(R.string.bluetoothProblem))
        }
    }

    override fun onItemClick(position: Int, pairedDevice: Boolean) {
        val mHandler = Handler(Handler.Callback {
            message: Message? ->
            when (message?.what) {
                MessageConstants.MESSAGE_SWITCH_ACTIVITY -> mView?.openControllerActivity()
                MessageConstants.MESSAGE_TOAST -> mView?.showToast( message.data?.getString("toast") ?: "message is null")
            }
            false
        })
        if (pairedDevice) {
            Log.d("test", "Name: " + mItems[position].address)
            mConnectThread = ConnectThread(mItems[position], myBluetoothManager, mHandler)
        } else {
            Log.d("test", "Name: " + mPairedItems[position].address)
            mConnectThread = ConnectThread(mPairedItems[position], myBluetoothManager, mHandler)
        }
        mConnectThread?.start()
    }

    private fun addPairedDevices() {
        mPairedItems.clear()
        mPairedAdapter?.setItems(mPairedItems)
        mPairedAdapter?.notifyDataSetChanged()
        myBluetoothManager.queryPairedDevices()?.forEach { bluetoothDevice -> mPairedItems.add(bluetoothDevice) }
        mPairedAdapter?.setItems(mPairedItems)
        mPairedAdapter?.notifyDataSetChanged()
        mView?.setPairedListHeight()
    }
}