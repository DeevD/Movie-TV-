package com.gabilheri.moviestmdb.ui.base

import android.os.Bundle
import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.gabilheri.moviestmdb.R


/**
 * Created by Hein Htet on 2019-06-30.
 */
abstract class BaseTvActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    fun addFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.tv_frame_content, fragment)
        fragmentTransaction.commit()
    }

}