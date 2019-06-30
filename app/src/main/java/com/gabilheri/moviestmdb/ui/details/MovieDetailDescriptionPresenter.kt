package com.gabilheri.moviestmdb.ui.details

import android.support.v17.leanback.widget.Presenter
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gabilheri.moviestmdb.R
import com.gabilheri.moviestmdb.data.models.MovieDetails

/**
 * Created by Hein Htet on 2019-06-30.
 */
class MovieDetailDescriptionPresenter : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        return MovieDetailViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.vh_detail, parent, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, item: Any?) {
        if (item is MovieDetails) {
            (viewHolder as MovieDetailViewHolder).bind(item)
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {
    }

}