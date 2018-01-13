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
import com.tumpraktikum.roboticsimulatorcontroller.helper.ConnectThreadException
import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothManager
import com.tumpraktikum.roboticsimulatorcontroller.helper.interfaces.MessageConstants
import javax.inject.Inject


class MainPresenter @Inject constructor(private val mBluetoothmanager: MyBluetoothManager, private val mContext:Context) :
        MainContract.Presenter {

    private lateinit var mView: MainContract.View


    private var mPermissionNearbyBluetoothDevices: Boolean = false
    private lateinit var mConnectThread: ConnectThread

    private lateinit var mAdapter: BluetoothListAdapter
    private lateinit var mPairedAdapter: BluetoothListAdapter

    private var mItems: ArrayList<BluetoothDevice> = ArrayList()
    private var mPairedItems: ArrayList<BluetoothDevice> = ArrayList()


    override fun takeView(view: MainContract.View) {
        this.mView = view
        if (isBluetoothOn()) {
            mView.showBluetoothDevices()
            initBluetoothAdapter()
        }else
            mView.showEmptyView()
    }

    override fun setPermissionNearbyBluetoothDevices(permissionGranted: Boolean) {
        // ToDo: Only for not paired devices, or for all devices? Isn't used now
        mPermissionNearbyBluetoothDevices = permissionGranted
    }

    private fun isBluetoothOn(): Boolean {
        return mBluetoothmanager.isBluetoothEnabled()
    }

    private fun initBluetoothAdapter() {
        initBluetoothAdapterPairedDevices()
        initBluetoothAdapterNotPairedDevices()
        mBluetoothmanager.startDiscovery()
    }

    private fun initBluetoothAdapterPairedDevices() {
        mPairedAdapter = mView.setPairedAdapter()

        mPairedItems.clear()
        mPairedAdapter.setItems(mPairedItems)
        mPairedAdapter.notifyDataSetChanged()

        mBluetoothmanager.queryPairedDevices().forEach { bluetoothDevice -> mPairedItems.add(bluetoothDevice) }
        mPairedAdapter.setItems(mPairedItems)
        mPairedAdapter.notifyDataSetChanged()

        mView.setPairedListHeight()
    }

    private fun initBluetoothAdapterNotPairedDevices() {
        mAdapter = mView.setAdapter()
        mAdapter.setItems(mItems)
        mAdapter.notifyDataSetChanged()
        mView.setOtherListHeight()
    }

    override fun cancelDiscovery() {
        mBluetoothmanager.cancelDiscovery()
    }

    override fun turnBluetoothOn(context: AppCompatActivity) {
        mBluetoothmanager.enableBluetooth(context)
    }

    override fun connectToDevice() {
    }

    override fun bluetoothDeviceFound(context: Context, intent: Intent) {
        val action = intent.action
        if (BluetoothDevice.ACTION_FOUND == action) {
            // Discovery has found a device. Get the BluetoothDevice
            // object and its info from the Intent.
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            addBluetoothDeviceToNotPairedDevices(device)
        }
    }

    private fun addBluetoothDeviceToNotPairedDevices(device: BluetoothDevice) {
        if (!mItems.contains(device) && !mPairedItems.contains(device)) {
            mItems.add(device)
            mAdapter.setItems(mItems)
            mAdapter.notifyDataSetChanged()
            mView.setOtherListHeight()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MyBluetoothManager.REQUEST_ENABLE_BT && resultCode == -1) {
            // bluetooth turned on successfully
            mView.showBluetoothDevices()
            initBluetoothAdapter()
        } else {
            //something wen wrong with bluetooth intent
            mView.showToast(mContext.getString(R.string.bluetoothProblem))
        }
    }

    override fun onItemClick(position: Int, pairedDevice: Boolean) {
        mView.showLoading()
        val mHandler = getHandler()
        try{
            mConnectThread = getConnectThread(position, pairedDevice, mHandler)
        }catch (e: ConnectThreadException) {
            handleConnectionError()
        }
        mConnectThread.start()
    }

    private fun getHandler() : Handler{
        return Handler(Handler.Callback {
            message: Message? ->
            when (message?.what) {
                MessageConstants.MESSAGE_SWITCH_ACTIVITY -> mView.openControllerActivity()
                MessageConstants.MESSAGE_TOAST -> mView.showToast( message.data?.getString("toast") ?: "message is null")
                MessageConstants.MESSAGE_ERROR_BLUETOOTH_CONNECTION -> handleConnectionError()
            }
            false
        })
    }

    private fun getConnectThread(position: Int, pairedDevice: Boolean, mHandler: Handler): ConnectThread {
        return if (pairedDevice) {
            Log.d("test", "Name: " + mPairedItems[position].address)
            ConnectThread(mPairedItems[position], mBluetoothmanager, mHandler)
        } else {
            Log.d("test", "Name: " + mItems[position].address)
            ConnectThread(mItems[position], mBluetoothmanager, mHandler)
        }
    }

    private fun handleConnectionError() {
        mView.showBluetoothDevices()
        mView.showToast("Couldn't connect to the device")
    }
}