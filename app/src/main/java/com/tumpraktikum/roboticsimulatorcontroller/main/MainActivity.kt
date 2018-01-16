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

    // Create a BroadcastReceiver for ACTION_FOUND AND ACTION_STATE_CHANGED.
    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            mPresenter.bluetoothActionFound(context, intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        } else {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mPresenter.onActivityResult(requestCode, resultCode, data)
    }

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

    private fun askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // Only ask for these permissions on runtime when running Android 6.0 or higher
            when (ContextCompat.checkSelfPermission(baseContext, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                PackageManager.PERMISSION_DENIED -> (AlertDialog.Builder(this)
                        .setTitle("Runtime Permissions up ahead")
                        .setMessage(Html.fromHtml("<p>To find nearby bluetooth devices please click \"Allow\" on the runtime permissions popup.</p>"))
                        .setNeutralButton("Okay", DialogInterface.OnClickListener { _, _ ->
                            if (ContextCompat.checkSelfPermission(baseContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(this,
                                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                                        FINE_LOATION_ACCESS_REQUEST_CODE)
                            }
                        })
                        .show())
                PackageManager.PERMISSION_GRANTED -> {
                    mPresenter.locationPermissionGranted(true)
                }
            }
        }
    }

    override fun setPairedListHeight() {
        setListViewHeightBasedOnChildren(listViewPairedDevices)

    }

    override fun setOtherListHeight() {
        setListViewHeightBasedOnChildren(listViewOtherDevices)
    }

    /**** Method for Setting the Height of the ListView dynamically.
     * Hack to fix the issue of not showing all the items of the ListView
     * when placed inside a ScrollView   */
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