package com.renato.moviedatabase.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.renato.moviedatabase.R
import com.renato.moviedatabase.appComponent
import com.renato.moviedatabase.data.model.Movie
import com.renato.moviedatabase.di.DaggerMoviesComponent
import com.renato.moviedatabase.di.ViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import khronos.Dates.today
import khronos.days
import khronos.minus
import khronos.toString
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.item_movie.view.*

class ListFragment : Fragment() {
    private val groupAdapter: GroupAdapter<ViewHolder> = GroupAdapter()
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var viewModel: MovieListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, obtainVMFactory())
            .get(MovieListViewModel::class.java)


        viewModel.observableState.observe(this, Observer { state -> state?.let { render(state) } })

        viewModel.dispatch(Action.LoadFeed(null))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun obtainVMFactory(): ViewModelFactory<MovieListViewModel> {
        return DaggerMoviesComponent.builder()
            .appComponent(appComponent)
            .build()
            .provideMovieListVM()
    }

    private fun render(state: State) {
        with(state) {
            when {
                isLoading -> renderLoadingState()
                isError -> renderErrorState()
                else -> renderPosts(state.movies)
            }
        }
    }

    private fun renderLoadingState() {
        list.visibility = View.GONE
        errorMessage.visibility = View.GONE
        fab.hide()
        progressBar.show()
    }

    private fun renderErrorState() {
        progressBar.hide()
        list.visibility = View.GONE
        errorMessage.visibility = View.VISIBLE
        //errorMessage.text = getString(R.string.feed_loading_error_message)
    }

    private fun renderPosts(movies: List<Movie>) {
        progressBar.hide()
        fab.show()
        errorMessage.visibility = View.GONE
        list.visibility = View.VISIBLE
        groupAdapter.update(movies.map { MovieItem(it) })
    }


    private fun setupRecyclerView() {
        groupAdapter.spanCount = 2

        gridLayoutManager = GridLayoutManager(this.context, groupAdapter.spanCount, getOrientation(), false).apply {
            spanSizeLookup = groupAdapter.spanSizeLookup
        }

//        val snapHelper = LinearSnapHelper()
//        snapHelper.attachToRecyclerView(list)

        list.apply {
            layoutManager = gridLayoutManager
            adapter = groupAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        }

        //TODO ideally this would be Passed as an Intent to the Presenter
        groupAdapter.setOnItemClickListener { item, view ->
            // Pass item to Bundle
            val bundle = bundleOf("movie" to (item as MovieItem).movie)

            // Push action to navigation controller
            view.findNavController().navigate(R.id.movieClick, bundle, null, null)
        }
viewModel.observableState.value
        fab.setOnClickListener {
            showDialog()
        }
    }

    private fun getOrientation(): Int {
        return if (resources.getBoolean(R.bool.isTablet) && resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager.HORIZONTAL
        } else {
            GridLayoutManager.VERTICAL
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialog() {
        MaterialDialog(this.context!!, BottomSheet())
            .show {
                customView(R.layout.filter_bottom_sheet)
                val seekBar = this.view.findViewById<SeekBar>(R.id.seekBar)
                val title = this.view.findViewById<TextView>(R.id.filterTitle)

                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        title.text = "Show Changes for the last ${seekBar!!.progress} days"
                    }

                }

                )
                title.text = "Show Changes for the last 0 days"
                positiveButton(R.string.set) { dialog ->
                    // convert to date format
                    //TODO move this logic to useCase
                    val startDate = (today - seekBar.progress.days).toString("YYYYMMdd")
                    viewModel.dispatch(Action.LoadFeed(startDate))
                }
            }
    }
}

