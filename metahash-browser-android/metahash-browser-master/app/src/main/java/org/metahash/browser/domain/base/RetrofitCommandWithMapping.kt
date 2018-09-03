package org.metahash.browser.domain.base

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.metahash.browser.extentions.applyObservableAsync
import org.metahash.browser.extentions.applyObservableCompute

abstract class RetrofitCommandWithMapping<RESULT, RESPONSE> {

    protected abstract fun serviceRequest(): Observable<RESPONSE>
    private var subscribeScheduler = Schedulers.io()

    fun execute(): Observable<RESULT> {
        val request = serviceRequest().applyObservableAsync(subscribeScheduler)
        return request.afterResponse().applyObservableCompute()
    }


    abstract fun Observable<RESPONSE>.afterResponse(): Observable<RESULT>

    fun setSubscribeScheduler(scheduler: Scheduler) {
        subscribeScheduler = scheduler
    }
}