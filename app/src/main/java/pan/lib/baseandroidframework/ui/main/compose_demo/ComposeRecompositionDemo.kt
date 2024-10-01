package pan.lib.baseandroidframework.ui.main.compose_demo

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun ComposeRecompositionDemo() {

    Log.i("ComposeRecompositionDemo", "测试范围1")
    Column {
        Heavy(user)
        Log.i("ComposeRecompositionDemo", "测试范围2")

        Text(
            text = "点击变大，当前数值是: $number",
            modifier = Modifier
                .clickable {
                    number++
                    user = User("Mars")
                }
                .background(androidx.compose.ui.graphics.Color.Gray)
                .height(50.dp)
                .wrapContentHeight(),
        )

    }


}

@Composable
fun Heavy(user: User) {
    Log.i("ComposeRecompositionDemo", "执行了很重的操作")
    Text(text = "test ${user.name}")
}

var number by mutableIntStateOf(1)
var user = User("Mars")

/*
当name是val时，点击按钮是，不会触发Heavy重组，因为name是不可变的
如果name是var，点击按钮时，会触发Heavy重组，因为name是可变的
不然的话，如果重新赋值新user对象，只要name相同，data class就是认为相同的
但是Heavy会继续观察之前旧的对象，新的对象被改变了，也不会被观察到
*/
data class User(var name: String)


/*
推荐的写法：
@Stable 是一个稳定性标记，
加上这个注解就是在告诉 Compose 编译器插件，这个类型是可靠的，不用检查，由人工来保证；
但是人工保证并不能做到绝对，程序还是可能会出现问题，那么我们怎么处理呢？就是让它不相等，
也就是我们不去重写 equals 方法

另外，@Stable 的稳定，需要满足下面三点

现在相等就永远相等；
当公开属性改变的时候，要通知到用这个属性的 Composition
公开属性，也必须全都是可靠类型，或者说稳定类型
*/
@Stable
class NiceUser(name: String) {
    var name by mutableStateOf(name)
}
