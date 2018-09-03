package org.metahash.browser.presentation.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.makeramen.roundedimageview.RoundedImageView
import org.metahash.browser.R
import org.metahash.browser.data.PopularApp
import org.metahash.browser.extentions.bind

class PopularItemView
@JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyle: Int = 0)
    : ConstraintLayout(context, attrs, defStyle) {

    private val ivIcon by bind<RoundedImageView>(R.id.ivIcon)
    private val tvTitle by bind<TextView>(R.id.tvTitle)
    private val tvCompany by bind<TextView>(R.id.tvCompany)
    val tvDecription by bind<TextView>(R.id.tvDecription)

    val options = RequestOptions()
            .override(300, 300)

    fun populate(item: PopularApp) {
        with(item) {
            tvTitle.text = name
            tvCompany.text = company
            tvDecription.text = description

            Glide.with(context)
                    .load(icon ?: "")
                    .apply(options)
                    .into(ivIcon)
        }
    }
}