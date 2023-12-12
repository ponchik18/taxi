package com.modsen.promocodeservice.exception

import java.util.*

data class ErrorMessageResponse(
    val statusCode: Int,
    val timestamp: Date,
    val message: String
) {}