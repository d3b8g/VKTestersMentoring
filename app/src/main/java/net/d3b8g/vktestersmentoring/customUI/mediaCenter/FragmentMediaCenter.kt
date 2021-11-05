package net.d3b8g.vktestersmentoring.customUI.mediaCenter

import android.content.Context
import android.media.MediaRecorder
import android.util.AttributeSet
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.ui.dictaphone.Dictaphone

class FragmentMediaCenter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : RelativeLayout(context, attrs, defStyle) {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val textStatus: TextView
    private val circleBackground: RelativeLayout
    private val circleFilled: ImageView
    private val actionButton: ImageButton

    init {
        inflate(context, R.layout.fragment_mcenter, this)
        gravity = CENTER_IN_PARENT

        textStatus = findViewById(R.id.statusRecording)
        circleBackground = findViewById(R.id.circleBackground)
        circleFilled = findViewById(R.id.circleFilled)
        actionButton = findViewById(R.id.actionButtonPLayer)

        actionButton.setOnClickListener {
            recording_anim = !recording_anim
            if (recording_anim) {
                scope.launch { (context as Dictaphone).recordingMicrophone() }
                startReproduceAudio()
            } else {
                stopReproduceAudio()
                Dictaphone.microphoneState = false
            }
        }
    }

    fun startReproduceAudio() {
        actionButton.background = ContextCompat.getDrawable(context, R.drawable.ic_stop)
        textStatus.text = "Записываем аудио"
        scope.launch {
            audioReproduceAnimation()
        }
    }

    private fun stopReproduceAudio() {
        mDictaphone?.stop()
        mDictaphone?.release()
        if (isDictaphone) actionButton.background =
            ContextCompat.getDrawable(context, R.drawable.ic_mic)
        else actionButton.background =
            ContextCompat.getDrawable(context, R.drawable.ic_start)
        textStatus.text = "Запись остановлена"
    }

    private suspend fun audioReproduceAnimation() = withContext(Dispatchers.Main) {
        circleFilled.visibility = VISIBLE
        while (recording_anim) {
            circleFilled.animate().alpha(1.0f).duration = 1500L
            delay(1000L)
            circleFilled.animate().alpha(0.0f).duration = 1500L
            delay(1900L)
        }
        if (!recording_anim) {
            circleBackground.visibility = GONE
            stopReproduceAudio()
        }
    }

    companion object {
        var recording_anim = Dictaphone.microphoneState
        var mDictaphone: MediaRecorder? = null
        var isDictaphone: Boolean = true
    }
}