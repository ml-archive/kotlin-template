package dk.eboks.app.presentation.ui.dialogs

import android.animation.ArgbEvaluator
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.transition.Fade
import android.view.View
import dk.eboks.app.R
import kotlinx.android.synthetic.main.activity_context_sheet.*
import timber.log.Timber
import android.widget.FrameLayout
import dk.eboks.app.util.MathUtil
import android.support.v4.content.ContextCompat
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.view.animation.*


/**
 * Created by bison on 05/02/18.
 */
class ContextSheetActivity : AppCompatActivity() {
    var sheetBehavior : BottomSheetBehavior<View>? = null
    var shouldClose = false
    lateinit var fadeAnim : Animation
    lateinit var bounceAnim : Animation
    val handleOffsetMaxDP = 24
    var handleBounceDistance = 0f
    var handleOffsetMax : Float = 0f
    val evaluator: ArgbEvaluator = ArgbEvaluator()
    var handleStartColor: Int = 0
    var handleEndColor: Int = 0
    var firstExpand = true

    val callback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            //Timber.e("Slideoffset: $slideOffset")
            touchVeilV.alpha = MathUtil.reMapFloat(-1.0f, 1.0f, 0f, 1.0f, slideOffset)
            if(slideOffset >= 0) {
                val params = contextSheetHandle.layoutParams as FrameLayout.LayoutParams
                params.topMargin = MathUtil.lerp(handleOffsetMax, 0f, slideOffset).toInt()
                contextSheetHandle.layoutParams = params
                val background = contextSheetHandle.background
                setDrawableColor(background, (evaluator.evaluate(slideOffset, handleStartColor, handleEndColor) as Int))
                //shape.paint.color = evaluator.evaluate(slideOffset, 0)
            }

        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            Timber.e("State changed to $newState")
            if(newState == BottomSheetBehavior.STATE_HIDDEN)
            {
                shouldClose = true
                finish()
                overridePendingTransition(0,0)
            }
            if(newState == BottomSheetBehavior.STATE_EXPANDED)
            {
                if(firstExpand) {
                    contextSheetHandle.startAnimation(bounceAnim)
                    firstExpand = false
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_context_sheet)
        setupBottomSheet()
        touchVeilV.setOnClickListener {
            sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }
        handleStartColor = Color.parseColor("#999999")
        handleEndColor = Color.WHITE

        fadeAnim = AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_out)
        fadeAnim.duration = 150
        handleOffsetMax = resources.displayMetrics.density * handleOffsetMaxDP.toFloat()
        handleBounceDistance = resources.displayMetrics.density * 4.0f

        bounceAnim = TranslateAnimation(0f, 0f,0f, handleBounceDistance)
        bounceAnim.interpolator = AccelerateDecelerateInterpolator()
        bounceAnim.duration = 400
        bounceAnim.repeatCount = 5
        bounceAnim.repeatMode = Animation.REVERSE
        bounceAnim.fillBefore = true
        bounceAnim.fillAfter = true
    }

    override fun onResume() {
        super.onResume()
        firstExpand = true
    }

    fun setupBottomSheet()
    {
        sheetBehavior = BottomSheetBehavior.from(contextSheet)
        sheetBehavior?.isHideable = true
        sheetBehavior?.peekHeight = (resources.displayMetrics.density * 172.0).toInt()
        sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        contextSheet.post {
            sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }
        sheetBehavior?.setBottomSheetCallback(callback)
    }

    override fun onBackPressed() {
        sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
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
}