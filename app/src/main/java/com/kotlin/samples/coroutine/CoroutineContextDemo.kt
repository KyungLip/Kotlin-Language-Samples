package com.kotlin.samples.coroutine

import com.kotlin.samples.utils.logI
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * @author liupeng
 * @version v1.0
 * @e-mail kyliupeng@didiglobal.com
 * @date 2023/5/6 3:06 下午
 * @Desc
 */
class CoroutineContextDemo {
    fun entrance() {
        //1.CoroutineContext相加
        val myCoroutineName1 = MyCoroutineName1(MyCoroutineName1::class.java.name)
        val myCoroutineName2 = MyCoroutineName2(MyCoroutineName2::class.java.name)
        val myCoroutineName3 = MyCoroutineName3(MyCoroutineName3::class.java.name)
        val combinedContext = myCoroutineName1 + myCoroutineName2 + myCoroutineName3
        logI(combinedContext.toString())

        //2.CoroutineContext查找
        val findCoroutineName1 = combinedContext[MyCoroutineName1]
        logI(findCoroutineName1.toString())
        //3.CoroutineContext相减
        val leftCoroutineContext = combinedContext.minusKey(MyCoroutineName2)
        logI(leftCoroutineContext.toString())

    }
}

class MyCoroutineName1(val name: String) : AbstractCoroutineContextElement(MyCoroutineName1) {
    //1.Key生成的方式一：伴生对象。
    companion object Key : CoroutineContext.Key<MyCoroutineName1>

    //2.Key生成的方式二：类继承并创建对象。
//    override val key: CoroutineContext.Key<*>
//        get() = MyKey()
//
//    class MyKey:CoroutineContext.Key<MyCoroutineName1>
}

class MyCoroutineName2(val name: String) : AbstractCoroutineContextElement(MyCoroutineName2) {
    companion object Key : CoroutineContext.Key<MyCoroutineName2>
}

class MyCoroutineName3(val name: String) : AbstractCoroutineContextElement(MyCoroutineName3) {
    companion object Key : CoroutineContext.Key<MyCoroutineName3>
}