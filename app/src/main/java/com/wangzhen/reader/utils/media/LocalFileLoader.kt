package com.wangzhen.reader.utils.media

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import androidx.loader.content.CursorLoader
import com.wangzhen.reader.utils.media.MediaStoreHelper.MediaResultCallback
import java.io.File

/**
 * LocalFileLoader
 * Created by wangzhen on 2023/4/13
 */
class LocalFileLoader(context: Context) : CursorLoader(context) {
    init {
        initLoader()
    }

    private fun initLoader() {
        uri = FILE_URI
        projection = FILE_PROJECTION
        selection = SELECTION
        selectionArgs = arrayOf(SEARCH_TYPE)
        sortOrder = SORT_ORDER
    }

    fun parseData(cursor: Cursor?, callback: MediaResultCallback?) {
        val files: ArrayList<File> = ArrayList()
        if (cursor == null) {
            callback?.onResultCallback(files)
            return
        }
        cursor.moveToPosition(-1)
        while (cursor.moveToNext()) {
            val path =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
            if (!TextUtils.isEmpty(path)) {
                val file = File(path)
                if (!file.isDirectory && file.exists()) {
                    files.add(file)
                }
            }
        }
        callback?.onResultCallback(files)
    }

    companion object {
        private val FILE_URI = Uri.parse("content://media/external/file")
        private const val SELECTION = MediaStore.Files.FileColumns.DATA + " like ?"
        private const val SEARCH_TYPE = "%.txt"
        private const val SORT_ORDER = MediaStore.Files.FileColumns.DISPLAY_NAME + " DESC"
        private val FILE_PROJECTION =
            arrayOf(MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.DISPLAY_NAME)
    }
}