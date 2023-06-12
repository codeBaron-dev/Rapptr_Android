package com.rapptrlabs.androidtest.remote

import com.rapptrlabs.androidtest.chat.model.ChatsModel
import com.rapptrlabs.androidtest.login.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Endpoints {
    @GET("chat_log.php")
    fun getChats(): Call<ChatsModel>

    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("email") email: String, @Field("password") password: String
    ): Call<LoginResponse>
}