package org.metahash.browser.data

data class GetPopularRequest(
        val id: Int,
        val version: String,
        val method: String,
        val params: Any)