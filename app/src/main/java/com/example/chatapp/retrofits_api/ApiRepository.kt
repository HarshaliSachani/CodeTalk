package com.example.chatapp.retrofits_api

import com.example.chatapp.models.UserLoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiRepository {

    private var apiInterface = ApiService.createService(ApiInterface::class.java)

/*    fun authUser(emailId: String, password: String): Call<UserLoginResponse> {
        return apiInterface.authUser(emailId, password)
    }*/


    fun authUser(emailId: String, password: String, onResponse: (res: UserLoginResponse?) -> Unit) = checkResponse(emailId, password, onResponse)


    fun authAdmin(emailId: String, password: String) = apiInterface.authAdmin(emailId, password)
    fun createGroup(id: String, groupName: String, groupIcon: String) = apiInterface.createGroup(id, groupName, groupIcon)
    fun addUser(groupName: String, emailId: String, userName: String) = apiInterface.addUser(groupName, emailId, userName)
    fun deleteGroup(groupName: String) = apiInterface.deleteGroup(groupName)
    fun removeUser(groupName: String, emailId: String) = apiInterface.removeUser(groupName, emailId)
    fun getAdminGroupList() = apiInterface.getAdminGroupList()
    fun getUserGroupList(emailId: String) = apiInterface.getUserGroupList(emailId)
    fun getGroupUserList(emailId: String) = apiInterface.getGroupUserList(emailId)
    fun generateOtp(emailId: String, isAdmin: Boolean) = apiInterface.generateOtp(emailId, isAdmin)
    fun updatePassword(emailId: String, isAdmin: Boolean, password: String) = apiInterface.updatePassword(emailId, isAdmin, password)
    fun clearChatHistory(groupName: String) = apiInterface.clearChatHistory(groupName)


    private fun checkResponse(emailId: String, password: String, onResponse: (res: UserLoginResponse?) -> Unit) {
        apiInterface.authUser(emailId, password).enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                onResponse(response.body())
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {

            }


        })


    }

}


/*

 fun authAdmin(emailId: String, password: String) = apiInterface.authAdmin(Constants.API_BASE_URL + Constants.ADMIN_LOGIN, emailId, password)
    fun authUser(emailId: String, password: String) = apiInterface.authUser(Constants.API_BASE_URL + Constants.USER_LOGIN, emailId, password)
    fun createGroup(groupName: String, id: String) = apiInterface.createGroup(Constants.API_BASE_URL + Constants.CREATE_GROUP, groupName, id)
    fun addUser(groupName: String, emailId: String, userName: String) = apiInterface.addUser(Constants.API_BASE_URL + Constants.ADD_USER, groupName, emailId, userName)
    fun getAdminGroupList() = apiInterface.getAdminGroupList(Constants.API_BASE_URL + Constants.ADMIN_GROUP)
    fun getUserGroupList(emailId: String) = apiInterface.getUserGroupList(Constants.API_BASE_URL + Constants.USER_GROUP, emailId)

*
* */


/*

admin
admintest1@gmail.com
Admin1342

user
harshalisachani@gmail.com
Admin1342




* */