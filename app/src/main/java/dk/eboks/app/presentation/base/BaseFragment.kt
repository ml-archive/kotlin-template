package dk.eboks.app.presentation.base

import android.app.FragmentManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import dk.eboks.app.App
import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.injection.components.PresentationComponent
import dk.eboks.app.injection.modules.PresentationModule
import kotlinx.android.synthetic.*
import timber.log.Timber

abstract class BaseFragment : Fragment(), BaseView {
    protected val component: PresentationComponent by lazy {
        App.instance().appComponent.plus(PresentationModule())
    }

    /**
     * easy shortcut to get an inflater, this only gets instantiated if you use it and only the first time
     */
    val inflator by lazy {
        LayoutInflater.from(context)
    }

    val mainHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    /**
     * Make another default error handler and override showErrorDialog if you wanna do something really custom
     *
     * CloseFunction dictates whatever happens to a view of this type if ViewError::shouldClose
     * is true
     */
    open val defaultErrorHandler: ViewErrorController by lazy {
        //ViewErrorController(context = context, closeFunction = {fragmentManager.popBackStack()} )
        ViewErrorController(context = context, closeFunction = {activity.onBackPressed()} )
    }

    /*
    private val shakeDetector : ShakeDetector? = if(BuildConfig.DEBUG) ShakeDetector() else null
    private var sensorManager : SensorManager? = null
    private var acceleroMeter : Sensor? = null
    protected var showEmptyState : Boolean = false
    */

    override fun onStart() {
        super.onStart()
        //if(BuildConfig.DEBUG) Timber.v("${this.javaClass.simpleName} onStart")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(BuildConfig.DEBUG)
        {
            //setupShakeDetection()
        }
        //if(BuildConfig.DEBUG) Timber.v("${this.javaClass.simpleName} onCreate")
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(BuildConfig.DEBUG) Timber.v("${this.javaClass.simpleName} onViewCreated")
        clearFindViewByIdCache()
    }

    /*
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
    */

    override fun onPause() {
        super.onPause()
        //sensorManager?.unregisterListener(shakeDetector)
        //if(BuildConfig.DEBUG) Timber.v("${this.javaClass.simpleName} onPause")
    }

    override fun onResume() {
        super.onResume()
        //sensorManager?.registerListener(shakeDetector, acceleroMeter, SensorManager.SENSOR_DELAY_UI)
        //if(BuildConfig.DEBUG) Timber.v("${this.javaClass.simpleName} onResume")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        //if(BuildConfig.DEBUG) Timber.v("${this.javaClass.simpleName} onViewStateRestored")
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

    override fun showErrorDialog(error : ViewError) {
        defaultErrorHandler.showErrorDialog(error)
    }

    override fun showToast(msg: String, showLongTime: Boolean) {
        val dur = if(showLongTime) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        Toast.makeText(context, msg, dur).show()
    }

    fun setRootFragment(resId: Int, fragment: BaseFragment?) {
        fragment?.let {
            activity.supportFragmentManager.popBackStack(
                    BaseActivity.backStackRootTag,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            activity.supportFragmentManager.beginTransaction()
                    .replace(resId, fragment)
                    .addToBackStack(BaseActivity.backStackRootTag)
                    .commit()
        }
    }

    fun addFragmentOnTop(resId: Int, fragment: BaseFragment?, addToBack: Boolean = true) {
        fragment?.let {
            val trans = activity.supportFragmentManager.beginTransaction().replace(resId, it)
            if (addToBack)
                trans.addToBackStack(null)
            trans.commit()
        }
    }

    fun finishActivity(resultCode : Int? = null)
    {
        resultCode?.let { code->
            activity.setResult(code)
        }
        activity.finish()
    }
}