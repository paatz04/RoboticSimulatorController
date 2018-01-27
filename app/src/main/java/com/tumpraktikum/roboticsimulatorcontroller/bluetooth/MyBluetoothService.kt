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
        //when this object is initialized the private inner class is created and the thread started
        //through Thread.start();
        mConnectedThread = ConnectedThread()
        mConnectedThread.start()
    }

    /*
     *  This method is used to write data over the socket (representing our bluetooth connection)
     */
    fun write(message: String) {
        mConnectedThread.write(message)
    }

    fun close() {
      mConnectedThread.close()
    }

    /*
    When the activity changes, a new handler is passed for the specific activity to listen to the,
    Thread message and handle them accordingly.
     */
    fun updateHandler(handler: Handler){
        Log.d("MyBluetoothService", "Handler updated")
        this.mHandler = handler
    }

    /*
    The ConnectedThread holds a reference of the input and output stream and is responsible for writing
    and receiving data over the bluetooth socket. Errors are handled through the Handler, and not directly
    as exceptions or negative return values
     */
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

        /*
        The main job of this thread is to listen for input from the mSocket.inputStream
         */
        override fun run() {
            Log.d(MyBluetoothService::class.java.simpleName, "ConnectedThread run()")
            listenToInputStream()
        }


        /*
        When data is received through the input stream, the data is delegated to the activity, using the
        appropriate handler. If an exception occurs the connection is closed and the activity is also notifyed
         */
        private fun listenToInputStream() {
            while (true) {
                try {
                    Log.d(MyBluetoothService::class.java.simpleName, "listenToInputStream()")
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
            Log.d(MyBluetoothService::class.java.simpleName, "sendReceivedDataToActivity($receivedData)")

            val msg = mHandler.obtainMessage(MessageConstants.MESSAGE_BLUETOOTH_MESSAGE, receivedData)
            msg.sendToTarget()
        }


        /*
        This method writes a string in UTF format through the mSocket.outputStream. (to the established
        bluetooth connection/device) If an error occurs the connection closes and the activity is notified
         */
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

        /*
        This method closes the input/output streams and the socket on errors
         */
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