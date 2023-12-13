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

class SplashViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    private val TAG = javaClass.simpleName

    private var groupListResponse = MutableLiveData<UserLoginResponse>()
    val groupList: LiveData<UserLoginResponse> = groupListResponse


    fun fetchUserAllGroup(emailId: String) {
        apiRepository.getUserGroupList(emailId).enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                groupListResponse.value = response.body()
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                Log.e(TAG, "onError: ${t.message}")

            }

        })

    }


    fun fetchAdminAllGroup() {
        apiRepository.getAdminGroupList().enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                groupListResponse.value = response.body()
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                Log.e(TAG, "onError: ${t.message}")

            }

        })

    }

}