package com.example.tvshows.utilities

import android.content.Context
import android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
import android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK

fun Context.orientation(): Int {
    return resources.configuration.orientation
}

fun Context.isTablet(): Boolean {
    return resources.configuration.screenLayout and SCREENLAYOUT_SIZE_MASK >= SCREENLAYOUT_SIZE_LARGE
}