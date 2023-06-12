package com.rapptrlabs.androidtest.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.rapptrlabs.androidtest.Prefs
import com.rapptrlabs.androidtest.chat.model.ChatsModel
import com.rapptrlabs.androidtest.login.model.LoginResponse
import com.rapptrlabs.androidtest.remote.Endpoints
import com.rapptrlabs.androidtest.remote.responsemanager.ErrorMapper
import com.rapptrlabs.androidtest.remote.responsemanager.ResponseStateHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SharedRepository(private val endpoints: Endpoints) {
    var errorMapper: ErrorMapper = ErrorMapper()

    fun getChats(): MutableLiveData<ResponseStateHandler<ChatsModel?>> {
        val responseStateHandler: MutableLiveData<ResponseStateHandler<ChatsModel?>> =
            MutableLiveData()
        responseStateHandler.postValue(ResponseStateHandler.Loading)
        endpoints.getChats().enqueue(object : Callback<ChatsModel> {
            override fun onResponse(call: Call<ChatsModel>, response: Response<ChatsModel>) {
                try {
                    if (response.isSuccessful) {
                        val chats: ChatsModel? = response.body()
                        responseStateHandler.postValue(ResponseStateHandler.Success(chats))
                    } else {
                        val error = errorMapper.parseErrorMessage(response.errorBody())
                        responseStateHandler.postValue(ResponseStateHandler.Error(error?.message))
                    }
                } catch (exception: Exception) {
                    responseStateHandler.postValue(ResponseStateHandler.Exception(exception))
                }
            }

            override fun onFailure(call: Call<ChatsModel>, t: Throwable) {
                responseStateHandler.postValue(ResponseStateHandler.ThrowableError(t))
            }
        })
        return responseStateHandler
    }

    fun login(
        email: String,
        password: String,
        context: Context
    ): MutableLiveData<ResponseStateHandler<LoginResponse?>> {
        val responseStateHandler: MutableLiveData<ResponseStateHandler<LoginResponse?>> =
            MutableLiveData()
        responseStateHandler.postValue(ResponseStateHandler.Loading)
        endpoints.login(email, password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                try {
                    if (response.isSuccessful) {
                        val duration =
                            response.raw().receivedResponseAtMillis /*- response.raw().sentRequestAtMillis*/
                        Prefs(context).setLongValue(Prefs.REQUEST_DURATION, duration)
                        val loginResponse: LoginResponse? = response.body()
                        responseStateHandler.postValue(ResponseStateHandler.Success(loginResponse))
                    } else {
                        val error = errorMapper.parseErrorMessage(response.errorBody())
                        responseStateHandler.postValue(ResponseStateHandler.Error(error?.message))
                    }
                } catch (exception: Exception) {
                    responseStateHandler.postValue(ResponseStateHandler.Exception(exception))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                responseStateHandler.postValue(ResponseStateHandler.ThrowableError(t))
            }
        })
        return responseStateHandler
    }
}