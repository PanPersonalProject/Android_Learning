package pan.lib.baseandroidframework.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pan.lib.baseandroidframework.R

/**
 * Desc:RecyclerView.Adapter的demo adapter
 */

data class User(val id: Long, val name: String, val age: Int) //会自动实现equals(),判断每个属性是否相等

class DemoRvAdapter(private val onItemClick: (item: User) -> Unit) :
    RecyclerView.Adapter<DemoRvAdapter.MyViewHolder>() {

    private var list = mutableListOf<User>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return MyViewHolder(view) //四级缓存获取的view不需要调用onCreateViewHolder
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        /**屏幕正在展示的item(Scarp)和超出屏幕上下一个的item(Cache)都不会执行onBindViewHolder,此外的view在RecycledViewPool需要重新onBindViewHolder
         * 不要在onBindViewHolder里面设置onClickListener，会导致重复创建onClickListener
         */
        holder.bind(list[position])

    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            // 如果payloads为空，说明是完全刷新，正常绑定数据
            onBindViewHolder(holder, position)
        } else {
            // 当payloads不为空时，根据payloads更新特定的UI元素
            val bundle = payloads.firstOrNull() as? Bundle ?: return // 安全转换并取第一个payload

            // 根据payload更新UI
            bundle.getString("name")?.let { newName ->
                holder.tvName.text = newName
            }

            bundle.getInt("age").let { newAge ->
                holder.tvAge.text = newAge.toString()
            }
        }
    }

    //此方法会在视图绑定到窗口时被调用
    override fun onViewAttachedToWindow(holder: MyViewHolder) {
        super.onViewAttachedToWindow(holder)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvAge: TextView = itemView.findViewById(R.id.tvAge)

        init {
            itemView.setOnClickListener {
                onItemClick(list[adapterPosition])
            }
        }

        fun bind(user: User) {
            tvName.text = user.name
            tvAge.text = user.age.toString()

        }
    }

    fun submitList(lifecycleScope: LifecycleCoroutineScope, newList: List<User>, diff: Boolean) {
        if (!diff) {
            list.clear()
            list.addAll(newList)
            notifyDataSetChanged()
        } else {
            lifecycleScope.launch(Dispatchers.Main) {
                val diffResult = withContext(Dispatchers.IO) {
                    // 在IO线程中执行DiffUtil计算
                    DiffUtil.calculateDiff(DiffUtilCallback(list, newList))
                }

                list = newList.toMutableList()
                diffResult.dispatchUpdatesTo(this@DemoRvAdapter)

            }


        }

    }
}
