package com.rapptrlabs.androidtest.repository

import android.content.Context
import androidx.lifecycle.ViewModel

class SharedViewModel(private val sharedRepository: SharedRepository): ViewModel() {

    var email_text: String? = null
    var password_text: String? = null

    fun getChats() = sharedRepository.getChats()

    fun login(email: String, password: String, context: Context) = sharedRepository.login(email, password, context)
}