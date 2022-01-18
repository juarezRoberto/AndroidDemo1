package com.juarez.coppeldemo.extensions

import android.widget.ImageView
import com.squareup.picasso.Picasso

/**
 * convert String to Int value
 */
fun String.convertToInt(): Int {
    return try {
        toInt()
    } catch (e: NumberFormatException) {
        0
    }
}

fun ImageView.loadImage(url: String?) {
    Picasso.get().load(url).into(this)
}


