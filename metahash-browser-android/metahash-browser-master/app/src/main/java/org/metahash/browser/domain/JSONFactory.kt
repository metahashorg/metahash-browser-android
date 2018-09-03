package org.metahash.browser.domain

import org.metahash.browser.data.GetPopularRequest

object JSONFactory {

    private const val ID = 1
    private const val VERSION = "1.0.0"

    private const val METHOD_GET_BY_ID = "getByID"
    private const val METHOD_GET_POPULAR = "getPopular"

    fun getRequestData(type: TYPE): GetPopularRequest {
        return when(type) {
            TYPE.POPULAR -> getPopularRequest()
        }
    }

    private fun getPopularRequest() = GetPopularRequest(
            ID, VERSION, METHOD_GET_POPULAR, Any()
    )

    enum class TYPE {
        POPULAR
    }
}