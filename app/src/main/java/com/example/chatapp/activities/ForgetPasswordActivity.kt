package com.example.chatapp.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityForgetpasswordBinding
import com.example.chatapp.fragments.ForgetPasswordFragment
import com.example.chatapp.utils.Constants
import com.example.chatapp.viewmodels.ForgetPasswordViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgetPasswordActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityForgetpasswordBinding
    private val model: ForgetPasswordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgetpassword)
        setObserver()
        addFragment(ForgetPasswordFragment(), R.id.fragmentFrame)

    }

    private fun setObserver() {
        model.getOtp.observe(this) {
            if (it != null)
                if (it.success) {
                    Toast.makeText(this@ForgetPasswordActivity, it.message, Toast.LENGTH_SHORT).show()
                    val otpFragment = OTPActivity()
                    val bundle = Bundle()
                    bundle.putString(Constants.OTP, it.data)
                    otpFragment.arguments = bundle
                    addFragment(otpFragment, R.id.fragmentFrame)
                }
        }

        model.otpVerified.observe(this) {
            if (it != null && it)
                addFragment(UpdatePasswordActivity(), R.id.fragmentFrame)
        }

        model.isFinish.observe(this) {
            finish()
        }

    }

    override fun onClick(v: View?) {

    }

}