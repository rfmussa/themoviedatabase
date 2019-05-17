package com.renato.moviedatabase.ui

import com.renato.moviedatabase.R
import com.renato.moviedatabase.data.model.Movie
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_movie.*

const val IMAGE_URL = "https://image.tmdb.org/t/p/w185_and_h278_bestv2"

class MovieItem constructor(val movie: Movie) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.text = movie.title
        Picasso.get()
            .load(IMAGE_URL + movie.posterImage)

            .into(viewHolder.image)

        // Set item specific transition names
        viewHolder.image.transitionName = movie.posterImage
    }

    override fun getLayout() = R.layout.item_movie

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount / 2
}