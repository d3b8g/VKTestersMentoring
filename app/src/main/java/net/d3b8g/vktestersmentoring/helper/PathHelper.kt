package net.d3b8g.vktestersmentoring.helper

import android.os.Environment
import java.io.File

class PathHelper {
    companion object{
        var audioPath = "${Environment.getExternalStorageDirectory().absolutePath}${File.separator}VKTMentoring${File.separator}audio${File.separator}"
    }
}