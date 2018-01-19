package com.tumpraktikum.roboticsimulatorcontroller.main

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.tumpraktikum.roboticsimulatorcontroller.R
import com.tumpraktikum.roboticsimulatorcontroller.application.App
import com.tumpraktikum.roboticsimulatorcontroller.controller.ControllerActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), MainContract.View {

    @Inject lateinit var mPresenter: MainPresenter

    val FINE_LOATION_ACCESS_REQUEST_CODE = 1

    /*
     Create a BroadcastReceiver for ACTION_FOUND AND ACTION_STATE_CHANGED.
     This method is called when either a device is found through discovery or when the bluetooth
     state changed (eg. Bluetooth turning off or turning on)
      */

    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            mPresenter.bluetoothActionFound(context, intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        If Bluetooth is available, setup buttons, register Receiver and ask for location permission
        in order to be able to search for devices through discovery.
         */
        if (isBluetoothAvailable()) {
            btnTurnOnBluetooth.setOnClickListener { mPresenter.turnBluetoothOn(this) }

            (application as App).appComponent.inject(this)

            val filter = IntentFilter()
            filter.addAction(BluetoothDevice.ACTION_FOUND)
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            registerReceiver(mReceiver, filter)
            askForPermission()

            listViewOtherDevices.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ -> mPresenter.onItemClick(i, false) }
            listViewPairedDevices.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ -> mPresenter.onItemClick(i, true) }

            button.setOnClickListener { _ -> askForPermission() }
        } else { //if no Bluetooth is available, show empty screen and do nothing
            showNotSupported()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isBluetoothAvailable())
            mPresenter.takeView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        /*
        when the activity is destroyed it's important to cancle the discovery and unregister the
        receiver to avoid battery issues
         */
        if (isBluetoothAvailable()) {
            unregisterReceiver(mReceiver)
            mPresenter.cancelDiscovery()
        }
    }

    private fun isBluetoothAvailable(): Boolean {
        return BluetoothAdapter.getDefaultAdapter() != null
    }

    override fun openControllerActivity() {
        val intent = Intent(this, ControllerActivity::class.java)
        startActivity(intent)
    }

    override fun showNotSupported() {
        invisibleAllViews()
        rlNotSupported.visibility = View.VISIBLE
    }

    override fun showEmptyView() {
        invisibleAllViews()
        rlEmptyView.visibility = View.VISIBLE
    }

    override fun showBluetoothDevices() {
        invisibleAllViews()
        rlList.visibility = View.VISIBLE
    }

    override fun showPermissionButton() {
        button.visibility = View.VISIBLE
    }

    override fun showLoading() {
        invisibleAllViews()
        rlLoading.visibility = View.VISIBLE
    }

    private fun invisibleAllViews() {
        rlNotSupported.visibility = View.GONE
        rlEmptyView.visibility = View.GONE
        rlList.visibility = View.GONE
        rlLoading.visibility = View.GONE
        button.visibility = View.GONE
    }

    override fun setAdapter(): BluetoothListAdapter {
        val adapter = BluetoothListAdapter(this)
        listViewOtherDevices.adapter = adapter
        return adapter
    }

    override fun setPairedAdapter(): BluetoothListAdapter {
        val adapter = BluetoothListAdapter(this)
        listViewPairedDevices.adapter = adapter
        return adapter
    }

    /*
    When requesting to turn on bluetooth, the result will be returned through the onActivityResult
    lifecycle method. The result is delegated to the presenter which will handle the result accordingly
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mPresenter.onActivityResult(requestCode, resultCode, data)
    }

    /*
    When requesting a permission (in our case the location permission for bluetooth discovery) the
    response is sent back through the onRequestPermissionResult method. This method checks if the
    permission has been granted, and if yes it sets the locationGranted property of the presenter.
     */
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            FINE_LOATION_ACCESS_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                mPresenter.locationPermissionGranted((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                return
            }
        }
    }

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    /*
    This methods asks for permission for location services, in order to make the bluetooth discovery work
    It's only required on devices post VERSION_CODES.M Marshmallow, because previously the permissions
    are not granted during runtime, but when the application is first installed. For >M the permission
    can always be revoked so this method has to always be called. For <M devices the permission can only be
    revoked by uninstalling the application.
     */
    private fun askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // Only ask for these permissions on runtime when running Android 6.0 or higher
            when (ContextCompat.checkSelfPermission(baseContext, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //if permission is denied, create an alert dialogue and ask for runtime permission
                PackageManager.PERMISSION_DENIED -> (AlertDialog.Builder(this)
                        .setTitle("Runtime Permissions up ahead")
                        .setMessage(Html.fromHtml("<p>To find nearby new bluetooth devices please click \"Allow\" on the runtime permissions popup.</p>"))
                        .setNeutralButton("Okay", DialogInterface.OnClickListener { _, _ ->
                            if (ContextCompat.checkSelfPermission(baseContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                //response comes through onRequestPermissionResult callback
                                ActivityCompat.requestPermissions(this,
                                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                                        FINE_LOATION_ACCESS_REQUEST_CODE)
                            }
                        })
                        .show())
                //if the permission is already granted, proceed with setting location permission to granted
                PackageManager.PERMISSION_GRANTED -> {
                    mPresenter.locationPermissionGranted(true)
                }
            }
        }else // on devices previous to Marshmallow the permission is granted when the app is installed (no runtime permissions)
        {
            mPresenter.locationPermissionGranted(true)
        }
    }

    /*
    Sets the list height manually, because two lists are within a Scrollview. Without this method
    the inner listViews would not expand and there for not work
     */
    override fun setPairedListHeight() {
        setListViewHeightBasedOnChildren(listViewPairedDevices)

    }

    override fun setOtherListHeight() {
        setListViewHeightBasedOnChildren(listViewOtherDevices)
    }

    /*
    Method for Setting the Height of the ListView dynamically.
    Hack to fix the issue of not showing all the items of the ListView
    when placed inside a ScrollView
      */
    private fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter ?: return

        val desiredWidth = MeasureSpec.makeMeasureSpec(listView.width, MeasureSpec.UNSPECIFIED)
        var totalHeight = 0
        var view: View? = null
        for (i in 0 until listAdapter.count) {
            view = listAdapter.getView(i, view, listView)
            if (i == 0)
                view!!.layoutParams = ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT)

            view!!.measure(desiredWidth, MeasureSpec.UNSPECIFIED)
            totalHeight += view.measuredHeight
        }
        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
    }
}