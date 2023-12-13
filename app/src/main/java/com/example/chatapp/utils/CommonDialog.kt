package com.example.chatapp.utils

import android.app.Activity
import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.chatapp.R
import com.example.chatapp.databinding.DialogCommonBinding

class CommonDialog(builder: Builder) : Dialog(builder.activity!!, R.style.Theme_Dialog) {
    private val TAG = javaClass.simpleName
    private var activity: Activity? = null
    private var dialog: Dialog? = null

    private lateinit var dialogBinding: DialogCommonBinding
    private var isDialogOpen = false

    private fun initDialog(builder: Builder): Dialog {
        activity = builder.activity
        Log.e(TAG, "check activity instance initDialog=$activity")
        dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_common, null, false)
        val dialog = Dialog(activity!!, R.style.Theme_Dialog)
        dialog.setContentView(dialogBinding.root)

//        if (dialog.window != null) dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation

        dialogBinding.txtTitle.text = builder.title
        dialogBinding.txtMsg.text = builder.message

        if (builder.btn1Text != null && builder.btn1Text != "") {
            dialogBinding.btn1.isVisible = true
            dialogBinding.btn1.text = builder.btn1Text
            dialogBinding.btn1.setOnClickListener {
                dismissDialog()
                if (builder.onBtn1ClickListener != null) builder.onBtn1ClickListener!!()
            }
        } else dialogBinding.btn1.isVisible = false

        if (builder.btn2Text != null && builder.btn2Text != "") {
            dialogBinding.btn2.isVisible = true
            dialogBinding.btn2.text = builder.btn2Text
            dialogBinding.btn2.setOnClickListener {
                dismissDialog()
                if (builder.onBtn2ClickListener != null) builder.onBtn2ClickListener!!()
            }
        } else dialogBinding.btn2.isVisible = false

        return dialog

    }

    fun showDialog() {
        Log.e(TAG, "check activity instance showDialog=$activity")
        Log.e(TAG, "check activity instance showDialog=${activity!!.isFinishing}")
        Log.e(TAG, "check activity instance showDialog=${activity!!.isDestroyed}")
        Log.e(
            TAG, "check activity instance showDialog=${
                dialog
            }"
        )
        Log.e(TAG, "check activity instance showDialog=${dialog?.isShowing}")
        if (activity != null && !activity!!.isFinishing && !activity!!.isDestroyed && !dialog?.isShowing!!) {
            try {
                dialog?.show()
                isDialogOpen = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun dismissDialog() {
        if (!activity!!.isFinishing && !activity!!.isDestroyed && dialog?.isShowing!!) {
            try {
                isDialogOpen = false
                dialog?.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    class Builder {
        var activity: Activity? = null
        var cancelable = true
        var title: String? = ""
        var message: String? = ""
        var btn1Text: String? = ""
        var btn2Text: String? = ""
        var onBtn1ClickListener: (() -> Unit?)? = null
        var onBtn2ClickListener: (() -> Unit?)? = null

        fun setButton1(text: String?, onClickListener: () -> Unit): Builder {
            btn1Text = text
            onBtn1ClickListener = onClickListener
            return this
        }

        fun setButton2(text: String?, onClickListener: () -> Unit): Builder {
            btn2Text = text
            onBtn2ClickListener = onClickListener
            return this
        }

        fun setMessage(message: String?): Builder {
            this.message = message
            return this
        }

        fun setTitle(title: String?): Builder {
            this.title = title
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun create(activity: Activity): CommonDialog {
            this.activity = activity
            Log.e("TAG", "check activity instance builder=$activity")
            return CommonDialog(this)
        }

    }

    init {
        dialog = initDialog(builder)
        showDialog()
    }

}