package org.metahash.browser.extentions

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ObservableTransformerFactory {

    companion object {

        fun <T> applyAsync(
                subscribeScheduler: Scheduler = Schedulers.io(),
                observeScheduler: Scheduler = AndroidSchedulers.mainThread()
        ): ObservableTransformer<T, T>{
            return ObservableTransformer { source ->
                source
                        .subscribeOn(subscribeScheduler)
                        .observeOn(observeScheduler)
            }
        }
    }
}

fun <T> Observable<T>.applyObservableCompute(): Observable<T> {
    return compose(ObservableTransformerFactory.applyAsync(subscribeScheduler = Schedulers.computation()))
}

fun <T> Observable<T>.applyObservableAsync(scheduler: Scheduler = Schedulers.io()): Observable<T> {
    return compose(ObservableTransformerFactory.applyAsync(subscribeScheduler = scheduler))
}

fun <T> Observable<T>.applyAsync(subscribeScheduler: Scheduler, observeScheduler: Scheduler): Observable<T> {
    return compose(ObservableTransformerFactory.applyAsync(
            subscribeScheduler = subscribeScheduler,
            observeScheduler = observeScheduler
    ))
}