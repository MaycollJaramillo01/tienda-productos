package com.example.census.Util

import android.content.Context
import android.content.Intent

class ActivityUtils {
    companion object {
        fun openActivity(context: Context, targetClass: Class<*>) {
            val intent = Intent(context, targetClass)
            context.startActivity(intent)
        }
    }
}
