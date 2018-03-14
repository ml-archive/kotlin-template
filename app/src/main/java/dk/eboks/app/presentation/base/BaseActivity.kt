package dk.eboks.app.presentation.base

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import dk.eboks.app.BuildConfig
import dk.eboks.app.injection.components.DaggerPresentationComponent
import dk.eboks.app.injection.components.PresentationComponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.util.ShakeDetector
import dk.nodes.arch.presentation.base.BaseView
import timber.log.Timber
import android.view.KeyEvent.KEYCODE_VOLUME_DOWN
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.presentation.ui.screens.debug.DebugActivity
import dk.eboks.app.util.guard
import kotlinx.android.synthetic.main.include_toolnar.*


abstract class BaseActivity : AppCompatActivity(), BaseView {
    protected val component: PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
                .appComponent((application as dk.eboks.app.App).appComponent)
                .presentationModule(PresentationModule())
                .build()
    }

    private val shakeDetector : ShakeDetector? = if(BuildConfig.DEBUG) ShakeDetector() else null
    private var sensorManager : SensorManager? = null
    private var acceleroMeter : Sensor? = null
    protected var showEmptyState : Boolean = false
    protected var countToDebug = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(BuildConfig.DEBUG)
        {
            countToDebug = 0
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
                Timber.d("showEmptyState $showEmptyState")

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

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            countToDebug++
            if(countToDebug > 2)
            {
                startActivity(Intent(this, DebugActivity::class.java))
                countToDebug = 0
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun setToolbar(iconResId : Int, title : String? = null, subtitle : String? = null, callback : (()->Unit)? = null, hideIcon : Boolean=false, showImgIcon : Boolean=false, redOptionsText : String? = null, userShareTvAllignedLeft : Boolean=false)
    {
        findViewById<ImageView>(R.id.toolbarIb)?.let {
            it.setImageResource(iconResId)
            it.setOnClickListener { callback?.invoke() }
        }
        findViewById<TextView>(R.id.toolbarTv)?.let {
            if(title != null) {
                it.visibility = View.VISIBLE
                it.text = title
            }
            else
                it.visibility = View.GONE
        }
        findViewById<TextView>(R.id.toolbarSubTv)?.let {
            if(subtitle != null) {
                it.visibility = View.VISIBLE
                it.text = subtitle
            }
            else
                it.visibility = View.GONE
        }

        if(hideIcon){
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 0, 0, 0)
            toolbarTv.layoutParams = params
            toolbarIb.visibility = View.GONE
        }

        if (showImgIcon) {imgOptionIV.visibility = View.VISIBLE}

        if(redOptionsText != null) {
            redOptionTv.visibility = View.VISIBLE
            redOptionTv.text = "REGISTRATIONS"
        }

        if(userShareTvAllignedLeft){ userShareTv.setPadding(0,0,0,0) }
    }


    fun replaceFragment(resId : Int, fragment : BaseFragment?, addToBack : Boolean = true)
    {
        fragment?.let{
            val trans = supportFragmentManager.beginTransaction().replace(resId, it, fragment.javaClass.simpleName)
            if(addToBack)
                trans.addToBackStack(fragment.javaClass.simpleName)
            trans.commit()
        }
    }

    fun addFragment(resId : Int, fragment : BaseFragment?, addToBack : Boolean = true)
    {
        fragment?.let{
            val trans = supportFragmentManager.beginTransaction().add(resId, it, fragment.javaClass.simpleName)
            if(addToBack)
                trans.addToBackStack(fragment.javaClass.simpleName)
            trans.commit()
        }
    }

    protected open fun onShake() {}
}