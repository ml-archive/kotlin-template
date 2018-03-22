package dk.eboks.app.presentation.base

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.view.LayoutInflater
import dk.eboks.app.BuildConfig
import dk.eboks.app.injection.components.DaggerPresentationComponent
import dk.eboks.app.injection.components.PresentationComponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.util.ShakeDetector
import dk.eboks.app.presentation.base.BaseView
import kotlinx.android.synthetic.*
import timber.log.Timber

abstract class BaseFragment : Fragment(), BaseView {
    protected val component: PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
                .appComponent((activity.application as dk.eboks.app.App).appComponent)
                .presentationModule(PresentationModule())
                .build()
    }
    val inflator by lazy {
        LayoutInflater.from(context)
    }

    private val shakeDetector : ShakeDetector? = if(BuildConfig.DEBUG) ShakeDetector() else null
    private var sensorManager : SensorManager? = null
    private var acceleroMeter : Sensor? = null
    protected var showEmptyState : Boolean = false

    override fun onStart() {
        super.onStart()
        if(BuildConfig.DEBUG) Timber.v("${this.javaClass.simpleName} onStart")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(BuildConfig.DEBUG)
        {
            setupShakeDetection()
        }
        if(BuildConfig.DEBUG) Timber.v("${this.javaClass.simpleName} onCreate")
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(BuildConfig.DEBUG) Timber.v("${this.javaClass.simpleName} onViewCreated")
        clearFindViewByIdCache()
    }

    fun setupShakeDetection()
    {
        sensorManager = if(BuildConfig.DEBUG) context.getSystemService(Context.SENSOR_SERVICE) as SensorManager else null
        acceleroMeter = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        shakeDetector?.setOnShakeListener(object : ShakeDetector.OnShakeListener {
            override fun onShake(count: Int) {
                showEmptyState = !showEmptyState
                if(BuildConfig.DEBUG) Timber.v("showEmptyState $showEmptyState")
                this@BaseFragment.onShake()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(shakeDetector)
        if(BuildConfig.DEBUG) Timber.v("${this.javaClass.simpleName} onPause")
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(shakeDetector, acceleroMeter, SensorManager.SENSOR_DELAY_UI)
        if(BuildConfig.DEBUG) Timber.v("${this.javaClass.simpleName} onResume")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(BuildConfig.DEBUG) Timber.v("${this.javaClass.simpleName} onViewStateRestored")
    }

    /*
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Timber.e("${this.javaClass.simpleName} onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.e("${this.javaClass.simpleName} onDetach")
    }
    */

    fun getBaseActivity() : BaseActivity?
    {
        if(activity is BaseActivity)
            return activity as BaseActivity
        return null
    }

    protected open fun onShake() {}
}