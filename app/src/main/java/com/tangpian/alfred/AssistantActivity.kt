package com.tangpian.alfred

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.activity_assistant.*
import java.util.concurrent.TimeUnit

class AssistantActivity : AppCompatActivity() {

    companion object {
        private const val LOG_TAG = "CallActivity"
    }

    private var updatesDisposable = Disposables.empty()
    private var answerDisposable = Disposables.empty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assistant)
    }

    override fun onResume() {
        super.onResume()
        updatesDisposable = CallManager.updates()
            .doOnEach { Log.i(LOG_TAG, "updated call: $it") }
            .doOnError { throwable -> Log.e(LOG_TAG, "Error processing call", throwable) }
            .subscribe { updateView(it) }

        answerDisposable = Observable.interval(5, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                CallManager.acceptCall()
            }
    }

    private fun updateView(gsmCall: GsmCall) {
        textView.visibility = when (gsmCall.status) {
            GsmCall.Status.ACTIVE -> View.GONE
            else                  -> View.VISIBLE
        }
        textView.text = when (gsmCall.status) {
            GsmCall.Status.CONNECTING   -> "Connecting…"
            GsmCall.Status.DIALING      -> "Calling…"
            GsmCall.Status.RINGING      -> "Incoming call:" + gsmCall.displayName
            GsmCall.Status.ACTIVE       -> "Active:" + gsmCall.displayName
            GsmCall.Status.DISCONNECTED -> "Finished call"
            GsmCall.Status.UNKNOWN      -> ""
        }
        textView.visibility = when (gsmCall.status) {
            GsmCall.Status.ACTIVE -> View.VISIBLE
            else                  -> View.GONE
        }
        textView.visibility = when (gsmCall.status) {
            GsmCall.Status.DISCONNECTED -> View.GONE
            else                        -> View.VISIBLE
        }
    }

}
