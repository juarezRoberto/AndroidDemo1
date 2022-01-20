package com.juarez.coppeldemo.data.models

data class CustomResponse<T>(val isSuccess: Boolean, val data: T?, val message: String?)