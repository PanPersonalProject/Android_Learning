package pan.lib.baseandroidframework.ui.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil

class DiffUtilCallback(private val oldList: List<User>, private val newList: List<User>) :
    DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        //比较是不是同一个item，比如只要最重要的id是相同的就是同一个item，其他属性变了没关系
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        /* 如果是同一个item，比较item里面的的内容是否都相同，比如id相同，其他属性变了，就认为内容变了。
            这里用了dataclass == 比较，这里用了dataclass会自动实现equals(),判断每个属性是否相等*/
        oldList[oldItemPosition] == newList[newItemPosition]


    /* 它可以在 ViewType 相同，但是内容不相同的时候，用 payLoad 记录需要在这个 ViewHolder 中，具体需要更新的View。
         对性能要是要求没那么高的情况下，是可以不使用 getChangedPayload() 方法的。*/
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]

        val payload = Bundle()

        // 检查并记录name的变化
        if (oldUser.name != newUser.name) {
            payload.putString("name", newUser.name)
        }

        // 检查并记录age的变化
        if (oldUser.age != newUser.age) {
            payload.putInt("age", newUser.age)
        }

        // 如果payload中有变化的数据，则返回它；否则返回null
        return if (payload.size() > 0) payload else null
    }

}