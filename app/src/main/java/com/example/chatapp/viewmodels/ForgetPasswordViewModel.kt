package com.example.chatapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.models.CommonResponse
import com.example.chatapp.retrofits_api.ApiRepository
import com.example.chatapp.utils.AppPrefs
import com.example.chatapp.utils.PrefConstant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPasswordViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    private val TAG = javaClass.simpleName

    private var getOtpResponse = MutableLiveData<CommonResponse>()
    val getOtp: LiveData<CommonResponse> = getOtpResponse

    private var updatePwdResponse = MutableLiveData<CommonResponse>()
    val updatePwd: LiveData<CommonResponse> = updatePwdResponse

    var otpVerifiedResponse = MutableLiveData<Boolean>()
    val otpVerified: LiveData<Boolean> = otpVerifiedResponse

    var isFinishActivity = MutableLiveData<Boolean>()
    val isFinish: LiveData<Boolean> = isFinishActivity

    fun generateOtp(emailId: String) {
        Log.e(TAG, "generateOtp:  isAdmin=${AppPrefs.getBoolean(PrefConstant.isAdmin)}")
        apiRepository.generateOtp(emailId, AppPrefs.getBoolean(PrefConstant.isAdmin)).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                getOtpResponse.value = response.body()
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e(TAG, "onError: ${t.message}")

            }

        })

    }

    fun updatePassword(password: String) {
        Log.e(TAG, "generateOtp:  isAdmin=${AppPrefs.getBoolean(PrefConstant.isAdmin)}   emailId=${AppPrefs.getString(PrefConstant.temp_user_name)}")
        apiRepository.updatePassword(AppPrefs.getString(PrefConstant.temp_user_name), AppPrefs.getBoolean(PrefConstant.isAdmin), password).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                updatePwdResponse.value = response.body()
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e(TAG, "onError: ${t.message}")

            }

        })

    }

}