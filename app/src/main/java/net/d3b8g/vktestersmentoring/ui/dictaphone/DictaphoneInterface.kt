package net.d3b8g.vktestersmentoring.ui.dictaphone

interface DictaphoneInterface {
    suspend fun recordingMicrophone(): Unit?
}