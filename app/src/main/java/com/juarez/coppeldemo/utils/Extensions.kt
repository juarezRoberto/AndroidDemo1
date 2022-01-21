package com.juarez.coppeldemo.utils

import android.widget.ImageView
import com.juarez.coppeldemo.R
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
    Picasso.get().load(url).error(R.drawable.hero_placeholder).into(this)
}


