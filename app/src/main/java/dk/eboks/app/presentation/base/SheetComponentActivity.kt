package dk.eboks.app.presentation.base

import android.animation.ArgbEvaluator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import dk.eboks.app.R
import dk.eboks.app.presentation.ui.components.channels.requirements.ChannelRequirementsComponentFragment
import dk.eboks.app.presentation.ui.components.channels.settings.ChannelSettingsComponentFragment
import dk.eboks.app.presentation.ui.components.start.login.ActivationCodeComponentFragment
import dk.eboks.app.presentation.ui.components.start.login.ForgotPasswordComponentFragment
import dk.eboks.app.presentation.ui.components.verification.VerificationComponentFragment
import dk.eboks.app.util.MathUtil
import kotlinx.android.synthetic.main.activity_sheet_component.*
import timber.log.Timber

/**
 * Created by bison on 03-03-2018.
 */
class SheetComponentActivity : BaseActivity() {
    var sheetBehavior : BottomSheetBehavior<View>? = null
    var shouldClose = false
    private lateinit var fadeAnim : Animation
    private lateinit var bounceAnim : Animation
    private val handleOffsetMaxDP = 28
    private val handleOffsetMinDP = 0
    private var handleBounceDistance = 0f
    private var handleOffsetMax : Float = 0f
    private var handleOffsetMin : Float = 0f
    private var elevationMin : Float = 0f
    private var elevationMax : Float = 0f
    private val evaluator: ArgbEvaluator = ArgbEvaluator()
    private var handleStartColor: Int = 0
    private var handleEndColor: Int = 0
    private var firstExpand = true
    private var oldState = BottomSheetBehavior.STATE_COLLAPSED

    val callback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            //Timber.e("Slideoffset: $slideOffset")
            //touchVeilV.alpha = MathUtil.reMapFloat(-1.0f, 1.0f, .0f, 1.0f, slideOffset)
            touchVeilV.alpha = slideOffset
            if(slideOffset >= 0) {
                val params = contextSheetHandle.layoutParams as FrameLayout.LayoutParams
                params.topMargin = MathUtil.lerp(handleOffsetMax, handleOffsetMin, slideOffset).toInt()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    contextSheetHandle.elevation = MathUtil.lerp(elevationMin, elevationMax, slideOffset)
                }
                contextSheetHandle.layoutParams = params
                val background = contextSheetHandle.background
                setDrawableColor(background, (evaluator.evaluate(slideOffset, handleStartColor, handleEndColor) as Int))
                //shape.paint.color = evaluator.evaluate(slideOffset, 0)
            }

        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            //Timber.e("State changed to $newState")
            if(newState == BottomSheetBehavior.STATE_HIDDEN)
            {
                shouldClose = true
                finish()
                overridePendingTransition(0,0)
            }
            if(newState == BottomSheetBehavior.STATE_EXPANDED)
            {
                bounceAnim.repeatCount = Animation.INFINITE
                contextSheetHandle.startAnimation(bounceAnim)
            }
            if(newState == BottomSheetBehavior.STATE_DRAGGING)
            {
                if(oldState == BottomSheetBehavior.STATE_COLLAPSED)
                {
                    touchVeilV.visibility = View.VISIBLE
                }
                if(oldState == BottomSheetBehavior.STATE_EXPANDED) {
                    contextSheetHandle.animation?.let {
                        if (!it.hasEnded())
                            contextSheetHandle.animation.repeatCount = 0
                    }
                }
            }
            if(newState == BottomSheetBehavior.STATE_COLLAPSED)
            {
                //touchVeilV.visibility = View.GONE
                /*
                contextSheetSv.scrollTo(0,0)
                contextSheetHandle.animation?.let {
                    if(!it.hasEnded())
                        contextSheetHandle.animation.repeatCount = 0
                }
                */
                contextSheet.post {
                    sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                }

            }
            oldState = newState
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sheet_component)
        setContentSheet(R.layout.sheet_component)
        setupBottomSheet()
        touchVeilV.setOnClickListener {
            sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }
        handleStartColor = Color.parseColor("#CACACA")
        handleEndColor = Color.WHITE

        fadeAnim = AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_out)
        fadeAnim.duration = 150
        handleOffsetMax = resources.displayMetrics.density * handleOffsetMaxDP.toFloat()
        handleOffsetMin = resources.displayMetrics.density * handleOffsetMinDP.toFloat()
        handleBounceDistance = resources.displayMetrics.density * 6.0f
        elevationMin = 0f
        elevationMax = resources.displayMetrics.density * 2.0f


        bounceAnim = TranslateAnimation(0f, 0f, 0f, handleBounceDistance)
        bounceAnim.interpolator = AccelerateDecelerateInterpolator()
        bounceAnim.duration = 1500
        bounceAnim.repeatCount = Animation.INFINITE
        bounceAnim.repeatMode = Animation.REVERSE
        bounceAnim.fillBefore = true
        bounceAnim.fillAfter = false

        val compname = intent.getStringExtra("component")
        try {
            val clazz = Class.forName(compname)
            val frag = clazz.newInstance() as BaseFragment
            intent.getBundleExtra("arguments")?.let{
                frag.arguments = it
            }
            addFragment(frag)
        }
        catch (t : Throwable)
        {
            Timber.e("Fragment $compname could not be instantiated")
        }
    }

    fun addFragment(fragment : BaseFragment?)
    {
        fragment?.let{
            supportFragmentManager.beginTransaction().add(R.id.sheetComponentFl, it, fragment.javaClass.simpleName).addToBackStack(fragment.javaClass.simpleName).commit()
        }
    }

    fun replaceFragment(fragment : BaseFragment?)
    {
        fragment?.let{
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_bottom,R.anim.slide_out_top)
                    .replace(R.id.sheetComponentFl, it, fragment.javaClass.simpleName)
                    .addToBackStack(fragment.javaClass.simpleName)
                    .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        firstExpand = true
    }

    fun setupBottomSheet()
    {
        sheetBehavior = BottomSheetBehavior.from(contextSheet)
        sheetBehavior?.isHideable = true
        sheetBehavior?.peekHeight = 0
        //sheetBehavior?.peekHeight = (resources.displayMetrics.density * 104.0).toInt()
        //sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED

        sheetBehavior?.setBottomSheetCallback(callback)
        /*
        contextSheet.post {
            callback.onSlide(contextSheet, 0f)
        }
        */
        contextSheet.post {
            sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }

    }

    fun setDrawableColor(background: Drawable, color : Int)
    {
        if (background is ShapeDrawable) {
            // cast to 'ShapeDrawable'
            background.paint.color = color
        } else if (background is GradientDrawable) {
            // cast to 'GradientDrawable'
            background.setColor(color)
        } else if (background is ColorDrawable) {
            // alpha value may need to be set again after this call
            background.color = color
        }
    }

    protected fun setContentSheet(resId : Int)
    {
        val li: LayoutInflater = LayoutInflater.from(this)
        val sheet = li.inflate(resId, contextSheetSv, false)
        contextSheetSv.addView(sheet)
    }

    override fun onBackPressed() {
        contextSheet.post {
            sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }
}