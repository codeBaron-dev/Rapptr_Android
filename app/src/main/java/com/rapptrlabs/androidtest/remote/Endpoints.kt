package com.rapptrlabs.androidtest.remote

import com.rapptrlabs.androidtest.chat.model.ChatsModel
import retrofit2.Call
import retrofit2.http.GET

interface Endpoints {
    @GET("chat_log.php")
    fun getChats(): Call<ChatsModel>
}