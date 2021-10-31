package net.d3b8g.vktestersmentoring.ui.gallery

import android.content.Context
import android.util.Log
import java.io.File

object Gallery {

    fun Context.audioPath(addPath: String): String {
        val path = externalCacheDir?.absolutePath + File.separator + "VKTMaudio_" + addPath
        Log.e("Dictaphone", path)
        return path
    }

    fun Context.getAppPath() = externalCacheDir?.absolutePath

    fun Context.getGallerySize(): Int = externalCacheDir?.listFiles()?.size ?: 0

    fun Context.getAudioModelList(): ArrayList<AudioAdapterModel> {
        val modelList: ArrayList<AudioAdapterModel> = ArrayList()
        externalCacheDir?.listFiles()?.forEachIndexed { index, file ->
            modelList.add(AudioAdapterModel(file.path, "${index+1}"))
        }
        return modelList
    }

}