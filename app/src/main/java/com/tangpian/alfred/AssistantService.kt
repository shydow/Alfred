package com.tangpian.alfred

import android.content.Intent
import android.telecom.Call
import android.telecom.InCallService
import android.util.Log

class AssistantService : InCallService() {


    companion object {
        private const val LOG_TAG = "AssistantService"
    }

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        Log.i(LOG_TAG, "onCallAdded: $call")
        call.registerCallback(callCallback)
        startActivity(Intent(this, AssistantActivity::class.java))
        CallManager.updateCall(call)
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        Log.i(LOG_TAG, "onCallRemoved: $call")
        call.unregisterCallback(callCallback)
        CallManager.updateCall(null)
    }

    private val callCallback = object : Call.Callback() {
        override fun onStateChanged(call: Call, state: Int) {
            Log.i(LOG_TAG, "Call.Callback onStateChanged: $call, state: $state")
            CallManager.updateCall(call)
        }
    }

}
