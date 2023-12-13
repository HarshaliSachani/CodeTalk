package com.example.chatapp.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivitySplashBinding
import com.example.chatapp.extensions.startDashboardActivity
import com.example.chatapp.extensions.startIntroActivity
import com.example.chatapp.utils.AppPrefs
import com.example.chatapp.utils.PrefConstant
import com.example.chatapp.viewmodels.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val model: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        binding.imgLogo.animate()
            .scaleX(1.0f)
            .scaleY(1.0f)
            .alpha(1.0f)
            .setDuration(1000)

        val userName = AppPrefs.getString(PrefConstant.user_name) ?: ""
        if (userName.isNotEmpty()) {
            Handler(Looper.getMainLooper()).postDelayed({
                startDashboardActivity()
                finish()
            }, 2000)
        } else
            Handler(Looper.getMainLooper()).postDelayed({
                startIntroActivity()
                finish()
            }, 2000)

    }


}