package org.metahash.browser.presentation.base

import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()

    override fun onDestroy() {
        unsubscribe()
        super.onDestroy()
    }

    override fun onPause() {
        if (isFinishing) {
            unsubscribe()
        }
        super.onPause()
    }

    private fun unsubscribe() {
        disposable.clear()
    }

    fun addSubscription(d: Disposable) {
        disposable.add(d)
    }
}