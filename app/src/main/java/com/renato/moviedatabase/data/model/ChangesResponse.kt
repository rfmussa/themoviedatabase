package com.renato.moviedatabase.data.model


import com.squareup.moshi.Json

data class ChangesResponse(
    @Json(name = "page")
    val page: Int = 0,
    @Json(name = "results")
    val results: List<Result> = listOf(),
    @Json(name = "total_pages")
    val totalPages: Int = 0,
    @Json(name = "total_results")
    val totalResults: Int = 0
) {
    data class Result(
        @Json(name = "adult")
        val adult: Boolean? = false,
        @Json(name = "id")
        val id: String = ""
    )
}