package com.renato.moviedatabase.data.model


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    @Json(name = "poster_path")
    val posterImage: String? = "",
    @Json(name = "backdrop_path")
    val backdropImage: String? = "",
    val genres: List<Genre> = listOf(),
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "original_language")
    val originalLanguage: String = "",
    @Json(name = "original_title")
    val originalTitle: String = "",
    @Json(name = "overview")
    val overview: String = "",
    @Json(name = "release_date")
    val releaseDate: String = "",
    @Json(name = "title")
    val title: String = "",
    @Json(name = "vote_average")
    val rating: String = "",
    @Json(name = "status")
    val status: String = ""
) : Parcelable {
    @Parcelize
    data class Genre(
        @Json(name = "id")
        val id: Int = 0,
        @Json(name = "name")
        val name: String = ""
    ) : Parcelable
}