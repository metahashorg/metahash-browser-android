package org.metahash.browser.domain

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.metahash.browser.data.GetPopularRequest
import org.metahash.browser.data.GetPopularResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BrowserApi {

    @POST("browser/")
    fun getPopular(@Body body: GetPopularRequest) : Observable<GetPopularResponse>
}