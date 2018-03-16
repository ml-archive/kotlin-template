package dk.eboks.app.presentation.base

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import dk.eboks.app.BuildConfig
import dk.eboks.app.injection.components.DaggerPresentationComponent
import dk.eboks.app.injection.components.PresentationComponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.util.ShakeDetector
import dk.nodes.arch.presentation.base.BaseView
import timber.log.Timber

abstract class BaseFragment : Fragment(), BaseView {
    protected val component: PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
                .appComponent((activity.application as dk.eboks.app.App).appComponent)
                .presentationModule(PresentationModule())
                .build()
    }

    private val shakeDetector : ShakeDetector? = if(BuildConfig.DEBUG) ShakeDetector() else null
    private var sensorManager : SensorManager? = null
    private var acceleroMeter : Sensor? = null
    protected var showEmptyState : Boolean = false

    override fun onStart() {
        super.onStart()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(BuildConfig.DEBUG)
        {
            setupShakeDetection()
        }
    }

    fun setupShakeDetection()
    {
        sensorManager = if(BuildConfig.DEBUG) context.getSystemService(Context.SENSOR_SERVICE) as SensorManager else null
        acceleroMeter = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        shakeDetector?.setOnShakeListener(object : ShakeDetector.OnShakeListener {
            override fun onShake(count: Int) {
                showEmptyState = !showEmptyState
                Timber.e("showEmptyState $showEmptyState")
                this@BaseFragment.onShake()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(shakeDetector)
        Timber.e("OnResume")
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(shakeDetector, acceleroMeter, SensorManager.SENSOR_DELAY_UI)
        setupTranslations()
    }

    fun getBaseActivity() : BaseActivity?
    {
        if(activity is BaseActivity)
            return activity as BaseActivity
        return null
    }

    protected open fun onShake() {}
}