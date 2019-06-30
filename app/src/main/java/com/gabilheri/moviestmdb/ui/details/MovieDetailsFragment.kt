package com.gabilheri.moviestmdb.ui.details

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v17.leanback.app.DetailsSupportFragment
import android.support.v17.leanback.widget.*
import com.gabilheri.moviestmdb.data.models.Movie
import com.gabilheri.moviestmdb.App
import com.gabilheri.moviestmdb.data.models.MovieDetails
import com.gabilheri.moviestmdb.data.Api.TheMovieDbAPI
import javax.inject.Inject
import timber.log.Timber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import com.gabilheri.moviestmdb.Config.API_KEY_URL
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import com.gabilheri.moviestmdb.dagger.modules.HttpClientModule
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.gabilheri.moviestmdb.R
import com.gabilheri.moviestmdb.data.models.CreditsResponse
import com.gabilheri.moviestmdb.data.models.MovieResponse
import com.gabilheri.moviestmdb.ui.details.person.PersonPresenter
import com.gabilheri.moviestmdb.ui.movies.MoviePresenter


/**
 * Created by Hein Htet on 2019-06-30.
 */
class MovieDetailsFragment : DetailsSupportFragment() {


    // Injects the API using Dagger
    @Inject
    lateinit var mDbAPI: TheMovieDbAPI

    private lateinit var movie: Movie
    private lateinit var movieDetails: MovieDetails
    private lateinit var mAdapter: ArrayObjectAdapter
    private lateinit var mDetailsOverviewRow: DetailsOverviewRow


    private val mCastAdapter = ArrayObjectAdapter(PersonPresenter())
    private val recommendAdapter = ArrayObjectAdapter(MoviePresenter())


    companion object {
        const val TRANSITION_NAME = "poster_transition"

        fun newInstance(movie: Movie): MovieDetailsFragment {
            val args = Bundle()
            args.putParcelable((Movie::class.java.simpleName), movie)
            val fragment = MovieDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.instance().appComponent().inject(this)
        if (arguments == null || !arguments!!.containsKey(Movie::class.java.simpleName)) {
            throw RuntimeException("An movie is necessary for MovieDetailsFragment")
        }
        movie = arguments!!.getParcelable<Parcelable>(Movie::class.java.simpleName) as Movie
        setUpAdapter()
        setUpDetailsOverviewRow()
        setupCastMembers()
        setUpRecommend()
    }

    /**
     * Sets up the adapter for this Fragment
     */
    private fun setUpAdapter() {
        val mFullWidthMovieDetailsPresenter = FullWidthDetailsOverviewRowPresenter(
                MovieDetailDescriptionPresenter(), DetailsOverviewLogoPresenter()
        )
        val helper = FullWidthDetailsOverviewSharedElementHelper()
        helper.setSharedElementEnterTransition(activity, TRANSITION_NAME) // the transition name is important
        mFullWidthMovieDetailsPresenter.setListener(helper) // Attach the listener
        // Define if this element is participating in the transition or not
        mFullWidthMovieDetailsPresenter.isParticipatingEntranceTransition = false

        // Class presenter selector allows the Adapter to render Rows and the details
        // It can be used in any of the Adapters by the Leanback library
        val classPresenterSelector = ClassPresenterSelector()
        classPresenterSelector.addClassPresenter(DetailsOverviewRow::class.java, mFullWidthMovieDetailsPresenter)
        classPresenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())
        mAdapter = ArrayObjectAdapter(classPresenterSelector)
        adapter = mAdapter

        mFullWidthMovieDetailsPresenter.actionsBackgroundColor = ContextCompat.getColor(fragmentActivity, R.color.primary_dark)
        mFullWidthMovieDetailsPresenter.backgroundColor = ContextCompat.getColor(fragmentActivity, R.color.primary)
    }

    private lateinit var fragmentActivity: Activity

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragmentActivity = context as Activity
    }


    /**
     * Sets up the details overview rows
     */
    private fun setUpDetailsOverviewRow() {
        mDetailsOverviewRow = DetailsOverviewRow(MovieDetails())
        mAdapter.add(mDetailsOverviewRow)
        loadImage(HttpClientModule.POSTER_URL + movie.posterPath)
        fetchMovieDetails()
    }

    private val mGlideDrawableSimpleTarget = object : SimpleTarget<GlideDrawable>() {
        override fun onResourceReady(resource: GlideDrawable, glideAnimation: GlideAnimation<in GlideDrawable>) {
            mDetailsOverviewRow.imageDrawable = resource
        }
    }

    /**
     * Loads the poster image into the DetailsOverviewRow
     * @param url
     * The poster URL
     */
    private fun loadImage(url: String) {
        Glide.with(activity)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into<SimpleTarget<GlideDrawable>>(mGlideDrawableSimpleTarget)
    }

    /**
     * Fetches the movie details for a specific Movie.
     */
    private fun fetchMovieDetails() {
        mDbAPI.getMovieDetails(movie.id, API_KEY_URL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    bindMovieDetails(it)
                }, {
                    Timber.e(it, "Error fetching data: %s", it.localizedMessage)
                })
    }

    private fun setupCastMembers() {
        mAdapter.add(ListRow(HeaderItem(0, "Cast"), mCastAdapter))
        fetchCastMembers()
    }

    private fun setUpRecommend() {
        mAdapter.add(ListRow(HeaderItem(1, "Similer Video"), recommendAdapter))
        fetchRecommendations()
    }


    private fun fetchRecommendations() {
        mDbAPI.getRecommendations(movie.getId(), API_KEY_URL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    bindRecommendations(it)
                }, {
                    Timber.e(it, "Error fetching recommendations: %s", it.localizedMessage);
                })
    }

    private fun bindRecommendations(response: MovieResponse) {
        recommendAdapter.addAll(0, response.results)
    }

    private fun bindCastMembers(response: CreditsResponse) {
        mCastAdapter.addAll(0, response.cast)
    }

    private fun fetchCastMembers() {
        mDbAPI.getCredits(movie.getId(), API_KEY_URL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    this.bindCastMembers(it)
                }, {
                    Timber.e(it, "Error fetching data: %s", it.localizedMessage)
                })
    }

    private fun bindMovieDetails(movieDetails: MovieDetails) {
        this.movieDetails = movieDetails
        mDetailsOverviewRow.item = this.movieDetails
    }

}