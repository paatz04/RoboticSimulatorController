package com.tumpraktikum.roboticsimulatorcontroller.helper

import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.tumpraktikum.roboticsimulatorcontroller.helper.interfaces.MessageConstants
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStream

class MyBluetoothService(private var mHandler: Handler, private val mSocket: BluetoothSocket) {
    private var mConnectedThread: ConnectedThread? = null

    companion object {
        private val TAG = "MY_APP_DEBUG_TAG"
    }

    init {
        mConnectedThread = ConnectedThread()
        mConnectedThread?.start()
    }

    fun write(message: String) {
        mConnectedThread?.write(message)
    }

    fun close() {
      mConnectedThread?.close()
    }

    fun updateHandler(handler: Handler){
        this.mHandler = handler
    }

    private inner class ConnectedThread : Thread() {
        private val mInStream: InputStream?
        private val mOutStream: DataOutputStream?
        private var mBuffer: ByteArray? = null

        init {
            mInStream = getInputStream()
            mOutStream = getOutputStream()
        }

        private fun getInputStream(): InputStream? {
            var inputStream: InputStream? = null
            try {
                inputStream = mSocket.inputStream
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred when creating input stream", e)
            }
            return inputStream
        }

        private fun getOutputStream(): DataOutputStream? {
            var outputStream: DataOutputStream? = null
            try {
                outputStream = DataOutputStream(mSocket.outputStream)
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred when creating output stream", e)
            }
            return outputStream
        }

        override fun run() {
            mBuffer = ByteArray(1024)
            var numberReadBytes: Int

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    numberReadBytes = readFromInputStreamIntoBuffer()
                    sendBufferToActivity(numberReadBytes)
                } catch (e: IOException) {
                    Log.d(TAG, "Input stream was disconnected", e)
                    break
                }
            }
        }

        private fun readFromInputStreamIntoBuffer(): Int {
            return mInStream!!.read(mBuffer)
        }

        private fun sendBufferToActivity(numberReadBytes: Int) {
            val readMsg = mHandler.obtainMessage(
                    MessageConstants.MESSAGE_READ, numberReadBytes, -1,
                    mBuffer)
            readMsg.sendToTarget()
        }

        // Call this from the main activity to send data to the remote device.
        fun write(message: String) {
            try {
                mOutStream!!.writeUTF(message)
                sendSentMessageToActivity(message)
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred when sending data", e)
                sendFailureMessageToActivity()
            }
        }

        private fun sendSentMessageToActivity(message: String) {
            val writtenMsg = mHandler.obtainMessage(
                    MessageConstants.MESSAGE_WRITE, -1, -1, message)
            writtenMsg.sendToTarget()
        }

        private fun sendFailureMessageToActivity() {
            // ToDo: return to the controller activity, because connection to the server closed
            val writeErrorMsg = mHandler.obtainMessage(MessageConstants.MESSAGE_TOAST)
            val bundle = Bundle()
            bundle.putString("toast", "Couldn't send data to the other device")
            writeErrorMsg.data = bundle
            mHandler.sendMessage(writeErrorMsg)
        }

        // Call this method from the main activity to shut down the connection.
        fun close() {
            // ToDo connection did not close correctly. Read isn't interrupted
            closeInputStream()
            closeOutputStream()
            closeSocket()
        }

        private fun closeInputStream() {
            try{
                mInStream?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the InputStream", e)
            }
        }

        private fun closeOutputStream() {
            try{
                mOutStream?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the OutputStream", e)
            }
        }

        private fun closeSocket() {
            try{
                mSocket.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the socket", e)
            }
        }
    }
}