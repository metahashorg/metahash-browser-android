package org.metahash.browser.domain.commands

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.metahash.browser.data.GetPopularResponse
import org.metahash.browser.domain.BrowserApi
import org.metahash.browser.domain.JSONFactory
import org.metahash.browser.domain.base.RetrofitCommand
import retrofit2.Response

class GetPopularCommand(
        private val api: BrowserApi
) : RetrofitCommand<GetPopularResponse>() {

    override fun serviceRequest() = api.getPopular(JSONFactory.getRequestData(JSONFactory.TYPE.POPULAR))
}