package com.kotlin.samples.coroutine

import com.kotlin.samples.utils.logI
import kotlinx.coroutines.delay
import kotlin.coroutines.*

/**
 * @author liupeng
 * @version v1.0
 * @e-mail kyliupeng@didiglobal.com
 * @date 2023/5/5 3:23 下午
 * @Desc
 */

class MyCoroutine : Continuation<String> {
    override val context: CoroutineContext
        get() = EmptyCoroutineContext

    override fun resumeWith(result: Result<String>) {
        logI("kylp", "MyCoroutine 回调resumeWith:${result.getOrNull()}")
    }
}

suspend fun commonSuspendFun1(): String {
    delay(100)
    return "Hello1"
}

suspend fun commonSuspendFun2(): String {
    delay(10000)
    return "Hello2"
}

suspend fun commonSuspendFun3(): String {
    return "Hello3"
}

fun testOne() {
    val myCoroutineFun: suspend () -> String = {
        logI("kylp", "返回 hello结果")
        val one = commonSuspendFun1()
        val two = commonSuspendFun2()
        one + two

    }

    val myCoroutine = MyCoroutine()

    val myContinuation = myCoroutineFun.createCoroutine(myCoroutine)
    myContinuation.resume(Unit)
}
