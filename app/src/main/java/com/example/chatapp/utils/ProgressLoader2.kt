package com.example.chatapp.utils

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import androidx.annotation.NonNull
import com.airbnb.lottie.LottieAnimationView
import com.example.chatapp.R

class ProgressLoader2(@NonNull var activity: Activity?, message: String? = "") : Dialog(
    activity!!, R.style.Theme_Dialog
) {

    var message: String? = message
        set(value) {
            field = value
            tvMessage?.text = value
        }
    private var tvMessage: TextView? = null
    private var lottieAnimationView: LottieAnimationView? = null

    fun showLoading(msg: String): ProgressLoader2 {
        if (activity != null && !activity!!.isFinishing && !activity!!.isDestroyed) {
            tvMessage?.text = msg
            show()
        }
        return this
    }

    fun showLoading(): ProgressLoader2 {
        if (activity != null && !activity!!.isFinishing && !activity!!.isDestroyed) {
            show()
        }
        return this
    }

    fun dismissLoading() {
        if (activity != null && !activity!!.isFinishing && !activity!!.isDestroyed)
            dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(R.layout.loader_layout2)

        tvMessage = findViewById(R.id.tvMessage)
        lottieAnimationView = findViewById(R.id.progressLottieAnimationView)

        tvMessage?.text = message
    }


}
