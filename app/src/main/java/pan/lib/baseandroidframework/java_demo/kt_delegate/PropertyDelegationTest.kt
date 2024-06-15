package pan.lib.baseandroidframework.java_demo.kt_delegate

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 实现ReadOnlyProperty接口，展示val属性委托机制。
 */
class Delegate1: ReadOnlyProperty<Any, String> {
    override fun getValue(thisRef: Any, property: KProperty<*>) = "ReadOnlyProperty委托，属性名:${property.name}"
}

/**
 * 实现ReadWriteProperty接口，支持var属性的读写委托。
 */
class Delegate2: ReadWriteProperty<Any, Int> {
    override fun getValue(thisRef: Any, property: KProperty<*>) = 20
    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        println("委托属性:${property.name}, 值:$value")
    }
}

/**
 * 测试类，包含委托属性d1和d2。
 */
class Test {
    val d1: String by Delegate1() // 委托ReadOnlyProperty实现只读属性
    var d2: Int by Delegate2()   // 委托ReadWriteProperty实现可读写属性
}

fun main() {
    val test = Test()
    println(test.d1) // 读取委托的值
    println(test.d2)
    test.d2 = 100   // 修改委托属性值
}
