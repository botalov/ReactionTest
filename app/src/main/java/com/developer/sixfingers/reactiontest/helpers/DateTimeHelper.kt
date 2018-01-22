package com.developer.sixfingers.reactiontest.helpers

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun convertNumberToString(value: Long): String{
    val formatter = SimpleDateFormat("ss:SS")
    return formatter.format(value)
}