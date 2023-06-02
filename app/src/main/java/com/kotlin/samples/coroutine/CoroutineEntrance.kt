package com.kotlin.samples.coroutine

import android.util.Log
import com.kotlin.samples.utils.logI
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @author liupeng
 * @version v1.0
 * @e-mail kyliupeng@didiglobal.com
 * @date 2023/5/5 2:10 下午
 * @Desc
 */
class CoroutineEntrance {
    fun entrance() {
//        runBlocking {
//            CoroutineScope(Dispatchers.IO).launch {
//                log("当前线程:${Thread.currentThread().name}")
//            }
//            launch {
//                delay(200L)
//                log("Task from runBlocking")
//            }
//
//            coroutineScope { // 创建一个协程作用域
//                launch {
//                    delay(500L)
//                    log("Task from nested launch")
//                }
//
//                delay(100L)
//                log("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
//            }
//
//            log("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
//        }
//        customCoroutineStart()
//        testOne()
//        CoroutineContextDemo().entrance()
        val coroutineScope = CoroutineScope(EmptyCoroutineContext)
        val job = coroutineScope.launch() {
            launch {

            }
            logI("当前线程:${Thread.currentThread().name}")
            logI("内部Job1:${coroutineContext[Job]}")
            withContext(Dispatchers.Main) {
                logI("内部Job2:${coroutineContext[Job]}")
            }
            logI("内部Job3:${coroutineContext[Job]}")
        }
        job.invokeOnCompletion {
            logI("invokeOnCompletion")
        }
        logI("外部Job:$job")
    }

    suspend fun gogo(): Int {
        delay(100)
        return 1
    }

    /**
     * 1.runBlocking会阻塞主线程
     */
    private fun runBlockingMode() {
        log("1.RunBlocking 前")
        runBlocking {
            val mutex = Mutex()
            mutex.withLock {

            }
            delay(100)
            //不会阻塞runBlocking
            launch {
                delay(100)
                log("launch")
            }
            log("2.RunBlocking 执行完成")
        }
        log("3.RunBlocking 后")
        //输出顺序索引为:1 2 launch 3
    }

    /**
     * 2.launch 不会阻塞
     */
    private fun launchCoroutine() {
        log("start")
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            launch {
                delay(200)
                log("3")
            }
            delay(100)
            log("2")

        }
        scope.launch {
            log("4")
        }
        log("end")
    }

    private fun coroutineScopeMode() {
        runBlocking {
            coroutineScope {
                launch {
                    delay(300)
                    log("1")
                }
                launch {
                    delay(100)
                    log("2")
                }
            }
        }
    }

    private fun log(msg: String?) {
        Log.i("kylp", msg ?: "null")
    }
}