package com.tumpraktikum.roboticsimulatorcontroller.main

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothManager
import javax.inject.Inject




class MainPresenter @Inject constructor(private val myBluetoothManager: MyBluetoothManager) :
        MainContract.Presenter {

    private var mView: MainContract.View? = null
    private var mAdapter: BluetoothListAdapter? = null
    private var mPairedAdapter: BluetoothListAdapter? = null


    private var mItems :ArrayList<BluetoothDevice> = ArrayList()
    private var mPairedItems :ArrayList<BluetoothDevice> = ArrayList()



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

    override fun startDiscovery(){
        mItems.clear()
        mAdapter?.setItems(mItems)
        mAdapter?.notifyDataSetChanged()
        mView?.setOtherListHeight()
        myBluetoothManager.startDiscovery()
    }

    override fun cancelDiscovery()
    {
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
            if(!mItems.contains(device)) {
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
            mView?.showBluetoothDevices()
            checkIfBluetoothOn()
        }else
        {
            //something wen wrong with bluetooth intent
            mView?.showToast()
        }
    }

    private fun addPairedDevices()
    {
        mPairedItems.clear()
        mPairedAdapter?.setItems(mPairedItems)
        mPairedAdapter?.notifyDataSetChanged()
        myBluetoothManager.queryPairedDevices()?.forEach{ bluetoothDevice -> mPairedItems.add(bluetoothDevice) }
        mPairedAdapter?.setItems(mPairedItems)
        mPairedAdapter?.notifyDataSetChanged()
        mView?.setPairedListHeight()
    }
}