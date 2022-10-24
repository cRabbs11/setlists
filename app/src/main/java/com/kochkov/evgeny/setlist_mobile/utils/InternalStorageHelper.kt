package com.kochkov.evgeny.setlist_mobile.utils

import android.content.Context

class InternalStorageHelper {

    fun saveInFile(filename: String, content: String, context: Context?) {
        context?.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it?.write(content.toByteArray())
        }
    }

    fun readFromFile(filename: String, context: Context?): String {
        var result = ""
        context?.openFileInput(filename)?.bufferedReader()?.useLines { lines ->
            lines.forEach {
                result += it
            }
        }
        return result
    }
}