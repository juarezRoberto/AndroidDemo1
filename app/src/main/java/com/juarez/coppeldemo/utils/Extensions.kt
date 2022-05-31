package com.juarez.coppeldemo.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.juarez.coppeldemo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

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

fun ImageView.loadImage(url: String?, @DrawableRes error: Int = R.drawable.hero_placeholder) {
    if (!url.isNullOrEmpty()) {
        Glide.with(this).load(url).error(error).into(this)
    }
}

inline fun <T> Flow<T>.launchAndCollectIn(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.(T) -> Unit,
) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(minActiveState) {
        collect {
            action(it)
        }
    }
}


