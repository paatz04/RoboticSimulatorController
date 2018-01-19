package com.tumpraktikum.roboticsimulatorcontroller.main

import android.bluetooth.BluetoothAdapter
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


//The MyBluetoothManager instance and the context are injected using Dagger2 injection.
class MainPresenter @Inject constructor(private val mBluetoothmanager: MyBluetoothManager, private val mContext:Context) :
        MainContract.Presenter {

    private lateinit var mView: MainContract.View


    private var mPermissionNearbyBluetoothDevices: Boolean = false
    //Thread that connects to a specific bluetooth device
    private lateinit var mConnectThread: ConnectThread

    //adapters for MainActivity ListViews to render paired and other devices separately
    private lateinit var mAdapter: BluetoothListAdapter
    private lateinit var mPairedAdapter: BluetoothListAdapter

    //Arraylists to hold the paired and other bluetooth devices
    private var mItems: ArrayList<BluetoothDevice> = ArrayList()
    private var mPairedItems: ArrayList<BluetoothDevice> = ArrayList()


    override fun takeView(view: MainContract.View) {
        this.mView = view
        setViews()
    }

    private fun setViews(){
        if (isBluetoothOn()) { // if bluetooth is on the adapters and lists are shown and initialized
            mView.showBluetoothDevices()
            initBluetoothAdapter()
            /*
            if location permission is not given, discovery doesn't work and therefor a button to grant
            permission is rendered instead of the other device list instead. Paired devices can still
            be shown and would still work.
             */
            if(!mPermissionNearbyBluetoothDevices)
                mView.showPermissionButton()
        }else // if Bluetooth is turned off the empty view with a button to turn it on is shown
            mView.showEmptyView()
    }

    private fun isBluetoothOn(): Boolean {
        return mBluetoothmanager.isBluetoothEnabled()
    }

    private fun initBluetoothAdapter() {
        initBluetoothAdapterPairedDevices()
        initBluetoothAdapterNotPairedDevices()
        //start searching new bluetooth devices
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

        //update listHeight when updating the adapter content
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

    override fun bluetoothActionFound(context: Context, intent: Intent) {
        val action = intent.action
        when ( action) {
            BluetoothDevice.ACTION_FOUND -> {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                //add the device to the other section of the list (not paired device)
                addBluetoothDeviceToNotPairedDevices(device)
            }
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                //if the broadcast returns with STATE_OFF, STATE_TURNING_OFF then we have to update
                //the views and show the empty view instead of the listViews
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                when(state)
                {
                    BluetoothAdapter.STATE_OFF,BluetoothAdapter.STATE_TURNING_OFF -> {
                        setViews()
                    }
                }
            }
        }
    }

    private fun addBluetoothDeviceToNotPairedDevices(device: BluetoothDevice) {
        //if the device we discovered is not already part of our list, add it to the list and update the adapter
        if (!mItems.contains(device) && !mPairedItems.contains(device)) {
            mItems.add(device)
            mAdapter.setItems(mItems)
            mAdapter.notifyDataSetChanged()
            mView.setOtherListHeight()
        }
    }

    override fun locationPermissionGranted(granted: Boolean) {
        mPermissionNearbyBluetoothDevices = granted
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MyBluetoothManager.REQUEST_ENABLE_BT && resultCode == -1) {
            // bluetooth turned on successfully, show listViews and start rendering the available devices
            mView.showBluetoothDevices()
            initBluetoothAdapter()
        } else {
            //something wen wrong with bluetooth intent, show message to the user
            mView.showToast(mContext.getString(R.string.bluetoothProblem))
        }
    }

    /*
    when we click on a bluetooth device, we start showing a loadincBar, hide the lists to avoid
    clicking on multiple devices and to avoid connecting to muliple devices and we start the
    Conntect Thread which tries to establish a connection with the given device
     */
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

    /*
    This method create a new handler which is needed to communicate between threads. This handler is created
    in the main UI thread. Other threads can used this handler to send messages and communicate with the UI thread
    The ConnectionThread uses this handler to navigate to send MESSAGE_SWITCH_ACTIVITY for success,
    MESSAGE_TOAST if an exception happens or any other message needs to be toasted, or MESSAGE_ERROR_BLUETOOTH_CONNECTION
    for when there was an other issue while establishing the bluetooth connection.
     */
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

    /*
    Creates and returns a ConnectThread for the appropriate given BluetoothDevice
     */
    private fun getConnectThread(position: Int, pairedDevice: Boolean, mHandler: Handler): ConnectThread {
        return if (pairedDevice) {
            Log.d("test", "Name: " + mPairedItems[position].address)
            ConnectThread(mPairedItems[position], mBluetoothmanager, mHandler)
        } else {
            Log.d("test", "Name: " + mItems[position].address)
            ConnectThread(mItems[position], mBluetoothmanager, mHandler)
        }
    }

    /*
    If an error while connecting to a bluetooth device happens, the connect thread send's an error
    message through the handler. A toast is shown and the list is re-rendered.
     */
    private fun handleConnectionError() {
        mView.showBluetoothDevices()
        mView.showToast(mContext.getString(R.string.device_connection_issue))
    }
}