package com.example.chatapp.retrofits_api

import com.example.chatapp.models.ChatMsgResponse
import com.example.chatapp.models.CommonResponse
import com.example.chatapp.models.GroupUserResponse
import com.example.chatapp.models.UserLoginResponse
import com.example.chatapp.utils.Constants
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {

    @FormUrlEncoded
    @POST(Constants.ADMIN_LOGIN)
    fun authAdmin(@Field(Constants.EMAIL_ID) emailId: String, @Field(Constants.PASSWORD) pwd: String): Call<UserLoginResponse>

    @FormUrlEncoded
    @POST(Constants.USER_LOGIN)
    fun authUser(@Field(Constants.EMAIL_ID) emailId: String, @Field(Constants.PASSWORD) pwd: String): Call<UserLoginResponse>

    @FormUrlEncoded
    @POST(Constants.CREATE_GROUP)
    fun createGroup(@Field(Constants.ID) id: String, @Field(Constants.GROUP) groupName: String, @Field(Constants.GROUP_ICON) groupIcon: String): Call<CommonResponse>

    @FormUrlEncoded
    @POST(Constants.ADD_USER)
    fun addUser(@Field(Constants.GROUP) groupName: String, @Field(Constants.EMAIL_ID) emailId: String, @Field(Constants.USER_NAME) userName: String): Call<CommonResponse>

    @FormUrlEncoded
    @POST(Constants.DELETE_GROUP)
    fun deleteGroup(@Field(Constants.GROUP) groupName: String): Call<CommonResponse>

    @GET(Constants.ADMIN_GROUP)
    fun getAdminGroupList(): Call<UserLoginResponse>

    @FormUrlEncoded
    @POST(Constants.USER_GROUP)
    fun getUserGroupList(@Field(Constants.EMAIL_ID) emailId: String): Call<UserLoginResponse>

    @FormUrlEncoded
    @POST(Constants.GROUP_USER)
    fun getGroupUserList(@Field(Constants.GROUP) groupName: String): Call<GroupUserResponse>

    @FormUrlEncoded
    @POST(Constants.DELETE_USER)
    fun removeUser(@Field(Constants.GROUP) groupName: String, @Field(Constants.EMAIL_ID) emailId: String): Call<CommonResponse>

    @FormUrlEncoded
    @POST(Constants.GENERATE_OTP)
    fun generateOtp(@Field(Constants.EMAIL_ID) groupName: String, @Field(Constants.IS_ADMIN) isAdmin: Boolean): Call<CommonResponse>

    @FormUrlEncoded
    @POST(Constants.FORGET_PASSWORD)
    fun updatePassword(@Field(Constants.EMAIL_ID) groupName: String, @Field(Constants.IS_ADMIN) isAdmin: Boolean, @Field(Constants.PASSWORD) password: String): Call<CommonResponse>

    @FormUrlEncoded
    @POST(Constants.CLEAR_CHAT_HISTORY)
    fun clearChatHistory(@Field(Constants.GROUP) groupName: String): Call<ChatMsgResponse>

}


/*
package com.example.chatapp.retrofits_api

import com.example.chatapp.models.UserLoginResponse
import com.example.chatapp.utils.Constants
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST
    fun authAdmin(
        @Url url: String, @Field(Constants.EMAIL_ID) emailId: String, @Field(Constants.PASSWORD) pwd: String
    ): Call<UserLoginResponse>

    @FormUrlEncoded
    @POST
    fun authUser(
        @Url url: String, @Field(Constants.EMAIL_ID) emailId: String, @Field(Constants.PASSWORD) pwd: String
    ): Call<UserLoginResponse>

    @FormUrlEncoded
    @POST
    fun createGroup(
        @Url url: String, @Field(Constants.GROUP) groupName: String, @Field(Constants.ID) id: String
    ): Call<JsonElement>

    @FormUrlEncoded
    @POST
    fun addUser(
        @Url url: String, @Field(Constants.GROUP) groupName: String, @Field(Constants.EMAIL_ID) emailId: String, @Field(Constants.USER_NAME) userName: String
    ): Call<JsonElement>

    @GET
    fun getAdminGroupList(@Url url: String): Call<UserLoginResponse>

    @FormUrlEncoded
    @POST
    fun getUserGroupList(
        @Url url: String, @Field(Constants.EMAIL_ID) emailId: String
    ): Call<UserLoginResponse>

}


* */