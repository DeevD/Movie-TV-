package com.gabilheri.moviestmdb.ui.details.person

import android.content.Context
import android.support.v17.leanback.widget.ImageCardView
import android.support.v17.leanback.widget.Presenter
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import com.gabilheri.moviestmdb.R
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gabilheri.moviestmdb.dagger.modules.HttpClientModule
import com.bumptech.glide.Glide
import com.gabilheri.moviestmdb.data.models.CastMember




/**
 * Created by Hein Htet on 2019-06-30.
 */
class PersonPresenter : Presenter() {

    var mContext: Context? = null


    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        if (mContext == null) {
            mContext = ContextThemeWrapper(parent.context, R.style.PersonCardTheme)

        }
        return ViewHolder(ImageCardView(mContext))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, item: Any?) {
        val view = viewHolder?.view as ImageCardView
        val member = item as CastMember

        Glide.with(view.context)
                .load(HttpClientModule.POSTER_URL + member.profilePath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view.mainImageView)

        view.titleText = member.name
        view.contentText = member.character
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {

    }

}