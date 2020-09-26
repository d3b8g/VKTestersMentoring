package net.d3b8g.vktestersmentoring.ui.splashscreen

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.google.android.material.textfield.TextInputEditText
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.prefs.pMineName
import kotlin.math.roundToInt

class SplashScreen:AppCompatActivity(), SensorEventListener{

    lateinit var input:TextInputEditText
    lateinit var rotatedLinear:LinearLayout
    var mGravity = floatArrayOf(0f,0f,0f)
    var mGeomagnetic = floatArrayOf(0f,0f,0f)
    var azimuth = 0f
    var correctAzimuth = 0f
    lateinit var mSensorManager:SensorManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_splash)

        val btn = findViewById<Button>(R.id.register_start)
        input = findViewById(R.id.register_input)
        rotatedLinear = findViewById(R.id.rotated_layout)

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        btn.setOnClickListener {
            input?.let {it->
                if(!it.text.isNullOrEmpty() && it.text!!.length>3 && it.text!!.contains(' ')){
                    pMineName(this,true,it.text!!.toString())
                    PreferenceManager.getDefaultSharedPreferences(this).edit {
                        putBoolean("make_splash",true).apply()
                    }
                    finish()
                }else{
                    Toast.makeText(this,"Поле должно содержать Имя и Фамилию.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_GAME)
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME)

    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        var alpha = 0.97f
        synchronized(this){
            if(event.sensor.type == Sensor.TYPE_ACCELEROMETER){
                mGravity[0]=(alpha*mGravity[0]+(1-alpha)*event.values[0])
                mGravity[1]=(alpha*mGravity[1]+(1-alpha)*event.values[1])
                mGravity[2]=(alpha*mGravity[2]+(1-alpha)*event.values[2])
            }
            if(event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD){
                mGeomagnetic[0]=(alpha*mGeomagnetic[0]+(1-alpha)*event.values[0])
                mGeomagnetic[1]=(alpha*mGeomagnetic[1]+(1-alpha)*event.values[1])
                mGeomagnetic[2]=(alpha*mGeomagnetic[2]+(1-alpha)*event.values[2])
            }

            var R = floatArrayOf(0f,0f,0f,0f,0f,0f,0f,0f,0f)
            var I = floatArrayOf(0f,0f,0f,0f,0f,0f,0f,0f,0f)
            var success = SensorManager.getRotationMatrix(R,I,mGravity,mGeomagnetic)
            if(success){
                var orientation = floatArrayOf(0f,0f,0f)
                SensorManager.getOrientation(R,orientation)
                azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                azimuth = (azimuth+360)%360

                var anim = RotateAnimation(-correctAzimuth,-azimuth,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
                anim.duration = 500
                anim.repeatCount = 0
                anim.fillAfter = true

                rotatedLinear.startAnimation(anim)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}