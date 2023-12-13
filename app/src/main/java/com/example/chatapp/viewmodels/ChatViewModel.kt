package com.example.chatapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.models.ChatMsgResponse
import com.example.chatapp.models.CommonResponse
import com.example.chatapp.models.GroupUserResponse
import com.example.chatapp.retrofits_api.ApiRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    private val TAG = javaClass.simpleName

    private var deleteGroupResponse = MutableLiveData<CommonResponse>()
    val deleteGroup: LiveData<CommonResponse> = deleteGroupResponse

    private var removeUserResponse = MutableLiveData<CommonResponse>()
    val removeUser: LiveData<CommonResponse> = removeUserResponse

    private var addUserResponse = MutableLiveData<CommonResponse>()
    val addUser: LiveData<CommonResponse> = addUserResponse

    private var groupUserResponse = MutableLiveData<GroupUserResponse>()
    val groupUserData: LiveData<GroupUserResponse> = groupUserResponse

    private var clearChatHistoryResponse = MutableLiveData<ChatMsgResponse>()
    val clearChatHistory: LiveData<ChatMsgResponse> = clearChatHistoryResponse


    public fun addUser(groupName: String, emailId: String, userName: String) {
        apiRepository.addUser(groupName, emailId, userName).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                Log.e(TAG, "onResponse: $response")
                addUserResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e(TAG, "onError: ${t.message}")

            }

        })

    }

    public fun deleteGroup(groupName: String) {
        apiRepository.deleteGroup(groupName).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                Log.e(TAG, "onResponse: $response")
                deleteGroupResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e(TAG, "onError: ${t.message}")

            }

        })

    }

    public fun fetchGroupUser(groupName: String) {
        apiRepository.getGroupUserList(groupName).enqueue(object : Callback<GroupUserResponse> {
            override fun onResponse(call: Call<GroupUserResponse>, response: Response<GroupUserResponse>) {
                Log.e(TAG, "onResponse: $response  status=${response.isSuccessful}")
                groupUserResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<GroupUserResponse>, t: Throwable) {
                Log.e(TAG, "onError: ${t.message}")

            }

        })

    }

    fun removeUser(groupName: String, emailId: String) {
        apiRepository.removeUser(groupName, emailId).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                Log.e(TAG, "onResponse: $response")
                removeUserResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e(TAG, "onError: ${t.message}")

            }

        })

    }

    fun clearChatHistory(groupName: String) {
        apiRepository.clearChatHistory(groupName).enqueue(object : Callback<ChatMsgResponse> {
            override fun onResponse(call: Call<ChatMsgResponse>, response: Response<ChatMsgResponse>) {
                clearChatHistoryResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<ChatMsgResponse>, t: Throwable) {
            }

        })
    }

}