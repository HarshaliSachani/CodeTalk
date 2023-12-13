package com.example.chatapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.models.CommonResponse
import com.example.chatapp.models.UserLoginResponse
import com.example.chatapp.retrofits_api.ApiRepository
import com.example.chatapp.utils.AppPrefs
import com.example.chatapp.utils.PrefConstant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    private val TAG = javaClass.simpleName

    private var createGroupResponse = MutableLiveData<CommonResponse>()
    val createGroupData: LiveData<CommonResponse> = createGroupResponse

    private var fetchGroupResponse = MutableLiveData<UserLoginResponse>()
    val fetchGroupData: LiveData<UserLoginResponse> = fetchGroupResponse

    fun createNewGroup(id: String, groupName: String, groupIcon: String) {
        apiRepository.createGroup(id, groupName, groupIcon).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                Log.e(TAG, "onResponse: $response")
                 createGroupResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e(TAG, "onError: ${t.message}")

            }

        })

    }

    fun fetchAdminAllGroup() {
        apiRepository.getAdminGroupList().enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                Log.e(TAG, "onResponse: $response")
                fetchGroupResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                Log.e(TAG, "onError: ${t.message}")

            }

        })

    }

    fun fetchUserAllGroup() {
        apiRepository.getUserGroupList(AppPrefs.getString(PrefConstant.user_name)).enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                Log.e(TAG, "onResponse: $response")
                fetchGroupResponse.postValue(response.body())

            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                Log.e(TAG, "onError: ${t.message}")

            }

        })

    }

}