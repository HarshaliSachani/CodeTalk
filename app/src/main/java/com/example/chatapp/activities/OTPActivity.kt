package com.example.chatapp.activities

import `in`.aabhasjindal.otptextview.OTPListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentOtpBinding
import com.example.chatapp.fragments.BaseFragment
import com.example.chatapp.utils.CommonDialog
import com.example.chatapp.utils.Constants
import com.example.chatapp.viewmodels.ForgetPasswordViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class OTPActivity : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentOtpBinding
    private val model: ForgetPasswordViewModel by sharedViewModel()
    private var otp = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentOtpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        arguments?.let {
            if (it.containsKey(Constants.OTP))
                otp = it.getString(Constants.OTP, "")

        }

    }

    private fun setListener() {
        binding.btnVerifyOtp.setOnClickListener(this)

        binding.otpView.otpListener = object : OTPListener {
            override fun onInteractionListener() {
                // fired when user types something in the Otpbox
            }

            override fun onOTPComplete(otp: String) {
                // fired when user has entered the OTP fully.
//                validateOtp(otp)
            }
        }
    }

    private fun validateOtp(enteredOtp: String) {
        Log.e(TAG, "validateOtp:   enteredOtp=$enteredOtp    otp=$otp")
        if (enteredOtp.isEmpty())
            CommonDialog.Builder()
                .setTitle(resources.getString(R.string.alert))
                .setMessage(resources.getString(R.string.enter_otp))
                .setButton2(resources.getString(R.string.ok)) {}
                .create(requireActivity())
        else if (enteredOtp != otp)
            CommonDialog.Builder()
                .setTitle(resources.getString(R.string.alert))
                .setMessage(resources.getString(R.string.enter_valid_otp))
                .setButton2(resources.getString(R.string.ok)) {}
                .create(requireActivity())
        else model.otpVerifiedResponse.value = true
    }

    override fun onClick(v: View?) {
        if (v == binding.btnVerifyOtp) {
            val enteredOtp = binding.otpView.otp.toString()
            validateOtp(enteredOtp)

        }

    }

}