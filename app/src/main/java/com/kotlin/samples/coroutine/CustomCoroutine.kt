package com.kotlin.samples.coroutine

import com.kotlin.samples.utils.logI
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.Executors
import kotlin.coroutines.intrinsics.*
import kotlin.coroutines.suspendCoroutine

/**
 * @author liupeng
 * @version v1.0
 * @e-mail kyliupeng@didiglobal.com
 * @date 2023/5/5 5:06 下午
 * @Desc
 */

fun customCoroutineStart(){
    Main.execute {
        resumeWithImpl(SuspendLambdaImpl())
    }
//    suspendCoroutineUninterceptedOrReturn<> {  }
//    suspendCoroutine<> {  }
}

/**
 * 主线程池
 */
val Main =
    Executors.newSingleThreadExecutor { runnable -> Thread(runnable).also { it.name = "main" } }

/**
 * IO线程池
 */
val IO =
    Executors.newSingleThreadExecutor { runnable -> Thread(runnable).also { it.name = "io" } }

interface CustomContinuation<in T> {
    fun resumeWith(result: T)
}

class SuspendLambdaImpl : CustomContinuation<String> {
    var label = 0
    var result: String? = null
    override fun resumeWith(_result: String) {
        result = _result
        resumeWithImpl(this)
    }
}

fun resumeWithImpl(continuateion: SuspendLambdaImpl) {
    when(continuateion.label){
        0->{
            logI("start thread: ${Thread.currentThread().name}")
            continuateion.label=1
            //挂起
            if(getUserInfo(continuateion)==1){
                return
            }
        }
        1->{
            logI("result:getUserInfo= ${continuateion.result}  ${Thread.currentThread().name}")
            continuateion.label=2
            if(getFriendList(continuateion.result!!,continuateion)==1){
                return
            }
        }
        2->{
            logI("result:getFriendList= ${continuateion.result} ${Thread.currentThread().name}")
            continuateion.label=3
            if(getFeedList(continuateion.result!!,continuateion)==1){
                return
            }
        }
        3->{
            logI("result:getFeedList=${continuateion.result} ${Thread.currentThread().name}")
        }
    }
}

/**
 * Step1
 */
fun getUserInfo(continuation: CustomContinuation<String>): Any {
    IO.execute {
        //开始执行耗时操作
        logI("getUserInfo ...thread:${Thread.currentThread().name}")
        Thread.sleep(1000)
        //操作完成，进行下一步
        Main.execute { continuation.resumeWith("userName:Tom") }
    }
    return 1
}

/**
 * step2
 */
fun getFriendList(userName: String, continuateion: CustomContinuation<String>): Any {
    IO.execute {
        //执行耗时操作
        logI("getFriendList by $userName..... thread: ${Thread.currentThread().name}")
        Thread.sleep(1000)
        //操作完成，进行下一步
        Main.execute { continuateion.resumeWith("Li,Wang,Zhang") }
    }
    return 1
}

/**
 * step3
 */
fun getFeedList(friendList: String, continuation: CustomContinuation<String>): Any {
    IO.execute {
        //执行耗时操作
        logI("getFeedList by ${friendList}..... thread: ${Thread.currentThread().name}")
        Thread.sleep(1000)
        //操作完成，进行下一步
        Main.execute {
            continuation.resumeWith("feed1,feed2,feed3")
        }
    }
    return 1
}
