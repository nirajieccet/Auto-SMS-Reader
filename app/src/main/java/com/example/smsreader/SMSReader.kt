package com.example.smsreader

import android.app.Application

class SMSReader : Application() {
    override fun onCreate() {
        super.onCreate()
        val appSignatureHelper = AppSignatureHelper(this)
        appSignatureHelper.appSignatures
    }
}