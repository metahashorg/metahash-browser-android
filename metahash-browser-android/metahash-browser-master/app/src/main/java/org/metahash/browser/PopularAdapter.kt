package org.metahash.browser

import android.content.Context
import android.view.View
import org.metahash.browser.data.PopularApp
import org.metahash.browser.presentation.view.BaseRecyclerAdapter
import org.metahash.browser.presentation.view.PopularItemView

class PopularAdapter(val context: Context, data: MutableList<PopularApp>, layout: Int)
    : BaseRecyclerAdapter<PopularAdapter.Holder, PopularApp>(context, data, layout, Holder::class.java) {

    override fun performFilter(query: String, item: PopularApp) = item.name.contains(query, true)

    override fun bindViewHolder(item: PopularApp, holder: Holder) {
        holder.populate(item)
    }

    inner class Holder(view: View) : BaseRecyclerAdapter<Holder, PopularApp>.Holder(view) {

        fun populate(item: PopularApp) {
            itemView as PopularItemView
            itemView.populate(item)
        }
    }
}