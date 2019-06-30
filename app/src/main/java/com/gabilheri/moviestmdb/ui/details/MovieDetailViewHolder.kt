package com.gabilheri.moviestmdb.ui.details

import android.graphics.drawable.GradientDrawable
import android.support.v17.leanback.widget.Presenter
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.gabilheri.moviestmdb.R
import com.gabilheri.moviestmdb.data.models.MovieDetails
import java.util.*


/**
 * Created by Hein Htet on 2019-06-30.
 */
class MovieDetailViewHolder(private val itemView: View) : Presenter.ViewHolder(itemView) {


    init {
        ButterKnife.bind(this, itemView)
    }

    @BindView(R.id.movie_title)
    lateinit var movieTitleTV: TextView

    @BindView(R.id.movie_year)
    lateinit var movieYearTV: TextView

    @BindView(R.id.overview)
    lateinit var movieOverview: TextView

    @BindView(R.id.runtime)
    lateinit var mRuntimeTV: TextView

    @BindView(R.id.tagline)
    lateinit var mTaglineTV: TextView

    @BindView(R.id.director_tv)
    lateinit var mDirectorTv: TextView

    @BindView(R.id.overview_label)
    lateinit var mOverviewLabelTV: TextView

    @BindView(R.id.genres)
    lateinit var mGenresLayout: LinearLayout


    fun bind(movie: MovieDetails) {
        if (movie.title != null) {
            mRuntimeTV.text = String.format(Locale.getDefault(), "%d minutes", movie.runtime)
            mTaglineTV.text = movie.tagline
            movieTitleTV.text = movie.title
            movieYearTV.text = String.format(Locale.getDefault(), "(%s)", movie.releaseDate.substring(0, 4))
            movieOverview.text = movie.overview
            mGenresLayout.removeAllViews()

            if (movie.director != null) {
                mDirectorTv.text = String.format(Locale.getDefault(), "Director: %s", movie.director)
            }

            val _16dp = itemView.resources.getDimension(R.dimen.full_padding).toInt()
            val _8dp = itemView.resources.getDimension(R.dimen.half_padding).toInt()
            val corner = itemView.resources.getDimension(R.dimen.genre_corner)

            // Adds each genre to the genre layout
            for (g in movie.genres) {
                val tv = TextView(itemView.context)
                tv.text = g.name
                val shape = GradientDrawable()
                shape.shape = GradientDrawable.RECTANGLE
                shape.cornerRadius = corner
                shape.setColor(ContextCompat.getColor(itemView.context, R.color.primary_dark))
                tv.setPadding(_8dp, _8dp, _8dp, _8dp)
                tv.background = shape

                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT))
                params.setMargins(0, 0, _16dp, 0)
                tv.layoutParams = params

                mGenresLayout.addView(tv)
            }
        }

    }

}