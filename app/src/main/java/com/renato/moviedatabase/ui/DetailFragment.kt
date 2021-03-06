package com.renato.moviedatabase.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.renato.moviedatabase.R
import com.renato.moviedatabase.appComponent
import com.renato.moviedatabase.data.model.Movie
import com.renato.moviedatabase.di.DaggerMoviesComponent
import com.renato.moviedatabase.di.ViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, obtainVMFactory())
            .get(DetailViewModel::class.java)

        viewModel.observableState.observe(this, Observer { state -> state?.let { render(state) } })

        viewModel.dispatch(DetailAction.LoadMovie(arguments!!.getParcelable("movie") as Movie))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collapsingToolbar?.let {
            it.setContentScrimColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
            it.setExpandedTitleColor(resources.getColor(android.R.color.white))
            it.setCollapsedTitleTextColor(resources.getColor(android.R.color.white))
            it.title = "Movie Details"
            it.isTitleEnabled = true
        }

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        setupWithNavController(toolbar,findNavController())

    }

    private fun obtainVMFactory(): ViewModelFactory<DetailViewModel> {
        return DaggerMoviesComponent.builder()
            .appComponent(appComponent)
            .build()
            .provideDetailVM()
    }

    private fun render(state: DetailState) {
        if (state.movie == null) return

        renderMovie(state.movie)
    }

    private fun renderMovie(movie: Movie) {
        Picasso.get()
            .load(IMAGE_URL + movie.posterImage)
            .placeholder(R.drawable.placeholder)
            .fit()
            .into(poster)

        Picasso.get()
            .load(IMAGE_URL + movie.backdropImage)
            .placeholder(R.drawable.placeholder)
            .into(backdrop)

        title.text = movie.title
        overview.text = movie.overview
        releaseDate.text = movie.releaseDate
        status.text = movie.status
        rating.text = movie.rating
    }
}