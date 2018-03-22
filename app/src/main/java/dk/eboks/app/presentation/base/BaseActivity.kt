package dk.eboks.app.presentation.base

import android.app.FragmentManager
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.injection.components.DaggerPresentationComponent
import dk.eboks.app.injection.components.PresentationComponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.presentation.ui.screens.debug.DebugActivity
import dk.eboks.app.util.ShakeDetector
import dk.nodes.nstack.kotlin.inflater.NStackBaseContext
import kotlinx.android.synthetic.main.include_toolbar.*
import net.hockeyapp.android.CrashManager
import net.hockeyapp.android.CrashManagerListener
import net.hockeyapp.android.UpdateManager
import timber.log.Timber


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
    var backPressedCallback: (()->Boolean)? = null

    open val defaultErrorHandler: ViewErrorController by lazy {
        ViewErrorController(context = this, closeFunction = { finish()} )
    }


    companion object {
        val backStackRootTag = "root_fragment"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(BuildConfig.DEBUG)
        {
            countToDebug = 0
            setupShakeDetection()
        }

        CrashManager.register(this, object : CrashManagerListener() {
            override fun onNoCrashesFound() {
                super.onNoCrashesFound()
                Timber.d("No crashes found")
            }

            override fun onNewCrashesFound() {
                super.onNewCrashesFound()
                Timber.d("New crashes found")
            }

            override fun shouldAutoUploadCrashes(): Boolean {
                return true
            }
        })
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

    override fun onPause() {
        sensorManager?.unregisterListener(shakeDetector)
        if(BuildConfig.DEBUG)
            UpdateManager.unregister()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(shakeDetector, acceleroMeter, SensorManager.SENSOR_DELAY_UI)
        if(BuildConfig.DEBUG) {
            UpdateManager.register(this)
        }
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
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            backPressedCallback?.let {
                Timber.e("Back pressed handling and blocking onBackPressed")
                if(it())
                    return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    fun getToolbar() : Toolbar {
        return mainTb
    }

    fun setRootFragment(resId : Int, fragment : BaseFragment?)
    {
        fragment?.let {
            supportFragmentManager.popBackStack(backStackRootTag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.beginTransaction()
                    .replace(resId, fragment)
                    .addToBackStack(backStackRootTag)
                    .commit()
        }
    }

    fun addFragmentOnTop(resId : Int, fragment : BaseFragment?, addToBack : Boolean = true)
    {
        fragment?.let{
            val trans = supportFragmentManager.beginTransaction().replace(resId, it)
            if(addToBack)
                trans.addToBackStack(null)
            trans.commit()
        }
    }

    fun openComponentDrawer(cls : Class<out BaseFragment>)
    {
        val intent = Intent(this, SheetComponentActivity::class.java)
        intent.putExtra("component", cls.name)
        startActivity(intent)
        overridePendingTransition(0,0)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(NStackBaseContext(newBase))
    }

    protected open fun onShake() {}

    /**
     * override this to indicate what primary navigation you belong to (only in screens with visible bottom navigation)
     */
    open fun getNavigationMenuAction() : Int { return -1 }

    /**
     * Shows the generic error if given null message and title
     */
    override fun showErrorDialog(error : ViewError) {
        defaultErrorHandler.showErrorDialog(error)
    }
}