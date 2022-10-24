package com.kochkov.evgeny.setlist_mobile.utils

import android.content.Context
import java.io.File

class SearchHistoryHelper(val context: Context) {

    private val searchHistoryFilename = "search_history"

    private fun appendInFile(filename: String, text: String) {
        val file = File(context.filesDir, filename)
        file.appendText(text)
        file.appendText("\n")
    }

    private fun readLinesFromFile(filename: String): List<String> {
        val file = File(context.filesDir, filename)
        if (!file.exists()) {
            file.createNewFile()
        }
        return  file.readLines()
    }

    fun saveSearchQuery(text: String) {
        appendInFile(searchHistoryFilename, text)
    }

    fun getHistorySearchList(): List<String> {
        return readLinesFromFile(searchHistoryFilename)
    }
}