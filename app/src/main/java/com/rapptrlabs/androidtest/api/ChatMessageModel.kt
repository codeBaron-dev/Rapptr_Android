package com.rapptrlabs.androidtest.api

data class ChatMessageModel(
    val userId: Int,
    val avatarUrl: String,
    val username: String,
    val message: String
)