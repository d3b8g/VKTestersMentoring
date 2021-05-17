package net.d3b8g.vktestersmentoring.helper

import android.os.Environment

class PathHelper {
    companion object {
        val audioPath = "${Environment.getExternalStorageDirectory().absolutePath}/VKTMentoring/audio/"
    }
}