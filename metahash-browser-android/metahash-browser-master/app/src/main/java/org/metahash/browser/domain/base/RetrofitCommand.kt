package org.metahash.browser.domain.base

import io.reactivex.Observable

abstract class RetrofitCommand<RESULT> : RetrofitCommandWithMapping<RESULT, RESULT>() {

    override fun Observable<RESULT>.afterResponse() : Observable<RESULT> = this
}