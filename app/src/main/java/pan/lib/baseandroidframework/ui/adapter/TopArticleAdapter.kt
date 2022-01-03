package pan.lib.baseandroidframework.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import pan.lib.baseandroidframework.R


/**
 * author: Pan
 * date: 2020/7/12
 */
class TopArticleAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_top_article) {

    override fun convert(helper: BaseViewHolder, str: String) {
        helper.setText(R.id.tvTitle, str)
    }
}