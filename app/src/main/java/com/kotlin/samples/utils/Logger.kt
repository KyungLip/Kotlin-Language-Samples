package com.kotlin.samples.utils

import android.util.Log

/**
 * @author liupeng
 * @version v1.0
 * @e-mail kyliupeng@didiglobal.com
 * @date 2023/5/5 3:28 下午
 * @Desc
 */
fun logI(tag: String, msg: String?) {
    Log.i(tag, "${Thread.currentThread().name}: ${msg ?: "null"}")
}

fun logI(msg: String?) {
    logI("kylp", msg)
}