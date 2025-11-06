package com.example.census.Util

import android.content.Context
import android.content.Intent

/**
 * Utility entry point that centralises explicit activity navigation.
 */
object ActivityUtils {
    /** Opens the [targetClass] activity from the provided [context]. */
    fun openActivity(context: Context, targetClass: Class<*>) {
        val intent = Intent(context, targetClass)
        context.startActivity(intent)
    }
}
