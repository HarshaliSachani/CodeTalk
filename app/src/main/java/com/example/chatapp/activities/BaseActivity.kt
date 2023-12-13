package com.example.chatapp.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.chatapp.utils.ProgressLoader
import com.example.chatapp.utils.ProgressLoader2

abstract class BaseActivity : AppCompatActivity() {

    var TAG = javaClass.simpleName
    private var progressLoader: ProgressLoader? = null
    private var progressLoader2: ProgressLoader2? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addLoader()
    }

    fun addFragment(fragment: Fragment, layout: Int) {
        Log.e(TAG, "addFragment:  simpleName=${fragment.javaClass.simpleName} ")
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(layout, fragment, fragment.javaClass.simpleName)
//        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }

    private fun addLoader() {
        progressLoader = ProgressLoader(this)
        progressLoader2 = ProgressLoader2(this)
    }

    fun showLoader(showLoader2: Boolean = false) {
        if (showLoader2) progressLoader2?.showLoading()
        else progressLoader?.showLoading()
    }

    fun hideLoader(showLoader2: Boolean = false) {
        if (showLoader2) progressLoader2?.dismissLoading()
        else progressLoader?.dismissLoading()
    }


}
