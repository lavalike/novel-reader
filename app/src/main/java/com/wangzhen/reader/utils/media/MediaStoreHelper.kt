package com.wangzhen.reader.utils.media

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.wangzhen.reader.utils.media.LoaderCreator.create
import java.io.File
import java.lang.ref.WeakReference

/**
 * MediaStoreHelper
 * Created by wangzhen on 2023/4/13
 */
object MediaStoreHelper {
    /**
     * scan all txt files from media
     */
    @JvmStatic
    fun getAllBookFile(activity: FragmentActivity, resultCallback: MediaResultCallback?) {
        LoaderManager.getInstance(activity).initLoader(
            LoaderCreator.ALL_BOOK_FILE, null, MediaLoaderCallbacks(activity, resultCallback)
        )
    }

    interface MediaResultCallback {
        fun onResultCallback(files: ArrayList<File>)
    }

    internal class MediaLoaderCallbacks(
        val context: Context, private val callback: MediaResultCallback?
    ) : LoaderManager.LoaderCallbacks<Cursor> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            return create(context)
        }

        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
            (loader as LocalFileLoader).parseData(data, callback)
        }

        override fun onLoaderReset(loader: Loader<Cursor>) {}
    }
}