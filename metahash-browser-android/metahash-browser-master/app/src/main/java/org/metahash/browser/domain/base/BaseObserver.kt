package org.metahash.browser.domain.base

import io.reactivex.observers.DisposableObserver


class BaseObserver<T> : DisposableObserver<T>() {

    var onNext: (T) -> Unit = { }
    var onError: (Throwable) -> Unit = { }
    var onComplete: () -> Unit = { }
    var onStart: () -> Unit = { }

    override fun onStart() {
        onStart.invoke()
    }

    override fun onComplete() {
        onComplete.invoke()
    }

    override fun onNext(t: T) {
        onNext.invoke(t)
    }

    override fun onError(e: Throwable) {
        onError.invoke(e)
    }
}