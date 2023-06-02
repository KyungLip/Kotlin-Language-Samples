package com.kotlin.samples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kotlin.samples.coroutine.CoroutineEntrance
import com.kotlin.samples.flow.FlowEntrance
import com.kotlin.samples.sequence.SequenceSamples
import com.kotlin.samples.utils.logI
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.getOrSet
import kotlin.concurrent.withLock
import kotlin.math.log
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    private val mainScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        FlowEntrance().entrance()
//        CoroutineEntrance().entrance()
//        JavaEntrance().entrance()
//        SequenceSamples().entrance()
        main()
        logI("OnCreate:end")
    }

    private fun main() = runBlocking {
        val mutableStateFlow = MutableStateFlow(1)
        val stateFlow = mutableStateFlow.asStateFlow()

        GlobalScope.launch {
            // 触发并处理接收的数据
            stateFlow.collect {
                logI("test1: $it")
            }
        }

        // 启动一个新协程
        GlobalScope.launch {
            // 订阅监听，当collect方法触发订阅时，会首先会调onSubscription方法
            stateFlow.onSubscription {
                logI("test2: ")
                // 发射数据：2
                // 向下游发射数据：2，其他接收者收不到
                emit(2)
            }.onEach {
                // 处理接收的数据
                logI("test2: $it")
            }.collect()
        }

        // 发送数据：3，多次发送
        GlobalScope.launch {
            mutableStateFlow.emit(3)
            mutableStateFlow.emit(3)
            mutableStateFlow.compareAndSet(3, 3)
        }
    }
}

/**
 * select 表达式可以同时等待多个挂起函数，并 选择 第一个可用的。
Select 延迟值
延迟值可以使用 onAwait 子句查询。 让我们启动一个异步函数，它在随机的延迟后会延迟返回字符串：

fun CoroutineScope.asyncString(time: Int) = async {
delay(time.toLong())
"Waited for $time ms"
}
让我们随机启动十余个异步函数，每个都延迟随机的时间。

fun CoroutineScope.asyncStringsList(): List<Deferred<String>> {
val random = Random(3)
return List(12) { asyncString(random.nextInt(1000)) }
}
现在 main 函数在等待第一个函数完成，并统计仍处于激活状态的延迟值的数量。注意，我们在这里使用 select 表达式事实上是作为一种 Kotlin DSL， 所以我们可以用任意代码为它提供子句。在这种情况下，我们遍历一个延迟值的队列，并为每个延迟值提供 onAwait 子句的调用。

import kotlinx.coroutines.*
import kotlinx.coroutines.selects.*
import java.util.*

fun CoroutineScope.asyncString(time: Int) = async {
delay(time.toLong())
"Waited for $time ms"
}

fun CoroutineScope.asyncStringsList(): List<Deferred<String>> {
val random = Random(3)
return List(12) { asyncString(random.nextInt(1000)) }
}

fun main() = runBlocking<Unit> {
//sampleStart
val list = asyncStringsList()
val result = select<String> {
list.withIndex().forEach { (index, deferred) ->
deferred.onAwait { answer ->
"Deferred $index produced answer '$answer'"
}
}
}
println(result)
val countActive = list.count { it.isActive }
println("$countActive coroutines are still active")
//sampleEnd
}
可以在这里获取完整代码。

该输出如下：

Deferred 4 produced answer 'Waited for 128 ms'
11 coroutines are still active
在延迟值通道上切换
我们现在来编写一个通道生产者函数，它消费一个产生延迟字符串的通道，并等待每个接收的延迟值，但它只在下一个延迟值到达或者通道关闭之前处于运行状态。此示例将 onReceiveCatching 和 onAwait 子句放在同一个 select 中：

fun CoroutineScope.switchMapDeferreds(input: ReceiveChannel<Deferred<String>>) = produce<String> {
var current = input.receive() // 从第一个接收到的延迟值开始
while (isActive) { // 循环直到被取消或关闭
val next = select<Deferred<String>?> { // 从这个 select 中返回下一个延迟值或 null
input.onReceiveCatching { update ->
update.getOrNull()
}
current.onAwait { value ->
send(value) // 发送当前延迟生成的值
input.receiveCatching().getOrNull() // 然后使用从输入通道得到的下一个延迟值
}
}
if (next == null) {
println("Channel was closed")
break // 跳出循环
} else {
current = next
}
}
}
为了测试它，我们将用一个简单的异步函数，它在特定的延迟后返回特定的字符串：

fun CoroutineScope.asyncString(str: String, time: Long) = async {
delay(time)
str
}
main 函数只是启动一个协程来打印 switchMapDeferreds 的结果并向它发送一些测试数据：

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.*

fun CoroutineScope.switchMapDeferreds(input: ReceiveChannel<Deferred<String>>) = produce<String> {
var current = input.receive() // 从第一个接收到的延迟值开始
while (isActive) { // 循环直到被取消或关闭
val next = select<Deferred<String>?> { // 从这个 select 中返回下一个延迟值或 null
input.onReceiveCatching { update ->
update.getOrNull()
}
current.onAwait { value ->
send(value) // 发送当前延迟生成的值
input.receiveCatching().getOrNull() // 然后使用从输入通道得到的下一个延迟值
}
}
if (next == null) {
println("Channel was closed")
break // 跳出循环
} else {
current = next
}
}
}

fun CoroutineScope.asyncString(str: String, time: Long) = async {
delay(time)
str
}

fun main() = runBlocking<Unit> {
//sampleStart
val chan = Channel<Deferred<String>>() // 测试使用的通道
launch { // 启动打印协程
for (s in switchMapDeferreds(chan))
println(s) // 打印每个获得的字符串
}
chan.send(asyncString("BEGIN", 100))
delay(200) // 充足的时间来生产 "BEGIN"
chan.send(asyncString("Slow", 500))
delay(100) // 不充足的时间来生产 "Slow"
chan.send(asyncString("Replace", 100))
delay(500) // 在最后一个前给它一点时间
chan.send(asyncString("END", 500))
delay(1000) // 给执行一段时间
chan.close() // 关闭通道……
delay(500) // 然后等待一段时间来让它结束
//sampleEnd
}
可以在这里获取完整代码。

这段代码的执行结果：

BEGIN
Replace
END
Channel was closed
 */