package com.kotlin.samples.sequence

import com.kotlin.samples.utils.logI

/**
 * @author liupeng
 * @version v1.0
 * @e-mail kyliupeng@didiglobal.com
 * @date 2023/5/25 3:20 下午
 * @Desc
 */
class SequenceSamples {

    fun entrance() {
        //一.序列的创建方式
        //1.通过元素，使用sequenceOf函数，列出元素作为其参数。
        val numSequence1 = sequenceOf(1, 2, 3, 4, 5, 6)
        logI("numSequence1:${numSequence1.toList()}")

        //2.通过已有的Iterable对象，对于已有的一个Iterable对象(例如:List或Set),则可以通过调用asSequence 函数来创建一个序列。
        val numList = listOf(1, 2, 3, 4, 5, 6)
        val numSequence2 = numList.asSequence()
        logI("numSequence2:${numSequence2.toList()}")

        //3.通过函数generateSequence，传入一个用于计算元素的函数来构建序列，此序列是无限，当返回null时序列生成停止。
        var index = 1
        val numSequence3 = generateSequence {
            if (index >= 10) {
                null
            } else {
                index++
            }
        }
        logI("numSequence3:${numSequence3.toList()}")

        //第一个参数seed为种子也就是初始值，第二个参数中的it实际是指上一个元素，此例子中是1。
        val numSequence4 = generateSequence(1) {
            if (it >= 10) {
                null
            } else {
                it + 1
            }
        }
        logI("numSequence4:${numSequence4.toList()}")

        //跟上面相同，只不过seed变成了一个函数。
        val numSequence5 = generateSequence(seedFunction = {
            1
        }, nextFunction = {
            if (it >= 10) {
                null
            } else {
                it + 1
            }
        })
        logI("numSequence5:${numSequence5.toList()}")

        //4.通过sequence{}来创建序列，使用yield()或yieldAll来生产元素，生产以后会暂停sequence()的执行，直到使用者请求下一个元素。
        //yield()使用单个元素作为参数；yieldAll()使用Iterable对象，Iterator或者其它Sequence。
        //yieldAll的Sequence参数可以是无限的。但是必须在最后待用，否则之后的所有调用都永远不会执行。
        val numSequence6 = sequence<Int> {
            logI("Sequence:yield生产前")
            yield(1)
            logI("Sequence:yield生产后")
            yieldAll(listOf(2, 3, 4, 5, 6))
            yieldAll(generateSequence(1) {
                it + 1
            })
        }
        logI("numSequence6:${numSequence6.first()}")
        //二.序列操作
        //3.1 Iterable
        val list = listOf<Int>(1, 2, 3, 4, 5, 6)
        val newList = list.filter { logI("Filter:$it");it > 3 }
            .map { logI("Map:$it");it * it }
            .take(3)
        logI("Iterable:$newList")

        //3.2 Sequence
        val sequence = list.asSequence().filter { logI("Filter:$it");it > 3 }
            .map { logI("Map:$it");it * it }
            .take(3)
        logI("Sequence:${sequence.toList()}")
    }
    /**
     * 关于序列操作，根据其状态要求可以分为以下几类：

    无状态 操作不需要状态，并且可以独立处理每个元素，例如 map() 或 filter()。
    无状态操作还可能需要少量常数个状态来处理元素，例如 take() 与 drop()。
    有状态 操作需要大量状态，通常与序列中元素的数量成比例。
    如果序列操作返回延迟生成的另一个序列，则称为 中间序列。 否则，该操作为 末端 操作。 末端操作的示例为 toList() 或 sum()。只能通过末端操作才能检索序列元素。

    序列可以多次迭代；但是，某些序列实现可能会约束自己仅迭代一次。其文档中特别提到了这一点。
     */


}