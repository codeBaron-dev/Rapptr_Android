package com.rapptrlabs.androidtest.remote.responsemanager

import java.io.Serializable

data class ErrorModel(
    val message: String? = null,
    val errors: String? = null
): Serializable