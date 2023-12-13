package com.example.chatapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.models.UserLoginResponse
import com.example.chatapp.retrofits_api.ApiRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    private val TAG = javaClass.simpleName

    private var loginResponse = MutableLiveData<UserLoginResponse>()
    val userLogin: LiveData<UserLoginResponse> = loginResponse


    fun authUser(emailId: String, pwd: String) {
        /*apiRepository.authUser(emailId, pwd).enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                loginResponse.value = response.body()
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                Log.e(TAG, "onError: ${t.message}")

            }

        })*/

        apiRepository.authUser(emailId, pwd) {
            loginResponse.value = it
        }

    }

    fun authAdmin(emailId: String, pwd: String) {
        apiRepository.authAdmin(emailId, pwd).enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                Log.e(TAG, "onResponse: ${response}  body=${response.body()}")
                loginResponse.value = response.body()
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                Log.e(TAG, "onError: ${t.message}")

            }

        })

    }

}