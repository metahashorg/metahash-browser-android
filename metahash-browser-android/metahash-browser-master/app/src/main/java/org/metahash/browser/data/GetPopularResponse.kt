package org.metahash.browser.data

data class GetPopularResponse(
        val result: String,
        val version: String,
        val id: String,
        val data: List<PopularApp>
)

data class PopularApp(
        val icon: String? = null,
        val name: String,
        val company: String,
        val description: String,
        val url: String?
)