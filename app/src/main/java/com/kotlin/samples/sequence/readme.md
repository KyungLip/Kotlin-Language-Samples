# Sequence

A sequence that returns values through its iterator. The values are evaluated lazily, and the
sequence is potentially infinite. Sequences can be iterated multiple times, however some sequence
implementations might constrain themselves to be iterated only once. That is mentioned specifically
in their documentation (e.g. generateSequence overload). The latter sequences throw an exception on
an attempt to iterate them the second time. Sequence operations, like Sequence.map, Sequence.filter
etc, generally preserve that property of a sequence, and again it's documented for an operation if
it doesn't. Params:T - the type of elements in the sequence.

Unlike collections, sequences don't contain elements, they produce them while iterating.

简单理解就是:
 序列即通过迭代器返回值。只不过这些值是被惰性地求值，且序列可以是无限的。

 序列与Iterable具有相似的操作函数，但它们的执行逻辑却是不同的。
 
 1.执行顺序不同
 Iterable:是在对整个集合完成每个处理步骤，在进行下一步。
 Sequence:是对每个元素逐个执行所有的处理步骤。

 2.执行时机
 Iterable:是在每个步骤调用时立即执行。
 Sequence:延迟执行的，只有请求整个处理链的结果时才进行好实际计算。
 Iterable:会生成中间集合，在处理多个步骤时，每个步骤都会对所有数据进行处理并且完成后会返回结果，这个结果就是中间集合。
 
所以，Sequence可以避免生成中间步骤，从而提高整个集合处理的性能。但是，Sequence的惰性性质增加了一些开销，这些开销在
处理较小的集合或进行跟简单的计算时可能并不会比Iterable表现好。
因此，应该根据使用场景来选择使用Sequence和Iterable。

