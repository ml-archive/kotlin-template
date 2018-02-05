package dk.eboks.app.presentation.base

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dk.eboks.app.BuildConfig
import dk.eboks.app.util.ShakeDetector
import dk.nodes.arch.presentation.base.BaseView
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity(), BaseView {
    private val shakeDetector : ShakeDetector? = if(BuildConfig.DEBUG) ShakeDetector() else null
    private var sensorManager : SensorManager? = null
    private var acceleroMeter : Sensor? = null
    protected var showEmptyState : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Make sure all of our dependencies get injected
        injectDependencies()
        if(BuildConfig.DEBUG)
        {
            setupShakeDetection()
        }
    }

    fun setupShakeDetection()
    {
        sensorManager = if(BuildConfig.DEBUG) getSystemService(Context.SENSOR_SERVICE) as SensorManager else null
        acceleroMeter = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        shakeDetector?.setOnShakeListener(object : ShakeDetector.OnShakeListener {
            override fun onShake(count: Int) {
                showEmptyState = !showEmptyState
                Timber.e("showEmptyState $showEmptyState")
                this@BaseActivity.onShake()
            }
        })
    }

    override fun onStart() {
        super.onStart()

        setupTranslations()
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(shakeDetector)
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(shakeDetector, acceleroMeter, SensorManager.SENSOR_DELAY_UI)

    }

    protected abstract fun injectDependencies()
    protected open fun onShake() {}
}