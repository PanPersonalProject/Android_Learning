package pan.lib.baseandroidframework.java_demo.kt_delegate

/**
 * 类委托demo
 */
interface Work {
    fun app()
    fun ui()
    fun service()
}

/**
 * 员工类，实现了Work接口，具体执行各项工作任务。
 */
class Employee : Work {
    override fun app() {
        println("Employee doing app")
    }

    override fun ui() {
        println("Employee doing ui")
    }

    override fun service() {
        println("Employee doing service")
    }
}

/**
 * 老板类，通过委托机制实现Work接口，将具体工作交给Employee执行。
 * @param employee 负责执行工作的员工实例。
 */
class Boss(private val employee: Employee) : Work by employee

/**
 * 程序入口。
 * 主要演示了Boss类如何通过委托方式调用Employee实现的工作任务。
 */
fun main() {
    val boss = Boss(Employee())
    boss.app()
    boss.ui()
    boss.service()
}
