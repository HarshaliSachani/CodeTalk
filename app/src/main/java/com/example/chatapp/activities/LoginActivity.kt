package com.example.chatapp.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityLoginBinding
import com.example.chatapp.extensions.startDashboardActivity
import com.example.chatapp.extensions.startForgetPasswordActivity
import com.example.chatapp.utils.AppPrefs
import com.example.chatapp.utils.CommonDialog
import com.example.chatapp.utils.PrefConstant
import com.example.chatapp.viewmodels.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private val model: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        setListener()
        setObserver()

    }

    private fun setListener() {
        binding.btnLogin.setOnClickListener(this)
        binding.tvForgetPassword.setOnClickListener(this)

    }

    private fun setObserver() {
        model.userLogin.observe(this) { userLoginResponse ->
            Log.e(TAG, "setObserver:    userLoginResponse=$userLoginResponse")
            if (userLoginResponse != null)
                if (userLoginResponse.success) {
                    AppPrefs.saveValue(PrefConstant.id, userLoginResponse.data._id)
                    AppPrefs.saveValue(PrefConstant.display_name, userLoginResponse.data.name)
                    AppPrefs.saveValue(PrefConstant.user_name, userLoginResponse.data.user_name)

                    if (!AppPrefs.getBoolean(PrefConstant.isAdmin))
                        AppPrefs.saveValue(PrefConstant.user_profile, userLoginResponse.data.user_profile)

                    startDashboardActivity()
                    finish()

                } else
                    CommonDialog.Builder()
                        .setTitle(resources.getString(R.string.alert))
                        .setMessage(userLoginResponse.message)
                        .setButton2(resources.getString(R.string.ok)) {}
                        .create(this@LoginActivity)

        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvForgetPassword -> startForgetPasswordActivity()
            R.id.btnLogin -> {
                val userName = binding.edtUserName.text?.trim().toString()
                val pwd = binding.edtPassWord.text?.trim().toString()

                Log.e(TAG, "onClick: ${AppPrefs.getBoolean(PrefConstant.isAdmin)}")

                if (userName.isEmpty())
                    CommonDialog.Builder()
                        .setTitle(resources.getString(R.string.alert))
                        .setMessage(resources.getString(R.string.enter_user_email_id))
                        .setButton2(resources.getString(R.string.ok)) {}
                        .create(this@LoginActivity)
                else if (pwd.isEmpty())
                    CommonDialog.Builder()
                        .setTitle(resources.getString(R.string.alert))
                        .setMessage(resources.getString(R.string.enter_password))
                        .setButton2(resources.getString(R.string.ok)) {

                        }
                        .create(this@LoginActivity)
                else {
                    if (AppPrefs.getBoolean(PrefConstant.isAdmin)) model.authAdmin(userName, pwd)
                    else model.authUser(userName, pwd)
                }


            }
        }
    }

}