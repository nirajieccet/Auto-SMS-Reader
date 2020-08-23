package com.example.smsreader

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.phone.SmsRetriever
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MySMSBroadcastReceiver.OTPReceiveListener {
    private var smsReceiver: MySMSBroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerBroadcastReceiver()
        //startSMSListener()
    }

    private fun startSMSListener() {
        try {
            val client = SmsRetriever.getClient(this)
            val task = client.startSmsRetriever()
            task.addOnSuccessListener { _ ->
                Log.e("TAG", "started")
                showToast("addOnSuccessListener")
                registerReceiver(smsReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
            }
            task.addOnFailureListener { e ->
                Log.e("TAG", e.toString())
                showToast("addOnFailureListener")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun onBtnResendClick(view: View){
        startSMSListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver)
            //LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver!!)
        }
    }


    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun onOTPReceived(message: String) {
        var msg = message.split(" ")
        var otp = msg[msg.size-2]
        showToast("OTP $otp}")
        editText.setText(otp)
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver)
        }
    }

    override fun onOTPTimeOut() {
        showToast("OTP Time out")
    }

    private fun registerBroadcastReceiver(){
        smsReceiver = MySMSBroadcastReceiver()
        smsReceiver!!.initOTPListener(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        this.registerReceiver(smsReceiver, intentFilter)
    }
}
