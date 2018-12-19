package dk.eboks.app.presentation.base

import android.animation.ArgbEvaluator
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dk.eboks.app.R
import dk.eboks.app.util.MathUtil
import kotlinx.android.synthetic.main.activity_base_sheet.*
import timber.log.Timber

/**
 * Created by bison on 05/02/18.
 */
abstract class BaseSheetActivity : BaseActivity() {
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
            touchVeilV.alpha = MathUtil.reMapFloat(0f, 1.0f, .0f, 1.0f, slideOffset)
            //touchVeilV.alpha = slideOffset
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
                hideKeyboard(bottomSheet)
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
                touchVeilV.visibility = View.GONE
                contextSheetSv.scrollTo(0,0)
                contextSheetHandle.animation?.let {
                    if(!it.hasEnded())
                        contextSheetHandle.animation.repeatCount = 0
                }
            }
            oldState = newState
        }

    }

    private fun hideKeyboard(bottomSheet: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(bottomSheet.windowToken, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_sheet)
        setupBottomSheet()
        touchVeilV.setOnClickListener {
            sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
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
    }

    override fun onResume() {
        super.onResume()
        firstExpand = true
    }

    fun setupBottomSheet()
    {
        sheetBehavior = BottomSheetBehavior.from(contextSheet)
        sheetBehavior?.isHideable = false
        sheetBehavior?.peekHeight = (resources.displayMetrics.density * 104.0).toInt()
        if(shouldStartExpanded())
            sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        else
            sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        /*
        contextSheet.post {
            sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        */
        sheetBehavior?.setBottomSheetCallback(callback)
        contextSheet.post {
            callback.onSlide(contextSheet, 0f)
        }
    }

    fun setupPeakHeight(height: Int){
        sheetBehavior?.peekHeight = (resources.displayMetrics.density * height).toInt()
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

    fun expand()
    {
        sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun collapse()
    {
        sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    protected fun setContentSheet(resId : Int)
    {
        val li: LayoutInflater = LayoutInflater.from(this)
        val sheet = li.inflate(resId, contextSheetSv, false)
        contextSheetSv.addView(sheet)
    }

    override fun onBackPressed() {
        Timber.e("onBackPressed")
        super.onBackPressed()
    }

    open fun shouldStartExpanded() : Boolean
    {
        return false
    }
}