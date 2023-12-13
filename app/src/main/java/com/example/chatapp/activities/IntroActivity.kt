package com.example.chatapp.activities

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityIntroBinding
import com.example.chatapp.extensions.startLoginActivity
import com.example.chatapp.utils.AppPrefs
import com.example.chatapp.utils.PrefConstant

class IntroActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)

        setClickListener()

    }

    private fun setClickListener() {
        binding.btnGetStarted.setOnClickListener(this)
        binding.btnLoginAsAdmin.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnGetStarted -> {
                AppPrefs.saveValue(PrefConstant.isAdmin, false)
                startLoginActivity()
                finish()
            }
            R.id.btnLoginAsAdmin -> {
                AppPrefs.saveValue(PrefConstant.isAdmin, true)
                startLoginActivity()
                finish()
            }
        }
    }
}