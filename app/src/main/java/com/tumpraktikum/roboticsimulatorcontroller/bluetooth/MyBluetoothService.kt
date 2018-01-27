package com.tumpraktikum.roboticsimulatorcontroller.bluetooth

import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.util.Log
import com.tumpraktikum.roboticsimulatorcontroller.interfaces.MessageConstants
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

class MyBluetoothService(private var mHandler: Handler, private val mSocket: BluetoothSocket) {
    private var mConnectedThread: ConnectedThread


    init {
        mConnectedThread = ConnectedThread()
        mConnectedThread.start()
    }

    fun write(message: String) {
        mConnectedThread.write(message)
    }

    fun close() {
        Log.d(MyBluetoothService::class.toString(), "close")
        mConnectedThread.close()
    }

    fun updateHandler(handler: Handler){
        Log.d("MyBluetoothService", "Handler updated")
        this.mHandler = handler
    }


    private inner class ConnectedThread : Thread() {

        private val mInStream: DataInputStream
        private val mOutStream: DataOutputStream

        init {
            try {
                mInStream = getInputStream()
                mOutStream = getOutputStream()
            }catch (e: IOException) {
                throw MyBluetoothServiceException(e.message ?: "Error occured when creating input/output stream")
            }
        }

        private fun getInputStream(): DataInputStream {
            try {
                return DataInputStream(mSocket.inputStream)
            } catch (e: IOException) {
                Log.e(MyBluetoothService::class.java.simpleName, "Error occurred when creating input stream", e)
                throw IOException("Error occurred when creating input stream", e)
            }
        }

        private fun getOutputStream(): DataOutputStream {
            try {
                return DataOutputStream(mSocket.outputStream)
            } catch (e: IOException) {
                Log.e(MyBluetoothService::class.java.simpleName, "Error occurred when creating output stream", e)
                throw IOException("Error occurred when creating output stream", e)
            }
        }

        override fun run() {
            listenToInputStream()
        }

        private fun listenToInputStream() {
            while (true) {
                try {
                    sendReceivedDataToActivity(mInStream.readUTF())
                } catch (e: IOException) {
                    Log.d(MyBluetoothService::class.java.simpleName, "Input stream was disconnected")
                    sendBluetoothConnectionClosedToActivity()
                    close()
                    break
                }
            }
        }

        private fun sendReceivedDataToActivity(receivedData: String) {
            val msg = mHandler.obtainMessage(MessageConstants.MESSAGE_BLUETOOTH_MESSAGE, receivedData)
            msg.sendToTarget()
        }

        fun write(message: String) {
            try {
                mOutStream.writeUTF(message)
                sendSentMessageToActivity(message)
            } catch (e: IOException) {
                Log.e(MyBluetoothService::class.java.simpleName, "Error occurred when sending data")
                sendBluetoothConnectionClosedToActivity()
                close()
            }
        }

        private fun sendSentMessageToActivity(message: String) {
            val writtenMsg = mHandler.obtainMessage(
                    MessageConstants.MESSAGE_WRITE, -1, -1, message)
            writtenMsg.sendToTarget()
        }

        private fun sendBluetoothConnectionClosedToActivity() {
            val message = mHandler.obtainMessage(MessageConstants.MESSAGE_BLUETOOTH_CONNECTION_CLOSED)
            mHandler.sendMessage(message)
            Log.d(MyBluetoothService::class.java.simpleName, "sent connection closed")
        }

        fun close() {
            closeInputStream()
            closeOutputStream()
            closeSocket()
        }

        private fun closeInputStream() {
            try{
                mInStream.close()
            } catch (e: IOException) {
                Log.e(MyBluetoothService::class.java.simpleName, "Could not close the InputStream")
            }
        }

        private fun closeOutputStream() {
            try{
                mOutStream.close()
            } catch (e: IOException) {
                Log.e(MyBluetoothService::class.java.simpleName, "Could not close the OutputStream")
            }
        }

        private fun closeSocket() {
            try{
                mSocket.close()
            } catch (e: IOException) {
                Log.e(MyBluetoothService::class.java.simpleName, "Could not close the socket")
            }
        }
    }
}