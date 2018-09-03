package org.metahash.browser.extentions

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.View

fun View.makeVisible() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

fun View.makeGone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

fun Activity.fromUI(action:() -> Unit) {
    Handler(Looper.getMainLooper()).post(action)
}