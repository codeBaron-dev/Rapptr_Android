package com.rapptrlabs.androidtest.repository

import androidx.lifecycle.ViewModel

class SharedViewModel(private val sharedRepository: SharedRepository): ViewModel() {

    fun getChats() = sharedRepository.getChats()
}