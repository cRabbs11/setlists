package com.example.evgeny.setlist_mobile.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.*
import java.nio.Buffer

class ExternalStorageHelper {

    fun saveInFile(filename: String, content: String, directoryName: String?, context: Context?) {
        if (getStorageStatus() == Environment.MEDIA_MOUNTED) {
            //Создаем файл в хранилище
            val file = File(context?.getExternalFilesDir(directoryName), filename)
            //Создаем буфер для записи, используем use, чтобы закрыть потом поток
            BufferedWriter(FileWriter(file)).use {
                //Пишем в файл
                it.write(content)
            }
        } else {
            Log.d("BMTH", "denied")
        }
    }

    fun readFromFile(filename: String, directoryName: String?, context: Context?): String {
        var result = ""
        if (getStorageStatus()==Environment.MEDIA_MOUNTED) {
            val file  = File(context?.getExternalFilesDir(directoryName), filename)
            BufferedReader(FileReader(file)).useLines { lines ->
                lines.forEach {
                    result +=it
                }
            }
        }
        return result
    }

    private fun getStorageStatus() : String {
        return Environment.getExternalStorageState()
    }
}