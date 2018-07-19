package dk.eboks.app.presentation.base

import android.app.FragmentManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.Toast
import dk.eboks.app.App
import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.injection.components.PresentationComponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.presentation.ui.debug.screens.DebugActivity
import dk.eboks.app.util.BroadcastReceiver
import dk.nodes.nstack.kotlin.inflater.NStackBaseContext
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber


abstract class BaseActivity : AppCompatActivity(), BaseView {
    protected val component: PresentationComponent by lazy {
        App.instance().appComponent.plus(PresentationModule())
    }
    /*
    private val shakeDetector: ShakeDetector? = if (BuildConfig.DEBUG) ShakeDetector() else null
    private var sensorManager: SensorManager? = null
    private var acceleroMeter: Sensor? = null
    */
    //protected var showEmptyState: Boolean = false
    protected var countToDebug = 0
    var backPressedCallback: (() -> Boolean)? = null

    open val defaultErrorHandler: ViewErrorController by lazy {
        ViewErrorController(context = this, closeFunction = { finish() })
    }


    companion object {
        val backStackRootTag = "root_fragment"
    }

    private val broadcastReceiver : BroadcastReceiver = BroadcastReceiver()

    /**
     * easy shortcut to get an inflater, this only gets instantiated if you use it and only the first time
     */
    val inflator by lazy {
        LayoutInflater.from(this)
    }

    val mainHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            countToDebug = 0
            setupShakeDetection()
        }
        broadcastReceiver.addFilter("session_expired")
        broadcastReceiver.setListener(object : BroadcastReceiver.OnIntentListener {
            override fun onIntent(intent: Intent) {
                handleSessionExpired()
            }
        })

        broadcastReceiver.register(this)
    }

    override fun onDestroy() {
        broadcastReceiver.deregister(this)
        super.onDestroy()
    }

    fun setupShakeDetection() {
        /*
        sensorManager = if (BuildConfig.DEBUG) getSystemService(Context.SENSOR_SERVICE) as SensorManager else null
        acceleroMeter = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        shakeDetector?.setOnShakeListener(object : ShakeDetector.OnShakeListener {
            override fun onShake(count: Int) {
                showEmptyState = !showEmptyState
                Timber.d("showEmptyState $showEmptyState")

                this@BaseActivity.onShake()
            }
        })
        */
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            countToDebug++
            if (countToDebug > 2) {
                startActivity(Intent(this, DebugActivity::class.java))
                countToDebug = 0
            }
            return true
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backPressedCallback?.let {
                Timber.e("Back pressed handling and blocking onBackPressed")
                if (it())
                    return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    fun getToolbar(): Toolbar {
        return mainTb
    }

    fun setRootFragment(resId: Int, fragment: Fragment?) {
        fragment?.let {
            supportFragmentManager.popBackStack(
                    backStackRootTag,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            supportFragmentManager.beginTransaction()
                    .replace(resId, fragment)
                    .addToBackStack(backStackRootTag)
                    .commit()
        }
    }

    fun addFragmentOnTop(resId: Int, fragment: Fragment?, addToBack: Boolean = true) {
        fragment?.let {
            val trans = supportFragmentManager.beginTransaction().replace(resId, it)
            if (addToBack)
                trans.addToBackStack(null)
            trans.commit()
        }
    }

    fun openComponentDrawer(cls: Class<out BaseFragment>, arguments: Bundle? = null) {
        val intent = Intent(this, SheetComponentActivity::class.java)
        intent.putExtra("component", cls.name)
        arguments?.let {
            intent.putExtra("arguments", it)
        }
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(NStackBaseContext(newBase))
    }

    protected open fun onShake() {}

    /**
     * override this to indicate what primary navigation you belong to (only in screens with visible bottom navigation)
     */
    open fun getNavigationMenuAction(): Int {
        return -1
    }

    /**
     * Override this if your activity shouldn't finish itself when receiving the session expired event
     */
    open fun handleSessionExpired()
    {
        finish()
    }

    /**
     * Shows the generic error if given null message and title
     */
    override fun showErrorDialog(error: ViewError) {
        defaultErrorHandler.showErrorDialog(error)
    }

    override fun showToast(msg: String, showLongTime: Boolean) {
        val dur = if (showLongTime) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        Toast.makeText(this, msg, dur).show()
    }
}