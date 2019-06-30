package com.gabilheri.moviestmdb.ui.details

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import com.gabilheri.moviestmdb.R
import com.gabilheri.moviestmdb.dagger.modules.HttpClientModule
import com.gabilheri.moviestmdb.data.models.Movie
import com.gabilheri.moviestmdb.ui.base.BaseTvActivity
import com.gabilheri.moviestmdb.ui.base.GlideBackgroundManager


/**
 * Created by Hein Htet on 2019-06-30.
 */
class MovieDetailsActivity : BaseTvActivity() {
    private lateinit var mBackgroundManager: GlideBackgroundManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movie = intent.extras!!
                .getParcelable<Parcelable>(Movie::class.java.simpleName) as Movie
        val detailsFragment = MovieDetailsFragment.newInstance(movie)
        addFragment(detailsFragment) // Method from BaseTvActivity
        mBackgroundManager = GlideBackgroundManager(this)
        if (movie.backdropPath != null) {
            mBackgroundManager.loadImage(HttpClientModule.BACKDROP_URL + movie.backdropPath)
        } else {
            mBackgroundManager.setBackground(ContextCompat.getDrawable(this, R.drawable.material_bg))
        }
    }
}