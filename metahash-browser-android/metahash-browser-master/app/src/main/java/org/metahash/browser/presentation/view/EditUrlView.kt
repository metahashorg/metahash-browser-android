package org.metahash.browser.presentation.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import org.metahash.browser.R
import org.metahash.browser.data.WebViewState
import org.metahash.browser.extentions.bind
import org.metahash.browser.extentions.makeGone
import org.metahash.browser.extentions.makeVisible
import org.metahash.browser.extentions.showKeyboard

class EditUrlView
@JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyle: Int = 0)
    : ConstraintLayout(context, attrs, defStyle) {

    private val vCancel by bind<View>(R.id.vCancel)
    private val etUrl by bind<AppCompatEditText>(R.id.etUrl)
    private val tvUrl by bind<TextView>(R.id.tvUrl)
    private val ivRefresh by bind<ImageView>(R.id.ivRefresh)
    private val ivStop by bind<ImageView>(R.id.ivStop)
    private var state = WebViewState.IDLE

    var onCancelClick = {}
    var onUrlEntered: (String) -> Unit = {}
    var onRefreshClick = {}
    var onStopClick = {}

    init {
        LayoutInflater.from(context).inflate(R.layout.view_edit_url, this)
        initView()
        manageState()
    }

    private fun initView() {
        vCancel.setOnClickListener {
            onCancelClick.invoke()
        }
        etUrl.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                    actionId == EditorInfo.IME_ACTION_DONE) {
                val userUrl = etUrl.text.toString()
                //show ui
                tvUrl.text = etUrl.text.toString()
                etUrl.makeGone()
                tvUrl.makeVisible()
                onUrlEntered.invoke(userUrl)
                true
            }
            false
        }
        tvUrl.setOnClickListener {
            etUrl.makeVisible()
            tvUrl.makeGone()
            etUrl.requestFocus()
            etUrl.setSelection(etUrl.length())
            context.showKeyboard(etUrl.windowToken)
        }
        ivRefresh.setOnClickListener {
            onRefreshClick.invoke()
        }
        ivStop.setOnClickListener {
            onStopClick.invoke()
        }
    }

    fun show(state: WebViewState) {
        if (etUrl.text.isEmpty()) {
            tvUrl.makeGone()
            etUrl.makeVisible()
            etUrl.requestFocus()
            etUrl.setSelection(etUrl.length())
            context.showKeyboard(etUrl.windowToken)
        } else {
            tvUrl.makeVisible()
            etUrl.makeGone()
        }
        this.state = state
        manageState()

        this.makeVisible()
    }

    fun setUrl(url: String) {
        etUrl.setText(url)
        tvUrl.text = url
    }

    private fun manageState() {
        when(state) {
            WebViewState.LOADING -> {
                ivRefresh.makeGone()
                ivStop.makeVisible()
            }
            WebViewState.IDLE -> {
                ivRefresh.makeGone()
                ivStop.makeGone()
            }
            WebViewState.LOADED -> {
                ivRefresh.makeVisible()
                ivStop.makeGone()
            }
        }
    }
}