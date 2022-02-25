package com.juarez.coppeldemo.utils

import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.juarez.coppeldemo.R
import com.squareup.picasso.Picasso
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

fun ImageView.loadImage(url: String?) {
    if (!url.isNullOrEmpty()) {
        Picasso.get().load(url).error(R.drawable.hero_placeholder).into(this)
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


