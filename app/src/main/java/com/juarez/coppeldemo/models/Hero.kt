package com.juarez.coppeldemo.models

data class Hero(val id: String, val name: String, val image: Image)

data class Image(val url: String)