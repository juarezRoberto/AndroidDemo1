package com.juarez.coppeldemo.models

data class CustomResponse<T>(val isSuccess: Boolean, val data: T?, val message: String?)