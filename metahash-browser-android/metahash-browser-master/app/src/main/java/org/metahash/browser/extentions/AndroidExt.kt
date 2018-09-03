package org.metahash.browser.extentions

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager

fun Activity.fromUI(action:() -> Unit, delay: Long = 0) {
    if (delay == 0L) {
        Handler(Looper.getMainLooper()).post(action)
    } else {
        Handler(Looper.getMainLooper()).postDelayed(action, delay)
    }
}

val Int.dpToPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.pxToDp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Float.dpToPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

inline fun View.waitForLayout(crossinline f: () -> Unit) = with(viewTreeObserver) {
    addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            f()
        }
    })
}

fun Activity.hideKeyboard(token: IBinder) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(token, 0)
}

fun Activity.showKeyboard(token: IBinder) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInputFromWindow(token, InputMethodManager.SHOW_FORCED, 0)
}

fun Context.hideKeyboard(token: IBinder) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(token, 0)
}

fun Context.showKeyboard(token: IBinder) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInputFromWindow(token, InputMethodManager.SHOW_FORCED, 0)
}