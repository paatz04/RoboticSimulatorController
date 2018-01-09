package com.tumpraktikum.roboticsimulatorcontroller.main

import android.Manifest
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
import android.widget.ListView
import android.widget.Toast
import com.tumpraktikum.roboticsimulatorcontroller.R
import com.tumpraktikum.roboticsimulatorcontroller.application.App
import com.tumpraktikum.roboticsimulatorcontroller.controller.ControllerActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject






class MainActivity : AppCompatActivity(), MainContract.View {

    @Inject lateinit var mPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnTurnOnBluetooth.setOnClickListener { mPresenter.turnBluetoothOn(this) }

        (application as App).appComponent.inject(this)

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(mReceiver, filter)
        askForPermission()


    }


    // Create a BroadcastReceiver for ACTION_FOUND.
    private val mReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            mPresenter.bluetoothDeviceFound(context, intent)
        }
    }

    override fun onResume() {
        super.onResume()
        mPresenter.takeView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
        mPresenter.cancelDiscovery()
        mPresenter.dropView()
    }

    override fun openControllerActivity() {
        val intent = Intent(this, ControllerActivity::class.java)
        startActivity(intent)
    }

    override fun showNotSupported() {
        rlNotSupported.visibility = View.VISIBLE
        rlEmptyView.visibility = View.GONE
        rlList.visibility = View.GONE
    }

    override fun showEmptyView() {
        rlNotSupported.visibility = View.GONE
        rlEmptyView.visibility = View.VISIBLE
        rlList.visibility = View.GONE
    }

    override fun showBluetoothDevices() {
        rlNotSupported.visibility = View.GONE
        rlEmptyView.visibility = View.GONE
        rlList.visibility = View.VISIBLE
    }

    override fun setAdapter() : BluetoothListAdapter {
        val adapter = BluetoothListAdapter(this)
        listViewOtherDevices.adapter = adapter
        return adapter
    }

    override fun setPairedAdapter() : BluetoothListAdapter {
        val adapter = BluetoothListAdapter(this)
        listViewPairedDevices.adapter = adapter
        return adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mPresenter.onActivityResult(requestCode,resultCode,data)
    }

    override fun showToast() {
        Toast.makeText(this,getString(R.string.bluetoothProblem),Toast.LENGTH_LONG).show()
    }

    fun askForPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // Only ask for these permissions on runtime when running Android 6.0 or higher
            when (ContextCompat.checkSelfPermission(baseContext, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                PackageManager.PERMISSION_DENIED -> (AlertDialog.Builder(this)
                        .setTitle("Runtime Permissions up ahead")
                        .setMessage(Html.fromHtml("<p>To find nearby bluetooth devices please click \"Allow\" on the runtime permissions popup.</p>" + "<p>For more info see <a href=\"http://developer.android.com/about/versions/marshmallow/android-6.0-changes.html#behavior-hardware-id\">here</a>.</p>"))
                        .setNeutralButton("Okay", DialogInterface.OnClickListener { _, _ ->
                            if (ContextCompat.checkSelfPermission(baseContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(this,
                                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                                        1)
                            }
                        })
                        .show())
                PackageManager.PERMISSION_GRANTED -> {
                    mPresenter?.startDiscovery()
                }
            }
        }
    }

    override fun setPairedListHeight(){
        setListViewHeightBasedOnChildren(listViewPairedDevices)

    }

    override fun setOtherListHeight(){
        setListViewHeightBasedOnChildren(listViewOtherDevices)
    }


    /**** Method for Setting the Height of the ListView dynamically.
     * Hack to fix the issue of not showing all the items of the ListView
     * when placed inside a ScrollView   */
    fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.getAdapter() ?: return

        val desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED)
        var totalHeight = 0
        var view: View? = null
        for (i in 0 until listAdapter.getCount()) {
            view = listAdapter.getView(i, view, listView)
            if (i == 0)
                view!!.layoutParams = ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT)

            view!!.measure(desiredWidth, MeasureSpec.UNSPECIFIED)
            totalHeight += view.measuredHeight
        }
        val params = listView.getLayoutParams()
        params.height = totalHeight + listView.getDividerHeight() * (listAdapter.getCount() - 1)
        listView.setLayoutParams(params)
    }
}