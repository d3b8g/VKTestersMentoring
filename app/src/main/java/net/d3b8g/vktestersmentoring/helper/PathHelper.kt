package net.d3b8g.vktestersmentoring.helper

/*
Copyright (c) 2021 github.com/d3b8g
All Rights Reserved

This product is protected by copyright and distributed under
licenses restricting copying, distribution and decompilation.

Use this code only for non commercial purpose.
*/

import android.os.Environment
import java.io.File

object PathHelper {

    fun String.getAudioPath(): String = File(audioPath, this).absolutePath

    val audioPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath
}